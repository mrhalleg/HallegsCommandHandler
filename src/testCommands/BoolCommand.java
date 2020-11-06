package testCommands;

import commandManagement.CommandManagerFactory.CommandClass;
import commandManagement.CommandManagerFactory.CommandMehtod;
import converter.defaults.IntegerConverter.IntMin;

@CommandClass(name = "bool", alias = {"boolean"}, children = {BoolNotCommand.class})
public class BoolCommand {
	@CommandMehtod(hasEnvironemntParameter = false)
	public static boolean bool(int i, String... a) {
		for (String string : a) {
			System.out.println(string);
		}
		return true;
	}

	@CommandClass(name = "and")
	public static class AndCommand {

		@CommandMehtod()
		public static boolean and(boolean a, boolean b) {
			System.out.println(" a AND b = " + (a && b));
			return true;
		}
	}

	@CommandClass(name = "or")
	public static class OrCommand {
		@CommandMehtod()
		public static boolean or(boolean a, boolean b, boolean c, boolean d) {
			System.out.println(" a OR b = " + (a || b || c || d));
			return true;
		}

		@CommandMehtod()
		public static boolean or(boolean a, boolean b, boolean c) {
			System.out.println(" a OR b OR c= " + (a || b || c));
			return true;
		}
	}

	@CommandClass(name = "add")
	public static class AddCommand {
		@CommandMehtod()
		public static boolean add(@IntMin(min = 0) int arg1, int arg2) {
			System.out.println(arg1 + " + " + arg2 + " = " + (arg1 + arg2));
			return true;
		}
	}
}
