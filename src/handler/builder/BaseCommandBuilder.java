package handler.builder;

import commandManagement.CommandManagerFactory.CommandClass;
import handler.command.BaseCommand;

public class BaseCommandBuilder {

	public BaseCommand build(Class<?> clazz, CommandClass comm) {
		return new BaseCommand(clazz, comm);
	}

}
