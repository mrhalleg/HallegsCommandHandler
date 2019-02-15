package arguments;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.bukkit.command.CommandSender;

import annotations.PluginArgument;
import exceptions.CommandManagerException;

public abstract class Argument<T> {
	protected String[] params;
	protected String name;

	public void init(PluginArgument anno, Parameter parameter) {
		this.name = parameter.getType().getName();
		if (anno == null) {
			return;
		}
		this.params = new String[anno.params().length];
		for (int i = 0; i < this.params.length; i++) {
			this.params[i] = anno.params()[i];
		}
	}

	abstract public T convert(CommandSender sender, String string);

	public List<String> complete(CommandSender sender) {
		return new ArrayList<>();
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static Argument loadArgument(Method meth, int i, List<Class<?>> defaults)
			throws CommandManagerException {
		Class<?> argClass = null;
		PluginArgument anno = null;

		// search for annotation
		for (Annotation a : meth.getParameterAnnotations()[i]) {
			if (a instanceof PluginArgument) {
				anno = (PluginArgument) a;
				argClass = ((PluginArgument) a).type();
				if (!ClassUtils.isAssignable(meth.getParameterTypes()[i], getClassFor(argClass),
						true)) {
					throw new CommandManagerException("The Argument Class " + argClass
							+ " is specefied as the Argument class for the " + i
							+ "ht parameter of the mehtod\"" + "\" but is not convert to a " + meth
									.getParameterTypes()[i].toString());
				}
			}
		}

		// if not set already search in defaults
		if (argClass == null || argClass == Argument.class) {
			for (Class<?> def : defaults) {
				Class<?> type = meth.getParameterTypes()[i];
				if (meth.isVarArgs() && i == meth.getParameterCount() - 1) {
					type = type.getComponentType();
				}
				if (ClassUtils.isAssignable(getClassFor(def), type, true)) {
					argClass = def;
				}
			}
		}

		// if no default value is found throw exception
		if (argClass == null || argClass == Argument.class) {
			throw new CommandManagerException("There was no suitable Converter found for the " + i
					+ "th parameter of " + meth.getName() + "(" + meth.getParameters()[i].getType()
							.toString() + ")");
		}

		// check if class from annotation extends Argument
		if (!Argument.class.isAssignableFrom(argClass)) {
			throw new CommandManagerException(
					"The Class specefied in the PluginCommand annotaion for the " + i
							+ ". parameter of " + meth.getName() + " in " + meth.getClass()
							+ " does not extend Argument");
		}

		Argument arg = null;
		try {
			arg = (Argument) argClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new CommandManagerException(e.getMessage());
		}

		arg.init(anno, meth.getParameters()[i]);
		return arg;
	}

	public static Class<?> getClassFor(Class<?> arg) throws CommandManagerException {
		try {
			return arg.getMethod("convert", CommandSender.class, String.class).getReturnType();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new CommandManagerException(e.getMessage());
		}
	}

	private static boolean isAssignableFrom(Class<?> cls, Class<?> toClass) {
		return ClassUtils.isAssignable(cls, toClass);
	}
}