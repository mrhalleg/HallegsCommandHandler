package handler.method;

import commandManagement.result.method.MethodResult;
import commandManagement.result.method.MethodSuccesResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class InvokerEndMehtodParameter extends MethodParameter {
	private Method methode;

	public InvokerEndMehtodParameter(Method methode) {
		this.methode = methode;
	}

	@Override
	public MethodResult command(String[] args, int offset, List<Object> list, Object t) {

		if (offset != args.length) {
			return null;
		}

		try {
			Object[] arr = list.toArray();
			this.methode.invoke(null, arr);
			return new MethodSuccesResult(this);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> complete(String[] args, int offset) {

		List<String> ret = new LinkedList<>();
		return ret;

	}

	@Override
	public String printTree(String pre, String params) {
		return "\n" + pre + "+- " + "(" + params + ")";
	}

	@Override
	public String toString() {
		return "InvokerEndMehtodParameter{" +
				"methode=" + this.methode +
				'}';
	}
}
