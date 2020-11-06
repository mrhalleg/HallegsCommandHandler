package commandManagement.result.command;

import handler.CommandHandler;

public class CommandFailResult extends CommandResult {
	protected CommandHandler command;

	public CommandFailResult(CommandHandler commandHandler) {
		this.command = commandHandler;
	}

	@Override
	public String toString() {
		return "CommandFailResult{" +
				"command=" + this.command +
				'}';
	}
}
