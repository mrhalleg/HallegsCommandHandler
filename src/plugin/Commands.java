package plugin;

import org.bukkit.entity.Player;

import annotations.PluginArgument;
import annotations.PluginCommand;
import arguments.IntegerArgument;

public class Commands {
	@PluginCommand(command = "command")
	public static boolean base(Player p) {
		p.sendMessage("you executed a command base command with ");
		return true;
	}

	@PluginCommand(command = "command int")
	public static boolean sub(Player p, @PluginArgument(type = IntegerArgument.class) int i) {
		p.sendMessage("you executed a command with " + i);
		return true;
	}

	@PluginCommand(command = "command player")
	public static boolean player(Player p, Player player) {
		p.sendMessage("ypu booped " + player.getName());
		player.sendMessage("boop!");
		return true;
	}

	@PluginCommand(command = "command player gogo test")
	public static boolean gogog(Player p, Player player) {
		p.sendMessage("ypu booped " + player.getName());
		player.sendMessage("boop!");
		return true;
	}

	@PluginCommand(command = "array")
	public static boolean array(Player p, Player s, Player... is) {
		for (Player i : is) {
			System.out.println("i: " + i);
		}
		return true;
	}
}
