package net.morphbank.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import net.morphbank.excel.tools.CellTools;
import net.morphbank.excel.tools.HeaderTools;
import net.morphbank.excel.tools.MissingColumnException;

/**
 * All tests on a row or comparaison of rows
 * 
 * @author gjimenez
 * 
 */
public class RowValidation extends Validation {

	public static final int MANDATORY_CELL_TEST = 1;
	public static final int MISSING_HEADER_TEST = 2;
	public static final int UNIQUE_HEADER_TEST = 3;

	Row row;
	HeaderTools headerTools;
	HashMap<String, String> errorsFound = new HashMap<String, String>();
	LinkedHashMap<String, Object[]> multipleErrorsFound = new LinkedHashMap<String, Object[]>();

	public RowValidation(Row row, HeaderTools headerTools) {
		this.row = row;
		this.headerTools = headerTools;
	}

	public boolean chooseMethod(int choice, Object... args) throws MissingColumnException {
		switch (choice) {
		case MANDATORY_CELL_TEST:
			if (args[0] instanceof String) {
				return mandatoryCells((String) args[0]); 
			} else {
				return mandatoryCells((String[]) args[0]);
			}
		case MISSING_HEADER_TEST:
			if (args[0] instanceof String) {
				return validateHeader((String) args[0]); 
			} else {
				return validateHeader((String[]) args[0]);
			}
		case UNIQUE_HEADER_TEST:
			return uniqueHeader();
		default:
			return false;
		}
	}

	/**
	 * Each cell in row belonging to the list of mandatory cells needs to have a
	 * value
	 * @param mandatoryCells
	 *            headers of columns that need checking
	 * @return false is one mandatory cell is empty in the row
	 * @return true if test is passed (all required cells have a value)
	 *  or skipped (a header is missing)
	 * @throws Exception 
	 */
	public boolean mandatoryCells(String[] mandatoryCells) throws MissingColumnException {
		errorsFound.clear();
		boolean isHeaderOk = this.validateHeader(mandatoryCells);
		if (!isHeaderOk)
				throw new MissingColumnException();

		boolean emptyCell = false;
		int[] cellNumbers = new int[mandatoryCells.length];
		for (int i = 0; i < mandatoryCells.length; i++) {
			String header = mandatoryCells[i];
			cellNumbers[i] = headerTools.getColNumber(header);
			Cell cell = row.getCell(cellNumbers[i]);
			CellTools cellTools = new CellTools(cell, headerTools);
			String refValue = "";
			if (cellTools.isEmpty()) {
				refValue = CellReference.convertNumToColString(cellNumbers[i])
						+ (row.getRowNum() + 1);
				errorsFound.put(refValue, "");
				emptyCell = true;
			}
		}
		return !emptyCell;

	}

	/**
	 * From a list of headers, check if the sheet has all the 
	 * listed headers
	 * @param headerList
	 * @return true if all headers have been found
	 * @return false if one or more header is missing
	 */
	public boolean validateHeader(String[] headerList) {
		errorsFound.clear();
		boolean isHeaderOk = true;
		Iterator<Cell> it = this.headerTools.getHeaders().cellIterator();
		for (String currentHeader:headerList) {
			boolean headerFound = false;
			while(it.hasNext()) {
				Cell cell = it.next();
				String header = cell.getStringCellValue();
				if (currentHeader.equals(header)) {
					headerFound = true;
					break;
				}
			}
			if (!headerFound) {
				this.errorsFound.put("Row 1", currentHeader);
				isHeaderOk = false;
			}
		}
		return isHeaderOk;
	}
	
	/**
	 * Check if the sheet has the listed header
	 * @param header
	 * @return true if the header has been found
	 * @return false if the header is missing
	 */
	public boolean validateHeader(String header) {
		String[] headerList = new String[]{header};
		return validateHeader(headerList);
	}
	
	/**
	 * Check if the header row has unique values
	 * @return true if all values (including empty cells)
	 * are unique
	 */
	public boolean uniqueHeader() {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		Row headers = headerTools.getHeaders();
		Iterator<Cell> it = headers.cellIterator();
		while (it.hasNext()) {
			Cell cell = it.next();
			cells.add(cell);
		}
		ColumnValidation validation = new ColumnValidation(cells);
		boolean testResult = validation.uniqueStringValue();
		if (!validation.uniqueStringValue()) {
			multipleErrorsFound = validation.getMultipleErrorsFound();
		}
		return testResult;
	}
	
	/**
	 * Each cell in row belonging to the list of mandatory cells needs to have a
	 * value
	 * 
	 * @param mandatoryCell
	 *            header of columns that need checking
	 * @return false is one mandatory cell is empty in the row
	 * @return true if test is passed (all required cells have a value)
	 * @throws Exception 
	 */
	public boolean mandatoryCells(String mandatoryCell) throws MissingColumnException {
		String[] mandatoryCells = new String[] { mandatoryCell };
		return this.mandatoryCells(mandatoryCells);
	}

	public HashMap<String, String> getErrorsFound() {
		return errorsFound;
	}

	public void setErrorsFound(HashMap<String, String> errorsFound) {
		this.errorsFound = errorsFound;
	}

	public LinkedHashMap<String, Object[]> getMultipleErrorsFound() {
		return multipleErrorsFound;
	}

	public void setMultipleErrorsFound(
			LinkedHashMap<String, Object[]> multipleErrorsFound) {
		this.multipleErrorsFound = multipleErrorsFound;
	}
	
	

}
