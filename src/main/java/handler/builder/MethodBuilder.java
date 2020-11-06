package handler.builder;

import commandManagement.CommandManagerLoadingException;
import commandManagement.annotations.CommandMehtod;
import commandManagement.annotations.UseConverter;
import converter.Converter;
import handler.method.*;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MethodBuilder {
	public MethodParameter build(Method meth, CommandMehtod anno, List<Class<? extends Converter<?>>> defaultConverter)
			throws CommandManagerLoadingException {
		NodeMethodParameter prev = null;
		NodeMethodParameter first = null;

		int i = 0;

		if (anno.hasEnvironemntParameter()) {
			prev = loadEnvironmentParameter(meth);
			first = prev;
			i++;
		}

		for (; i < meth.getParameterTypes().length; i++) {

			NodeMethodParameter curr = loadParameter(meth, i, defaultConverter);
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

	private NodeMethodParameter loadEnvironmentParameter(Method meth) throws CommandManagerLoadingException {
		if (meth.getParameters().length < 1) {
			throw new CommandManagerLoadingException("mehtod needs atleast 1 parameter to use Special Annotation");
		}
		return new EnvironmentMethodParameter(meth.getParameters()[0]);
	}

	private NodeMethodParameter loadParameter(Method m, int i, List<Class<? extends Converter<?>>> defaultConverter)
			throws CommandManagerLoadingException {
		Class<?> param = null;

		if (m.getParameters()[i].isVarArgs()) {
			param = m.getParameterTypes()[i].getComponentType();
		} else {
			param = m.getParameterTypes()[i];
		}

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

		if (m.getParameters()[i].isVarArgs()) {
			return new VarMethodParameter(conv, param);
		} else {
			return new StandardMehtodParameter(conv);
		}
	}
}
