package org.qingfox.framework.database.beans;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {

	private static final long serialVersionUID = 8849184942059467559L;

	private Integer pageIndex;

	private Integer recordNumber;

	private Long recordCount;

	private Long pageCount;

	private List<T> records;

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}

	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}

	public Long getPageCount() {
		return pageCount;
	}

	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

}
