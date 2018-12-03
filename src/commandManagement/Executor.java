package commandManagement;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import commandManagement.CommandManager.ArgumentParameter;
import commandManagement.CommandManager.PluginCommand;
import net.md_5.bungee.api.ChatColor;

public class Executor {
	private Method methode;
	private List<Argument> arguments;
	private String permission;
	private boolean opOnly;
	private boolean playerOnly;

	public Executor(Method m) throws Exception {

		// initialize fields
		this.methode = m;
		this.arguments = new ArrayList<>();
		this.opOnly = m.getAnnotation(PluginCommand.class).opOnly();
		this.permission = m.getAnnotation(PluginCommand.class).permission();

		// determine playerOnly value by type of first Argument
		this.playerOnly = false;
		if (m.getParameterTypes()[0] == Player.class) {
			this.playerOnly = true;
		} else if (m.getParameterTypes()[0] != CommandSender.class) {
			throw new Exception("The methode " + m.getName()
					+ " has PluginCommand Annotation but has no CommandSender as first Parameter.");
		}

		// add arguments of method to list
		for (int i = 1; i < m.getParameterTypes().length; i++) {
			Annotation[] annos = m.getParameterAnnotations()[i];
			ArgumentParameter anno = null;
			for (int e = 0; e < annos.length; e++) {
				if (annos[e] instanceof ArgumentParameter) {
					anno = (ArgumentParameter) annos[e];
					break;
				}
			}

			Class<Argument> argClass = null;

			// get the class from type() in the annotation or use default argument class
			if (anno == null || anno.type() == Argument.class) {
				argClass = CommandManager.getDefaultArgument(m.getParameterTypes()[i]);
			} else {
				try {
					argClass = anno.type();
				} catch (ClassCastException x) {
					throw new Exception("The Argument class " + anno.type()
							+ " referenced in the methode " + m.getName()
							+ " is not a child of Argument.");
				}
			}

			if (argClass.getMethod("check", CommandSender.class, String.class).getReturnType() != m
					.getParameterTypes()[i]) {
				throw new Exception("The Argument class " + anno.type()
						+ " referenced in the methode " + m.getName()
						+ " does not return the right type for the parameter type " + m
								.getParameterTypes()[i] + "(" + argClass.getGenericSuperclass()
						+ ")");
			}

			// get a instance of the class
			Argument arg = argClass.newInstance();

			// add the params from the annotation to the new instance
			if (anno != null) {
				arg.setParams(anno.params());
			}

			// add the argument instance to the list
			this.arguments.add(arg);
		}

	}

	public List<Object> getParameter(CommandSender sender, String[] args) {
		if (args.length != this.arguments.size()) {
			return null;
		}

		List<Object> ret = new LinkedList<>();

		for (int i = 0; i < args.length; i++) {
			Object o = this.arguments.get(i).check(sender, args[i]);
			if (o != null) {
				ret.add(o);
			} else {
				return null;
			}
		}

		return ret;
	}

	public boolean command(CommandSender sender, Command command, String label, String[] args) {
		try {
			// check if this command can be used and with which parameters
			List<Object> ret = getParameter(sender, args);
			if (ret == null) {
				return false;
			}

			// check for a valid CommandSender
			if (this.opOnly && !sender.isOp() || (!this.permission.isEmpty() && !sender
					.hasPermission(this.permission))) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");
				return true;
			}
			if (this.playerOnly && !(sender instanceof Player)) {
				sender.sendMessage("You have to be a player to use this command!");
				return true;
			}

			// call the method with the right parameters and sender
			Object[] obj = new Object[ret.size() + 1];
			obj[0] = sender;
			for (int i = 1; i < obj.length; i++) {
				obj[i] = ret.get(i - 1);
			}
			this.methode.invoke(null, obj);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<String> complete(CommandSender sender, Command command, String label,
			String[] args) {
		if (args.length > this.arguments.size()) {
			return new ArrayList<>();
		}
		return this.arguments.get(args.length - 1).complete(sender);
	}
}