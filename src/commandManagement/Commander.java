package commandManagement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class Commander implements CommandExecutor, TabCompleter {
	List<Executor> exes;

	public Commander(PluginCommand c) {
		this.exes = new ArrayList<>();
		c.setExecutor(this);
		c.setTabCompleter(this);
	}

	public void addMehtodeExecutor(Executor m) {
		this.exes.add(m);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (Executor e : this.exes) {
			if (e.command(sender, command, label, args)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label,
			String[] args) {
		List<Executor> compatible = new LinkedList<>();

		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			for (int e = 0; e < this.exes.size(); e++) {
				if (this.exes.get(e).getParameter(sender, args) != null) {
					compatible.add(this.exes.get(e));
				}
			}
		}

		List<String> ret = new LinkedList<>();

		for (Executor e : compatible) {
			ret.addAll(e.complete(sender, command, label, args));
		}
		return ret;
	}
}
