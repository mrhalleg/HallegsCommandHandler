package testCommands;

import commandManagement.CommandManager;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;

public class Main {

	public static void main(String[] args) {
		try {
			CommandManager.manage(new SubCommandBuilder(), new BaseCommandBuilder(),
					new MethodBuilder(), BoolCommand.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
