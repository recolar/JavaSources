package com.rec.db;

/**
 * �����ѯ��ҳ�����Ϣ.
 * 
 * @author recolar
 * @version 1.0.0
 * @date 2013-9-25
 * @since JDK1.6.0
 * 
 */
public class PageParam {

	/**
	 * һ���ж�����.
	 */
	private int totalRowsCount;

	/**
	 * һ���ж���ҳ.
	 */
	private int totalPageCount;

	/**
	 * ��ǰ�ڼ�ҳ
	 */
	private int pageNumber;

	/**
	 * ÿҳ�ж�����.
	 */
	private int rowsCountPerPage;

	/**
	 * ��ǰҳ�ڼ��п�ʼ.
	 */
	@SuppressWarnings("unused")
	private int beginRowNumber;

	/**
	 * ��ǰҳ�ڼ��н���.
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
