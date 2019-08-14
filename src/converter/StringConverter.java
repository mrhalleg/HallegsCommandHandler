package converter;

import org.bukkit.command.CommandSender;

public class StringConverter extends Converter<String> {
	@Override
	public String check(CommandSender sender, String string) {
		return string;
	}
}
