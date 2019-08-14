package plugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import commandManagement.CommandManager.PluginCommand;
import commandManagement.CommandManager.UseDefaultConverter;
import converter.StringConverter;

@UseDefaultConverter(converter = StringConverter.class)
public class TestPluginCommands {

	@PluginCommand(name = "bool and")
	public static boolean and(Player p, boolean a, boolean b) {
		p.sendMessage(" a AND b = " + (a && b));
		return true;
	}

	@PluginCommand(name = "bool or")
	public static boolean or(Player p, boolean a, boolean b) {
		p.sendMessage(" a OR b = " + (a || b));
		return true;
	}

	@PluginCommand(name = "bool")
	public static boolean bool(Player p, boolean a) {
		p.sendMessage(" value = " + a);
		return true;
	}

	@PluginCommand(name = "add", opOnly = false, permission = "")
	public static boolean addCommand(CommandSender sender, int arg1, int arg2) {
		sender.sendMessage(arg1 + " + " + arg2 + " = " + (arg1 + arg2));
		return true;
	}
}
