package handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import commandManagement.CommandManagerLoadingException;

/**
 * Manages one base command. e.g. every user input beginning with "/test". one
 * command has one or more CommandMethods (which are managed by a
 * {@link #CommandMethodHandler}). Represents a Root int the Command .
 */
public class BaseCommandHandler implements CommandExecutor, TabCompleter {
	private List<SubCommandHandler> handler;
	private JavaPlugin plugin;

	public BaseCommandHandler(JavaPlugin plugin) {
		this.handler = new ArrayList<>();
		this.plugin = plugin;
	}

	public void addHandler(String[] args, Method m) throws CommandManagerLoadingException {
		PluginCommand c = plugin.getCommand(args[0]);
		c.setExecutor(this);
		c.setTabCompleter(this);

		for (SubCommandHandler h : handler) {
			if (h.addHandler(args, 0, m)) {
				return;
			}
		}
		SubCommandHandler sub = new SubCommandHandler(args[0]);
		sub.addHandler(args, 0, m);
		handler.add(sub);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command com, String label, String[] arr) {
		String[] args = new String[arr.length + 1];
		args[0] = label;
		for (int i = 0; i < arr.length; i++) {
			args[i + 1] = arr[i];
		}

		for (SubCommandHandler e : this.handler) {
			if (e.isCommand(label)) {
				if (!e.command(sender, com, args, 0)) {
					sender.sendMessage("Invalid use of Command. Possible usses:");
					sender.sendMessage(e.printTree("", true));
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command com, String label, String[] arr) {
		System.out.println("------");
		List<String> ret = new LinkedList<>();

		List<String> list = new LinkedList<String>();
		list.add(label);
		boolean trimed = false;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].isBlank()) {
				trimed = true;
				break;
			}
			list.add(arr[i]);
		}
		if (!trimed) {
			list.remove(list.size() - 1);
		}

		trimed = list.size() == arr.length;

		String[] args = list.toArray(new String[0]);

		for (int i = 0; i < args.length; i++) {
			System.out.println("args[" + i + "] = \"" + args[i] + "\"");
		}
		System.out.println("args.legngth = " + args.length);
		System.out.println("trimed: " + trimed);

		for (SubCommandHandler e : this.handler) {
			if (e.isCommand(label)) {
				ret.addAll(e.complete(sender, com, args, 0));
				break;
			}
		}

		System.out.println("ret = " + ret);

		return ret;
	}

	public void printTree() {
		String out = "/";
		for (int i = 0; i < handler.size(); i++) {
			out += handler.get(i).printTree("", i >= handler.size() - 1);
		}

		for (String s : out.split("\n")) {
			plugin.getLogger().info(s);
		}
	}
}
