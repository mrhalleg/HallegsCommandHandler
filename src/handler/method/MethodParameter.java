package handler.method;

import commandManagement.result.method.MethodResult;
import handler.CommandTreeNode;

import java.util.LinkedList;
import java.util.List;

public abstract class MethodParameter extends CommandTreeNode {
	public abstract MethodResult search(String[] args, int offset, List<Object> list, Object environment);

	public abstract String printTree(String pre, String params);

	public MethodResult search(String[] args, int i, Object environment) {
		return search(args, i, new LinkedList<Object>(), environment);
	}

	public String printTree(String pre) {
		return printTree(pre, null);
	}

}
