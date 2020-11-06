package converter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class Converter<T> {

	public Converter() {
	}

	public void loadAnnotations(Annotation[] annos) {

	}

	abstract public T convert(String string);

	public List<String> complete() {
		return new ArrayList<>();
	}

	public String tip() {
		return "<?>";
	}

	public static Class<?> getTypeParameter(Class<? extends Converter<?>> convClass) {
		try {
			return convClass.getMethod("convert", String.class).getReturnType();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

}