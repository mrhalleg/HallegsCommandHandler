package plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import annotations.PluginCommand;
import commandManagement.CommandManager;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			CommandManager.manage(this, new Class[] { TestPlugin.class, Commands.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PluginCommand(command = "test")
	public static boolean test(Player p) {
		p.sendMessage("test command executed!");

		return true;
	}

	@PluginCommand(command = "test")
	public static boolean test(Player p, int i) {
		p.sendMessage("test command executed! with arguent " + i);
		return true;
	}
}
