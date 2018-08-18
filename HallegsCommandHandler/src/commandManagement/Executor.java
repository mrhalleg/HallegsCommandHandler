package commandManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import commandManagement.CommandManager.PluginCommand;
import net.md_5.bungee.api.ChatColor;

public class Executor implements CommandExecutor, TabCompleter {
	private Method methode;
	private List<Argument> arguments;
	private String permission;
	private boolean opOnly;
	private boolean playerOnly;

	public Executor(Method m) throws Exception {
		this.methode = m;
		this.arguments = new ArrayList<>();
		this.opOnly = m.getAnnotation(PluginCommand.class).opOnly();
		this.permission = m.getAnnotation(PluginCommand.class).permission();

		this.playerOnly = false;
		if (m.getParameterTypes()[0] == Player.class) {
			this.playerOnly = true;
		} else if (m.getParameterTypes()[0] != CommandSender.class) {
			throw new Exception("The methode " + m.getName()
					+ " has PluginCommand Anatation but has no CommandSender as first Parameter.");
		}
		for (int i = 1; i < m.getParameterTypes().length; i++) {
			if (CommandSender.class.isAssignableFrom(m.getParameterTypes()[i])) {
				throw new Exception("The methode " + m.getName()
						+ " has PluginCommand Anatation but has Parameters which dont inherit from Arguments.");
			}
			this.arguments.add((Argument) m.getParameterTypes()[i].newInstance());
		}
	}

	private List<Argument> getParameter(CommandSender sender, String[] args) {
		if (args.length != this.arguments.size()) {
			return null;
		}

		List<Argument> ret = new LinkedList<>();

		for (int i = 0; i < args.length; i++) {
			Argument a = this.arguments.get(i).run(sender, args[i]);
			if (a != null) {
				ret.add(a);
			} else {
				return null;
			}
		}

		return ret;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {

			if (this.opOnly && !sender.isOp() || (!this.permission.isEmpty() && !sender
					.hasPermission(this.permission))) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");
				return true;
			}
			if (this.playerOnly && !(sender instanceof Player)) {
				sender.sendMessage("You have to be a player to use this command!");
				return true;
			}

			List<Argument> ret = getParameter(sender, args);

			if (ret == null) {
				return false;
			}

			Object[] obj = new Object[ret.size() + 1];
			obj[0] = sender;
			for (int i = 1; i < obj.length; i++) {
				obj[1] = ret.get(i - 1);
			}

			this.methode.invoke(null, obj);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length > this.arguments.size()) {
			return new ArrayList<>();
		}
		return this.arguments.get(args.length - 1).tap(sender, args);
	}
}