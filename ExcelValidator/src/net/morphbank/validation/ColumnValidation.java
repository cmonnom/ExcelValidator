package net.morphbank.validation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.morphbank.excel.tools.CellTools;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

/**
 * All tests on an entire column or comparaison of two columns
 * 
 * @author gjimenez
 * 
 */
public class ColumnValidation extends Validation{ //TODO rewrite method description info

	public static final int PATTERN_ABSENT_TEST = 1;
	public static final int PATTERN_PRESENT_TEST = 2;
	public static final int EXTRA_SPACES_TEST = 3;
	public static final int UNIQUE_STRING_TEST = 4;
	public static final int UNIQUE_NUMERIC_TEST = 5;
	public static final int UNIQUE_TEST = 6;
	public static final int DROP_DOWN_TEST = 7;
	
	ArrayList<Cell> col1, col2;
	LinkedHashMap<String, String> errorFound = new LinkedHashMap<String, String>();
	LinkedHashMap<String, Object[]> multipleErrorsFound = new LinkedHashMap<String, Object[]>();

	public ColumnValidation(ArrayList<Cell> col1, ArrayList<Cell> col2) {
		this.col1 = col1;
		this.col2 = col2;

	}

	public ColumnValidation(ArrayList<Cell> col) {
		this.col1 = col;
		this.col2 = null;
	}

	/**
	 * Test if a pattern is found in any cell of the column. Can be used to check
	 * if no cell in the column has special characters
	 * 
	 * @param pattern
	 * @return false is one of the patterns is detected in the column
	 * @return true is the test is successful (pattern not found)
	 */
	public boolean isPatternAbsent(String[] pattern) {
		boolean patternAbsent = true;
		errorFound.clear();
		for (int row = 0; row < col1.size(); row++) {
			for (String p : pattern) {
				Cell currentCell = col1.get(row);
				if (currentCell != null) {
					if (currentCell.getStringCellValue().contains(p)) {
						CellReference ref = new CellReference(currentCell);
						patternAbsent = false;
						errorFound.put(ref.formatAsString(),
								currentCell.getStringCellValue());
					}
				}
			}
		}
		return patternAbsent;
	}


	/**
	 * Test if a pattern is found in any cell of the column Can be used to check
	 * if no cell in the column has special characters
	 * 
	 * @param pattern
	 * @return false is one of the patterns is detected in the column
	 * @return true is the test is successful (pattern not found)
	 */
	public boolean isPatternAbsent(String pattern) {
		String[] patterns = { pattern };
		return isPatternAbsent(patterns);
	}


	/**
	 * Test if a pattern is found in any cell of the column Can be use to check
	 * if all cells have a valid extension type
	 * 
	 * @param pattern
	 * @return true if all cells in the column have the pattern (test is
	 *         successful)
	 * @return false if one cell does not have the pattern
	 */
	public boolean isPatternPresent(String[] pattern) {
		boolean allRowsOk = true;
		errorFound.clear();
		for (int row = 0; row < col1.size(); row++) {
			boolean patternDetected = false;
			Cell currentCell = null;
			CellReference ref = null;
			for (String p : pattern) {
				currentCell = col1.get(row);
				if (currentCell != null) {
					ref = new CellReference(currentCell);
					if (currentCell.getStringCellValue().contains(p)) {
						patternDetected = true;
					}
				}
			}
			if (!patternDetected && currentCell != null) {
				errorFound.put(ref.formatAsString(),
						currentCell.getStringCellValue());
			}
		}
		return allRowsOk;
	}


	/**
	 * Test if a pattern is found in any cell of the column Can be use to check
	 * if all cells have a valid extension type
	 * 
	 * @param pattern
	 * @return true if all cells in the column have the pattern (test is
	 *         successful)
	 * @return false if one cell does not have the pattern
	 */
	public boolean isPatternPresent(String pattern) {
		String[] patterns = new String[] { pattern };
		return isPatternPresent(patterns);
	}


	/**
	 * Find spaces at the begining or at the end of the cell content.
	 * 
	 * @return false if one cell or more in the column has extra spaces
	 * @return true if test is successful
	 */
	public boolean leadingTrailingSpaces() {
		boolean spacesFound = false;
		errorFound.clear();
		for (int row = 0; row < col1.size(); row++) {
			Cell cell = col1.get(row);
			if (cell != null) {
				String cellValue = cell.getStringCellValue();
				CellReference ref = new CellReference(cell);
				if (!cellValue.trim().equals(cellValue)) {
					spacesFound = true;
					errorFound.put(ref.formatAsString(), cellValue);
				}
			}
		}
		return !spacesFound;
	}

	/**
	 * Wrapper for uniqueStringValue and uniqueNumericValue
	 * This method check the first cell to detect the cell type
	 * @return true is all values are unique
	 */
	public boolean uniqueValue() {
		Cell cell1 = col1.get(1);
		int cellType = cell1.getCellType();
		if (cellType == Cell.CELL_TYPE_STRING) {
			return uniqueStringValue();
		}
		else {
			return uniqueNumericValue();
		}
	}
	
	/**
	 * Find duplicates of String in a column
	 * @return true if all values are unique
	 */
	public boolean uniqueStringValue() {
		boolean allValuesUnique = true;
		multipleErrorsFound.clear();
		ArrayList<String> duplicates = new ArrayList<String>();
		ArrayList<String> listProgress = new ArrayList<String>();
		for (int i = 0; i < col1.size(); i++) {
			duplicates.clear();
			Cell currentCell = col1.get(i);
			String currentCellValue = "";
			if (!(currentCell == null || listProgress.contains(currentCellValue = currentCell.getStringCellValue()))) {
				for (int j = i + 1; j < col1.size(); j++) {
					Cell dupCell = col1.get(j);
					if (dupCell != null) {
						String dupCellValue = dupCell.getStringCellValue();
						String cellCoordinates = new CellReference(dupCell)
						.formatAsString();
						if (currentCellValue.equals(dupCellValue)) {
							duplicates.add(cellCoordinates);
							allValuesUnique = false;
							listProgress.add(currentCellValue);
						}
					}
				}
				if (duplicates.size() > 0) {
					String cellCoordinates = new CellReference(currentCell)
							.formatAsString();
					multipleErrorsFound.put(cellCoordinates, duplicates.toArray());
				}
			}
		}
		return allValuesUnique;
	}

	/**
	 * Find duplicates of Numeric cells in a column
	 * @return true if all values are unique
	 */
	public boolean uniqueNumericValue() {
		boolean allValuesUnique = true;
		multipleErrorsFound.clear();
		ArrayList<String> duplicates = new ArrayList<String>();
		ArrayList<Double> listProgress = new ArrayList<Double>();
		for (int i = 0; i < col1.size(); i++) {
			duplicates.clear();
			Cell currentCell = col1.get(i);
			Double currentCellValue = 0.0;
			if (!(currentCell == null || listProgress.contains(currentCellValue = currentCell.getNumericCellValue()))) {
				for (int j = i + 1; j < col1.size(); j++) {
					Cell dupCell = col1.get(j);
					if (dupCell != null) {
						Double dupCellValue = dupCell.getNumericCellValue();
						String cellCoordinates = new CellReference(dupCell)
						.formatAsString();
						if (Double.compare(currentCellValue, dupCellValue) == 0) {
							duplicates.add(cellCoordinates);
							allValuesUnique = false;
							listProgress.add(currentCellValue);
						}
					}
				}
				if (duplicates.size() > 0) {
					String cellCoordinates = new CellReference(currentCell)
							.formatAsString();
					multipleErrorsFound.put(cellCoordinates, duplicates.toArray());
				}
			}
		}
		return allValuesUnique;
	}
	
	/**
	 * Map of errors <Cell Coordinates, Cell Content>
	 * @return
	 */
	public LinkedHashMap<String, String> getErrorsFound() {
		return errorFound;
	}

	public void setErrorsFound(LinkedHashMap<String, String> errorsFound) {
		this.errorFound = errorsFound;
	}

	/**
	 * Map of errors <Tested cell Coordinates, Other cells with errors>
	 * @return
	 */
	public LinkedHashMap<String, Object[]> getMultipleErrorsFound() {
		return multipleErrorsFound;
	}

	public void setMultipleErrorsFound(
			LinkedHashMap<String, Object[]> multipleErrorsFound) {
		this.multipleErrorsFound = multipleErrorsFound;
	}

	@Override
	public boolean chooseMethod(int choice, Object... args)
			throws Exception {
		switch(choice) {
		case PATTERN_ABSENT_TEST:
			if (args[0] instanceof String) {
				return isPatternAbsent((String) args[0]); 
			} else {
				return isPatternAbsent((String[]) args[0]);
			}
		case PATTERN_PRESENT_TEST:
			if (args[0] instanceof String) {
				return isPatternPresent((String) args[0]); 
			} else {
				return isPatternPresent((String[]) args[0]);
			}
		case EXTRA_SPACES_TEST:
			return leadingTrailingSpaces();
		case UNIQUE_STRING_TEST:
			return uniqueStringValue();
		case UNIQUE_NUMERIC_TEST:
			return uniqueNumericValue();
		case UNIQUE_TEST:
			return uniqueValue();
		case DROP_DOWN_TEST:
			return dropDown();
		}
		return false;
	}
	
	/**
	 * Test if cell values match values in a drop down list
	 * @return true if all values are in the drop down list
	 */
	public boolean dropDown() {
		boolean allValuesInDropDown = true;
		if (col2 == null) {
			return false;
		}
		String dropDownSheetName = col2.get(0).getSheet().getSheetName();
		String dropDownColumn = CellReference.convertNumToColString(col2.get(0).getColumnIndex());
		for (int i = 0; i < col1.size(); i++) {
			boolean valueInDropDown = false;
			Cell cellWithValue = col1.get(i);
			String cellWithValueContent = CellTools.getCellValue(cellWithValue);
			for (int j = 0; j < col2.size(); j++) {
				Cell cellFromDropDown = col2.get(j);
				String cellFromDropDownContent = CellTools.getCellValue(cellFromDropDown);
				if (cellWithValueContent.equals(cellFromDropDownContent)) {
					valueInDropDown = true;
					break;
				}
			}
			if (!valueInDropDown) {
				String cellCoordinates = new CellReference(cellWithValue).formatAsString();
				multipleErrorsFound.put(cellCoordinates, new String[]{dropDownSheetName, dropDownColumn});
				allValuesInDropDown = false;
			}
		}
		return allValuesInDropDown;
	}

}
