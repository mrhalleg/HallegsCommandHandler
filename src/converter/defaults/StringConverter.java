package converter.defaults;

import converter.Converter;

public class StringConverter extends Converter<String> {
	@Override
	public String convert(String string) {
		return string;
	}
}
