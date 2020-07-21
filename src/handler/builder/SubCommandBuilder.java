package handler.builder;

import commandManagement.CommandManager.CommandClass;
import handler.SubCommand;

public class SubCommandBuilder {
	public SubCommand build(Class<?> clazz, CommandClass anno) {
		return new SubCommand(clazz, anno);
	}
}
