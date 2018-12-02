package commandManagement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import arguments.ConstStringArgument;
import arguments.DoubleArgument;
import arguments.IntegerArgument;
import arguments.PlayerNameArgument;
import arguments.StringArgument;

public class CommandManager {
	private static Map<Class, Class<Argument>> defaultArgumentClasses;

	private final static Class<?>[] standardArgumentClasses = { ConstStringArgument.class,
			DoubleArgument.class, IntegerArgument.class, PlayerNameArgument.class,
			StringArgument.class };

	public static void manage(JavaPlugin plugin, Class<?>[] commandClasses,
			Class<Argument>[] argumentClasses) throws Exception {

		List<Class> classes = new LinkedList<>();
		classes.addAll(Arrays.asList(argumentClasses));
		classes.addAll(Arrays.asList(standardArgumentClasses));

		// load default argument classes
		for (Class c : classes) {

		}

		Map<String, Commander> exes = new HashMap<>();
		// load commandClasses and command methods in them
		for (Class c : commandClasses) {
			Method[] meth = c.getMethods();
			for (Method m : meth) {
				if (m.getAnnotation(PluginCommand.class) == null) {
					continue;
				}
				if (!Modifier.isStatic(m.getModifiers())) {
					throw new Exception("The methode " + m.getName()
							+ " has PluginCommand Annotation but is not static.");
				}
				if (m.getReturnType() != boolean.class) {
					throw new Exception("The methode " + m.getName()
							+ " has PluginCommand Annotation but does not return a boolean value.");
				}
				String name = m.getAnnotation(PluginCommand.class).name();

				if (exes.get(name) == null) {
					exes.put(name, new Commander(plugin.getCommand(name)));
				}
				exes.get(name).addMehtodeExecutor(new Executor(m));
			}

		}
	}

	public static Class<Argument> getDefaultArgument(Class s) {
		return defaultArgumentClasses.get(s);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface PluginCommand {
		String name();

		boolean opOnly()

		default true;

		String permission() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface ArgumentParameter {
		Class type() default Argument.class;

		String[] params() default {};
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface DefaultArgumentClass {
	}
}
