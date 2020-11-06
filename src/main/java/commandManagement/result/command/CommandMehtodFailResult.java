package commandManagement.result.command;

import commandManagement.result.FailResult;
import commandManagement.result.method.MethodFailResult;

import java.util.ArrayList;
import java.util.List;

public class CommandMehtodFailResult extends FailResult {
	private List<MethodFailResult> fails;

	public CommandMehtodFailResult() {
		this.fails = new ArrayList<MethodFailResult>();
	}

	public void add(MethodFailResult fail) {
		if (fail != null) {
			this.fails.add(fail);
		}
	}

	public boolean isEmpty() {
		return this.fails.isEmpty();
	}

	@Override
	public String toString() {
		String s = "Failed!";
		for (MethodFailResult r : this.fails) {
			s += joinNodes(" ") + r.toString() + "\n";
		}
		return s;
	}

	@Override
	public List<String> suggest() {
		return null;
	}
}
