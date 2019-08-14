package converter;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerNameConverter extends Converter<Player> {
	@Override
	public Player check(CommandSender sender, String string) {
		for (Player p : sender.getServer().getOnlinePlayers()) {
			if (p.getDisplayName().equals(string)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public List<String> complete(CommandSender sender) {
		List<String> ret = new LinkedList<>();
		for (Player p : sender.getServer().getOnlinePlayers()) {
			ret.add(p.getDisplayName());
		}
		return ret;
	}
}
