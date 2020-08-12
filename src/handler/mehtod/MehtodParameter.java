package handler.mehtod;

import java.util.LinkedList;
import java.util.List;

import handler.CommandTreeNode;

public abstract class MehtodParameter extends CommandTreeNode {
	public abstract boolean command(String[] args, int offset, List<Object> list, Object environment);

	public abstract List<String> complete(String[] args, int offset);

	public abstract String printTree(String pre, String params);

	public boolean command(String[] args, int i, Object environment) {
		return command(args, i, new LinkedList<Object>(), environment);
	}

	public String printTree(String pre) {
		return printTree(pre, null);
	}

}
