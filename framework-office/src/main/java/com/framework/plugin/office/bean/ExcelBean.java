package com.framework.plugin.office.bean;

import java.util.List;

public class ExcelBean {
	private Integer maxrownum;
	private Integer maxcellnum;
	private Integer maxsheetnum;
	private List<List<List<String>>> excelData;

	public String getCell(int sheetIndex, int rowIndex, int cellIndex) {
		return excelData.get(sheetIndex).get(rowIndex).get(cellIndex);
	}

	public Integer getMaxrownum() {
		return maxrownum;
	}

	public void setMaxrownum(Integer maxrownum) {
		this.maxrownum = maxrownum;
	}

	public Integer getMaxcellnum() {
		return maxcellnum;
	}

	public void setMaxcellnum(Integer maxcellnum) {
		this.maxcellnum = maxcellnum;
	}

	public List<List<List<String>>> getExcelData() {
		return excelData;
	}

	public void setExcelData(List<List<List<String>>> excelData) {
		this.excelData = excelData;
	}

	public Integer getMaxsheetnum() {
		return maxsheetnum;
	}

	public void setMaxsheetnum(Integer maxsheetnum) {
		this.maxsheetnum = maxsheetnum;
	}

}
