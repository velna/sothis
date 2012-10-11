package org.sothis.dal.query;

/**
 * ÅÅÐò×Ö¶Î
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
	 * ÅÅÐòµÄ×Ö¶ÎÃû
	 * 
	 * @return
	 */
	public String getField() {
		return field;
	}

	/**
	 * 
	 * @return ÉýÐò·µ»Øtrue£¬½µÐò·µ»Øfalse
	 */
	public boolean isAsc() {
		return asc;
	}

}
