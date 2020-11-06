package testCommands;

import commandManagement.CommandManagerFactory.CommandClass;
import commandManagement.CommandManagerFactory.CommandMehtod;

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
		public static boolean empty(String s) {
			System.out.println("maybe");
			return true;
		}
	}
}
