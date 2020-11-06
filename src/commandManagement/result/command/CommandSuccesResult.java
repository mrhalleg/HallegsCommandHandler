package commandManagement.result.command;

import commandManagement.result.method.MethodSuccesResult;

public class CommandSuccesResult extends CommandResult {
	private MethodSuccesResult succes;

	public CommandSuccesResult(MethodSuccesResult succes) {
		this.succes = succes;
	}

	public MethodSuccesResult getSucces() {
		return this.succes;
	}

	@Override
	public String toString() {
		return "CommandSuccesResult{" +
				"succes=" + this.succes +
				'}';
	}
}
