package handler.command;

import commandManagement.CommandManagerFactory.CommandClass;
import commandManagement.result.Result;

public class BaseCommand {

	protected SubCommand command;

	public BaseCommand(Class<?> clazz, CommandClass comm) {
		this.command = new SubCommand(clazz, comm);
	}

	public void printTree() {
		System.out.println(this.command.printTree("", true));
	}

	public Result search(String string, Object environment) {
		String cleaned = string.trim().replaceAll(" +", " ");
		String[] arr = cleaned.split(" ");
		return this.command.search(arr, 0, environment);
	}

	public SubCommand getCommand() {
		return this.command;
	}
}
