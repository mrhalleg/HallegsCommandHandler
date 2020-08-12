package mehtod;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import converter.Converter;

public class VarMethodParameter extends MethodChainElement {

	private Converter<?> converter;
	private Class<?> type;
	private MehtodParameter next;

	public VarMethodParameter(Converter<?> converter, Class<?> type) {
		this.converter = converter;
		this.type = type;
	}

	@Override
	public void setNext(MehtodParameter next) {
		this.next = next;
	}

	public boolean command(String[] args, int offset, List<Object> list, Object special, Object[] conv) {
		if (offset == args.length) {
			list.add(conv);
			return next.command(args, offset, list, special);
		}

		Object ret = converter.convert(args[offset]);
		if (ret == null) {
			return false;
		}

		conv[offset - (args.length - conv.length)] = ret;
		return command(args, offset + 1, list, special, conv);
	}

	@Override
	public boolean command(String[] args, int offset, List<Object> list, Object environment) {
		Object[] arr = (Object[]) Array.newInstance(type, args.length - offset);
		return command(args, offset, list, environment, arr);
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

		ret.addAll(complete(args, offset + 1));
		return ret;
	}

	@Override
	public String printTree(String pre, String params) {
		if (params == null) {
			return next.printTree(pre, converter.getClass().getSimpleName() + "...");
		} else {
			return next.printTree(pre, params + ", " + converter.getClass().getSimpleName() + "...");
		}

	}
}
