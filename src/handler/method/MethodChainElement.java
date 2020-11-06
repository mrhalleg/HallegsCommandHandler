package handler.method;

public abstract class MethodChainElement extends MethodParameter {

	public abstract void setNext(MethodParameter curr);
}
