package handler.method;

import commandManagement.result.method.MethodFailResult;
import commandManagement.result.method.MethodResult;
import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Parameter;
import java.util.List;

public class EnvironmentMethodParameter extends NodeMethodParameter {
	private Class<?> type;
	private MethodParameter next;

	public EnvironmentMethodParameter(Parameter parameter) {
		this.type = parameter.getType();
	}

	@Override
	public MethodResult search(String[] args, int offset, List<Object> list, Object special) {
		Class<?> c = special.getClass();
		if (special != null && ClassUtils.isAssignable(c, this.type, true)) {
			list.add(special);
			MethodResult ret = this.next.search(args, offset, list, special);
			ret.addPath(this);
			return ret;
		}
		return new MethodFailResult(this);
	}

	@Override
	public String printTree(String pre, String params) {
		return this.next.printTree(pre, params);
	}

	@Override
	public void setNext(MethodParameter curr) {
		this.next = curr;
	}

}
