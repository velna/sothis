package org.sothis.dal;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class SelectQueryBuilder extends AbstractQueryBuilder {
	private String[] selectFields;
	private boolean distinct = false;
	private boolean selectCount = false;

	private SelectQueryBuilder(Class<? extends Serializable> clazz) {
		super(clazz);
	}

	public static SelectQueryBuilder create(Class<? extends Serializable> clazz) {
		SelectQueryBuilder ret = new SelectQueryBuilder(clazz);
		ret.selectFields = new String[] { "o" };
		return ret;
	}

	public static SelectQueryBuilder createDistinct(
			Class<? extends Serializable> clazz) {
		SelectQueryBuilder ret = new SelectQueryBuilder(clazz);
		ret.selectFields = new String[] { "o" };
		ret.distinct = true;
		return ret;
	}

	public static SelectQueryBuilder create(
			Class<? extends Serializable> clazz, String... fields) {
		SelectQueryBuilder ret = new SelectQueryBuilder(clazz);
		ret.selectFields = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			ret.selectFields[i] = "o." + fields[i];
		}
		return ret;
	}

	public static SelectQueryBuilder createDistinct(
			Class<? extends Serializable> clazz, String... fields) {
		SelectQueryBuilder ret = new SelectQueryBuilder(clazz);
		ret.selectFields = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			ret.selectFields[i] = "o." + fields[i];
		}
		ret.distinct = true;
		return ret;
	}

	public static SelectQueryBuilder createCount(
			Class<? extends Serializable> clazz) {
		SelectQueryBuilder ret = new SelectQueryBuilder(clazz);
		ret.selectFields = new String[] { "count(*)" };
		ret.selectCount = true;
		return ret;
	}

	@Override
	protected String buildSqlString() {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		if (!this.selectCount && this.distinct) {
			sql.append("distinct ");
		}
		sql.append(StringUtils.join(this.selectFields, ","));
		sql.append(" from ").append(this.getEntityClass().getName())
				.append(" o");
		int paramPosition = 0;
		paramPosition = this.appendWhereClosureToSql(sql, paramPosition);
		if (!this.selectCount) {
			paramPosition = this.appendOrderByClosureToSql(sql, paramPosition);
		}
		return sql.toString();
	}

}
