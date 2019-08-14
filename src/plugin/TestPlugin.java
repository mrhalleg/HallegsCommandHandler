package plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import commandManagement.CommandManager;
import commandManagement.CommandManager.PluginCommand;
import commandManagement.CommandManager.UseDefaultConverter;
import converter.StringConverter;

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

	@PluginCommand(name = "test a b c d eeeee")
	public static boolean a(Player p) {
		p.sendMessage("test command executed! A");
		return true;
	}

	@PluginCommand(name = "test a b c d ttttt")
	public static boolean b(Player p) {
		p.sendMessage("test command executed! B");
		return true;
	}
}
