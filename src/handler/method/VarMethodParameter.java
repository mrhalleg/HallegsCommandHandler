package handler.method;

import commandManagement.result.method.MethodResult;
import converter.Converter;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

public class VarMethodParameter extends MethodChainElement {

	private Converter<?> converter;
	private Class<?> type;
	private MethodParameter next;

	public VarMethodParameter(Converter<?> converter, Class<?> type) {
		this.converter = converter;
		this.type = type;
	}

	@Override
	public void setNext(MethodParameter next) {
		this.next = next;
	}

	public MethodResult command(String[] args, int offset, List<Object> list, Object special, Object[] conv) {
		if (offset == args.length) {
			list.add(conv);
			return this.next.command(args, offset, list, special);
		}

		Object ret = this.converter.convert(args[offset]);
		if (ret == null) {
			return null;
		}

		conv[offset - (args.length - conv.length)] = ret;
		return command(args, offset + 1, list, special, conv);
	}

	@Override
	public MethodResult command(String[] args, int offset, List<Object> list, Object environment) {
		Object[] arr = (Object[]) Array.newInstance(this.type, args.length - offset);
		return command(args, offset, list, environment, arr);
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

		ret.addAll(complete(args, offset + 1));
		return ret;
	}

	@Override
	public String printTree(String pre, String params) {
		if (params == null) {
			return this.next.printTree(pre, this.converter.getClass().getSimpleName() + "...");
		} else {
			return this.next.printTree(pre, params + ", " + this.converter.getClass().getSimpleName() + "...");
		}

	}
}
