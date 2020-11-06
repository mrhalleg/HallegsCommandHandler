package commandManagement.executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokerExecutable implements Executable {
	private Object[] args;
	private Method method;

	public InvokerExecutable(Object[] args, Method method) {
		this.args = args;
		this.method = method;
	}

	@Override
	public void execute() throws InvocationTargetException, IllegalAccessException {
		this.method.invoke(null, this.args);
	}
}

