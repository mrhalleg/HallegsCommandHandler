package testCommands;

import commandManagement.CommandManagerFactory;
import org.apache.commons.lang.StringUtils;

@CommandManagerFactory.CommandClassContainer
public class Math {
	@CommandManagerFactory.CommandClass(name = "add", alias = {"sum", "addition"})
	public static class Add {
		@CommandManagerFactory.CommandMehtod
		public static boolean add(Integer... is) {
			int sum = 0;
			for (int i : is) {
				sum += i;
			}
			System.out.println(StringUtils.join(is, " + ") + " = " + sum);

			return true;
		}
	}

	@CommandManagerFactory.CommandClass(name = "mult", alias = {"multi", "multiply"})
	public static class Mult {
		@CommandManagerFactory.CommandMehtod
		public static boolean add(int... is) {
			int mult = 1;
			for (int i : is) {
				mult *= i;
			}
			//System.out.println(StringUtils.join(is, " * ") + " = " + mult);

			return true;
		}
	}

	@CommandManagerFactory.CommandClass(name = "echo")
	public static class Echo {
		@CommandManagerFactory.CommandMehtod
		public static boolean echo(String... is) {
			System.out.println(StringUtils.join(is, " "));
			return true;
		}
	}
}
