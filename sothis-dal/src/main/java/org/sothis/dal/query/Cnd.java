package org.sothis.dal.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Cnd {

	private List<Param> params;
	private int leftBraceCount = 0;
	private List<Sort> sorts;

	protected Cnd() {
	}

	public static Cnd where(String field, Op op, Object value) {
		Cnd cnd = new Cnd();
		return cnd.append(Logic.And).append(field, op, value);
	}

	public static Cnd where(String field, Object value) {
		Cnd cnd = new Cnd();
		return cnd.append(Logic.And).append(field, Op.EQ, value);
	}

	public Cnd and(String field, Op op, Object value) {
		return this.append(Logic.And).append(field, op, value);
	}

	public Cnd and(String field, Object value) {
		return this.append(Logic.And).append(field, Op.EQ, value);
	}

	public Cnd or(String field, Op op, Object value) {
		return this.append(Logic.Or).append(field, op, value);
	}

	public Cnd or(String field, Object value) {
		return this.append(Logic.Or).append(field, Op.EQ, value);
	}

	public Cnd andBrace(String field, Op op, Object value, int braceCount) {
		return this.append(Logic.And).startBrace(field, op, value, braceCount);
	}

	public Cnd andBrace(String field, Object value, int braceCount) {
		return this.append(Logic.And).startBrace(field, Op.EQ, value, braceCount);
	}

	public Cnd andBrace(String field, Op op, Object value) {
		return this.append(Logic.And).startBrace(field, op, value, 1);
	}

	public Cnd andBrace(String field, Object value) {
		return this.append(Logic.And).startBrace(field, Op.EQ, value, 1);
	}

	public Cnd orBrace(String field, Op op, Object value, int braceCount) {
		return this.append(Logic.Or).startBrace(field, op, value, braceCount);
	}

	public Cnd orBrace(String field, Object value, int braceCount) {
		return this.append(Logic.Or).startBrace(field, Op.EQ, value, braceCount);
	}

	public Cnd orBrace(String field, Op op, Object value) {
		return this.append(Logic.Or).startBrace(field, op, value, 1);
	}

	public Cnd orBrace(String field, Object value) {
		return this.append(Logic.Or).startBrace(field, Op.EQ, value, 1);
	}

	public Cnd endBrace(int braceCount) {
		if (braceCount < 0) {
			throw new IllegalArgumentException("braceCount: " + braceCount);
		}
		if (isParamListEmpty()) {
			throw new IllegalStateException("can not end brace when param list is empty");
		}
		if (braceCount > leftBraceCount) {
			throw new IllegalArgumentException("there is only " + leftBraceCount + " left braces");
		}
		leftBraceCount -= braceCount;
		for (int i = 0; i < braceCount; i++) {
			this.append(Logic.RightBrace);
		}
		return this;
	}

	public Cnd endBrace() {
		return endBrace(1);
	}

	public Cnd endAllBraces() {
		if (!isParamListEmpty()) {
			return endBrace(leftBraceCount);
		} else {
			return this;
		}
	}

	public Cnd orderBy(String field, boolean asc) {
		if (StringUtils.isBlank(field)) {
			throw new IllegalArgumentException("field: " + field);
		}
		if (null == this.sorts) {
			this.sorts = new ArrayList<Sort>();
		}
		this.sorts.add(new Sort(field, asc));
		return this;
	}

	public Cnd asc(String field) {
		return orderBy(field, true);
	}

	public Cnd desc(String field) {
		return orderBy(field, false);
	}

	@SuppressWarnings("unchecked")
	public List<Param> getParams() {
		return null == params ? Collections.EMPTY_LIST : Collections.unmodifiableList(params);
	}

	@SuppressWarnings("unchecked")
	public List<Sort> getSorts() {
		return null == sorts ? Collections.EMPTY_LIST : Collections.unmodifiableList(sorts);
	}

	protected boolean isParamListEmpty() {
		return null == this.params || this.params.isEmpty();
	}

	private Cnd append(Logic logicParam) {
		newParamListIfNeeded();
		if (logicParam.isBrace()) {
			params.add(logicParam);
		} else if (!this.params.isEmpty() && !(this.params.get(this.params.size() - 1) instanceof Logic)) {
			params.add(logicParam);
		}
		return this;
	}

	private Cnd append(String field, Op operator, Object value) {
		if (StringUtils.isBlank(field)) {
			throw new IllegalArgumentException("field: " + field);
		}
		if (null == operator) {
			throw new IllegalArgumentException("operator: " + operator);
		}
		if (operator.isInOperator()) {
			if (null == value) {
				throw new IllegalArgumentException("value can't be null of in operator");
			}
			if (!(value instanceof Collection)) {
				throw new IllegalArgumentException("value must be type of Collection");
			}
		}
		newParamListIfNeeded();
		params.add(new Expression(field, operator, value));
		return this;
	}

	private Cnd startBrace(String field, Op op, Object value, int braceCount) {
		if (braceCount < 0) {
			throw new IllegalArgumentException("braceCount: " + braceCount);
		}
		leftBraceCount += braceCount;
		for (int i = 0; i < braceCount; i++) {
			this.append(Logic.LeftBrace);
		}
		return this.and(field, op, value);
	}

	private void newParamListIfNeeded() {
		if (null == this.params) {
			this.params = new ArrayList<Param>();
		}
	}

}
