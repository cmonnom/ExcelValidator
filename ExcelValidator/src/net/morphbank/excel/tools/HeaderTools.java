package net.morphbank.excel.tools;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * This class groups methods for getting columns and cells
 * by header
 * @author gjimenez
 *
 */
public class HeaderTools {
	
	Sheet sheet;
	Row headers;

	public HeaderTools(Sheet sheet) {
		this.sheet = sheet;
		headers = sheet.getRow(0);
	}
	
	/**
	 * Get the colum number by its header (first cell)
	 * @param colName
	 * @return
	 */
	public int getColNumber(String colName) {
		for (int i = 0; i < headers.getLastCellNum(); i++) {
			String currentHeader = headers.getCell(i).getStringCellValue();
			if (currentHeader.equalsIgnoreCase(colName)) {
				return i;
			}
		}
		return -1;
	}
	
	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public Row getHeaders() {
		return headers;
	}

	public void setHeaders(Row headers) {
		this.headers = headers;
	}
	
}
