package newCommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class Argument {

	public Argument() {}

	public List<String> tap(CommandSender sender, String[] args) {
		return new ArrayList<>();
	}

	public Argument run(String string) {
		return null;
	}
}
