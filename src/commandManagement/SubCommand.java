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
	public boolean handle(String[] input, CommandSender sender) {
		if (input.length < 1 || !input[0].equals(this.name)) {
			return false;
		}

		String[] shortened = new String[input.length - 1];
		for (int i = 0; i < shortened.length; i++) {
			shortened[i] = input[i + 1];
		}

		for (SubCommand comm : this.subCommands) {
			if (comm.handle(shortened, sender)) {
				return true;
			}
		}

		for (ArgumentList list : this.argumentLists) {
			if (list.handle(shortened, sender)) {
				return true;
			}
		}
		return false;
	}

	public List<String> complete(String[] args, CommandSender sender) {
		// check if im last
		System.out.println("complete " + this.name);
		List<String> complete = new ArrayList<>();
		for (SubCommand c : this.subCommands) {
			System.out.println("sub: " + c.getName());
		}
		for (String s : args) {
			System.out.println("args: " + s);
		}
		if (args.length == 1) {
			System.out.println("im last");
			for (SubCommand comm : this.subCommands) {
				complete.add(comm.getName());
			}
		}
		return complete;
	}

	public String getName() {
		return this.name;
	}

	public boolean addArgumentList(String[] s, ArgumentList arg) {
		if (!s[0].equals(this.name)) {
			return false;
		}
		System.out.println("adding argument list to " + this.name);
		for (String string : s) {
			System.out.println("s: " + string);
		}
		if (s.length == 1) {
			System.out.println("addet to me");
			this.argumentLists.add(arg);
			return true;
		}

		String[] shortened = new String[s.length - 1];
		for (int i = 0; i < shortened.length; i++) {
			shortened[i] = s[i + 1];
		}

		for (SubCommand sub : this.subCommands) {
			if (sub.addArgumentList(shortened, arg)) {
				return true;
			}
		}

		System.out.println("creating command " + s[1]);
		SubCommand newCom = new SubCommand(s[1]);
		this.subCommands.add(newCom);
		newCom.addArgumentList(shortened, arg);
		return true;
	}

	@Override
	public String toString() {
		String s = "";
		for (SubCommand comm : this.subCommands) {
			s += "<" + this.name + ">" + comm.toString();
		}
		for (ArgumentList list : this.argumentLists) {
			s += "<" + this.name + ">" + list.toString();
		}
		return s;
	}
}
