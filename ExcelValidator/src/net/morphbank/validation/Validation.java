package net.morphbank.validation;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract class to allow calls from ColumnValidation and 
 * RowValidation 
 * @author gjimenez
 *
 */
public abstract class Validation {

	/**
	 * Execute a method by its method id
	 * Each method id must be declared in children of Validation
	 * @param choice method id
	 * @param args arguments needed for the method called
	 * @return true if the test is successful
	 * @throws Exception
	 */
	public abstract boolean chooseMethod(int choice, Object... args) throws Exception;
	
	public abstract Map<String, String> getErrorsFound();
	
	public abstract LinkedHashMap<String, Object[]> getMultipleErrorsFound();
}
