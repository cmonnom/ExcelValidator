package net.morphbank.excel.tools;

import org.apache.poi.ss.usermodel.Cell;

public class CellTools {
	Cell cell;
	HeaderTools headerTools;
	String sheetname;
	
	public CellTools(Cell cell, HeaderTools headerTools) {
		super();
		this.cell = cell;
		this.headerTools = headerTools;
		this.sheetname = headerTools.getSheet().getSheetName();
	}
	
	/**
	 * Check if a cell is empty.
	 * An empty cell is either null or blank or value of 0
	 * @return true is the cell is empty
	 */
	public boolean isEmpty() { //TODO figure out the case of formulas and the problem when the cell content is actually 0
		if (cell == null ) {
			return true;
		}
		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()
				|| type == Cell.CELL_TYPE_BLANK
				|| type == Cell.CELL_TYPE_NUMERIC && cell.getNumericCellValue() == 0) {
			return true;
		}
		return false;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}
	
	/**
	 * Get a String value of a cell regardless of CellType
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		}
		return "";
	}
	
}
