package converter.defaults;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import converter.Converter;

public class IntegerConverter extends Converter<Integer> {
	private int max = Integer.MAX_VALUE;
	private int min = Integer.MIN_VALUE;

	@Override
	public void loadAnnotations(Annotation[] annos) {
		for (Annotation a : annos) {
			if (a instanceof IntMax) {
				max = ((IntMax) a).max();
			} else if (a instanceof IntMin) {
				min = ((IntMin) a).min();
			}
		}
	}

	@Override
	public Integer convert(String string) {
		int i = 0;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return null;
		}
		if (i < min || i > max) {
			return null;
		}

		return i;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface IntMax {
		int max();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	public @interface IntMin {
		int min();
	}

}
