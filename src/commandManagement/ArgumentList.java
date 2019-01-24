package commandManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import annotations.PluginCommand;
import arguments.Argument;
import exceptions.CommandManagerException;

public class ArgumentList {
	private Method meth;
	private List<Argument> arguments;
	private boolean opOnly;
	private String permission;
	private boolean playerOnly;

	public ArgumentList(Method meth, List<Argument> arguments, boolean opOnly, String permission,
			boolean playerOnly) {
		super();
		this.meth = meth;
		this.arguments = arguments;
		this.opOnly = opOnly;
		this.permission = permission;
		this.playerOnly = playerOnly;
	}

	public boolean handle(String[] input, CommandSender sender) {
		if (input.length != this.arguments.size()) {
			return false;
		}

		Object[] objs = new Object[input.length + 1];
		objs[0] = sender;

		for (int i = 0; i < input.length; i++) {
			objs[i + 1] = this.arguments.get(i).convert(sender, input[i]);
			if (objs[i + 1] == null) {
				return false;
			}
		}

		return runMeth(objs);
	}

	public List<String> complete(String[] args, CommandSender sender) {
		int i = 0;
		for (; i < args.length && this.arguments.size() > i; i++) {
			if (this.arguments.get(i).convert(sender, args[i]) == null) {

			}
		}
		if (this.arguments.size() > i) {
			return this.arguments.get(i).complete(sender);
		}
		return new LinkedList<>();
	}

	private boolean runMeth(Object[] args) {
		try {
			return (Boolean) this.meth.invoke(null, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			String expected = "expected:\n";
			for (Class<?> c : this.meth.getParameterTypes()) {
				expected += c.toString() + "\n";
			}
			System.out.println(expected);
			String actual = "actual:\n";
			for (Object o : args) {
				actual += o.getClass().toString() + "\n";
			}
			System.out.println(actual);
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String toString() {
		String s = "(";
		for (Argument argument : this.arguments) {
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
		for (int i = 1; i < meth.getParameterCount(); i++) {
			arguments.add(Argument.loadArgument(meth, i, defaults));
		}
		return new ArgumentList(meth, arguments, opOnly, permission, playerOnly);
	}
}
