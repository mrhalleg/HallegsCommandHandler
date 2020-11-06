package handler;

import commandManagement.CommandManagerFactory.CommandClass;
import commandManagement.result.command.CommandFailResult;
import commandManagement.result.command.CommandMehtodFailResult;
import commandManagement.result.command.CommandResult;
import commandManagement.result.command.CommandSuccesResult;
import commandManagement.result.method.MethodFailResult;
import commandManagement.result.method.MethodResult;
import commandManagement.result.method.MethodSuccesResult;
import handler.method.MethodParameter;

import java.util.LinkedList;
import java.util.List;

public abstract class CommandHandler extends CommandTreeNode {
	protected String name;
	protected List<MethodParameter> methods;
	protected List<SubCommand> handler;
	protected String[] alias;

	public CommandHandler(Class<?> clazz, CommandClass anno) {
		super();
		this.name = anno.name();
		this.alias = anno.alias();
		this.methods = new LinkedList<>();
		this.handler = new LinkedList<>();
	}

	public void addMethod(MethodParameter method) {
		this.methods.add(method);
	}

	public void addCommand(SubCommand command) {
		this.handler.add(command);
	}

	public CommandResult command(String[] args, int offset, Object environment) {
		if (offset >= args.length) {
			return null;
		}

		if (!isMe(args[offset])) {
			return null;
		}

		for (SubCommand e : this.handler) {
			CommandResult ret = e.command(args, offset + 1, environment);
			if (ret != null) {
				return ret;
			}
		}

		CommandMehtodFailResult fail = new CommandMehtodFailResult();
		for (MethodParameter m : this.methods) {
			MethodResult ret = m.command(args, offset + 1, environment);
			if (ret instanceof MethodSuccesResult) {
				return new CommandSuccesResult((MethodSuccesResult) ret);
			} else if (ret instanceof MethodFailResult) {
				fail.add((MethodFailResult) ret);
			}
		}
		if (!fail.isEmpty()) {
			return fail;
		}

		return new CommandFailResult(this);
	}

	public List<String> complete(String[] args, int offset) {
		List<String> ret = new LinkedList<>();

		if (offset == args.length) {
			ret.add(this.name);
			return ret;
		}

		if (!isMe(args[offset])) {
			return ret;
		}

		for (SubCommand e : this.handler) {
			ret.addAll(e.complete(args, offset + 1));
		}

		for (MethodParameter e : this.methods) {
			ret.addAll(e.complete(args, offset + 1));
		}
		return ret;
	}

	protected boolean isMe(String str) {
		if (this.name.equals(str)) {
			return true;
		}

		for (String a : this.alias) {
			if (a.equals(str)) {
				return true;
			}
		}

		return false;
	}

	public String printTree(String pre, boolean last) {

		String out = "\n" + pre + "+- " + toString();
		if (last) {
			pre = pre + "   ";
		} else {
			pre = pre + "|  ";
		}
		for (int i = 0; i < this.handler.size(); i++) {
			out += this.handler.get(i).printTree(pre, (i >= this.handler.size() - 1 && this.methods.isEmpty()));
		}

		for (int i = 0; i < this.methods.size(); i++) {
			out += this.methods.get(i).printTree(pre);
		}
		return out;
	}

	@Override
	public String toString() {
		return this.name + " [" + String.join(" ", this.alias) + "]";
	}

	public String getName() {
		return this.name;
	}
}
