package handler;

import commandManagement.CommandManagerFactory.CommandClass;

/**
 * Manages
 */
public class SubCommand extends CommandHandler {

	public SubCommand(Class<?> clazz, CommandClass anno) {
		super(clazz, anno);
	}

}
