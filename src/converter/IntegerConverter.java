package converter;

import org.bukkit.command.CommandSender;

public class IntegerConverter extends Converter<Integer> {
	@Override
	public Integer check(CommandSender sender, String string) {
		int i = 0;
		try {
			i = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return null;
		}
		return i;
	}

}
