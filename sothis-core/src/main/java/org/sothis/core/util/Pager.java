package org.sothis.core.util;

public class Pager {

	private int startRow = 0;

	private int pageSize = 1;

	private int totalRows = 0;

	private int maxPage = 0;

	public Pager(int pageSize) {
		this.setPageSize(pageSize);
	}

	public Pager(int pageSize, int maxPage) {
		this.setPageSize(pageSize);
		this.setMaxPage(maxPage);
	}

	public Pager() {
	}

	public static Pager make(int startRow, int pageSize) {
		Pager pager = new Pager(pageSize);
		pager.setStartRow(startRow);
		return pager;
	}

	public final int getTotalRows() {
		return totalRows;
	}

	public final void setTotalRows(int totalRows) {
		this.totalRows = totalRows < 0 ? 0 : totalRows;
	}

	public final int getStartRow() {
		return startRow;
	}

	public final void setStartRow(int startRow) {
		this.startRow = startRow < 0 ? 0 : startRow;
	}

	public final int getCurrentPage() {
		if (pageSize > 0) {
			return this.startRow / pageSize + 1;
		} else {
			return this.startRow + 1;
		}
	}

	public final void setCurrentPage(int currentPage) {
		if (maxPage > 0) {
			currentPage = currentPage <= maxPage ? currentPage : maxPage;
		}
		this.setStartRow((currentPage - 1) * pageSize);
	}

	public final int getPageSize() {
		return pageSize;
	}

	public final void setPageSize(int pageSize) {
		int curPage = this.getCurrentPage();
		this.pageSize = pageSize > 0 ? pageSize : 1;
		setCurrentPage(curPage);
	}

	public final int getTotalPages() {
		int totalPages = totalRows % pageSize == 0 ? totalRows / pageSize : totalRows / pageSize + 1;
		if (maxPage > 0) {
			totalPages = totalPages <= maxPage ? totalPages : maxPage;
		}
		return totalPages;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage > 0 ? maxPage : 0;
		if (this.maxPage > 0 && getCurrentPage() > maxPage) {
			setCurrentPage(1);
		}
	}

}
