package converter.defaults;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import converter.Converter;

public class PlayerNameConverter extends Converter<Player> {
	@Override
	public Player convert(String string) {
//		for (Player p : sender.getServer().getOnlinePlayers()) {
//			if (p.getDisplayName().equals(string)) {
//				return p;
//			}
//		}
		return null;
	}

	@Override
	public List<String> complete() {
		List<String> ret = new LinkedList<>();
//		for (Player p : sender.getServer().getOnlinePlayers()) {
//			ret.add(p.getDisplayName());
//		}
		return ret;
	}
}
