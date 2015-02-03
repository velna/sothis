package org.sothis.dal.sql;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;
import org.sothis.dal.query.Logic;
import org.sothis.dal.query.Op;
import org.sothis.dal.query.OrderBy;
import org.sothis.dal.query.Sort;

/**
 * sql的查询生成器，基本JPA
 * 
 * @author velna
 * 
 */
public class SqlQueryBuilder {
	public final static String WHERE_PARAM_PREFIX = "w_";
	public final static String SET_PARAM_PREFIX = "s_";

	private final static int SQL_LEN_CAPACITY = 128;
	private final static String[] OP_MAP;
	private final static String[] LOGIC_MAP;
	static {
		OP_MAP = new String[Op.values().length];
		OP_MAP[Op.EQ.ordinal()] = " = ";
		OP_MAP[Op.GT.ordinal()] = " > ";
		OP_MAP[Op.GTE.ordinal()] = " >= ";
		OP_MAP[Op.IN.ordinal()] = " in ";
		OP_MAP[Op.LIKE.ordinal()] = " like ";
		OP_MAP[Op.LT.ordinal()] = " < ";
		OP_MAP[Op.LTE.ordinal()] = " <= ";
		OP_MAP[Op.NE.ordinal()] = " <> ";
		OP_MAP[Op.NIN.ordinal()] = " not in ";

		LOGIC_MAP = new String[Logic.values().length];
		LOGIC_MAP[Logic.AND.ordinal()] = " and ";
		LOGIC_MAP[Logic.OR.ordinal()] = " or ";
	}

	public static void main(String[] args) {
		StringBuilder sql = new StringBuilder();
		appendWhere(
				Cnd.or(Cnd.make("a", "a").and(Cnd.or(Cnd.make("c", "de"), Cnd.make("e", "f"))),
						Cnd.and(Cnd.make("g", "h"), Cnd.make("i", "k"))), sql);
		System.out.println(sql);
	}

	public static Query select(Class<?> entityClass, Cnd cnd, Chain chain, EntityManager entityManager) {
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("select ");
		if (chain != null) {
			sql.append("new ").append(entityClass.getName()).append('(');
			for (Iterator<Chain> i = chain.iterator(); i.hasNext();) {
				sql.append(i.next().name());
				if (i.hasNext()) {
					sql.append(", ");
				}
			}
			sql.append(')');
		} else {
			sql.append('o');
		}
		sql.append(" from ").append(entityClass.getName()).append(" o");
		appendWhere(cnd, sql);
		appendOrderBy(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		setParamValues(query, cnd, new IntegerHolder(0));
		return query;
	}

	public static Query update(Class<?> entityClass, Cnd cnd, Chain chain, EntityManager entityManager) {
		if (chain == null) {
			throw new IllegalArgumentException("chain can not be null");
		}
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("update ").append(entityClass.getName()).append(" o set ");
		int index = 0;
		for (Iterator<Chain> i = chain.iterator(); i.hasNext();) {
			Chain c = i.next();
			sql.append("o.").append(c.name()).append("=:").append(SET_PARAM_PREFIX).append(index++);
			if (i.hasNext()) {
				sql.append(", ");
			}
		}

		appendWhere(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		index = 0;
		for (Iterator<Chain> i = chain.iterator(); i.hasNext();) {
			Chain c = i.next();
			query.setParameter(SET_PARAM_PREFIX + index++, c.value());
		}
		setParamValues(query, cnd, new IntegerHolder(0));
		return query;
	}

	public static Query delete(Class<?> entityClass, Cnd cnd, EntityManager entityManager) {
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("delete from ").append(entityClass.getName()).append(" o");

		appendWhere(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		setParamValues(query, cnd, new IntegerHolder(0));
		return query;
	}

	public static Query count(Class<?> entityClass, Cnd cnd, EntityManager entityManager) {
		StringBuilder sql = new StringBuilder(SQL_LEN_CAPACITY);
		sql.append("select count(*) from ").append(entityClass.getName()).append(" o");

		appendWhere(cnd, sql);
		Query query = entityManager.createQuery(sql.toString());
		setParamValues(query, cnd, new IntegerHolder(0));
		return query;
	}

	private static void appendWhere(Cnd cnd, StringBuilder sql) {
		if (null != cnd && !cnd.isOrderByOnly()) {
			sql.append(" where ");
			appendCndToSql(cnd, sql, new IntegerHolder(0));
		}
	}

	private static void appendOrderBy(OrderBy orderBy, StringBuilder sql) {
		if (null != orderBy) {
			List<Sort> sorts = orderBy.getSorts();
			if (sorts.size() > 0) {
				sql.append(" order by ");
				for (Iterator<Sort> i = sorts.iterator(); i.hasNext();) {
					Sort sort = i.next();
					sql.append(sort.getField()).append(' ').append(sort.isAsc() ? "asc" : "desc");
					if (i.hasNext()) {
						sql.append(", ");
					}
				}
			}
		}
	}

	private static void appendCndToSql(Cnd cnd, StringBuilder sql, IntegerHolder paramIndex) {
		Object op = cnd.getOp();
		if (op instanceof Op) {
			if (cnd.isNot()) {
				sql.append(" not ");
			}
			Op _op = (Op) op;
			sql.append(cnd.getLeft()).append(OP_MAP[_op.ordinal()]);
			if (_op == Op.IN || _op == Op.NIN) {
				sql.append('(');
				if (cnd.getRight() instanceof Collection) {
					Collection<?> values = (Collection<?>) cnd.getRight();
					for (Iterator<?> i = values.iterator(); i.hasNext();) {
						i.next();
						sql.append(':').append(WHERE_PARAM_PREFIX).append(paramIndex.getAndIncrease());
						if (i.hasNext()) {
							sql.append(", ");
						}
					}
				} else if (cnd.getRight().getClass().isArray()) {
					for (int i = 0; i < Array.getLength(cnd.getRight()); i++) {
						sql.append(':').append(WHERE_PARAM_PREFIX).append(paramIndex.getAndIncrease());
						if (i < Array.getLength(cnd.getRight()) - 1) {
							sql.append(", ");
						}
					}
				}
				sql.append(')');
			} else {
				sql.append(':').append(WHERE_PARAM_PREFIX).append(paramIndex.getAndIncrease());
			}
		} else if (op instanceof Logic) {
			if (cnd.isNot()) {
				sql.append(" not (");
			}
			Cnd left = (Cnd) cnd.getLeft();
			if (left.getOp() instanceof Logic) {
				sql.append(" (");
			}
			appendCndToSql(left, sql, paramIndex);
			if (left.getOp() instanceof Logic) {
				sql.append(") ");
			}
			sql.append(LOGIC_MAP[((Logic) op).ordinal()]);
			Cnd right = (Cnd) cnd.getRight();
			if (right.getOp() instanceof Logic) {
				sql.append(" (");
			}
			appendCndToSql(right, sql, paramIndex);
			if (right.getOp() instanceof Logic) {
				sql.append(") ");
			}
			if (cnd.isNot()) {
				sql.append(") ");
			}
		} else {
			throw new RuntimeException("unknown op: " + op);
		}
	}

	private static void setParamValues(Query query, Cnd cnd, IntegerHolder paramIndex) {
		if (null == cnd) {
			return;
		}
		Object op = cnd.getOp();
		if (op instanceof Op) {
			Op _op = (Op) op;
			if (_op == Op.IN || _op == Op.NIN) {
				if (cnd.getRight() instanceof Collection) {
					Collection<?> values = (Collection<?>) cnd.getRight();
					for (Iterator<?> i = values.iterator(); i.hasNext();) {
						query.setParameter(WHERE_PARAM_PREFIX + paramIndex.getAndIncrease(), i.next());
					}
				} else if (cnd.getRight().getClass().isArray()) {
					for (int i = 0; i < Array.getLength(cnd.getRight()); i++) {
						query.setParameter(WHERE_PARAM_PREFIX + paramIndex.getAndIncrease(), Array.get(cnd.getRight(), i));
					}
				}
			} else {
				query.setParameter(WHERE_PARAM_PREFIX + paramIndex.getAndIncrease(), cnd.getRight());
			}
		} else if (op instanceof Logic) {
			setParamValues(query, (Cnd) cnd.getLeft(), paramIndex);
			setParamValues(query, (Cnd) cnd.getRight(), paramIndex);
		} else {
			throw new RuntimeException("unknown op: " + op);
		}
	}

	private static class IntegerHolder {
		private int value;

		public IntegerHolder(int initValue) {
			this.value = initValue;
		}

		public int getAndIncrease() {
			return value++;
		}

	}
}
