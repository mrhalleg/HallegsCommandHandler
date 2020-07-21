package handler;

import commandManagement.CommandManager.CommandClass;

/**
 * Manages
 */
public class SubCommand extends CommandHandler {

	public SubCommand(Class<?> clazz, CommandClass anno) {
		super(clazz, anno);
	}

}
