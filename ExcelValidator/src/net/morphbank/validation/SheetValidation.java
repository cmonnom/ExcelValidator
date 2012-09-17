package net.morphbank.validation;

import java.io.BufferedWriter;
import java.util.ArrayList;
import net.morphbank.excel.tools.ErrorManager;
import net.morphbank.excel.tools.PairMethodArguments;
import net.morphbank.excel.tools.SheetTools;
import org.apache.poi.ss.usermodel.Workbook;
import com.google.common.net.MediaType;


/**
 * Create calls to single/multiple row or column validations
 * @author gjimenez
 *
 */
public class SheetValidation extends SheetTools {
	MediaType mediaType;
	BufferedWriter out;
	ErrorManager errorManager;

	public SheetValidation(BufferedWriter out, Workbook workbook,
			String sheetName, MediaType mediaType) {
		super(workbook, sheetName);
		this.mediaType = mediaType;
		this.out = out;
		this.errorManager = new ErrorManager(out, sheetName, mediaType);
	}

	public ColumnValidation createColumnValidation(String header) {
		return new ColumnValidation(this.getColumn(header));
	}
	
	public ColumnValidation createColumnValidation(int columnNumber) {
		return new ColumnValidation(this.getColumn(columnNumber));
	}
	
	public ColumnValidation createColumnValidation(String header1, String header2) {
		return new ColumnValidation(this.getColumn(header1), this.getColumn(header2));
	}
	
	public ColumnValidation createColumnValidation(int columnNumber1, int columnNumber2) {
		return new ColumnValidation(this.getColumn(columnNumber1), this.getColumn(columnNumber2));
	}
	
	public RowValidation createRowValidation(int rowNumber) {
		return new RowValidation(this.getSheet().getRow(rowNumber), this.getHeaderTools());
	}
	
	
	
	/**
	 * Group multiple requests for row validations
	 * Use a range of cells from start to end
	 * @param testsToRun pairs of (method to run, arguments required)
	 * @param start lowest row id
	 * @param end highest row id
	 * @return
	 */
	public ArrayList<CreateValidation> createMultipleValidation(PairMethodArguments[] testsToRun, int start, int end) {
		int[] rows = SheetTools.getRowIdsFromRange(start, end);
		return createMultipleRowValidation(testsToRun, rows);
	}
	
	
	/**
	 * Group multiple requests for row validations
	 * @param testsToRun pairs of (method to run, arguments required)
	 * @param rowNumber row for which the tests will be run
	 * @return a list of validation and methods associated tha can be executed with the run() method
	 */
	public ArrayList<CreateValidation> createMultipleValidation(PairMethodArguments[] testsToRun, int rowNumber) {
		int[] rowNumbers = new int[]{rowNumber};
		return createMultipleRowValidation(testsToRun, rowNumbers);
	}
	
	/**
	 * Group multiple requests for row validations
	 * @param testsToRun pairs of (method to run, arguments required)
	 * @param rowNumbers rows for which the tests will be run
	 * @return a list of validation and methods associated tha can be executed with the run() method
	 */
	public ArrayList<CreateValidation> createMultipleRowValidation(PairMethodArguments[] testsToRun, int[] rowNumbers) {
		ArrayList<CreateValidation> validations = new ArrayList<CreateValidation>();
		if (rowNumbers[0] == -1) {
			rowNumbers = getAllRowNumbers();
		}
		for (PairMethodArguments testToRun:testsToRun) {
			for (int rowNumber:rowNumbers) {
				RowValidation rowValidation = this.createRowValidation(rowNumber);
				CreateValidation validation = new CreateValidation(rowValidation, testToRun.getMethodId(), testToRun.getArguments());
				validations.add(validation);
			}
		}
		return validations;
	}
	
	public int[] getAllRowNumbers(){
		int lastRow = this.getSheet().getLastRowNum();
		int[] allRows = new int[lastRow];
		for (int i = 1; i <= lastRow; i++) {
			allRows[i-1] = i;
		}
		return allRows;
	}
	
	public ErrorManager getErrorManager() {
		return errorManager;
	}

	public void setErrorManager(ErrorManager errorManager) {
		this.errorManager = errorManager;
	}
	

	
}
