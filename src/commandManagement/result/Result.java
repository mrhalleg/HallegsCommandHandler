package commandManagement.result;

import handler.CommandTreeNode;

public interface Result {
	boolean isSucces();

	public void addPath(CommandTreeNode node);
}