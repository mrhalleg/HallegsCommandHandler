package commandManagement.result;

import handler.CommandTreeNode;

import java.util.ArrayList;
import java.util.List;

public abstract class PathResult {
	protected List<CommandTreeNode> nodes;

	protected PathResult() {
		this.nodes = new ArrayList<>();
	}


	public void addPath(CommandTreeNode node) {
		this.nodes.add(node);
	}


	@Override
	public String toString() {
		return joinNodes(" ");
	}


	protected String joinNodes(String sep) {
		String s = "";
		for (int i = this.nodes.size() - 1; i >= 0; i--) {
			s += this.nodes.get(i).toString() + sep;
		}
		return s;
	}
}
