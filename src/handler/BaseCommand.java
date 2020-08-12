package handler;

import java.util.List;

import commandManagement.CommandManager.CommandClass;

/**
 * Manages one base command. e.g. every user input beginning with "/test". one
 * command has one or more CommandMethods (which are managed by a
 * {@link #CommandMethodHandler}). Represents a Root int the Command .
 */
public class BaseCommand {

	protected SubCommand command;

	public BaseCommand(Class<?> clazz, CommandClass comm) {
		command = new SubCommand(clazz, comm);
	}

	public void printTree() {
		System.out.println(command.printTree("", true));
	}

	public String command(String string) {
		String[] arr = string.split(" ");
		return command.command(arr, 0);
	}

	public List<String> complete(String string) {
		String[] arr = string.split(" ");
		return command.complete(arr, 0);
	}

	public CommandHandler getCommand() {
		return command;
	}
}
