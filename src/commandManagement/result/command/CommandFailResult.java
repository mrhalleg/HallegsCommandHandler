package commandManagement.result.command;

import commandManagement.result.FailResult;
import handler.command.CommandHandler;

import java.util.List;

public class CommandFailResult extends FailResult {
	protected CommandHandler command;

	public CommandFailResult(CommandHandler commandHandler) {
		this.command = commandHandler;
	}

	@Override
	public List<String> suggest() {
		return null;
	}

	@Override
	public String toString() {
		return super.toString() + this.command;
	}
}
