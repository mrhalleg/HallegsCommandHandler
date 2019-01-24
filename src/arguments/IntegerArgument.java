package arguments;

import org.bukkit.command.CommandSender;

public class IntegerArgument extends Argument<Integer> {
	@Override
	public Integer convert(CommandSender sender, String string) {
		int i = 0;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return null;
		}
		return i;
	}
}
