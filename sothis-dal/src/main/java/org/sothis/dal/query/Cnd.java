package org.sothis.dal.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Cnd implements OrderBy {
	private final Object left;
	private final Object op;
	private final Object right;
	private boolean not;
	private final List<Sort> sorts = new LinkedList<Sort>();

	private Cnd() {
		this.left = null;
		this.op = null;
		this.right = null;
	}

	private Cnd(Object left, Object op, Object right) {
		if (null == op) {
			throw new IllegalArgumentException("op can not be null");
		}
		this.left = left;
		this.op = op;
		this.right = right;
	}

	public static Cnd make(String field, Object op, Object value) {
		return new Cnd(field, op, value);
	}

	public static Cnd make(String field, Object value) {
		return new Cnd(field, Op.EQ, value);
	}

	public Cnd and(String field, Object op, Object value) {
		Cnd cnd = new Cnd(field, op, value);
		return and(this, cnd);
	}

	public Cnd and(String field, Object value) {
		Cnd cnd = new Cnd(field, Op.EQ, value);
		return and(this, cnd);
	}

	public Cnd and(Cnd cnd) {
		return and(this, cnd);
	}

	public Cnd or(Cnd cnd) {
		return or(this, cnd);
	}

	public Cnd or(String field, Object op, Object value) {
		Cnd cnd = new Cnd(field, op, value);
		return or(this, cnd);
	}

	public Cnd or(String field, Object value) {
		Cnd cnd = new Cnd(field, Op.EQ, value);
		return or(this, cnd);
	}

	public static Cnd and(Cnd left, Cnd right) {
		return new Cnd(left, Logic.AND, right);
	}

	public static Cnd or(Cnd left, Cnd right) {
		return new Cnd(left, Logic.OR, right);
	}

	public Cnd not() {
		this.not = true;
		return this;
	}

	public static OrderBy orderBy(String field, boolean asc) {
		if (asc) {
			return new Cnd().asc(field);
		} else {
			return new Cnd().desc(field);
		}
	}

	public OrderBy asc(String field) {
		this.sorts.add(new Sort(field, true));
		return this;
	}

	public OrderBy desc(String field) {
		this.sorts.add(new Sort(field, false));
		return this;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		if (not) {
			ret.append('!');
		}
		if (not || op instanceof Logic) {
			ret.append("(");
		}
		ret.append(left).append(' ').append(op).append(' ').append(right);
		if (not || op instanceof Logic) {
			ret.append(")");
		}
		return ret.toString();
	}

	public Object getLeft() {
		return left;
	}

	public Object getOp() {
		return op;
	}

	public Object getRight() {
		return right;
	}

	public boolean isNot() {
		return not;
	}

	public boolean isOrderByOnly() {
		return op == null;
	}

	public List<Sort> getSorts() {
		return Collections.unmodifiableList(sorts);
	}

}
