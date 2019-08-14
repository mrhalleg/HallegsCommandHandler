package handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import commandManagement.CommandManagerLoadingException;

/**
 * Manages
 */
public class SubCommandHandler {
	private String command;
	private List<CommandMethodHandler> methods;
	private List<SubCommandHandler> handler;

	public SubCommandHandler(String command) {
		this.handler = new ArrayList<>();
		this.methods = new ArrayList<>();
		this.command = command;
	}

	public boolean command(CommandSender sender, Command com, String[] args, int offset) {
		if (!args[offset].equals(this.command)) {
			return false;
		}

		for (SubCommandHandler e : this.handler) {
			if (e.command(sender, com, args, offset + 1)) {
				return true;
			}
		}

		for (CommandMethodHandler m : methods) {
			if (m.command(sender, com, args, offset + 1)) {
				return true;
			}
		}
		return false;
	}

	public List<String> complete(CommandSender sender, Command com, String[] args, int offset) {
		System.out.println("in subcommand " + command + " offset: " + offset);
		List<String> ret = new LinkedList<>();

		// do i need to add myself?
		if (offset == args.length) {
			System.out.println("adding myself");
			ret.add(this.command);
			return ret;
		}

		System.out.println("adding subs");
		for (SubCommandHandler e : this.handler) {
			ret.addAll(e.complete(sender, com, args, offset + 1));
		}

		System.out.println("adding methods");
		for (CommandMethodHandler e : this.methods) {
			ret.addAll(e.complete(sender, com, args, offset + 1, 0));
			System.out.println("returning in sub: " + ret);
		}
		return ret;
	}

	public boolean addHandler(String[] args, int offset, Method m) throws CommandManagerLoadingException {

		if (!args[offset].equals(command)) {
			return false;
		}

		if (offset == args.length - 1) {
			methods.add(new CommandMethodHandler(m));
			return true;
		}

		for (SubCommandHandler e : this.handler) {
			if (e.addHandler(args, offset + 1, m)) {
				return true;
			}
		}
		SubCommandHandler sub = new SubCommandHandler(args[offset + 1]);
		sub.addHandler(args, offset + 1, m);
		handler.add(sub);
		return true;
	}

	public String printTree(String pre, boolean last) {
		String out = "\n" + pre + "+- " + command;
		if (last) {
			pre = pre + "   ";
		} else {
			pre = pre + "|  ";
		}
		for (int i = 0; i < handler.size(); i++) {
			out += handler.get(i).printTree(pre, (i >= handler.size() - 1 && methods.isEmpty()));
		}

		for (int i = 0; i < methods.size(); i++) {
			out += methods.get(i).printTree(pre);
		}
		return out;
	}

	public boolean isCommand(String com) {
		return this.command.equals(com);
	}

}
