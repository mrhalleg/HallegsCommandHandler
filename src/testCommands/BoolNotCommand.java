package testCommands;

import commandManagement.CommandManager.CommandClass;
import commandManagement.CommandManager.CommandMehtod;

@CommandClass(name = "not")
public class BoolNotCommand {
	@CommandMehtod()
	public static boolean not(boolean v) {
		System.out.println(" NOT a = " + !v);
		return true;
	}

	@CommandClass(name = "maybe")
	public static class MaybeCommand {
		@CommandMehtod()
		public static boolean empty() {
			System.out.println("maybe");
			return true;
		}
	}
}
