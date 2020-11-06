package commandManagement.result.method;

import handler.method.MethodParameter;

public class MethodFailResult extends MethodResult {
	protected MethodParameter parameter;

	public MethodFailResult(MethodParameter methodParameter) {
		this.parameter = methodParameter;
	}

	@Override
	public boolean isSucces() {
		return false;
	}

	@Override
	public String toString() {
		return this.parameter.toString();
	}
}
