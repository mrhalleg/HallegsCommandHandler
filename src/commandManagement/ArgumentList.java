package commandManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import annotations.PluginCommand;
import arguments.Argument;
import exceptions.CommandManagerException;

public class ArgumentList {
	protected Method meth;
	protected List<Argument> arguments;
	protected boolean opOnly;
	protected String permission;
	protected boolean playerOnly;

	public ArgumentList(Method meth, List<Argument> arguments, boolean opOnly, String permission,
			boolean playerOnly) {
		super();
		this.meth = meth;
		this.arguments = arguments;
		this.opOnly = opOnly;
		this.permission = permission;
		this.playerOnly = playerOnly;
	}

	public boolean handle(ArrayList<String> input, CommandSender sender) {
		Object[] objs = new Object[this.meth.getParameterCount()];
		objs[0] = sender;
		if (input.size() != this.arguments.size()) {
			return false;
		}
		for (int i = 0; i < this.arguments.size(); i++) {
			Argument<?> arg = this.arguments.get(i);
			objs[i + 1] = arg.convert(sender, input.get(i));
			if (objs[i + 1] == null) {
				return false;
			}
		}
		return runMeth(objs);
	}

	public List<String> complete(List<String> args, CommandSender sender) {
		List<String> complete = new ArrayList<>();
		if (this.arguments.size() < args.size()) {
			return complete;
		}
		for (int i = 0; i < args.size(); i++) {
			if (args.get(i).isEmpty()) {
				complete.addAll(this.arguments.get(i).complete(sender));
				return complete;
			} else if (this.arguments.get(i).convert(sender, args.get(i)) == null) {
				return complete;
			}
		}
		return complete;
	}

	protected boolean runMeth(Object[] args) {
		try {
			return (Boolean) this.meth.invoke(null, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String logString(String prefix) {
		String s = prefix + "(";
		for (Argument<?> argument : this.arguments) {
			s += argument.toString();
		}
		return s + ")\n";
	}

	public static ArgumentList loadArgumentList(Method meth, List<Class<?>> defaults)
			throws CommandManagerException {
		// check for right method
		if (!Modifier.isStatic(meth.getModifiers())) {
			throw new CommandManagerException("The methode " + meth.getName()
					+ " has PluginCommand Annotation but is not static.");
		}
		if (meth.getReturnType() != boolean.class) {
			throw new CommandManagerException("The methode " + meth.getName()
					+ " has PluginCommand Annotation but does not return a boolean value.");
		}
		// setup attributes
		List<Argument> arguments = new ArrayList<>();
		boolean opOnly = meth.getAnnotation(PluginCommand.class).opOnly();
		String permission = meth.getAnnotation(PluginCommand.class).permission();

		// determine playerOnly value by type of first Argument
		boolean playerOnly = false;
		if (meth.getParameterTypes()[0] == Player.class) {
			playerOnly = true;
		} else if (meth.getParameterTypes()[0] != CommandSender.class) {
			throw new CommandManagerException("The methode " + meth.getName()
					+ " has PluginCommand Annotation but has no CommandSender as first Parameter.");
		}

		// load arguments
		boolean isArray = false;
		for (int i = 1; i < meth.getParameterCount(); i++) {
			arguments.add(Argument.loadArgument(meth, i, defaults));
			if (meth.isVarArgs()) {
				isArray = true;
			}
		}
		if (isArray) {
			return new VarArgumentList(meth, arguments, opOnly, permission, playerOnly);
		} else {
			return new ArgumentList(meth, arguments, opOnly, permission, playerOnly);
		}
	}
}
