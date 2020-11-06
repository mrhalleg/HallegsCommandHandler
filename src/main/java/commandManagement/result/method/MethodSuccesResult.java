package commandManagement.result.method;

import commandManagement.executable.Executable;
import commandManagement.result.Result;
import commandManagement.result.SuccesResult;
import handler.method.InvokerEndMehtodParameter;

public class MethodSuccesResult extends SuccesResult implements MethodResult, Result {
	protected InvokerEndMehtodParameter end;

	public MethodSuccesResult(InvokerEndMehtodParameter end, Executable exe) {
		super(exe);
		this.end = end;
	}
}
