package handler;

import commandManagement.CommandManagerFactory.CommandClass;
import commandManagement.result.command.CommandResult;

import java.util.List;

public class BaseCommand {

	protected SubCommand command;

	public BaseCommand(Class<?> clazz, CommandClass comm) {
		this.command = new SubCommand(clazz, comm);
	}

	public void printTree() {
		System.out.println(this.command.printTree("", true));
	}

	public CommandResult command(String string, Object environment) {
		String[] arr = string.split(" ");
		return this.command.command(arr, 0, environment);
	}

	public List<String> complete(String string) {
		String[] arr = string.split(" ");
		return this.command.complete(arr, 0);
	}

	public CommandHandler getCommand() {
		return this.command;
	}
}
