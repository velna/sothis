package org.sothis.dal.mongo;

import java.util.List;

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
 * mongo db �Ĳ�ѯ������
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

	/**
	 * ��{@code cnd}ת��Ϊmongo db���õĲ�ѯ����
	 * 
	 * @param cnd
	 * @return
	 */
	public static DBObject cndToQuery(Cnd cnd) {
		if (null == cnd) {
			return null;
		}
		DBObject query = new BasicDBObject();
		Object op = cnd.getOp();
		if (op instanceof Op) {
			if (op == Op.EQ) {
				query.put((String) cnd.getLeft(), cnd.getRight());
			} else {
				query.put((String) cnd.getLeft(), new BasicDBObject(OP_MAP[((Op) op).ordinal()], cnd.getRight()));
			}
		} else if (op instanceof Logic) {
			BasicDBList logicQuery = new BasicDBList();
			logicQuery.add(cndToQuery((Cnd) cnd.getLeft()));
			logicQuery.add(cndToQuery((Cnd) cnd.getRight()));
			query.put(LOGIC_MAP[((Logic) op).ordinal()], logicQuery);
		} else if (op instanceof String) {
			String _op = (String) op;
			if (_op.length() > 1 && _op.charAt(0) == '$') {
				query.put((String) cnd.getLeft(), new BasicDBObject(_op, cnd.getRight()));
			}
		} else {
			throw new RuntimeException("unknown op: " + op);
		}
		return query;
	}

	/**
	 * ��{@code chain}ת��Ϊmongo db���õ��ֶ���Ϣ
	 * 
	 * @param chain
	 * @return
	 */
	public static DBObject chainToFields(Chain chain) {
		if (null == chain) {
			return null;
		}
		DBObject fields = new BasicDBObject();
		for (Chain c : chain) {
			fields.put(c.name(), 1);
		}
		return fields;
	}

	/**
	 * ��{@code chain}ת��Ϊmongo db���õ�update���
	 * 
	 * @param chain
	 * @return
	 */
	public static DBObject chainToUpdate(Chain chain) {
		if (null == chain) {
			return null;
		}
		DBObject update = new BasicDBObject();
		for (Chain c : chain) {
			if (c.value() instanceof Chain) {
				update.put(c.name(), chainToUpdate((Chain) c.value()));
			} else {
				update.put(c.name(), c.value());
			}
		}
		return update;
	}

	/**
	 * ��{@code orderBy}ת��Ϊmongo db���õ��������
	 * 
	 * @param orderBy
	 * @return
	 */
	public static DBObject orderByToSorts(OrderBy orderBy) {
		if (null == orderBy) {
			return null;
		}
		List<Sort> sorts = orderBy.getSorts();
		if (sorts.isEmpty()) {
			return null;
		}
		DBObject sort = new BasicDBObject();
		for (Sort s : sorts) {
			sort.put(s.getField(), s.isAsc() ? 1 : -1);
		}
		return sort;
	}
}