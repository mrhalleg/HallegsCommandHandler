package minecraft.builder;

import commandManagement.CommandManagerFactory.CommandClass;
import handler.builder.BaseCommandBuilder;
import handler.command.BaseCommand;
import minecraft.handler.MinecraftBaseCommand;
import minecraft.handler.MinecraftBaseCommand.MinecraftBaseCommandClass;
import minecraft.handler.MinecraftSubCommand.MinecraftCommandClass;

public class MinecraftBaseCommandBuilder extends BaseCommandBuilder {

	@Override
	public BaseCommand build(Class<?> clazz, CommandClass comm) {
		return new MinecraftBaseCommand(clazz, comm, clazz.getAnnotation(MinecraftCommandClass.class),
				clazz.getAnnotation(MinecraftBaseCommandClass.class));
	}

}
