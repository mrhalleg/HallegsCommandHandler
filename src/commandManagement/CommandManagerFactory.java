package commandManagement;

import converter.Converter;
import converter.defaults.BooleanConverter;
import converter.defaults.DoubleConverter;
import converter.defaults.IntegerConverter;
import converter.defaults.StringConverter;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;
import handler.command.BaseCommand;
import handler.command.CommandHandler;
import handler.method.MethodParameter;
import org.apache.commons.lang.ClassUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

public abstract class CommandManagerFactory {
	private static Map<Class, Class<Converter>> defaultArgumentClasses;

	private static List<Class<? extends Converter<?>>> standardConverter() {
		List<Class<? extends Converter<?>>> list = new LinkedList();
		list.add(BooleanConverter.class);
		list.add(StringConverter.class);
		list.add(IntegerConverter.class);
		list.add(DoubleConverter.class);
		return list;
	}

	public static CommandManager createCommandManager(SubCommandBuilder subBuilder, BaseCommandBuilder baseBuilder, MethodBuilder methodBuilder,
													  Class<?>... commandClasses) {
		List<BaseCommand> bases = new ArrayList<>();
		for (Class c : commandClasses) {
			try {
				bases.add(loadBaseClass(c, subBuilder, baseBuilder, methodBuilder, standardConverter()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new CommandManager(bases);
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

		CommandHandler handler = subBuilder.build(clazz, anno);
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

		MethodParameter handler = methodBuilder.build(meth, anno, standardConverter);
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
		boolean hasEnvironemntParameter() default false;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface UseConverter {
		Class<? extends Converter<?>> type();

		String[] params() default {};
	}
}
