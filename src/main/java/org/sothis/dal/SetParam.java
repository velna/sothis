package org.sothis.dal;

class SetParam implements SqlParam {

	private String field;
	private Object value;

	public SetParam(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	@Override
	public int appendToSql(StringBuilder sql, int paramPosition) {
		sql.append(field).append(" = ?").append(paramPosition++);
		return paramPosition;
	}

	public String getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}

}
