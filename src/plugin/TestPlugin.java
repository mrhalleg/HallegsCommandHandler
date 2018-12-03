package plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import commandManagement.CommandManager;
import commandManagement.CommandManager.PluginCommand;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			CommandManager.manage(this, new Class<?>[] { TestPlugin.class }, new Class<?>[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PluginCommand(name = "test")
	public static boolean test(Player p) {
		p.sendMessage("test command executed!");
		return true;
	}

	@PluginCommand(name = "test")
	public static boolean test(Player p, int i) {
		p.sendMessage("test command executed! with arguent " + i);
		return true;
	}
}
