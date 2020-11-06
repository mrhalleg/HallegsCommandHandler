package testCommands;

import commandManagement.CommandManager;
import commandManagement.CommandManagerFactory;
import commandManagement.result.Result;
import commandManagement.result.SuccesResult;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {
		try {
			CommandManager manager = CommandManagerFactory.createCommandManager(new SubCommandBuilder(), new BaseCommandBuilder(),
					new MethodBuilder(), MathCommands.class, BoolCommands.class);

			BufferedReader reader =
					new BufferedReader(new InputStreamReader(System.in));
			manager.printTree();
			while (true) {
				Result r = manager.search(reader.readLine(), null);
				if (r instanceof SuccesResult) {
					((SuccesResult) r).getExecutable().execute();
				}
				System.out.println(r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
