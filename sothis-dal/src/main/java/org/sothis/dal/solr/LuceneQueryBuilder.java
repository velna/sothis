package org.sothis.dal.solr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;

public class LuceneQueryBuilder {

	private BooleanQuery booleanQuery = new BooleanQuery();
	public final static FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			TimeZone.getTimeZone("UTC"));
	/**
	 * 约定：以$$开头的字符串不需要特殊字符转义
	 */
	public final static String NO_ESCAPSE = "$$";

	public LuceneQueryBuilder range(String field, Object min, Object max, boolean minInclusive, boolean maxInclusive,
			BooleanClause.Occur occur) {
		if (min != null || max != null) {
			Object obj = null == min ? max : min;
			Query query = null;
			if (Integer.class.isInstance(obj)) {
				query = NumericRangeQuery.newIntRange(field, (Integer) min, (Integer) max, minInclusive, maxInclusive);
			} else if (Long.class.isInstance(obj)) {
				query = NumericRangeQuery.newLongRange(field, (Long) min, (Long) max, minInclusive, maxInclusive);
			} else if (Double.class.isInstance(obj)) {
				query = NumericRangeQuery.newDoubleRange(field, (Double) min, (Double) max, minInclusive, maxInclusive);
			} else if (Float.class.isInstance(obj)) {
				query = NumericRangeQuery.newFloatRange(field, (Float) min, (Float) max, minInclusive, maxInclusive);
			} else if (Date.class.isInstance(obj)) {
				query = new TermRangeQuery(field, null == min ? null : DATE_FORMAT.format((Date) min),
						null == max ? null : DATE_FORMAT.format((Date) max), minInclusive, maxInclusive);
			} else {
				query = new TermRangeQuery(field, null == min ? (String) min : min.toString(),
						null == max ? (String) max : max.toString(), minInclusive, maxInclusive);
			}
			booleanQuery.add(query, occur);
		}
		return this;
	}

	public LuceneQueryBuilder range(String field, Object min, Object max, boolean minInclusive, boolean maxInclusive) {
		return range(field, min, max, minInclusive, maxInclusive, BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder range(String field, Object min, Object max) {
		return range(field, min, max, true, true, BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder range(String field, Object min, Object max, BooleanClause.Occur occur) {
		return range(field, min, max, true, true, occur);
	}

	public LuceneQueryBuilder gt(String field, Object value) {
		return range(field, value, null, false, true, BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder gt(String field, Object value, BooleanClause.Occur occur) {
		return range(field, value, null, false, true, occur);
	}

	public LuceneQueryBuilder gte(String field, Object value) {
		return range(field, value, null, true, true, BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder gte(String field, Object value, BooleanClause.Occur occur) {
		return range(field, value, null, true, true, occur);
	}

	public LuceneQueryBuilder lt(String field, Object value) {
		return range(field, null, value, true, false, BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder lt(String field, Object value, BooleanClause.Occur occur) {
		return range(field, null, value, true, false, occur);
	}

	public LuceneQueryBuilder lte(String field, Object value) {
		return range(field, null, value, true, true, BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder lte(String field, Object value, BooleanClause.Occur occur) {
		return range(field, null, value, true, true, occur);
	}

	public LuceneQueryBuilder eq(String field, Object value, BooleanClause.Occur occur) {
		if (null != value) {
			Query query = new TermQuery(new Term(field, value.toString()));
			booleanQuery.add(query, occur);
		}
		return this;
	}

	public LuceneQueryBuilder eq(String field, Object value) {
		return eq(field, getRealValue(value), BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder eq(String field, Object value, boolean escapse) {
		return eq(field, escapse ? QueryParser.escape(value.toString()) : value.toString(), BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder ne(String field, Object value) {
		return eq(field, getRealValue(value), BooleanClause.Occur.MUST_NOT);
	}

	public LuceneQueryBuilder ne(String field, Object value, BooleanClause.Occur occur) {
		return eq(field, getRealValue(value), occur);
	}

	public LuceneQueryBuilder in(String field, List<Object> value, boolean escapse, BooleanClause.Occur occur) {
		if (null == value || value.isEmpty()) {
			throw new IllegalArgumentException("in query with null or empty list!!");
		}
		BooleanQuery bQuery = new BooleanQuery();
		for (Object v : value) {
			Query query = new TermQuery(new Term(field, escapse ? QueryParser.escape(v.toString()) : v.toString()));
			bQuery.add(query, BooleanClause.Occur.SHOULD);
		}
		booleanQuery.add(bQuery, occur);
		return this;
	}

	public LuceneQueryBuilder in(String field, List<Object> value) {
		return in(field, getRealValue(value), false, BooleanClause.Occur.MUST);
	}

	public LuceneQueryBuilder in(String field, List<Object> value, BooleanClause.Occur occur) {
		return in(field, getRealValue(value), false, occur);
	}

	public LuceneQueryBuilder in(String field, List<Object> value, boolean escapse) {
		return in(field, value, escapse, BooleanClause.Occur.MUST);
	}

	public Query toQuery() {
		return this.booleanQuery;
	}

	/**
	 * 获取真实value<br/>
	 * 如果是非string的value，返回转义后的原值<br/>
	 * 如果是string型，如果以$$开头，则截取$$后面的字符且不需要转义
	 * 
	 * @param value
	 * @return
	 */
	private Object getRealValue(Object value) {
		if (value instanceof String) {
			if (NO_ESCAPSE.equals(value)) {
				return value;
			} else if (StringUtils.startsWith((String) value, NO_ESCAPSE)) {
				return StringUtils.substring((String) value, NO_ESCAPSE.length());
			}
		}
		return QueryParser.escape(value.toString());
	}

	private List<Object> getRealValue(List<Object> value) {
		if (null == value || value.isEmpty()) {
			return value;
		}
		List<Object> ret = new ArrayList<Object>(value.size());
		for (Object v : value) {
			ret.add(getRealValue(v));
		}
		return ret;
	}
}
