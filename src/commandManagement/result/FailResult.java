package commandManagement.result;

import java.util.List;

public abstract class FailResult extends PathResult implements Result {
	@Override
	public boolean isSucces() {
		return false;
	}

	public abstract List<String> suggest();

	@Override
	public String toString() {
		return "Failed!\n" + super.toString();
	}
}
