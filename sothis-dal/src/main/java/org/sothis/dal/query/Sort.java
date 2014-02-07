package org.sothis.dal.query;

/**
 * 排序字段
 * 
 * @author velna
 * 
 */
public class Sort {
	private final String field;
	private final boolean asc;

	public Sort(String field, boolean asc) {
		this.field = field;
		this.asc = asc;
	}

	/**
	 * 排序的字段名
	 * 
	 * @return
	 */
	public String getField() {
		return field;
	}

	/**
	 * 
	 * @return 升序返回true，降序返回false
	 */
	public boolean isAsc() {
		return asc;
	}

}
