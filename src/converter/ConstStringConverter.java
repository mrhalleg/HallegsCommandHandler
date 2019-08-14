package converter;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

public class ConstStringConverter extends Converter<String> {
	@Override
	public String check(CommandSender sender, String string) {
		for (int i = 0; i < this.params.length; i++) {
			if (string.equals(this.params[i])) {
				return string;
			}
		}
		return null;
	}

	@Override
	public List<String> complete(CommandSender sender) {
		return Arrays.asList(this.params);
	}
}
