package plugin;

import org.bukkit.entity.Player;

import arguments.StringConverter;
import commandManagement.CommandManager.PluginCommand;
import commandManagement.CommandManager.UseDefaultConverter;

@UseDefaultConverter(converter = StringConverter.class)
public class TestPluginCommands {

	@PluginCommand(name = "bool and")
	public static boolean and(Player p, boolean a, boolean b) {
		p.sendMessage(" a OR b = " + (a && b));
		return true;
	}

	@PluginCommand(name = "bool or")
	public static boolean or(Player p, boolean a, boolean b) {
		p.sendMessage(" a OR b = " + (a || b));
		return true;
	}
}
