package converter.defaults;

import java.util.LinkedList;
import java.util.List;

import converter.Converter;

public class BooleanConverter extends Converter<Boolean> {

	@Override
	public Boolean convert(String string) {
		if (string.equals("true") || string.equals("1")) {
			return true;
		} else if (string.equals("false") || string.equals("0")) {
			return false;
		}
		return null;
	}

	@Override
	public List<String> complete() {
		List<String> ret = new LinkedList<>();
		ret.add("true");
		ret.add("false");
		return ret;
	}
}
