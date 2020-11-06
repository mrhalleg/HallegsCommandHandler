package handler.method;

import commandManagement.result.method.MethodResult;
import handler.CommandTreeNode;

import java.util.LinkedList;
import java.util.List;

public abstract class MethodParameter extends CommandTreeNode {
	public abstract MethodResult command(String[] args, int offset, List<Object> list, Object environment);

	public abstract List<String> complete(String[] args, int offset);

	public abstract String printTree(String pre, String params);

	public MethodResult command(String[] args, int i, Object environment) {
		return command(args, i, new LinkedList<Object>(), environment);
	}

	public String printTree(String pre) {
		return printTree(pre, null);
	}

}
