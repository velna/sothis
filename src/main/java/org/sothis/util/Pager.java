package org.sothis.util;

public class Pager {

	private int startRow = 0;

	private int pageSize = 1;

	private int totalRows = 0;

	public Pager(int pageSize) {
		this.setPageSize(pageSize);
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows < 0 ? 0 : totalRows;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow < 0 ? 0 : startRow;
	}

	public int getCurrentPage() {
		if (pageSize > 0) {
			return this.startRow / pageSize + 1;
		} else {
			return this.startRow + 1;
		}
	}

	public void setCurrentPage(int currentPage) {
		this.setStartRow((currentPage - 1) * pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		int curPage = this.getCurrentPage();
		this.pageSize = pageSize > 0 ? pageSize : 1;
		setCurrentPage(curPage);
	}

	public int getTotalPages() {
		return totalRows % pageSize == 0 ? totalRows / pageSize : totalRows
				/ pageSize + 1;
	}

}
