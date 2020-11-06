package minecraft.handler;

import commandManagement.CommandManagerFactory.CommandClass;
import handler.SubCommand;

/**
 * Manages
 */
public class MinecraftSubCommand extends SubCommand {

	public MinecraftSubCommand(Class<?> clazz, CommandClass anno, MinecraftCommandClass mc) {
		super(clazz, anno);
	}

	public @interface MinecraftCommandClass {
		boolean opOnly();

		String permission();
	}

}
