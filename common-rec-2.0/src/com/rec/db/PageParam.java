package com.rec.db;

/**
 * 保存查询分页相关信息.
 * 
 * @author recolar
 * @version 1.0.0
 * @date 2013-9-25
 * @since JDK1.6.0
 * 
 */
public class PageParam {

	/**
	 * 一共有多少行.
	 */
	private int totalRowsCount;

	/**
	 * 一共有多少页.
	 */
	private int totalPageCount;

	/**
	 * 当前第几页
	 */
	private int pageNumber;

	/**
	 * 每页有多少行.
	 */
	private int rowsCountPerPage;

	/**
	 * 当前页第几行开始.
	 */
	@SuppressWarnings("unused")
	private int beginRowNumber;

	/**
	 * 当前页第几行结束.
	 */
	@SuppressWarnings("unused")
	private int endRowNumber;

	public PageParam() {
	}

	public int getTotalRowsCount() {
		return totalRowsCount;
	}

	public void setTotalRowsCount(int totalRowsCount) {
		this.totalRowsCount = totalRowsCount;
	}

	public int getTotalPageCount() {
		return totalRowsCount % rowsCountPerPage == 0 ? totalRowsCount / rowsCountPerPage
				: (totalRowsCount / rowsCountPerPage) + 1;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getRowsCountPerPage() {
		return rowsCountPerPage;
	}

	public void setRowsCountPerPage(int rowsCountPerPage) {
		this.rowsCountPerPage = rowsCountPerPage;
	}

	public int getBeginRowNumber() {
		return (this.pageNumber - 1) * this.rowsCountPerPage + 1;
	}

	public void setBeginRowNumber(int beginRowNumber) {
		this.beginRowNumber = beginRowNumber;
	}

	public int getEndRowNumber() {
		if (this.pageNumber == this.totalPageCount) {
			return this.totalRowsCount;
		}
		return this.pageNumber * rowsCountPerPage;
	}

	public void setEndRowNumber(int endRowNumber) {
		this.endRowNumber = endRowNumber;
	}

	@Override
	public String toString() {
		return "PageParam [totalRowsCount=" + totalRowsCount + ", totalPageCount=" + getTotalPageCount()
				+ ", pageNumber=" + pageNumber + ", rowsCountPerPage=" + rowsCountPerPage + ", beginRowNumber="
				+ getBeginRowNumber() + ", endRowNumber=" + getEndRowNumber() + "]";
	}

}
