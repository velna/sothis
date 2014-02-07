package org.sothis.dal.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 查询条件类。通过本类可以简便的生成查询条件。
 * 
 * @author velna
 * 
 */
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

	/**
	 * 生成一个由简单表达式开始的查询条件
	 * 
	 * @param field
	 *            字段名
	 * @param op
	 *            操作符
	 * @param value
	 *            字段值
	 * @return
	 */
	public static Cnd make(String field, Object op, Object value) {
		return new Cnd(field, op, value);
	}

	/**
	 * 相当于Cnd.make(field, Op.EQ, value);
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Cnd make(String field, Object value) {
		return new Cnd(field, Op.EQ, value);
	}

	/**
	 * 向当前条件添加一个简单表达式，并用and逻辑运算符进行连接
	 * 
	 * @param field
	 * @param op
	 * @param value
	 * @return
	 */
	public Cnd and(String field, Object op, Object value) {
		Cnd cnd = new Cnd(field, op, value);
		return and(this, cnd);
	}

	/**
	 * 相当于cnd.and(field, Op.EQ, value);
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public Cnd and(String field, Object value) {
		Cnd cnd = new Cnd(field, Op.EQ, value);
		return and(this, cnd);
	}

	/**
	 * 向当前条件添加一个条件，并用and逻辑运算符进行连接
	 * 
	 * @param cnd
	 * @return
	 */
	public Cnd and(Cnd cnd) {
		return and(this, cnd);
	}

	/**
	 * 向当前条件添加一个条件，并用or逻辑运算符进行连接
	 * 
	 * @param cnd
	 * @return
	 */
	public Cnd or(Cnd cnd) {
		return or(this, cnd);
	}

	/**
	 * 向当前条件添加一个简单表达式，并用or逻辑运算符进行连接
	 * 
	 * @param field
	 * @param op
	 * @param value
	 * @return
	 */
	public Cnd or(String field, Object op, Object value) {
		Cnd cnd = new Cnd(field, op, value);
		return or(this, cnd);
	}

	/**
	 * 相当于cnd.or(field, Op.EQ, value);
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public Cnd or(String field, Object value) {
		Cnd cnd = new Cnd(field, Op.EQ, value);
		return or(this, cnd);
	}

	/**
	 * 创建一个由两个条件组成的条件，这两个条件用and逻辑运算符进行连接
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Cnd and(Cnd left, Cnd right) {
		return new Cnd(left, Logic.AND, right);
	}

	/**
	 * 创建一个由两个条件组成的条件，这两个条件用or逻辑运算符进行连接
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Cnd or(Cnd left, Cnd right) {
		return new Cnd(left, Logic.OR, right);
	}

	/**
	 * 对当前条件进行取反逻辑运算
	 * 
	 * @return
	 */
	public Cnd not() {
		this.not = true;
		return this;
	}

	/**
	 * 创建一个只有排序的条件
	 * 
	 * @param field
	 * @param asc
	 * @return
	 */
	public static Cnd orderBy(String field, boolean asc) {
		if (asc) {
			return new Cnd().asc(field);
		} else {
			return new Cnd().desc(field);
		}
	}

	@Override
	public Cnd asc(String field) {
		this.sorts.add(new Sort(field, true));
		return this;
	}

	@Override
	public Cnd desc(String field) {
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

	/**
	 * 得到条件的左运算符
	 * 
	 * @return
	 */
	public Object getLeft() {
		return left;
	}

	/**
	 * 得到条件的操作符
	 * 
	 * @return
	 */
	public Object getOp() {
		return op;
	}

	/**
	 * 得到条件的右运算符
	 * 
	 * @return
	 */
	public Object getRight() {
		return right;
	}

	/**
	 * 得到当前条件是否已取反
	 * 
	 * @return
	 */
	public boolean isNot() {
		return not;
	}

	/**
	 * 得到当前条件是否只有排序
	 * 
	 * @return
	 */
	public boolean isOrderByOnly() {
		return op == null;
	}

	@Override
	public List<Sort> getSorts() {
		return Collections.unmodifiableList(sorts);
	}

}
