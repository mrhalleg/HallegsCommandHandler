package commandManagement.result;

import commandManagement.executable.Executable;

public abstract class SuccesResult extends PathResult implements Result {
	protected Executable executable;

	public SuccesResult(Executable executable) {
		this.executable = executable;
	}

	public Executable getExecutable() {
		return this.executable;
	}

	@Override
	public boolean isSucces() {
		return true;
	}

	@Override
	public String toString() {
		return "Succes!\n" + super.toString();
	}
}
