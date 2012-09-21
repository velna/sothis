package org.sothis.dal.query;

public class Expression implements Param {

	private String field;
	private Object value;
	private Op operator;

	public Expression(String field, Object value) {
		this(field, Op.EQ, value);
	}

	public Expression(String filed, Op operator, Object value) {
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

	public Op getOperator() {
		return operator;
	}

}
