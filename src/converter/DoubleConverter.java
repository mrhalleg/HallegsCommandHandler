package converter;

import org.bukkit.command.CommandSender;

public class DoubleConverter extends Converter<Double> {
	@Override
	public Double check(CommandSender sender, String string) {
		double d = 0;
		try {
			d = Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return null;
		}
		return d;
	}
}
