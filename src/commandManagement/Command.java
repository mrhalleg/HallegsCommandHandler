package commandManagement;

import java.util.List;

public class Command {
	private String name;

	private List<ArgumentList> argumentLists;
	private List<Command> subCommands;

	public Command(String name) {
		this.name = name;
	}

	// returns true if the input can be handled by this command
	public boolean handle(String[] input) {
		if (input.length < 1 || !input[0].equals(this.name)) {
			return false;
		}

		for (int i = 1; i < input.length; i++) {
			if (this.subCommands.get(i).handle(input)) {
				return true;
			}
		}

		for (int i = 1; i < input.length; i++) {
			if (this.argumentLists.get(i).handle(input)) {
				return true;
			}
		}

		return false;
	}

}
