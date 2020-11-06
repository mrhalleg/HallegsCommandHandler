package commandManagement;

import commandManagement.result.Result;
import handler.command.BaseCommand;

import java.util.List;

public class CommandManager {
	protected List<BaseCommand> bases;

	public CommandManager(List<BaseCommand> bases) {
		this.bases = bases;
	}

	public Result search(String command, Object o) {
		for (BaseCommand b : this.bases) {
			Result res = b.search(command, o);
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
