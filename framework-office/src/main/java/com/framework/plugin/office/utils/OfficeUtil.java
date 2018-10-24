package com.framework.plugin.office.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.framework.plugin.office.bean.ExcelBean;

public class OfficeUtil {

	/**
	 * 根据EXCEL文件生成BEAN
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ExcelBean getExcelBean(File file)
			throws FileNotFoundException, IOException {
		ExcelBean excelBean = new ExcelBean();
		List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));

		for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
			List<List<String>> rowList = new ArrayList<List<String>>();
			Iterator<Row> rowIt = sheet.rowIterator();
			while (rowIt.hasNext()) {
				Row row = rowIt.next();
				List<String> cellList = new ArrayList<String>();
				Iterator<Cell> cellIt = row.cellIterator();
				while (cellIt.hasNext()) {
					Cell cell = cellIt.next();
					cellList.add(getCellValue(cell));
				}
				if (excelBean.getMaxcellnum() == null
						|| excelBean.getMaxcellnum() < cellList.size()) {
					excelBean.setMaxcellnum(cellList.size());
				}
				rowList.add(cellList);
			}
			if (excelBean.getMaxrownum() == null
					|| excelBean.getMaxrownum() < rowList.size()) {
				excelBean.setMaxrownum(rowList.size());
			}
			sheetList.add(rowList);
		}
		hssfWorkbook.close();
		excelBean.setMaxsheetnum(sheetList.size());
		excelBean.setExcelData(sheetList);
		return excelBean;
	}

	/**
	 * 合并多个EXCEL文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ExcelBean getExcelBean(File[] files)
			throws FileNotFoundException, IOException {
		ExcelBean excelBean = new ExcelBean();
		List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();

		for (File file : files) {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(
					file));
			for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
				List<List<String>> rowList = new ArrayList<List<String>>();
				Iterator<Row> rowIt = sheet.rowIterator();
				while (rowIt.hasNext()) {
					Row row = rowIt.next();
					List<String> cellList = new ArrayList<String>();
					Iterator<Cell> cellIt = row.cellIterator();
					while (cellIt.hasNext()) {
						Cell cell = cellIt.next();
						cellList.add(getCellValue(cell));
					}
					if (excelBean.getMaxcellnum() == null
							|| excelBean.getMaxcellnum() < cellList.size()) {
						excelBean.setMaxcellnum(cellList.size());
					}
					rowList.add(cellList);
				}
				if (excelBean.getMaxrownum() == null
						|| excelBean.getMaxrownum() < rowList.size()) {
					excelBean.setMaxrownum(rowList.size());
				}
				sheetList.add(rowList);
			}
			hssfWorkbook.close();
		}

		excelBean.setMaxsheetnum(sheetList.size());
		excelBean.setExcelData(sheetList);
		return excelBean;
	}

	/**
	 * cell值转换为String
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue() + "";
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case Cell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue() + "";
		}
		return "";
	}
}
