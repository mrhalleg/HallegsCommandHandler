package minecraft.builder;

import commandManagement.CommandManager.CommandClass;
import handler.BaseCommand;
import handler.builder.BaseCommandBuilder;
import minecraft.handler.MinecraftBaseCommand;
import minecraft.handler.MinecraftBaseCommand.MinecraftBaseCommandClass;

public class MinecraftBaseCommandBuilder extends BaseCommandBuilder {

	@Override
	public BaseCommand build(Class<?> clazz, CommandClass comm) {
		return new MinecraftBaseCommand(clazz, comm, clazz.getAnnotation(MinecraftBaseCommandClass.class));
	}

}
