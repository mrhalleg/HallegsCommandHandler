package handler.builder;

import commandManagement.CommandManagerFactory.CommandClass;
import handler.command.CommandHandler;

public class SubCommandBuilder {
	public CommandHandler build(Class<?> clazz, CommandClass anno) {
		return new CommandHandler(clazz, anno);
	}
}
