package handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import commandManagement.CommandManager;
import commandManagement.CommandManager.PluginCommand;
import commandManagement.CommandManager.UseConverter;
import commandManagement.CommandManagerLoadingException;
import converter.Converter;
import net.md_5.bungee.api.ChatColor;

/**
 * Manages one Command and Method. Represents a Leaf in the command Forest.
 */
public class CommandMethodHandler {
	private Method methode;
	private List<Converter> converter;
	private String permission;
	private boolean opOnly;
	private boolean playerOnly;

	public CommandMethodHandler(Method m) throws CommandManagerLoadingException {

		// initialize fields
		this.methode = m;
		this.converter = new ArrayList<>();
		this.opOnly = m.getAnnotation(PluginCommand.class).opOnly();
		this.permission = m.getAnnotation(PluginCommand.class).permission();

		// determine playerOnly value by type of first Argument
		this.playerOnly = false;
		if (m.getParameterTypes()[0] == Player.class) {
			this.playerOnly = true;
		} else if (m.getParameterTypes()[0] != CommandSender.class) {
			throw new CommandManagerLoadingException("The methode has no CommandSender or Player as first Parameter.");
		}

		if (!Modifier.isStatic(m.getModifiers())) {
			throw new CommandManagerLoadingException("The methode is not static.");
		}

		if (m.getReturnType() != boolean.class) {
			throw new CommandManagerLoadingException("The methode does not return a boolean value.");
		}

		// add arguments of method to list
		for (int i = 1; i < m.getParameterTypes().length; i++) {
			Annotation[] annos = m.getParameterAnnotations()[i];
			UseConverter anno = null;
			for (int e = 0; e < annos.length; e++) {
				if (annos[e] instanceof UseConverter) {
					anno = (UseConverter) annos[e];
					break;
				}
			}

			Class<Converter> convClass = null;

			// get the class from type() in the annotation or use default argument class
			if (anno == null || anno.type() == Converter.class) {
				convClass = CommandManager.getDefaultArgument(m.getParameterTypes()[i]);
			} else {
				try {
					convClass = anno.type();
				} catch (ClassCastException x) {
					throw new CommandManagerLoadingException("The Converter class \"" + anno.type().getName()
							+ "\" of the " + i + "parameter is not a child of Converter.");
				}
			}

			if (convClass == null) {
				throw new CommandManagerLoadingException(
						"No Converter Class could be found for the " + i + " parameter");
			}

			try {
				if (!ClassUtils.isAssignable(
						convClass.getMethod("check", CommandSender.class, String.class).getReturnType(),
						m.getParameterTypes()[i], true)) {
					throw new CommandManagerLoadingException(
							"The Converter class \"" + anno.type().getName() + "\" for the " + i
									+ " parameter does not return the right type for the parameter type. (should be \""
									+ m.getParameterTypes()[i].getName() + "" + "\", is \""
									+ convClass.getGenericSuperclass().getTypeName() + "\")");
				}
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}

			// get a instance of the class
			Converter arg = null;
			try {
				arg = convClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}

			// add the params from the annotation to the new instance
			if (anno != null) {
				arg.setParams(anno.params());
			}

			// add the argument instance to the list
			this.converter.add(arg);
		}

	}

	public List<Object> getParameter(CommandSender sender, String[] args, int offset) {

		if (args.length - offset != this.converter.size()) {
			return null;
		}

		List<Object> ret = new LinkedList<>();

		for (int i = 0; i < (args.length - offset); i++) {
			Object o = this.converter.get(i).check(sender, args[offset + i]);
			if (o != null) {
				ret.add(o);
			} else {
				return null;
			}
		}

		return ret;
	}

	public boolean command(CommandSender sender, Command command, String[] args, int offset) {
		try {
			// check if this command can be used and with which parameters
			List<Object> ret = getParameter(sender, args, offset);
			if (ret == null) {
				return false;
			}

			// check for a valid CommandSender
			if (this.opOnly && !sender.isOp()
					|| (!this.permission.isEmpty() && !sender.hasPermission(this.permission))) {
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

	public List<String> complete(CommandSender sender, Command command, String[] args, int offset, int argnr) {

		System.out.println("in method offset: " + offset + " argnr: " + argnr);
		List<String> ret = new LinkedList<>();

		// too many arguments for me?
		if (argnr >= converter.size()) {
			System.out.println("argnr to big!");
			return ret;
		}

		// do i need to add the current converter?
		if (offset == args.length) {
			System.out.println("adding current!");
			ret.addAll(this.converter.get(argnr).complete(sender));
			System.out.println("returning in mehtod: " + ret);
			return ret;
		}

		// can i go deeper?
		if (converter.get(argnr).check(sender, args[offset]) == null) {
			System.out.println("doesnt fit anymore");
			return ret;
		}

		System.out.println("going deeper");
		return complete(sender, command, args, offset + 1, argnr + 1);
	}

	public String printTree(String pre) {
		String out = "\n" + pre + "+- (";

		for (int i = 1; i < this.methode.getParameterTypes().length; i++) {
			out += this.methode.getParameterTypes()[i].getSimpleName();
			if (!(i == this.methode.getParameterTypes().length - 1)) {
				out += ", ";
			}
		}
		out += ")";
		return out;
	}
}