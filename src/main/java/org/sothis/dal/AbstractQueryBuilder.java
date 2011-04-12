package org.sothis.dal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractQueryBuilder implements QueryBuilder {

	private Class<? extends Serializable> clazz;
	private List<SqlParam> paramList;
	private int leftBraceCount = 0;
	private List<SortParam> sortParamList;

	protected AbstractQueryBuilder(Class<? extends Serializable> clazz) {
		this.clazz = clazz;
	}

	public Class<? extends Serializable> getEntityClass() {
		return clazz;
	}

	protected abstract String buildSqlString();

	@Override
	public String toString() {
		this.endAllBraces();
		return buildSqlString();
	}

	@Override
	public Query createQuery(EntityManager entityManager) {
		this.endAllBraces();
		Query query = entityManager.createQuery(this.buildSqlString());
		this.setParamValues(query);
		return query;
	}

	private AbstractQueryBuilder append(String field, Operator operator,
			Object value) {
		if (StringUtils.isBlank(field)) {
			throw new IllegalArgumentException("field: " + field);
		}
		if (null == operator) {
			throw new IllegalArgumentException("operator: " + operator);
		}
		if (operator.isInOperator()) {
			if (null == value) {
				throw new IllegalArgumentException(
						"value can't be null of in operator");
			}
			if (!(value instanceof Collection)) {
				throw new IllegalArgumentException(
						"value must be type of Collection");
			}
		}
		newParamListIfNeeded();
		paramList.add(new QueryParam(field, operator, value));
		return this;
	}

	private AbstractQueryBuilder append(LogicParam logicParam) {
		newParamListIfNeeded();
		if (logicParam.isBraceParam()) {
			paramList.add(logicParam);
		} else if (!this.paramList.isEmpty()
				&& !(this.paramList.get(this.paramList.size() - 1) instanceof LogicParam)) {
			paramList.add(logicParam);
		}
		return this;
	}

	public AbstractQueryBuilder where(String field, Operator operator,
			Object value) {
		return this.and(field, operator, value);
	}

	public AbstractQueryBuilder and(String field, Operator operator,
			Object value) {
		return this.append(LogicParam.And).append(field, operator, value);
	}

	public AbstractQueryBuilder or(String field, Operator operator, Object value) {
		return this.append(LogicParam.Or).append(field, operator, value);
	}

	private AbstractQueryBuilder startBrace(String field, Operator operator,
			Object value, int braceCount) {
		if (braceCount < 0) {
			throw new IllegalArgumentException("braceCount: " + braceCount);
		}
		leftBraceCount += braceCount;
		for (int i = 0; i < braceCount; i++) {
			this.append(LogicParam.LeftBrace);
		}
		return this.and(field, operator, value);
	}

	public AbstractQueryBuilder andBrace(String field, Operator operator,
			Object value, int braceCount) {
		return this.append(LogicParam.And).startBrace(field, operator, value,
				braceCount);
	}

	public AbstractQueryBuilder andBrace(String field, Operator operator,
			Object value) {
		return this.andBrace(field, operator, value, 1);
	}

	public AbstractQueryBuilder orBrace(String field, Operator operator,
			Object value, int braceCount) {
		return this.append(LogicParam.Or).startBrace(field, operator, value,
				braceCount);
	}

	public AbstractQueryBuilder orBrace(String field, Operator operator,
			Object value) {
		return orBrace(field, operator, value, 1);
	}

	public AbstractQueryBuilder endBrace(int braceCount) {
		if (braceCount < 0) {
			throw new IllegalArgumentException("braceCount: " + braceCount);
		}
		if (isParamListEmpty()) {
			throw new IllegalStateException(
					"can not end brace when param list is empty");
		}
		if (braceCount > leftBraceCount) {
			throw new IllegalArgumentException("there is only "
					+ leftBraceCount + " left braces");
		}
		leftBraceCount -= braceCount;
		for (int i = 0; i < braceCount; i++) {
			this.append(LogicParam.RightBrace);
		}
		return this;
	}

	public AbstractQueryBuilder endBrace() {
		return endBrace(1);
	}

	public AbstractQueryBuilder endAllBraces() {
		if (!isParamListEmpty()) {
			return endBrace(leftBraceCount);
		} else {
			return this;
		}
	}

	protected int appendWhereClosureToSql(StringBuilder sql, int paramPosition) {
		if (!isParamListEmpty()) {
			sql.append(" where");
			for (SqlParam param : this.paramList) {
				sql.append(" ");
				paramPosition = param.appendToSql(sql, paramPosition);
			}
		}
		return paramPosition;
	}

	@SuppressWarnings("unchecked")
	private void setParamValues(Query query) {
		if (isParamListEmpty()) {
			return;
		}
		int index = 0;
		for (SqlParam sqlParam : this.paramList) {
			if (sqlParam instanceof QueryParam) {
				QueryParam param = (QueryParam) sqlParam;
				if (param.getOperator().isInOperator()) {
					Collection<Object> values = (Collection<Object>) param
							.getValue();
					for (Iterator<Object> iterator = values.iterator(); iterator
							.hasNext();) {
						query.setParameter(index++, iterator.next());
					}
				} else if (param.getOperator().isIsOperator()
						|| param.getOperator().isWhereString()) {
					// empty
				} else {
					query.setParameter(index++, param.getValue());
				}
			}
		}
	}

	public AbstractQueryBuilder orderBy(String field, boolean asc) {
		if (StringUtils.isBlank(field)) {
			throw new IllegalArgumentException("field: " + field);
		}
		if (null == this.sortParamList) {
			this.sortParamList = new ArrayList<SortParam>();
		}
		this.sortParamList.add(new SortParam(field, asc));
		return this;
	}

	protected int appendOrderByClosureToSql(StringBuilder sql, int paramPosition) {
		if (null != this.sortParamList && !this.sortParamList.isEmpty()) {
			sql.append(" order by");
			for (Iterator<SortParam> i = this.sortParamList.iterator(); i
					.hasNext();) {
				sql.append(" ");
				i.next().appendToSql(sql, 0);
				if (i.hasNext()) {
					sql.append(",");
				}
			}
		}
		return paramPosition;
	}

	private void newParamListIfNeeded() {
		if (null == this.paramList) {
			this.paramList = new ArrayList<SqlParam>();
		}
	}

	private boolean isParamListEmpty() {
		return null == this.paramList || this.paramList.isEmpty();
	}
}
