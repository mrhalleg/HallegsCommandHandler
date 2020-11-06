package testCommands;

import commandManagement.annotations.CommandClass;
import commandManagement.annotations.CommandMehtod;
import org.apache.commons.lang.StringUtils;

@CommandClass(name = "bool", alias = {"boolean"})
public class BoolCommands {
	@CommandMehtod()
	public static boolean ident(boolean b) {
		System.out.println(b + " = " + b);
		return true;
	}

	@CommandClass(name = "not")
	public static class NotCommand {
		@CommandMehtod()
		public static boolean nit(boolean b) {
			System.out.println("!" + b + " = " + !b);
			return true;
		}
	}

	@CommandClass(name = "and")
	public static class AndCommand {

		@CommandMehtod()
		public static boolean and(Boolean... bs) {
			boolean and = true;
			for (boolean b : bs) {
				and = and && b;
			}
			System.out.println(StringUtils.join(bs, " & ") + " = " + and);
			return true;
		}
	}

	@CommandClass(name = "or")
	public static class OrCommand {
		@CommandMehtod()
		public static boolean or(Boolean... bs) {
			boolean or = false;
			for (boolean b : bs) {
				or = or || b;
			}
			System.out.println(StringUtils.join(bs, " | ") + " = " + or);
			return true;
		}
	}
}
