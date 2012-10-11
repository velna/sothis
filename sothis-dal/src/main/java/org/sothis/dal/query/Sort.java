package org.sothis.dal.query;

public class Sort {
	private final String field;
	private final boolean asc;

	public Sort(String field, boolean asc) {
		this.field = field;
		this.asc = asc;
	}

	public String getField() {
		return field;
	}

	public boolean isAsc() {
		return asc;
	}

}
