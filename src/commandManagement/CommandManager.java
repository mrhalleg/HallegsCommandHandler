package commandManagement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.bukkit.plugin.java.JavaPlugin;

import converter.Converter;
import converter.defaults.BooleanConverter;
import converter.defaults.DoubleConverter;
import converter.defaults.IntegerConverter;
import converter.defaults.StringConverter;
import handler.BaseCommand;
import handler.CommandHandler;
import handler.SubCommand;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;
import mehtod.MehtodHandler;

/**
 * Main Class for HallegsCommandManager Libary. Call
 * {@link CommandManager#manage(JavaPlugin, Class...)} to initialize your
 * PluginCommand Mehtods.
 */
public abstract class CommandManager {
	private static Map<Class, Class<Converter>> defaultArgumentClasses;

	private static List<Class<? extends Converter<?>>> standardConverter() {
		List<Class<? extends Converter<?>>> list = new LinkedList();
		list.add(BooleanConverter.class);
		list.add(StringConverter.class);
		list.add(IntegerConverter.class);
		list.add(DoubleConverter.class);
		return list;
	}

	public static void manage(SubCommandBuilder subBuilder, BaseCommandBuilder baseBuilder, MethodBuilder methodBuilder,
			Class<?>... commandClasses) {

		// load commandClasses and command methods in them
		for (Class c : commandClasses) {
			try {
				BaseCommand base = loadBaseClass(c, subBuilder, baseBuilder, methodBuilder, standardConverter());
				base.printTree();
				base.command("bool add 1 2");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static BaseCommand loadBaseClass(Class<?> clazz, SubCommandBuilder subBuilder,
			BaseCommandBuilder baseBuilder, MethodBuilder methodBuilder,
			List<Class<? extends Converter<?>>> standardConverter) throws CommandManagerLoadingException {
		CommandClass comm = clazz.<CommandClass>getAnnotation(CommandClass.class);

		if (comm == null) {
			return null;
		}

		BaseCommand handler = baseBuilder.build(clazz, comm);

		for (Class<?> c : getChildCommands(clazz, comm)) {
			loadSubClass(c, handler.getCommand(), subBuilder, methodBuilder, standardConverter);
		}

		for (Method m : clazz.getMethods()) {
			loadMethod(m, handler.getCommand(), methodBuilder, standardConverter);
		}

		return handler;
	}

	private static void loadSubClass(Class<?> clazz, CommandHandler parent, SubCommandBuilder subBuilder,
			MethodBuilder methodBuilder, List<Class<? extends Converter<?>>> standardConverter)
			throws CommandManagerLoadingException {
		CommandClass anno = clazz.<CommandClass>getAnnotation(CommandClass.class);

		if (anno == null) {
			return;
		}

		SubCommand handler = subBuilder.build(clazz, anno);
		parent.addCommand(handler);

		for (Class c : getChildCommands(clazz, anno)) {
			loadSubClass(c, handler, subBuilder, methodBuilder, standardConverter);
		}

		for (Method m : clazz.getMethods()) {
			loadMethod(m, handler, methodBuilder, standardConverter);
		}
	}

	private static List<Class<?>> getChildCommands(Class<?> clazz, CommandClass anno) {
		List<Class<?>> ret = new LinkedList<>();
		ret.addAll(Arrays.asList(clazz.getDeclaredClasses()));
		ret.addAll(Arrays.asList(anno.children()));

		return ret;
	}

	private static void loadMethod(Method meth, CommandHandler parent, MethodBuilder methodBuilder,
			List<Class<? extends Converter<?>>> standardConverter) throws CommandManagerLoadingException {

		CommandMehtod anno = meth.<CommandMehtod>getAnnotation(CommandMehtod.class);

		if (anno == null) {
			return;
		}

		MehtodHandler handler = methodBuilder.build(meth, standardConverter);
		parent.addMethod(handler);
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
	@Target(ElementType.TYPE)
	public @interface CommandClass {
		String name();

		String[] alias() default {};

		Class<?>[] children() default {};
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface CommandMehtod {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface UseConverter {
		Class<? extends Converter<?>> type();

		String[] params() default {};
	}
}
