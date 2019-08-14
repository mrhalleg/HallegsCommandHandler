package commandManagement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import converter.BooleanConverter;
import converter.ConstStringConverter;
import converter.Converter;
import converter.DoubleConverter;
import converter.IntegerConverter;
import converter.PlayerNameConverter;
import converter.StringConverter;
import handler.BaseCommandHandler;

/**
 * Main Class for HallegsCommandManager Libary. Call
 * {@link CommandManager#manage(JavaPlugin, Class...)} to initialize your
 * PluginCommand Mehtods.
 */
public abstract class CommandManager {
	private static Map<Class, Class<Converter>> defaultArgumentClasses;

	private final static Class<?>[] standardArgumentClasses = { ConstStringConverter.class, DoubleConverter.class,
			IntegerConverter.class, PlayerNameConverter.class, StringConverter.class, BooleanConverter.class };

	public static void manage(JavaPlugin plugin, Class<?>... commandClasses) {

		List<Class> classes = new LinkedList<>();
		classes.addAll(Arrays.asList(standardArgumentClasses));

		// load default argument classes
		defaultArgumentClasses = new HashMap<>();
		for (Class c : classes) {
			defaultArgumentClasses.put(getArgumentType(c), c);
		}

		BaseCommandHandler baseHandler = new BaseCommandHandler(plugin);
		// load commandClasses and command methods in them
		plugin.getLogger().info(
				"Start Loading Methods With @PluginCommand Annotations in " + commandClasses.length + " Classes...");
		int errors = 0;
		int total = 0;
		for (Class c : commandClasses) {
			plugin.getLogger().info("Class \"" + c.getName() + "\":");
			Method[] meth = c.getMethods();
			for (Method m : meth) {

				if (m.getAnnotation(PluginCommand.class) == null) {
					continue;
				}

				String name = m.getAnnotation(PluginCommand.class).name();
				String[] args = name.split(" ");

				String status = " success.";
				try {
					baseHandler.addHandler(args, m);
				} catch (CommandManagerLoadingException e) {
					status = " FAILED: " + e.getMessage() + "!";
					errors++;
				}
				plugin.getLogger().info(" -> Method \"" + methodInformation(m) + "\"" + status);
				total++;
			}
		}
		plugin.getLogger().info("Done, " + errors + " of " + total + " Methods Failed to Load");
		plugin.getLogger().info("Loaded SubCommand Tree:");
		baseHandler.printTree();
	}

	public static String methodInformation(Method m) {
		String str = m.getName() + "(";
		for (int i = 0; i < m.getParameterTypes().length; i++) {
			str += m.getParameterTypes()[i].getSimpleName();
			if (!(i == m.getParameterTypes().length - 1)) {
				str += ", ";
			}
		}
		return str + ")";
	}

	public static Class getArgumentType(Class<Converter> c) {
		try {
			return c.getMethod("check", new Class[] { CommandSender.class, String.class }).getReturnType();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Class<Converter> getDefaultArgument(Class s) {
		// System.out.println("checking: " + s);
		for (Class c : defaultArgumentClasses.keySet()) {
			// System.out.println("curr: " + c);
			if (ClassUtils.isAssignable(c, s, true)) {
				return defaultArgumentClasses.get(c);
			}
		}
		return null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface PluginCommand {
		String name();

		boolean opOnly() default true;

		String permission() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface UseConverter {
		Class type() default Converter.class;

		String[] params() default {};
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface UseDefaultConverter {
		Class converter();
	}
}
