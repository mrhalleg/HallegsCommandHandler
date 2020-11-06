package handler.builder;

import commandManagement.annotations.CommandClass;
import handler.command.SubCommand;

public class SubCommandBuilder {
	public SubCommand build(Class<?> clazz, CommandClass anno) {
		return new SubCommand(clazz, anno);
	}
}
