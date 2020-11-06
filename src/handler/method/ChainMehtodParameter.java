package handler.method;

import commandManagement.result.method.MethodFailResult;
import commandManagement.result.method.MethodResult;
import converter.Converter;

import java.util.LinkedList;
import java.util.List;

public class ChainMehtodParameter extends MethodChainElement {
	private Converter<?> converter;
	private MethodParameter next;

	public ChainMehtodParameter(Converter<?> converter) {
		this.converter = converter;
	}

	@Override
	public void setNext(MethodParameter next) {
		this.next = next;
	}

	@Override
	public MethodResult command(String[] args, int offset, List<Object> list, Object environment) {
		if (offset == args.length) {
			return null;
		}
		Object ret = this.converter.convert(args[offset]);
		if (ret == null) {
			return null;
		}

		list.add(ret);
		MethodResult result = this.next.command(args, offset + 1, list, environment);
		if (result != null) {
			return result;
		}
		return new MethodFailResult(this);
	}

	@Override
	public List<String> complete(String[] args, int offset) {

		List<String> ret = new LinkedList<>();

		if (offset == args.length) {
			ret.addAll(this.converter.complete());
			return ret;
		}

		if (this.converter.convert(args[offset]) == null) {
			return ret;
		}

		ret.addAll(this.next.complete(args, offset + 1));
		return ret;
	}

	@Override
	public String printTree(String pre, String params) {
		if (params == null) {
			return this.next.printTree(pre, this.converter.getClass().getSimpleName());
		} else {
			return this.next.printTree(pre, params + ", " + this.converter.getClass().getSimpleName());
		}

	}

	@Override
	public String toString() {
		return "ChainMehtodParameter{" +
				"converter=" + this.converter.getClass().getTypeName() +
				'}';
	}
}