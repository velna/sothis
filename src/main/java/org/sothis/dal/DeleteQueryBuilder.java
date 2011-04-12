package org.sothis.dal;

import java.io.Serializable;

public class DeleteQueryBuilder extends AbstractQueryBuilder {

	public DeleteQueryBuilder(Class<? extends Serializable> clazz) {
		super(clazz);
	}

	@Override
	protected String buildSqlString() {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(this.getEntityClass().getName())
				.append(" o");
		int paramPosition = 0;
		paramPosition = this.appendWhereClosureToSql(sql, paramPosition);
		paramPosition = this.appendOrderByClosureToSql(sql, paramPosition);
		return sql.toString();
	}

}
