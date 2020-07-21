package handler.builder;

import commandManagement.CommandManager.CommandClass;
import handler.BaseCommand;

public class BaseCommandBuilder {

	public BaseCommand build(Class<?> clazz, CommandClass comm) {
		return new BaseCommand(clazz, comm);
	}

}
