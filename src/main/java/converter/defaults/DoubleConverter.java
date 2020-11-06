package converter.defaults;

import converter.Converter;

public class DoubleConverter extends Converter<Double> {
	@Override
	public Double convert(String string) {
		double d = 0;
		try {
			d = Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return null;
		}
		return d;
	}
}
