package plugin;

import org.bukkit.plugin.java.JavaPlugin;

import commandManagement.CommandManagerFactory;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;
import testCommands.BoolCommand;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			CommandManagerFactory.createCommandManager(new SubCommandBuilder(), new BaseCommandBuilder(),
					new MethodBuilder(), BoolCommand.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
