package org.sothis.dal;

interface SqlParam {
	public int appendToSql(StringBuilder sql, int paramPosition);
}
