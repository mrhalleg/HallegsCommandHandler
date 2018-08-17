package newCommands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class StringArgument extends Argument {
	private String string = "";

	public String getString() {
		return this.string;
	}

	@Override
	public Argument run(String string) {
		return new StringArgument();
	}

	@Override
	public List<String> tap(CommandSender sender, String[] args) {
		List<String> l = new LinkedList<>();
		l.add("ayylmao");
		return l;
	}

}
