package commandManagement;

import commandManagement.result.command.CommandResult;
import handler.BaseCommand;

import java.util.List;

public class CommandManager {
	protected List<BaseCommand> bases;

	public CommandManager(List<BaseCommand> bases) {
		this.bases = bases;
	}

	public CommandResult command(String command, Object o) {
		for (BaseCommand b : this.bases) {
			CommandResult res = b.command(command, o);
			if (res != null) {
				return res;
			}
		}

		return null;
	}

	public void printTree() {
		for (BaseCommand b : this.bases) {
			b.printTree();
		}
	}
}
