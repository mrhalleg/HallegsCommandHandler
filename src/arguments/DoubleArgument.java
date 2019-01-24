package arguments;

import org.bukkit.command.CommandSender;

public class DoubleArgument extends Argument<Double> {

	@Override
	public Double convert(CommandSender sender, String string) {
		double d = 0;
		try {
			d = Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return null;
		}
		return d;
	}
}
