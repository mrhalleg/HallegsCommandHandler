package plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import arguments.StringConverter;
import commandManagement.CommandManager;
import commandManagement.CommandManager.PluginCommand;
import commandManagement.CommandManager.UseDefaultConverter;

@UseDefaultConverter(converter = StringConverter.class)
public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			CommandManager.manage(this, TestPlugin.class, TestPluginCommands.class);
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
