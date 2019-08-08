package handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

/**
 * Manages one base command. e.g. every user input beginning with "/test". one
 * command has one or more CommandMethods (which are managed by a
 * {@link #CommandMethodHandler}).
 */
public class BaseCommandHandler implements CommandExecutor, TabCompleter {
	List<CommandMethodHandler> handler;

	public BaseCommandHandler(PluginCommand c) {
		this.handler = new ArrayList<>();
		c.setExecutor(this);
		c.setTabCompleter(this);
	}

	public void addMehtodeExecutor(CommandMethodHandler m) {
		this.handler.add(m);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (CommandMethodHandler e : this.handler) {
			if (e.command(sender, command, label, args)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<CommandMethodHandler> compatible = new LinkedList<>();

		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			for (int e = 0; e < this.handler.size(); e++) {
				if (this.handler.get(e).getParameter(sender, args) != null) {
					compatible.add(this.handler.get(e));
				}
			}
		}

		List<String> ret = new LinkedList<>();

		for (CommandMethodHandler e : compatible) {
			ret.addAll(e.complete(sender, command, label, args));
		}
		return ret;
	}

	public void printTree() {
		for (CommandMethodHandler h : handler) {
			h.printTree();
		}
	}
}
