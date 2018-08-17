package commandManagement;

import org.bukkit.command.CommandSender;

import commandManagement.CommandManager.PluginCommand;

public class MyCommands {
	@PluginCommand()
	public static boolean hallegsrecipebooks(CommandSender sender, StringArgument s) {
		String message = "the argument was: " + s.getString();
		System.out.println(message);
		sender.sendMessage(message);
		return true;
	}
}
