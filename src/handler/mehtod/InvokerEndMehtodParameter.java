package handler.mehtod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class InvokerEndMehtodParameter extends MehtodParameter {
	private Method methode;

	public InvokerEndMehtodParameter(Method methode) {
		this.methode = methode;
	}

	@Override
	public boolean command(String[] args, int offset, List<Object> list, Object t) {

		if (offset != args.length) {
			return false;
		}

		try {
			Object[] arr = list.toArray();
			methode.invoke(null, arr);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> complete(String[] args, int offset) {

		List<String> ret = new LinkedList<>();
		return ret;

	}

	@Override
	public String printTree(String pre, String params) {
		return "\n" + pre + "+- (" + params + ")";
	}
}
