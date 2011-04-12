package org.sothis.dal;

enum LogicParam implements SqlParam {
	LeftBrace("("), RightBrace(")"), And("and"), Or("or"), None("");
	private final String logic;

	LogicParam(String logic) {
		this.logic = logic;
	}

	public String getLogic() {
		return logic;
	}

	public boolean isBraceParam() {
		return this == LeftBrace || this == RightBrace;
	}

	public boolean isAndOrParam() {
		return this == And || this == Or;
	}

	@Override
	public int appendToSql(StringBuilder sql, int paramPosition) {
		sql.append(logic);
		return paramPosition;
	}

}
