package commandManagement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import annotations.PluginCommand;
import annotations.PluginCommandContainer;
import arguments.Argument;
import arguments.BooleanArgument;
import arguments.DoubleArgument;
import arguments.IntegerArgument;
import arguments.ItemArgument;
import arguments.MaterialArgument;
import arguments.PlayerNameArgument;
import arguments.StringArgument;
import exceptions.CommandManagerException;

public class CommandManager implements CommandExecutor, TabCompleter {
	private static CommandManager singleton;

	private final static Class<?>[] standardArgumentClasses = { StringArgument.class,
			IntegerArgument.class, BooleanArgument.class, PlayerNameArgument.class,
			ItemArgument.class, MaterialArgument.class, DoubleArgument.class };

	private List<SubCommand> subs;

	private JavaPlugin plugin;

	private CommandManager(JavaPlugin plugin) {
		this.subs = new LinkedList<>();
		this.plugin = plugin;
	}

	private void loadClasses(JavaPlugin plugin, Class<?>[] commandClasses)
			throws CommandManagerException {
		for (Class<?> c : commandClasses) {
			loadClass(this.plugin, c);
		}
	}

	private void loadClass(JavaPlugin plugin, Class<?> c) throws CommandManagerException {
		plugin.getLogger().info("loading command class " + c.getName());
		PluginCommandContainer classAnno = c.getAnnotation(PluginCommandContainer.class);
		List<Class<?>> defaults = new ArrayList<>();
		for (Class<?> arg : standardArgumentClasses) {
			defaults.add(arg);
		}

		if (classAnno != null) {
			outer: for (Class<?> def : classAnno.defaults()) {
				if (!Argument.class.isAssignableFrom(def)) {
					throw new CommandManagerException("The class " + def.getName()
							+ " is listed as a default argument in " + c.getName()
							+ " but does not extend Argument.");
				}
				for (Class<?> arg : defaults) {
					if (Argument.getClassFor(arg).equals(Argument.getClassFor(arg))) {
						continue outer;
					}
				}
				defaults.add(c);
			}
		}

		for (Class<?> clazz : defaults) {
			System.out.println(clazz + " for " + Argument.getClassFor(clazz));
		}

		Method[] meth = c.getMethods();
		for (Method m : meth) {

			PluginCommand anno = m.getAnnotation(PluginCommand.class);
			if (anno == null) {
				continue;
			}

			plugin.getLogger().info("loading command method " + m.getName());
			ArgumentList list = ArgumentList.loadArgumentList(m, defaults);
			addArgumentList(anno.command().split(" "), list, this);
		}
	}

	private void addArgumentList(String[] s, ArgumentList arg, CommandManager manager) {
		for (SubCommand comm : manager.subs) {
			if (comm.getName().equals(s[0])) {
				if (comm.addArgumentList(s, arg)) {
					return;
				}
			}
		}
		SubCommand newComm = new SubCommand(s[0]);
		manager.subs.add(newComm);
		newComm.addArgumentList(s, arg);
	}

	private void registerCommands() {
		for (SubCommand sub : this.subs) {
			this.plugin.getCommand(sub.getName()).setExecutor(this);
		}
	}

	private void registeTapComplete() {
		for (SubCommand sub : this.subs) {
			this.plugin.getCommand(sub.getName()).setTabCompleter(this);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command comm, String label, String[] args) {
		String[] array = new String[args.length + 1];
		array[0] = comm.getLabel();

		for (int i = 0; i < args.length; i++) {
			array[i + 1] = args[i];
		}

		for (int i = 0; i < this.subs.size(); i++) {
			if (this.subs.get(i).handle(array, sender)) {
				sender.sendMessage("command executed!");
				return true;
			}
		}
		sender.sendMessage("Command not found!");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command comm, String label,
			String[] args) {
		List<String> complete = new LinkedList<>();

		String[] array = new String[args.length + 1];
		array[0] = comm.getLabel();

		for (int i = 0; i < args.length; i++) {
			array[i + 1] = args[i];
		}

		for (SubCommand sub : this.subs) {
			complete.addAll(sub.complete(array, sender));
		}
		return complete;
	}

	@Override
	public String toString() {
		String s = "";
		for (SubCommand c : this.subs) {
			s += c.toString();
		}
		return s;
	}

	public static void manage(JavaPlugin plugin, Class<?>[] commandClasses)
			throws CommandManagerException {
		singleton = new CommandManager(plugin);
		singleton.loadClasses(plugin, commandClasses);
		singleton.registerCommands();
		plugin.getLogger().info("loaded Commands:\n" + singleton.toString());
	}
}
