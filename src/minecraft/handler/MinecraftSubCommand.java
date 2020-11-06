package minecraft.handler;

import commandManagement.CommandManagerFactory.CommandClass;
import handler.command.CommandHandler;

/**
 * Manages
 */
public class MinecraftSubCommand extends CommandHandler {

	public MinecraftSubCommand(Class<?> clazz, CommandClass anno, MinecraftCommandClass mc) {
		super(clazz, anno);
	}

	public @interface MinecraftCommandClass {
		boolean opOnly();

		String permission();
	}

}
