package commandManagement;

import commandManagement.annotations.CommandClass;
import commandManagement.annotations.CommandClassContainer;
import commandManagement.annotations.CommandMehtod;
import converter.Converter;
import converter.defaults.BooleanConverter;
import converter.defaults.DoubleConverter;
import converter.defaults.IntegerConverter;
import converter.defaults.StringConverter;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;
import handler.command.BaseCommand;
import handler.command.SubCommand;
import handler.method.MethodParameter;
import org.apache.commons.lang.ClassUtils;

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
													  Class<?>... classes) throws CommandManagerLoadingException {
		List<BaseCommand> bases = new ArrayList<>();
		for (Class<?> clazz : classes) {
			CommandClassContainer container = clazz.getAnnotation(CommandClassContainer.class);

			if (container != null) {
				for (Class<?> c : clazz.getDeclaredClasses()) {
					BaseCommand ret = loadBaseClass(c, subBuilder, baseBuilder, methodBuilder, standardConverter());
					if (ret != null) {
						bases.add(ret);
					}
				}
			} else {
				BaseCommand ret = loadBaseClass(clazz, subBuilder, baseBuilder, methodBuilder, standardConverter());
				if (ret != null) {
					bases.add(ret);
				}
			}
		}
		return new CommandManager(bases);
	}

	private static BaseCommand loadBaseClass(Class<?> clazz, SubCommandBuilder subBuilder,
											 BaseCommandBuilder baseBuilder, MethodBuilder methodBuilder,
											 List<Class<? extends Converter<?>>> standardConverter) throws CommandManagerLoadingException {
		CommandClass comm = clazz.getAnnotation(CommandClass.class);

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

	private static void loadSubClass(Class<?> clazz, SubCommand parent, SubCommandBuilder subBuilder,
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

	private static void loadMethod(Method meth, SubCommand parent, MethodBuilder methodBuilder,
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
		for (Class c : defaultArgumentClasses.keySet()) {
			if (ClassUtils.isAssignable(c, s, true)) {
				return defaultArgumentClasses.get(c);
			}
		}
		return null;
	}

}
