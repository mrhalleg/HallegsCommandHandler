package arguments;

import org.bukkit.command.CommandSender;

import commandManagement.Argument;

public class StringArgument extends Argument<String> {
	@Override
	public String check(CommandSender sender, String string) {
		return string;
	}
}
