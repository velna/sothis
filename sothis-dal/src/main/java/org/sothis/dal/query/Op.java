package org.sothis.dal.query;

public enum Op {
	GT(">"),
	GTE(">="),
	LT("<"),
	LTE("<="),
	EQ("="),
	NEQ("<>"),
	In("in"),
	NotIn("not in"),
	IsNull("is null"),
	IsNotNull("is not null"),
	Like("like"),
	NotLike("not like"),
	EMPTY("");
	private final String op;

	Op(String op) {
		this.op = op;
	}

	public String getOp() {
		return op;
	}

	public boolean isInOperator() {
		return this == In || this == NotIn;
	}

	public boolean isIsOperator() {
		return this == IsNull || this == IsNotNull;
	}

	public boolean isEmpty() {
		return this == EMPTY;
	}
}
