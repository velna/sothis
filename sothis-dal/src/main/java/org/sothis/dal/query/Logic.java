package org.sothis.dal.query;

public enum Logic implements Param {
	LeftBrace("("), RightBrace(")"), And("and"), Or("or"), None("");
	private final String logic;

	Logic(String logic) {
		this.logic = logic;
	}

	public String getLogic() {
		return logic;
	}

	public boolean isBrace() {
		return this == LeftBrace || this == RightBrace;
	}

	public boolean isAndOr() {
		return this == And || this == Or;
	}

}
