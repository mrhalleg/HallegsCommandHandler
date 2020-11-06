package minecraft.builder;

import commandManagement.CommandManagerFactory.CommandClass;
import handler.builder.SubCommandBuilder;
import handler.command.CommandHandler;
import minecraft.handler.MinecraftSubCommand;
import minecraft.handler.MinecraftSubCommand.MinecraftCommandClass;

public class MinecraftSubCommandBuilder extends SubCommandBuilder {
	@Override
	public CommandHandler build(Class<?> clazz, CommandClass anno) {
		return new MinecraftSubCommand(clazz, anno, clazz.getAnnotation(MinecraftCommandClass.class));
	}
}
