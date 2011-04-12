package org.sothis.dal;

import java.util.Collection;

class QueryParam implements SqlParam {

	private String field;
	private Object value;
	private Operator operator;

	public QueryParam(String field, Object value) {
		this(field, Operator.Equal, value);
	}

	public QueryParam(String filed, Operator operator, Object value) {
		this.field = filed;
		this.operator = operator;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}

	public Operator getOperator() {
		return operator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int appendToSql(StringBuilder sql, int paramPosition) {
		if (operator.isInOperator()) {
			sql.append("o.").append(field).append(" ").append(operator.getOp()).append("(");
			Collection<Object> values = (Collection<Object>) value;
			for (int i = 0; i < values.size(); i++) {
				if (i > 0) {
					sql.append(",");
				}
				sql.append("?").append(paramPosition++);
			}
			sql.append(")");
		} else if (operator.isIsOperator()) {
			sql.append("o.").append(field).append(" ").append(operator.getOp());
		} else if (operator.isWhereString()) {
			sql.append(field);
		} else {
			sql.append("o.").append(field).append(" ").append(operator.getOp()).append(" ?")
					.append(paramPosition++);
		}
		return paramPosition;
	}

}
