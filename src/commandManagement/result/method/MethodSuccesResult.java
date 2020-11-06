package commandManagement.result.method;

import handler.method.InvokerEndMehtodParameter;

public class MethodSuccesResult extends MethodResult {
	private InvokerEndMehtodParameter end;

	public MethodSuccesResult(InvokerEndMehtodParameter end) {
		this.end = end;
	}

	@Override
	public boolean isSucces() {
		return true;
	}

	@Override
	
	public String toString() {
		return "MethodSuccesResult{" +
				"end=" + end +
				'}';
	}
}
