package org.sothis.dal.query.sql;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;
import org.sothis.dal.query.Logic;
import org.sothis.dal.query.Op;
import org.sothis.dal.query.Sort;
import org.sothis.dal.query.Param;
import org.sothis.dal.query.Expression;

public class SqlQuery {
	private final static int SQL_LEN_CAPACITY = 128;
	public final static String WHERE_PARAM_PREFIX = "w_";
	public final static String SET_PARAM_PREFIX = "s_";

	private SqlQuery() {

	}

	public static Query makeSelect(Class<?> entityClass, Cnd cnd, Chain chain, EntityManager entityManager) {
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("select ");
		if (chain != null) {
			sql.append("new ").append(entityClass.getName()).append('(');
			for (Chain c : chain) {
				sql.append(c.name());
			}
			sql.append(')');
		} else {
			sql.append('o');
		}
		sql.append(" from ").append(entityClass.getName()).append(" o");

		appendCndToSqlString(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		setQueryParamValues(cnd, query);
		return query;
	}

	public static Query makeUpdate(Class<?> entityClass, Cnd cnd, Chain chain, EntityManager entityManager) {
		if (chain == null) {
			throw new IllegalArgumentException("chain can not be null");
		}
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("update ").append(entityClass.getName()).append(" o set ");
		int index = 0;
		for (Iterator<Chain> i = chain.iterator(); i.hasNext();) {
			Chain c = i.next();
			sql.append("o.").append(c.name()).append("=:").append(SET_PARAM_PREFIX).append(index);
			if (i.hasNext()) {
				sql.append(',');
			}
		}

		appendCndToSqlString(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		index = 0;
		for (Iterator<Chain> i = chain.iterator(); i.hasNext();) {
			Chain c = i.next();
			query.setParameter(SET_PARAM_PREFIX + index, c.value());
		}
		setQueryParamValues(cnd, query);
		return query;
	}

	public static Query makeDelete(Class<?> entityClass, Cnd cnd, EntityManager entityManager) {
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("delete from ").append(entityClass.getName()).append(" o");

		appendCndToSqlString(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		setQueryParamValues(cnd, query);
		return query;
	}

	public static Query makeCount(Class<?> entityClass, Cnd cnd, EntityManager entityManager) {
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("select count(*) from ").append(entityClass.getName()).append(" o");

		appendCndToSqlString(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		setQueryParamValues(cnd, query);
		return query;
	}

	private static void appendCndToSqlString(Cnd cnd, StringBuilder sql) {
		if (null == cnd) {
			return;
		}
		cnd.endAllBraces();
		List<Param> params = cnd.getParams();
		if (params.size() > 0) {
			sql.append(" where");
			int paramPosition = 0;
			for (Param param : params) {
				sql.append(' ');
				if (param instanceof Expression) {
					paramPosition = appendWhereParamToSql((Expression) param, sql, paramPosition);
				} else if (param instanceof Logic) {
					sql.append(((Logic) param).getLogic());
				}
			}
		}

		List<Sort> sortParams = cnd.getSorts();
		if (sortParams.size() > 0) {
			sql.append(" order by");
			for (Iterator<Sort> i = sortParams.iterator(); i.hasNext();) {
				sql.append(" ");
				Sort param = i.next();
				sql.append(param.getField()).append(param.isAsc() ? " asc" : " desc");
				if (i.hasNext()) {
					sql.append(',');
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static int appendWhereParamToSql(Expression param, StringBuilder sql, int paramIndex) {
		Op operator = param.getOperator();
		String field = param.getField();
		Object value = param.getValue();

		if (operator.isInOperator()) {
			sql.append("o.").append(field).append(" ").append(operator.getOp()).append("(");
			Collection<Object> values = (Collection<Object>) value;
			for (int i = 0; i < values.size(); i++) {
				if (i > 0) {
					sql.append(",");
				}
				sql.append(":").append(WHERE_PARAM_PREFIX).append(paramIndex++);
			}
			sql.append(")");
		} else if (operator.isIsOperator()) {
			sql.append("o.").append(field).append(" ").append(operator.getOp());
		} else if (operator.isEmpty()) {
			sql.append(field);
		} else {
			sql.append("o.").append(field).append(' ').append(operator.getOp()).append(':').append(WHERE_PARAM_PREFIX).append(paramIndex++);
		}
		return paramIndex;
	}

	@SuppressWarnings("unchecked")
	private static void setQueryParamValues(Cnd cnd, Query query) {
		if (null == cnd) {
			return;
		}
		List<Param> params = cnd.getParams();
		if (params.isEmpty()) {
			return;
		}
		int index = 0;
		for (Param sqlParam : params) {
			if (sqlParam instanceof Expression) {
				Expression param = (Expression) sqlParam;
				if (param.getOperator().isInOperator()) {
					Collection<Object> values = (Collection<Object>) param.getValue();
					for (Iterator<Object> iterator = values.iterator(); iterator.hasNext();) {
						query.setParameter(index++, iterator.next());
					}
				} else if (param.getOperator().isIsOperator() || param.getOperator().isEmpty()) {
					// empty
				} else {
					query.setParameter(WHERE_PARAM_PREFIX + (index++), param.getValue());
				}
			}
		}
	}

}
