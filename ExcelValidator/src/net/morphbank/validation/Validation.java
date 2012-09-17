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

	public abstract boolean chooseMethod(int choice, Object... args) throws Exception;
	
	public abstract Map<String, String> getErrorsFound();
	
	public abstract LinkedHashMap<String, Object[]> getMultipleErrorsFound();
}
