package net.morphbank.excel.tools;

public class PairMethodArguments {

	int methodId;
	Object[] arguments;
	
	/**
	 * Convient class for transporting the method id and its arguments
	 * @param methodId
	 * @param arguments
	 */
	public PairMethodArguments(int methodId, Object[] arguments) {
		super();
		this.methodId = methodId;
		this.arguments = arguments;
	}
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	public Object[] getArguments() {
		return arguments;
	}
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	
	
	
}
