package commandManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import arguments.Argument;

public class ArgumentList {
	private boolean opOnly;
	private List<Argument> arguments;

	private Method meth;

	public ArgumentList(Method meth) {
		this.meth = meth;
		this.arguments = new ArrayList<>();
	}

	public boolean handle(String[] input) {
		if (input.length != this.arguments.size()) {
			return false;
		}

		Object[] objs = new Object[input.length];

		for (int i = 0; i < input.length; i++) {
			objs[i] = this.arguments.get(i).check(null, input[i]);
			if (objs[i] == null) {
				return false;
			}
		}

		runMeth(objs);
		return true;
	}

	private void runMeth(Object[] args) {
		try {
			this.meth.invoke(null, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
