package org.sothis.dal.solr;

import java.util.List;
import java.util.Map;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.sothis.dal.AbstractJpaCompatibleDao.PropertyInfo;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;
import org.sothis.dal.query.Logic;
import org.sothis.dal.query.Op;
import org.sothis.dal.query.Sort;

public class SolrQueryBuilder {
	private final Map<String, PropertyInfo> propertyMap;

	public SolrQueryBuilder(Map<String, PropertyInfo> propertyMap) {
		this.propertyMap = propertyMap;
	}

	private String mapField(String property) {
		PropertyInfo pi = propertyMap.get(property);
		if (null == pi) {
			throw new IllegalArgumentException("no property named [" + property + "] found.");
		}
		return pi.getColumn().name();
	}

	public SolrQuery buildQuery(Cnd cnd, Chain chain) {
		SolrQuery query = new SolrQuery();

		Query q = buildLuceneQuery(cnd);
		String queryString = null == q ? "*:*" : q.toString();
		query.setQuery(queryString);

		if (null != chain) {
			for (Chain c : chain) {
				query.addField(mapField(c.name()));
			}
		}

		List<Sort> sorts = cnd.getSorts();
		if (!sorts.isEmpty()) {
			for (Sort sort : sorts) {
				query.addSortField(mapField(sort.getField()), sort.isAsc() ? ORDER.asc : ORDER.desc);
			}
		}

		return query;
	}

	@SuppressWarnings("unchecked")
	private Query buildLuceneQuery(Cnd cnd) {
		if (null == cnd) {
			return null;
		}
		Object op = cnd.getOp();
		if (op instanceof Op) {
			LuceneQueryBuilder luceneQB = new LuceneQueryBuilder();
			BooleanClause.Occur occur = cnd.isNot() ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.MUST;
			switch ((Op) op) {
			case EQ:
				luceneQB.eq(mapField((String) cnd.getLeft()), cnd.getRight(), occur);
				break;
			case GT:
				luceneQB.gt(mapField((String) cnd.getLeft()), cnd.getRight(), occur);
				break;
			case GTE:
				luceneQB.gte(mapField((String) cnd.getLeft()), cnd.getRight(), occur);
				break;
			case IN:
				luceneQB.in(mapField((String) cnd.getLeft()), (List<Object>) cnd.getRight(), occur);
				break;
			case LIKE:
				break;
			case LT:
				luceneQB.lt(mapField((String) cnd.getLeft()), cnd.getRight(), occur);
				break;
			case LTE:
				luceneQB.lte(mapField((String) cnd.getLeft()), cnd.getRight(), occur);
				break;
			case NE:
				luceneQB.ne(mapField((String) cnd.getLeft()), cnd.getRight(), occur);
				break;
			case NIN:
			default:
				throw new RuntimeException("unsupported op: " + op);
			}
			return luceneQB.toQuery();
		} else if (op instanceof Logic) {
			BooleanQuery query = new BooleanQuery();
			switch ((Logic) op) {
			case AND:
				query.add(buildLuceneQuery((Cnd) cnd.getLeft()), BooleanClause.Occur.MUST);
				query.add(buildLuceneQuery((Cnd) cnd.getRight()), BooleanClause.Occur.MUST);
				break;
			case OR:
				query.add(buildLuceneQuery((Cnd) cnd.getLeft()), BooleanClause.Occur.SHOULD);
				query.add(buildLuceneQuery((Cnd) cnd.getRight()), BooleanClause.Occur.SHOULD);
				break;
			}
			if (cnd.isNot()) {
				BooleanQuery q = new BooleanQuery();
				q.add(query, BooleanClause.Occur.MUST_NOT);
				return q;
			} else {
				return query;
			}
		} else {
			throw new RuntimeException("unknown op: " + op);
		}
	}
}
