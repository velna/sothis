package org.sothis.core.util.bwlist;

import java.util.Map;
import java.util.Set;

public class LeafExpression implements Expression {

	private final int id;
	private final String field;
	private final Matcher matcher;

	public LeafExpression(int id, String field, Matcher matcher) {
		super();
		this.id = id;
		this.field = field;
		this.matcher = matcher;
	}

	@Override
	public final boolean matches(Map<String, String> targets, BWResult result) {
		Boolean matchResult = result.getExpressionResult(this.id);
		String target = targets.get(field);
		if (null == matchResult) {
			matchResult = Boolean.FALSE;
			Set<Integer> exprIds = this.matcher.matches(target);
			for (Integer exprId : exprIds) {
				if (exprId == this.id) {
					matchResult = Boolean.TRUE;
				}
				result.setExpressionResult(exprId, true, field, target);
			}
		}
		if (matchResult == Boolean.FALSE) {
			result.setExpressionResult(this.id, matchResult, field, target);
		}
		return matchResult;
	}

	public int getId() {
		return id;
	}

	public String getField() {
		return field;
	}

	public Matcher getMatcher() {
		return matcher;
	}

}
