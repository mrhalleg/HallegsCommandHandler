package commandManagement.result.method;

import commandManagement.result.PathResult;
import handler.method.MethodParameter;

public class MethodFailResult extends PathResult implements MethodResult {
	protected MethodParameter parameter;

	public MethodFailResult(MethodParameter methodParameter) {
		this.parameter = methodParameter;
	}

	@Override
	public String toString() {
		return joinNodes(" ") + this.parameter.toString();
	}
}
