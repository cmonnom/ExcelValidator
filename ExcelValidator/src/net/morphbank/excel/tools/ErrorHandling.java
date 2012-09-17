package net.morphbank.excel.tools;

import java.util.Map;

public class ErrorHandling {

	boolean test;
	Map<Integer, String> errors;
	public ErrorHandling(boolean test, Map<Integer, String> errors) {
		super();
		this.test = test;
		this.errors = errors;
	}
	
	public void forbiddenPattern() {
		
	}
	
	public void requiredPattern() {
		
	}
	
	
	public boolean isTest() {
		return test;
	}
	public void setTest(boolean test) {
		this.test = test;
	}
	public Map<Integer, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<Integer, String> errors) {
		this.errors = errors;
	}
	
	
	
}
