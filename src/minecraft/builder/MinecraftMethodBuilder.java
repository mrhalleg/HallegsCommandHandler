package minecraft.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

import commandManagement.CommandManager.UseConverter;
import commandManagement.CommandManagerLoadingException;
import converter.Converter;
import mehtod.InvokerEndMehtodParameter;
import mehtod.MehtodParameter;
import mehtod.ChainMehtodParameter;

public class MinecraftMethodBuilder {
	public MehtodParameter build(Method meth, List<Class<? extends Converter<?>>> defaultConverter)
			throws CommandManagerLoadingException {
		ChainMehtodParameter prev = null;
		ChainMehtodParameter first = null;
		for (int i = 0; i < meth.getParameterTypes().length; i++) {
			ChainMehtodParameter curr = loadParameter(meth, i, defaultConverter);
			if (prev != null) {
				prev.setNext(curr);
			} else {
				first = curr;
			}
			prev = curr;
		}

		InvokerEndMehtodParameter end = new InvokerEndMehtodParameter(meth);

		if (prev != null) {
			prev.setNext(end);
		}

		if (first == null) {
			return end;
		} else {
			return first;
		}
	}

	private ChainMehtodParameter loadParameter(Method m, int i, List<Class<? extends Converter<?>>> defaultConverter)
			throws CommandManagerLoadingException {
		Class<?> param = m.getParameterTypes()[i];
		Class<? extends Converter<?>> convClass = null;
		UseConverter anno = null;

		// try to get a default value for the convertertype
		for (Class<? extends Converter<?>> c : defaultConverter) {
			if (ClassUtils.isAssignable(param, Converter.getTypeParameter(c), true)) {
				convClass = c;
				break;
			}
		}

		// check if a Anntotation is present and if so use it do initialize the
		// convertertype
		for (Annotation a : m.getParameterAnnotations()[i]) {
			if (a instanceof UseConverter) {
				anno = (UseConverter) a;
				convClass = anno.type();
				break;
			}
		}

		// check if its still not initialized
		if (convClass == null) {
			throw new CommandManagerLoadingException("No Converter Class could be found for the " + i + " parameter");
		}

		// check if the Conveter has the right Parameter type
		if (!ClassUtils.isAssignable(Converter.getTypeParameter(convClass), param, true)) {
			throw new CommandManagerLoadingException("The Converter class \"" + convClass.getName() + "\" for the " + i
					+ " parameter does not return the right type for the parameter type. (should be \""
					+ param.getName() + "" + "\", is \"" + Converter.getTypeParameter(convClass).getTypeName() + "\")");
		}

		// get a instance of the class
		Converter<?> conv = null;
		try {
			conv = convClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new CommandManagerLoadingException(
					"could not create instace of converter class for " + i + " parameter");
		}

		conv.loadAnnotations(m.getParameterAnnotations()[i]);

		// add the argument instance to the list
		return new ChainMehtodParameter(conv);
	}
}
