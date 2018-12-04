package arguments;

import org.bukkit.command.CommandSender;

public class StringArgument extends Argument<String> {
	@Override
	public String check(CommandSender sender, String string) {
		return string;
	}
}
