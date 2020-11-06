package handler.method;

import commandManagement.executable.InvokerExecutable;
import commandManagement.result.method.MethodResult;
import commandManagement.result.method.MethodSuccesResult;

import java.lang.reflect.Method;
import java.util.List;

public class InvokerEndMehtodParameter extends EndMethodParameter {
	private Method methode;

	public InvokerEndMehtodParameter(Method methode) {
		this.methode = methode;
	}

	@Override
	public MethodResult search(String[] args, int offset, List<Object> list, Object t) {

		if (offset != args.length) {
			return null;
		}

//		try {
		Object[] arr = list.toArray();
// 			this.methode.invoke(null, arr);
		return new MethodSuccesResult(this, new InvokerExecutable(arr, this.methode));
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//			return null;
//		}
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
