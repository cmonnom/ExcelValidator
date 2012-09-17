package net.morphbank.validation.test;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.google.common.net.MediaType;

import net.morphbank.excel.tools.ErrorManager;
import net.morphbank.excel.tools.MissingColumnException;
import net.morphbank.excel.tools.PairMethodArguments;
import net.morphbank.excel.tools.SheetTools;
import net.morphbank.validation.ColumnValidation;
import net.morphbank.validation.CreateValidation;
import net.morphbank.validation.RowValidation;
import net.morphbank.validation.SheetValidation;
import net.morphbank.validation.Validate;
import net.morphbank.validation.Validation;


public class ValidateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String excelFile = "/home/gjimenez/Morphbank/tests/validation.xls";
		String outputFile = "/home/gjimenez/Morphbank/tests/validationOutput.txt";
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(outputFile));
		ValidateTest test = new ValidateTest();
		Validate validate = new Validate(excelFile, out);
		//Sheet Test1
		SheetValidation validateSheet1 = new SheetValidation(validate.getOutput(), validate.getWorkbook(), "MySheet", MediaType.PLAIN_TEXT_UTF_8);
//		test.testPatternAbsent(validateSheet1);
//		test.testExtraSpaces(validateSheet1);
		test.testUniqueStringValue(validateSheet1);
//		test.testMandatoryCells(validateSheet1);
//		test.testCreateMultipleRowValidation(validateSheet1);
//		test.testRowRange();
//		test.binarySearch();
//		test.testUniqueNumericValue(validateSheet1);
//		test.testUniqueHeader(validateSheet1);
//		test.testUniqueValue(validateSheet1);
//		test.testDropDown(validateSheet1);
		
//		
//		check2 = validateSheet1.createMultipleRowValidation(new int[]{1,2,4,7,8,9,10}, RowValidation.MANDATORY_CELL_TEST, "Column B");
//		validateSheet1.createMultipleRowValidation(-1, RowValidation.MANDATORY_CELL_TEST, "Column B");
//		HashMap<Integer, Object[]> map =  new HashMap<Integer, Object[]>();
//		map.put(RowValidation.MANDATORY_CELL_TEST, new String[]{"Column B"});
//		validateSheet1.createMultipleRowValidation(-1, map);
//		
		
//		Cell cellTest = validateSheet1.getCell(1, 2);
//		CellStyle style = validate.getWorkbook().createCellStyle();
//		style.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
//		style.setFillPattern(CellStyle.FINE_DOTS);
//		cellTest.setCellValue("coucou");
//		cellTest.setCellStyle(style);
//		FileOutputStream fileOut = new FileOutputStream("/home/gjimenez/Morphbank/tests/workbookTest.xls");
//		validate.getWorkbook().write(fileOut);
//		fileOut.close();
//		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testPatternAbsent(SheetValidation sheetValidation) {
		String colA = "Column A";
		ColumnValidation validateColA = sheetValidation.createColumnValidation(colA);
		String[] pattern =new String[]{".", "/"};
		boolean check = validateColA.isPatternAbsent(pattern);
		if (!check) {
			ErrorManager errorManager = sheetValidation.getErrorManager();
			String message = ErrorManager.PATTERN_DETECTED_ERROR + errorManager.getListOfTerms(pattern, " or ");
			StringBuffer errorToPrint = errorManager.createMessage(message,
					validateColA.getErrorsFound());
			System.out.println(errorToPrint);
			errorManager.printMessage(errorToPrint.toString());
		}
		}

	public void testExtraSpaces(SheetValidation sheetValidation) {
		String colC = "Column C";
		ColumnValidation validateColC = sheetValidation.createColumnValidation(colC);
		boolean check = validateColC.leadingTrailingSpaces();
		if (!check) {
			StringBuffer errorToPrint = sheetValidation.getErrorManager().createMessage(ErrorManager.EXTRA_SPACES_ERROR,
					validateColC.getErrorsFound());
			System.out.println(errorToPrint);
			sheetValidation.getErrorManager().printMessage(errorToPrint.toString());
		}
	}
	
	public void testUniqueStringValue(SheetValidation sheetValidation) {
		String colC = "Column C";
		ColumnValidation validateColC = sheetValidation.createColumnValidation(colC);
		boolean check = validateColC.uniqueStringValue();
		if (!check) {
			StringBuffer errorToPrint = sheetValidation.getErrorManager()
					.createDupFoundMessage(ErrorManager.UNIQUENESS_ERROR, validateColC.getMultipleErrorsFound(), ",");
			System.out.println(errorToPrint);
			sheetValidation.getErrorManager().printMessage(errorToPrint.toString());
		}
	}
	
	public void testPatternPresent(SheetValidation sheetValidation) {
		String colB = "Column B";
		ColumnValidation validateColB = sheetValidation.createColumnValidation(colB);
		String[] pattern = new String[]{".jpg", ".tiff"};
		boolean check = validateColB.isPatternPresent(pattern);
		if (!check) {
			ErrorManager errorManager = sheetValidation.getErrorManager();
			String message = ErrorManager.PATTERN_NOT_FOUND_ERROR + errorManager.getListOfTerms(pattern, " or ");
			StringBuffer errorToPrint = errorManager.createMessage(message,
					validateColB.getErrorsFound());
			System.out.println(errorToPrint);
			errorManager.printMessage(errorToPrint.toString());
		}
	}
	
	public void testMandatoryCells(SheetValidation sheetValidation) {
		RowValidation validateRowB = sheetValidation.createRowValidation(9);
		boolean check = true;
		ErrorManager errorManager = sheetValidation.getErrorManager();
		try {
			check = validateRowB.mandatoryCells("Column C");
			if (!check) {
				StringBuffer errorToPrint = errorManager.createMessage(ErrorManager.EMPTY_CELL_ERROR, validateRowB.getErrorsFound());
				System.out.print(errorToPrint);
				errorManager.printMessage(errorToPrint.toString());
			}
		} catch (MissingColumnException e) {
			StringBuffer errorToPrint = errorManager.createMissingColumnMessage(ErrorManager.MISSING_COL_ERROR, validateRowB.getErrorsFound());
			System.out.println(errorToPrint);
			errorManager.printMessage(errorToPrint.toString());
		}
	}

	public void testCreateMultipleRowValidation(SheetValidation sheetValidation) {
		ErrorManager errorManager = sheetValidation.getErrorManager();
		boolean check = true;
		Validation validationError = null;
		try {
			PairMethodArguments test1 = new PairMethodArguments(RowValidation.MANDATORY_CELL_TEST, new String[]{"Column C"});
			PairMethodArguments test2 = new PairMethodArguments(RowValidation.MISSING_HEADER_TEST, new String[]{"Column B"});
			ArrayList<CreateValidation> rowValidations = 
					sheetValidation.createMultipleRowValidation(new PairMethodArguments[]{test1, test2}, new int[]{1,2,3,4,5,6,7,8,9,10,11,12});
			for (CreateValidation validation: rowValidations) {
				validationError = validation.getValidationType();
				check = validation.run();
				if (!check) {
					String message = ErrorManager.getErrorMessageFromTestId(validation.getTestId());
					StringBuffer errorToPrint = errorManager.createMessageFromErrorMessage(message, 
							new Object[]{validation.getValidationType().getErrorsFound()});
					System.out.print(errorToPrint);
					errorManager.printMessage(errorToPrint.toString());
				}
			}
		} catch (Exception e) {
			StringBuffer errorToPrint = errorManager.createMissingColumnMessage(ErrorManager.MISSING_COL_ERROR, validationError.getErrorsFound());
			System.err.println(errorToPrint);
			errorManager.printMessage(errorToPrint.toString());
		} 
	}
	
	public void testRowRange() {
		int[] rows = SheetTools.getRowIdsFromRange(0, 10);
		System.out.println(Arrays.toString(rows));
	}
	
	public void binarySearch() {
		String[] test = {"un", "deux", "trois", "quatre", "trois"};
		int[] test1 = {1,2,3,4,3};
		System.out.println(Arrays.binarySearch(test, "trois"));
	}
	
	public void testUniqueNumericValue(SheetValidation sheetValidation) {
		String colD = "Column D";
		ColumnValidation validateColD = sheetValidation.createColumnValidation(colD);
		boolean check = validateColD.uniqueNumericValue();
		if (!check) {
			StringBuffer errorToPrint = sheetValidation.getErrorManager()
					.createDupFoundMessage(ErrorManager.UNIQUENESS_ERROR, validateColD.getMultipleErrorsFound(), ",");
			System.out.println(errorToPrint);
			sheetValidation.getErrorManager().printMessage(errorToPrint.toString());
		}
	}
	
	public void testUniqueHeader(SheetValidation sheetValidation) {
		RowValidation validateHeader = sheetValidation.createRowValidation(0);
		boolean check = validateHeader.uniqueHeader();
		if (!check) {
			StringBuffer errorToPrint = sheetValidation.getErrorManager()
					.createDupFoundMessage(ErrorManager.UNIQUENESS_ERROR, validateHeader.getMultipleErrorsFound(), ",");
			System.out.println(errorToPrint);
			sheetValidation.getErrorManager().printMessage(errorToPrint.toString());
		}
	}
	
	public void testUniqueValue(SheetValidation sheetValidation) {
		String colC = "Column C";
		ColumnValidation validateColC = sheetValidation.createColumnValidation(colC);
		boolean check = validateColC.uniqueValue();
		if (!check) {
			StringBuffer errorToPrint = sheetValidation.getErrorManager()
					.createDupFoundMessage(ErrorManager.UNIQUENESS_ERROR, validateColC.getMultipleErrorsFound(), ",");
			System.out.println(errorToPrint);
			sheetValidation.getErrorManager().printMessage(errorToPrint.toString());
		}
	}
	
	public void testDropDown(SheetValidation sheetValidation) {
		String colB = "Column B";
		String colDD = "Drop Down";
		ColumnValidation validation = sheetValidation.createColumnValidation(colB, colDD);
		boolean check = validation.dropDown();
		if (!check) {
//			StringBuffer errorToPrint = sheetValidation.getErrorManager()
//					.createDropDownMessage(ErrorManager.DROP_DOWN_ERROR, validation.getMultipleErrorsFound());
			StringBuffer errorToPrint2 = sheetValidation.getErrorManager()
					.createMessageFromErrorMessage(ErrorManager.DROP_DOWN_ERROR, new Object[]{validation.getMultipleErrorsFound()});
			System.out.println(errorToPrint2);
//			sheetValidation.getErrorManager().printMessage(errorToPrint.toString());
		}
	}
	
}
