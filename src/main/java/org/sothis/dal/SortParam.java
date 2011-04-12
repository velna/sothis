package org.sothis.dal;

class SortParam implements SqlParam {
	private String field;
	private boolean asc;

	public SortParam(String field, boolean asc) {
		this.field = field;
		this.asc = asc;
	}

	public String getField() {
		return field;
	}

	public boolean isAsc() {
		return asc;
	}

	@Override
	public int appendToSql(StringBuilder sql, int paramPosition) {
		sql.append(field).append(asc ? " asc" : " desc");
		return paramPosition;
	}

}
