package net.morphbank.validation;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Entry point of the application.
 * Load a spreadsheet by name
 * @author gjimenez
 *
 */
public class Validate {

	private String excelFile;
	private Workbook workbook;
	private BufferedWriter output;

	public Validate(String excelFile, BufferedWriter out) {
		this.excelFile = excelFile;
		this.output = out;
		try {
			InputStream inp = new FileInputStream(excelFile);
			this.workbook = WorkbookFactory.create(inp);

		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			System.err.println("This file is not a valid Excel File");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public BufferedWriter getOutput() {
		return output;
	}

	public void setOutput(BufferedWriter out) {
		this.output = out;
	}
	
	
	
	
	
}
