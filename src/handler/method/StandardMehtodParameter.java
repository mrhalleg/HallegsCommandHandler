package handler.method;

import commandManagement.result.method.MethodFailResult;
import commandManagement.result.method.MethodResult;
import converter.Converter;

import java.util.List;

public class StandardMehtodParameter extends NodeMethodParameter {
	private Converter<?> converter;
	private MethodParameter next;

	public StandardMehtodParameter(Converter<?> converter) {
		this.converter = converter;
	}

	@Override
	public void setNext(MethodParameter next) {
		this.next = next;
	}

	@Override
	public MethodResult search(String[] args, int offset, List<Object> list, Object environment) {
		if (offset == args.length) {
			return null;
		}
		Object ret = this.converter.convert(args[offset]);
		if (ret == null) {
			return null;
		}

		list.add(ret);
		MethodResult result = this.next.search(args, offset + 1, list, environment);
		if (result != null) {
			result.addPath(this);
			return result;
		}
		return new MethodFailResult(this);
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
		return this.converter.getClass().getName();
	}
}