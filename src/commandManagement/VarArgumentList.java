package commandManagement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import arguments.Argument;
import exceptions.CommandManagerException;

public class VarArgumentList extends ArgumentList {

	public VarArgumentList(Method meth, List<Argument> arguments, boolean opOnly, String permission,
			boolean playerOnly) {
		super(meth, arguments, opOnly, permission, playerOnly);
	}

	@Override
	public boolean handle(ArrayList<String> input, CommandSender sender) {
		Object[] objs = new Object[this.meth.getParameterCount()];
		objs[0] = sender;

		// check for right length
		if (input.size() < this.arguments.size() - 1) {
			return false;
		}

		// convert all arguments except last one
		for (int i = 0; i < objs.length - 1; i++) {
			Argument<?> arg = this.arguments.get(i);
			objs[i + 1] = arg.convert(sender, input.get(i));
			if (objs[i + 1] == null) {
				return false;
			}
		}

		Object[] array = null;
		Argument<?> last = this.arguments.get(this.arguments.size() - 1);
		int arrayLength = input.size() - this.arguments.size() + 1;

		// get a array of the requered type
		try {
			array = (Object[]) java.lang.reflect.Array.newInstance(Argument.getClassFor(last
					.getClass()), arrayLength);
		} catch (NegativeArraySizeException | CommandManagerException e) {
			e.printStackTrace();
		}

		// convert all missing inputs and save in array
		for (int i = 0; i < array.length; i++) {
			array[i] = last.convert(sender, input.get(i + this.arguments.size() - 1));
			if (array[i] == null) {
				return false;
			}
		}

		// make array last value in objs
		objs[objs.length - 1] = array;

		return runMeth(objs);
	}

	@Override
	public List<String> complete(List<String> args, CommandSender sender) {
		List<String> complete = new ArrayList<>();
		for (int i = 0; i < args.size(); i++) {
			Argument<?> arg = null;
			if (i >= this.arguments.size()) {
				arg = this.arguments.get(this.arguments.size() - 1);
			} else {
				arg = this.arguments.get(i);
			}

			if (args.get(i).isEmpty()) {
				complete.addAll(arg.complete(sender));
				return complete;
			} else if (arg.convert(sender, args.get(i)) == null) {
				return complete;
			}
		}
		return complete;
	}

}
