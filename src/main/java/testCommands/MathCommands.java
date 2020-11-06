package testCommands;

import commandManagement.annotations.CommandClass;
import commandManagement.annotations.CommandClassContainer;
import commandManagement.annotations.CommandMehtod;
import org.apache.commons.lang3.StringUtils;

@CommandClassContainer
public class MathCommands {
	@CommandClass(name = "add", alias = {"sum", "addition"})
	public static class Add {
		@CommandMehtod
		public static boolean add(Integer... is) {
			int sum = 0;
			for (int i : is) {
				sum += i;
			}
			System.out.println(StringUtils.join(is, " + ") + " = " + sum);

			return true;
		}
	}

	@CommandClass(name = "mult", alias = {"multi", "multiply"})
	public static class Mult {
		@CommandMehtod
		public static boolean add(Integer... is) {
			int mult = 1;
			for (int i : is) {
				mult *= i;
			}
			System.out.println(StringUtils.join(is, " * ") + " = " + mult);

			return true;
		}
	}

	@CommandClass(name = "echo")
	public static class Echo {
		@CommandMehtod
		public static boolean echo(String... is) {
			System.out.println(StringUtils.join(is, " "));
			return true;
		}
	}
}
