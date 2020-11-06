package handler.builder;

import commandManagement.annotations.CommandClass;
import handler.command.BaseCommand;

public class BaseCommandBuilder {

	public BaseCommand build(Class<?> clazz, CommandClass comm) {
		return new BaseCommand(clazz, comm);
	}

}
