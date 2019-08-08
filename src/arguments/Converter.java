package arguments;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class Converter<T> {
	protected String[] params;

	public Converter() {}

	abstract public T check(CommandSender sender, String string);

	public List<String> complete(CommandSender sender) {
		return new ArrayList<>();
	}

	public void setParams(String[] params) {
		this.params = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			this.params[i] = params[i];
		}
	}
}
