package arguments;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class BooleanArgument extends Argument<Boolean> {

	@Override
	public Boolean check(CommandSender sender, String string) {
		if (string.equals("true") || string.equals("1")) {
			return true;
		} else if (string.equals("false") || string.equals("0")) {
			return false;
		}
		return null;
	}

	@Override
	public List<String> complete(CommandSender sender) {
		List<String> ret = new LinkedList<>();
		ret.add("true");
		ret.add("false");
		return ret;
	}
}
