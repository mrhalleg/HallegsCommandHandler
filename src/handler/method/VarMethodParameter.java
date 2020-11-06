package handler.method;

import commandManagement.CommandManagerLoadingException;
import commandManagement.result.method.MethodResult;
import converter.Converter;

import java.lang.reflect.Array;
import java.util.List;

public class VarMethodParameter extends NodeMethodParameter {

	private Converter<?> converter;
	private Class<?> type;
	private MethodParameter next;

	public VarMethodParameter(Converter<?> converter, Class<?> type) throws CommandManagerLoadingException {
		super();
		this.converter = converter;
		if (type.isPrimitive()) {
			throw new CommandManagerLoadingException("No primitive Types allowed in VarMethodParameter.");
		}
		this.type = type;
	}

	@Override
	public void setNext(MethodParameter next) {
		this.next = next;
	}

	public MethodResult search(String[] args, int offset, List<Object> list, Object special, Object[] conv) {
		if (offset == args.length) {
			list.add(conv);
			MethodResult ret = this.next.search(args, offset, list, special);
			ret.addPath(this);
			return ret;
		}

		Object ret = this.converter.convert(args[offset]);
		if (ret == null) {
			return null;
		}

		conv[offset - (args.length - conv.length)] = ret;
		return search(args, offset + 1, list, special, conv);
	}

	@Override
	public MethodResult search(String[] args, int offset, List<Object> list, Object environment) {
		Object[] arr = (Object[]) Array.newInstance(this.type, args.length - offset);
		return search(args, offset, list, environment, arr);
	}


	@Override
	public String printTree(String pre, String params) {
		if (params == null) {
			return this.next.printTree(pre, this.converter.getClass().getSimpleName() + "...");
		} else {
			return this.next.printTree(pre, params + ", " + this.converter.getClass().getSimpleName() + "...");
		}

	}

	@Override
	public String toString() {
		return this.converter.getClass().getName() + "...";
	}
}
