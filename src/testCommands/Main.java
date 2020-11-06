package testCommands;

import commandManagement.CommandManager;
import commandManagement.CommandManagerFactory;
import handler.builder.BaseCommandBuilder;
import handler.builder.MethodBuilder;
import handler.builder.SubCommandBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {
		try {
			CommandManager manager = CommandManagerFactory.createCommandManager(new SubCommandBuilder(), new BaseCommandBuilder(),
					new MethodBuilder(), BoolCommand.class);

			BufferedReader reader =
					new BufferedReader(new InputStreamReader(System.in));
			manager.printTree();
			while(true){
				System.out.println(manager.command(reader.readLine(),null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
