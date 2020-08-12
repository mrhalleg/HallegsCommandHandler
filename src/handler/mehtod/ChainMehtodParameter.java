package handler.mehtod;

import java.util.LinkedList;
import java.util.List;

import converter.Converter;

public class ChainMehtodParameter extends MethodChainElement {
	private Converter<?> converter;
	private MehtodParameter next;

	public ChainMehtodParameter(Converter<?> converter) {
		this.converter = converter;
	}

	@Override
	public void setNext(MehtodParameter next) {
		this.next = next;
	}

	@Override
	public boolean command(String[] args, int offset, List<Object> list, Object environment) {
		if (offset == args.length) {
			return false;
		}
		Object ret = converter.convert(args[offset]);
		if (ret == null) {
			return false;
		}

		list.add(ret);

		return next.command(args, offset + 1, list, environment);
	}

	@Override
	public List<String> complete(String[] args, int offset) {

		List<String> ret = new LinkedList<>();

		if (offset == args.length) {
			ret.addAll(this.converter.complete());
			return ret;
		}

		if (converter.convert(args[offset]) == null) {
			return ret;
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