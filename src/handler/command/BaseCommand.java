package handler.command;

import commandManagement.CommandManagerFactory.CommandClass;
import commandManagement.result.Result;

public class BaseCommand {

	protected CommandHandler command;

	public BaseCommand(Class<?> clazz, CommandClass comm) {
		this.command = new CommandHandler(clazz, comm);
	}

	public void printTree() {
		System.out.println(this.command.printTree("", true));
	}

	public Result search(String string, Object environment) {
		String[] arr = string.split(" ");
		return this.command.search(arr, 0, environment);
	}

	public CommandHandler getCommand() {
		return this.command;
	}
}
