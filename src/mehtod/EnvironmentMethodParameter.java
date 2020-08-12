package mehtod;

import java.lang.reflect.Parameter;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

public class EnvironmentMethodParameter extends MethodChainElement {
	private Class<?> type;
	private MehtodParameter next;

	public EnvironmentMethodParameter(Parameter parameter) {
		this.type = parameter.getType();
	}

	@Override
	public boolean command(String[] args, int offset, List<Object> list, Object special) {
		Class<?> c = special.getClass();
		if (special != null && ClassUtils.isAssignable(c, type, true)) {
			list.add(special);
			next.command(args, offset, list, special);
			return true;
		}
		return false;
	}

	@Override
	public List<String> complete(String[] args, int offset) {
		return next.complete(args, offset);
	}

	@Override
	public String printTree(String pre, String params) {
		return next.printTree(pre, params);
	}

	@Override
	public void setNext(MehtodParameter curr) {
		this.next = curr;
	}

}
