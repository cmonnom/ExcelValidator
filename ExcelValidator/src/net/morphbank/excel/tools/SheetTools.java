package net.morphbank.excel.tools;

import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


/**
 * This class groups methods for manipulating sheets
 * 
 * @author gjimenez
 * 
 */
public class SheetTools {

	Workbook workbook;
	Sheet sheet;
	HeaderTools headerTools;

	public SheetTools(Workbook workbook, String sheetName) {
		this.workbook = workbook;
		this.sheet = workbook.getSheet(sheetName);
		this.headerTools = new HeaderTools(sheet);
	}
	
	/**
	 * Get a cell by its column and row number
	 * @param colNum
	 * @param rowNum
	 * @return
	 */
	public Cell getCell(int colNum, int rowNum) {
		Row row = sheet.getRow(rowNum);
		return row.getCell(colNum);
	}
	
	/**
	 * Get the entire column by column number
	 * without the header cell
	 * @param colNum
	 * @return an ArrayList of Cell
	 */
	public ArrayList<Cell> getColumn(int colNum) {
		ArrayList<Cell> col = new ArrayList<Cell>();
		int lastRow = sheet.getLastRowNum();
		for (int i = 1; i < lastRow; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				col.add(row.getCell(colNum));
			}
		}
		return col;
		
	}
	
	/**
	 * Get the entire column by header
	 * without the header cell
	 * @param colNum
	 * @return an ArrayList of Cell
	 */
	public ArrayList<Cell> getColumn(String header) {
		int colNum = headerTools.getColNumber(header);
		return this.getColumn(colNum);
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public HeaderTools getHeaderTools() {
		return headerTools;
	}

	public void setHeaderTools(HeaderTools headertools) {
		this.headerTools = headertools;
	}
	
	/**
	 * Get all row ids between start and end (included)
	 * @param start
	 * @param end
	 * @return
	 */
	public static int[] getRowIdsFromRange(int start, int end) {
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		int[] rows = new int[end - start + 1];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = start + i;
		}
		return rows;
	}
	
}
