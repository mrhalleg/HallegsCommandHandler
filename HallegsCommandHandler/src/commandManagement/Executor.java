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

public class Executor implements CommandExecutor, TabCompleter {
	private Method methode;
	private List<Argument> arguments;

	public Executor(Method m) throws Exception {
		this.methode = m;
		this.arguments = new ArrayList<>();
		if (m.getParameterTypes()[0] != CommandSender.class) {
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

	private List<Argument> getParameter(String[] args) {
		if (args.length != this.arguments.size()) {
			return null;
		}

		List<Argument> ret = new LinkedList<>();

		for (int i = 0; i < args.length; i++) {
			Argument a = this.arguments.get(i).run(args[i]);
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
			List<Argument> ret = getParameter(args);

			if (ret == null) {
				return false;
			}

			Object[] obj = new Object[ret.size() + 1];
			obj[0] = sender;
			for (int i = 1; i < obj.length; i++) {
				obj[1] = ret.get(i - 1);
			}

			return (boolean) this.methode.invoke(null, obj);
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