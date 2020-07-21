package mehtod;

import java.util.LinkedList;
import java.util.List;

import converter.ConverterConvertException;

public abstract class MehtodHandler {
	public abstract boolean command(String[] args, int offset, List<Object> list) throws ConverterConvertException;

	public abstract List<String> complete(String[] args, int offset);

	public abstract String printTree(String pre, String params);

	public boolean command(String[] args, int i) throws ConverterConvertException {
		return command(args, i, new LinkedList<Object>());
	}

	public String printTree(String pre) {
		return printTree(pre, null);
	}

}
