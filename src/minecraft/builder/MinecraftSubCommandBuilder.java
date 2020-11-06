package minecraft.builder;

import commandManagement.annotations.CommandClass;
import handler.builder.SubCommandBuilder;
import handler.command.SubCommand;
import minecraft.handler.MinecraftSubCommand;
import minecraft.handler.MinecraftSubCommand.MinecraftCommandClass;

public class MinecraftSubCommandBuilder extends SubCommandBuilder {
	@Override
	public SubCommand build(Class<?> clazz, CommandClass anno) {
		return new MinecraftSubCommand(clazz, anno, clazz.getAnnotation(MinecraftCommandClass.class));
	}
}
