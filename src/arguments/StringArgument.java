package arguments;

import org.bukkit.command.CommandSender;

public class StringArgument extends Argument<String> {
	@Override
	public String convert(CommandSender sender, String string) {
		return string;
	}
}
