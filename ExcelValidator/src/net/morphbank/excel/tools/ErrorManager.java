package net.morphbank.excel.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.common.net.MediaType;



public class ErrorManager {

	BufferedWriter out;
	String sheetName;
	MediaType mediaType;
	
	public static final String UNKNOWN_ERROR = "Unknown error found";
	public static final String PATTERN_DETECTED_ERROR = "These characters are forbidden:";
	public static final String PATTERN_NOT_FOUND_ERROR = "Every cell should have the following:";
	public static final String EXTENSION_NOT_VALID_ERROR = "Every file should be of extension:";
	public static final String MISSING_COL_ERROR = "The column is missing or the header is mistyped.";
	public static final String EMPTY_CELL_ERROR = "This cell should not be empty.";
	public static final String EXTRA_SPACES_ERROR = "This cell's value has extra spaces leading or trailing";
	public static final String UNIQUENESS_ERROR = "Duplicate values found:";
	public static final String MULTIPLE_HEADER_VALUES_ERROR = "Column headers don't have unique names. Fix this error first before running other tests";
	public static final String DROP_DOWN_ERROR = " does not match any value in the list of";
	
	public ErrorManager(BufferedWriter out, String sheetName, MediaType mediaType) {
		this.out = out;
		this.sheetName = sheetName;
		this.mediaType = mediaType;
	}
	
	public void printMessage(String message) {
		if (mediaType.equals(MediaType.HTML_UTF_8)) {
			message = message.replaceAll("\n", "<br/>");
		}
		try {
			out.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getErrorMessageFromTestId(int testId) {
		switch(testId) {
		case 1: return EMPTY_CELL_ERROR;
		case 2: return MISSING_COL_ERROR;
		default: return UNKNOWN_ERROR;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * Create a message with cell coordinates and error message by
	 * calling the appropriate createMessage method.
	 * @param errorMessage
	 * @param args
	 * @return
	 */
	public StringBuffer createMessageFromErrorMessage(String errorMessage, Object[] args) {
		if (errorMessage.equals(MISSING_COL_ERROR)) {
			return this.createMissingColumnMessage(errorMessage, (Map<String, String>)args[0]);
		}
		if (errorMessage.equals(UNIQUENESS_ERROR) || errorMessage.equals(MULTIPLE_HEADER_VALUES_ERROR)) {
			return this.createDupFoundMessage(errorMessage, (LinkedHashMap<String, Object[]>) args[0], (String) args[1]);
		}
		if (errorMessage.equals(DROP_DOWN_ERROR)) {
			return this.createDropDownMessage(errorMessage, (LinkedHashMap<String, Object[]>) args[0]);
		}
		else {
			return this.createMessage(errorMessage, (Map<String, String>)args[0]);
		}
	}
	
	/**
	 * Create an error message with row number
	 * and content of cell.
	 * Add a customized message at the end.
	 * @param message customized message
	 * @param errors Map of cells with errors
	 */
	public StringBuffer createMessage(String message, Map<String, String> errors) {
		StringBuffer messageBuffered = new StringBuffer();
		Iterator<String> it = errors.keySet().iterator();
		while (it.hasNext()) {
			String cell = it.next();
			messageBuffered.append("In sheet ");
			messageBuffered.append(this.sheetName);
			messageBuffered.append(" cell ");
			messageBuffered.append(cell);
			messageBuffered.append(": ");
			messageBuffered.append(errors.get(cell));
			messageBuffered.append(" has error(s). ");
			messageBuffered.append(message);
			messageBuffered.append("\n");
			
		}
		return messageBuffered;
	}
	
	/**
	 * Error Message without the cell coordinates.
	 * Can be used when the error does not rely on a unique cell
	 * @param message
	 * @param error
	 * @return
	 */
	public StringBuffer createMessage(String message, String error) {
		StringBuffer messageBuffered = new StringBuffer();
		messageBuffered.append("In sheet ");
		messageBuffered.append(this.sheetName);
		messageBuffered.append(" ");
		messageBuffered.append(message);
		messageBuffered.append(" ");
		messageBuffered.append(error);
		messageBuffered.append("\n");
		return messageBuffered;
	}
	
	/**
	 * Create an error message with row number
	 * and content of cell.
	 * Add a customized message at the end.
	 * @param message customized message
	 * @param errors Map of cells with errors
	 */
	public StringBuffer createMissingColumnMessage(String message, Map<String, String> errors) {
		StringBuffer messageBuffered = new StringBuffer();
		Iterator<String> it = errors.keySet().iterator();
		while (it.hasNext()) {
			String row = it.next();
			messageBuffered.append("In sheet ");
			messageBuffered.append(this.sheetName);
			messageBuffered.append(" ");
			messageBuffered.append(row);
			messageBuffered.append(": ");
			messageBuffered.append(errors.get(row));
			messageBuffered.append(" has error(s). ");
			messageBuffered.append(message);
			messageBuffered.append("\n");
			
		}
		return messageBuffered;
	}
	
	/**
	 * Create an error message for column requiring
	 * no duplicate value
	 * @param message customized message
	 * @param cellValue the cell that should be unique
	 * @param dupRowNumbers array of row number of duplicates
	 * @return the error message as a StringBuffer
	 */
	public StringBuffer createDupFoundMessage(String message, LinkedHashMap<String, Object[]> multipleErrorsFound, String separator) {
		StringBuffer messageBuffered = new StringBuffer();
		Collection<String> keys = multipleErrorsFound.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()) {
			String next = it.next();
			String listOfDups = getListOfTerms(multipleErrorsFound.get(next), separator);
			messageBuffered.append(message);
			messageBuffered.append(next);
			messageBuffered.append(" is also in row(s) ");
			messageBuffered.append(listOfDups);
			messageBuffered.append("\n");
		}
		return messageBuffered;
	}
	
	/**
	 * Create an error message for values not matching 
	 * a drop down list
	 * @param message customized message
	 * @param error <error cell coordinates, [sheet name of drop down, column id or drop down]>
	 * @return the error message as a StringBuffer
	 */
	public StringBuffer createDropDownMessage(String message, LinkedHashMap<String, Object[]> error) {
		StringBuffer messageBuffered = new StringBuffer();
		Collection<String> keys = error.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()) {
			String next = it.next();
			String[] sheetNameColumn = (String[]) error.get(next);
			String sheetName = sheetNameColumn[0];
			String column = sheetNameColumn[1];
			messageBuffered.append("In sheet ");
			messageBuffered.append(this.sheetName);
			messageBuffered.append(",cell ");
			messageBuffered.append(next);
			messageBuffered.append(message);
			messageBuffered.append(" sheet ");
			messageBuffered.append(sheetName);
			messageBuffered.append(",column ");
			messageBuffered.append(column);
			messageBuffered.append(".\n");
		}
		return messageBuffered;
	}
	
	/**
	 * Create a String from an array of Strings
	 * separated by " or ".
	 * @param terms
	 * @return 
	 */
	public String getListOfTerms(String[] terms, String separator) {
		StringBuffer list = new StringBuffer();
		list.append("\"");
		for (String term:terms) {
			list.append(term);
			list.append(separator);
			
		}
		
		list = new StringBuffer(list.substring(0, list.length() - separator.length()));
		list.append("\"");
		return list.toString();
	}
	
	/**
	 * Create a String from an array of Object (Integer required)
	 * @param terms list of terms
	 * @param separator characters to separates the terms (",", " or ", " and "...)
	 * @return
	 */
	public String getListOfTerms(Object[] terms, String separator) {
		int size = terms.length;
		String[] list = new String[size];
		for (int i=0; i < size; i++) {
			list[i] = String.valueOf(terms[i]);
		}
		return getListOfTerms(list, separator);
	}
}
