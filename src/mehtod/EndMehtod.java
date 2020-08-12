package mehtod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class EndMehtod extends MehtodHandler {
	private Method methode;

	public EndMehtod(Method methode) {
		this.methode = methode;
	}

	@Override
	public boolean command(String[] args, int offset, List<Object> list) {

		if (offset != args.length) {
			return false;
		}

		try {
			methode.invoke(null, list.toArray());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}

		return false;
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
