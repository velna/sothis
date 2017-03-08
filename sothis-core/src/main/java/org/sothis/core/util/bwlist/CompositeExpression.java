package org.sothis.core.util.bwlist;

import java.util.Map;

public class CompositeExpression implements Expression {

	private final Logic logic;
	private final Expression left;
	private final Expression right;

	public CompositeExpression(Logic logic, Expression left, Expression right) {
		this.logic = logic;
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean matches(Map<String, String> targets, BWResult result) {
		boolean r = false;
		switch (this.logic) {
		case NOT:
			r = !this.left.matches(targets, result);
			break;
		case AND:
			r = this.right.matches(targets, result) && this.left.matches(targets, result);
			break;
		case OR:
			r = this.right.matches(targets, result) || this.left.matches(targets, result);
			break;
		}
		return r;
	}

	public Logic getLogic() {
		return logic;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

}
