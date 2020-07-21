package mehtod;

import java.util.LinkedList;
import java.util.List;

import converter.Converter;
import converter.ConverterConvertException;

public class MehtodParameter extends MehtodHandler {
	private Converter<?> converter;
	private MehtodHandler next;

	public MehtodParameter(Converter<?> converter) {
		this.converter = converter;
	}

	public void setNext(MehtodHandler next) {
		this.next = next;
	}

	@Override
	public boolean command(String[] args, int offset, List<Object> list) throws ConverterConvertException {

		Object ret = converter.convert(args[offset]);
		if (ret == null) {
			return false;
		}

		list.add(ret);

		next.command(args, offset + 1, list);

		return false;
	}

	@Override
	public List<String> complete(String[] args, int offset) {

		List<String> ret = new LinkedList<>();

		if (offset == args.length) {
			ret.addAll(this.converter.complete());
			return ret;
		}

		try {
			if (converter.convert(args[offset]) == null) {
				return ret;
			}
		} catch (ConverterConvertException e) {
		}

		ret.addAll(next.complete(args, offset + 1));
		return ret;
	}

	@Override
	public String printTree(String pre, String params) {
		if (params == null) {
			return next.printTree(pre, converter.getClass().getSimpleName());
		} else {
			return next.printTree(pre, params + ", " + converter.getClass().getSimpleName());
		}

	}
}