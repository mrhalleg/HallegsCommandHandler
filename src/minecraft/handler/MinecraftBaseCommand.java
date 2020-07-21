package minecraft.handler;

import commandManagement.CommandManager.CommandClass;
import handler.BaseCommand;
import minecraft.handler.MinecraftSubCommand.MinecraftCommandClass;

/**
 * Manages one base command. e.g. every user input beginning with "/test". one
 * command has one or more CommandMethods (which are managed by a
 * {@link #CommandMethodHandler}). Represents a Root int the Command .
 */
public class MinecraftBaseCommand extends BaseCommand {
	public String description;

	public MinecraftBaseCommand(Class<?> clazz, CommandClass comm, MinecraftCommandClass mcComm,
			MinecraftBaseCommandClass mcBase) {
		super(clazz, comm);
		description = mcBase.description();
	}

	public @interface MinecraftBaseCommandClass {
		String description();
	}
}
