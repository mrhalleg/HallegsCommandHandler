package handler.builder;

import commandManagement.CommandManagerFactory.CommandClass;
import handler.SubCommand;

public class SubCommandBuilder {
	public SubCommand build(Class<?> clazz, CommandClass anno) {
		return new SubCommand(clazz, anno);
	}
}
