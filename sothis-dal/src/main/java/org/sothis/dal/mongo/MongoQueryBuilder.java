package org.sothis.dal.mongo;

import java.util.List;
import java.util.Map;

import org.sothis.dal.AbstractJpaCompatibleDao.PropertyInfo;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;
import org.sothis.dal.query.Logic;
import org.sothis.dal.query.Op;
import org.sothis.dal.query.OrderBy;
import org.sothis.dal.query.Sort;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * mongo db 的查询生成器
 * 
 * @author velna
 * 
 */
public class MongoQueryBuilder {
	private final static String[] OP_MAP;
	private final static String[] LOGIC_MAP;
	static {
		OP_MAP = new String[Op.values().length];
		OP_MAP[Op.EQ.ordinal()] = "$eq";
		OP_MAP[Op.GT.ordinal()] = "$gt";
		OP_MAP[Op.GTE.ordinal()] = "$gte";
		OP_MAP[Op.IN.ordinal()] = "$im";
		OP_MAP[Op.LIKE.ordinal()] = "$regex";
		OP_MAP[Op.LT.ordinal()] = "$lt";
		OP_MAP[Op.LTE.ordinal()] = "$lte";
		OP_MAP[Op.NE.ordinal()] = "$ne";
		OP_MAP[Op.NIN.ordinal()] = "$nin";

		LOGIC_MAP = new String[Logic.values().length];
		LOGIC_MAP[Logic.AND.ordinal()] = "$and";
		LOGIC_MAP[Logic.OR.ordinal()] = "$or";
	}
	private final Map<String, PropertyInfo> propertyMap;
	private final DBObject defaultFields;

	public MongoQueryBuilder(Map<String, PropertyInfo> propertyMap) {
		this.propertyMap = propertyMap;
		defaultFields = new BasicDBObject();
		for (PropertyInfo p : propertyMap.values()) {
			defaultFields.put(p.getColumn().name(), 1);
		}
	}

	private String mapField(String property) {
		PropertyInfo pi = propertyMap.get(property);
		if (null == pi) {
			throw new IllegalArgumentException("no property named [" + property + "] found.");
		}
		return pi.getColumn().name();
	}

	/**
	 * 将{@code cnd}转化为mongo db可用的查询对象
	 * 
	 * @param cnd
	 * @return
	 */
	public DBObject cndToQuery(Cnd cnd) {
		if (null == cnd) {
			return null;
		}
		DBObject query = new BasicDBObject();
		Object op = cnd.getOp();
		if (op instanceof Op) {
			if (op == Op.EQ) {
				query.put(mapField((String) cnd.getLeft()), cnd.getRight());
			} else {
				query.put(mapField((String) cnd.getLeft()),
						new BasicDBObject(OP_MAP[((Op) op).ordinal()], cnd.getRight()));
			}
		} else if (op instanceof Logic) {
			BasicDBList logicQuery = new BasicDBList();
			logicQuery.add(cndToQuery((Cnd) cnd.getLeft()));
			logicQuery.add(cndToQuery((Cnd) cnd.getRight()));
			query.put(LOGIC_MAP[((Logic) op).ordinal()], logicQuery);
		} else if (op instanceof String) {
			String _op = (String) op;
			if (_op.length() > 1 && _op.charAt(0) == '$') {
				query.put(mapField((String) cnd.getLeft()), new BasicDBObject(_op, cnd.getRight()));
			} else {
				throw new RuntimeException("unknown op: " + op);
			}
		} else {
			throw new RuntimeException("unknown op: " + op);
		}
		if (cnd.isNot()) {
			BasicDBList nors = new BasicDBList();
			nors.add(query);
			return new BasicDBObject("$nor", nors);
		} else {
			return query;
		}
	}

	/**
	 * 将{@code chain}转化为mongo db可用的字段信息
	 * 
	 * @param chain
	 * @return
	 */
	public DBObject chainToFields(Chain chain) {
		if (null == chain || chain.size() == 0) {
			return defaultFields;
		}
		DBObject fields = new BasicDBObject();
		for (Chain c : chain) {
			fields.put(mapField(c.name()), 1);
		}
		return fields;
	}

	/**
	 * 将{@code chain}转化为mongo db可用的update语句
	 * 
	 * @param chain
	 * @return
	 */
	public DBObject chainToUpdate(Chain chain) {
		if (null == chain) {
			return null;
		}
		DBObject update = new BasicDBObject();
		for (Chain c : chain) {
			if (c.value() instanceof Chain) {
				update.put(mapField(c.name()), chainToUpdate((Chain) c.value()));
			} else {
				update.put(mapField(c.name()), c.value());
			}
		}
		return update;
	}

	/**
	 * 将{@code orderBy}转化为mongo db可用的排序对象
	 * 
	 * @param orderBy
	 * @return
	 */
	public DBObject orderByToSorts(OrderBy orderBy) {
		if (null == orderBy) {
			return null;
		}
		List<Sort> sorts = orderBy.getSorts();
		if (sorts.isEmpty()) {
			return null;
		}
		DBObject sort = new BasicDBObject();
		for (Sort s : sorts) {
			sort.put(mapField(s.getField()), s.isAsc() ? 1 : -1);
		}
		return sort;
	}
}
