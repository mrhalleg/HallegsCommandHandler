package plugin;

import commandManagement.CommandManagerFactory;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import testCommands.BoolCommands;

public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			CommandManagerFactory.createCommandManager(new SubCommandBuilder(), new BaseCommandBuilder(),
					new MethodBuilder(), BoolCommands.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
