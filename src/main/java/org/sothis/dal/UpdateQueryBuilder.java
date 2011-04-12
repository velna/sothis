package org.sothis.dal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdateQueryBuilder extends AbstractQueryBuilder {

	private List<SetParam> setParamList;

	public UpdateQueryBuilder(Class<? extends Entity> clazz) {
		super(clazz);
		this.setParamList = new ArrayList<SetParam>();
	}

	public UpdateQueryBuilder set(String field, Object value) {
		this.setParamList.add(new SetParam(field, value));
		return this;
	}

	@Override
	protected String buildSqlString() {
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(this.getEntityClass().getName())
				.append(" o set ");
		int paramPosition = 0;
		for (Iterator<SetParam> i = this.setParamList.iterator(); i.hasNext();) {
			sql.append(" ");
			paramPosition = i.next().appendToSql(sql, paramPosition);
			if (i.hasNext()) {
				sql.append(",");
			}
		}
		paramPosition = this.appendWhereClosureToSql(sql, paramPosition);
		paramPosition = this.appendOrderByClosureToSql(sql, paramPosition);
		return sql.toString();
	}

}
