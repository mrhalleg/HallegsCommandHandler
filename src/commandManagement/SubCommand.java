package commandManagement;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class SubCommand {
	private String name;

	private List<ArgumentList> argumentLists;
	private List<SubCommand> subCommands;

	public SubCommand(String name) {
		this.argumentLists = new ArrayList<>();
		this.subCommands = new ArrayList<>();
		this.name = name;
	}

	// returns true if the input can be handled by this command
	public boolean handle(ArrayList<String> input, CommandSender sender) {
		if (input.size() < 1 || !input.get(0).equals(this.name)) {
			return false;
		}
		input.remove(0);

		for (SubCommand comm : this.subCommands) {
			if (comm.handle(new ArrayList<>(input), sender)) {
				return true;
			}
		}

		for (ArgumentList list : this.argumentLists) {
			if (list.handle(new ArrayList<>(input), sender)) {
				return true;
			}
		}
		return false;
	}

	public List<String> complete(List<String> args, CommandSender sender) {
		List<String> complete = new ArrayList<>();

		if (args.size() == 0 || args.get(0).isEmpty()) {
			complete.add(this.getName());
			return complete;
		} else if (!args.get(0).equals(this.name)) {
			return complete;
		}

		args.remove(0);
		for (SubCommand comm : this.subCommands) {
			complete.addAll(comm.complete(new ArrayList<>(args), sender));
		}

		for (ArgumentList list : this.argumentLists) {
			complete.addAll(list.complete(new ArrayList<>(args), sender));
		}

		return complete;
	}

	public String getName() {
		return this.name;
	}

	public boolean addArgumentList(List<String> s, ArgumentList arg) {
		if (!s.get(0).equals(this.name)) {
			return false;
		}
		s.remove(0);
		if (s.size() == 0) {
			this.argumentLists.add(arg);
			return true;
		}
		for (SubCommand sub : this.subCommands) {
			if (sub.addArgumentList(new ArrayList<>(s), arg)) {
				return true;
			}
		}
		SubCommand newCom = new SubCommand(s.get(0));
		this.subCommands.add(newCom);
		newCom.addArgumentList(new ArrayList<>(s), arg);
		return true;
	}

	public String logString(String prefix) {
		String s = "";
		for (SubCommand comm : this.subCommands) {
			s += comm.logString(prefix + "<" + this.name + ">");
		}
		for (ArgumentList list : this.argumentLists) {
			s += list.logString(prefix + "<" + this.name + ">");
		}
		return s;
	}
}
