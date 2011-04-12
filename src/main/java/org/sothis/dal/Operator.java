package org.sothis.dal;

public enum Operator {
	GreaterThan(">"),
	GreaterEqualThan(">="),
	LessThan("<"),
	LessEqualThan("<="),
	Equal("="),
	NotEqual("<>"),
	In("in"),
	NotIn("not in"),
	IsNull("is null"),
	IsNotNull("is not null"),
	Like("like"),
	NotLike("not like"),
	WhereString("#");
	private final String op;

	Operator(String op) {
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

	public boolean isWhereString() {
		return this == WhereString;
	}
}
