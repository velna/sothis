package org.sothis.dal.support;

import org.springframework.jdbc.core.JdbcTemplate;

public class MySqlJdbcTemplate extends JdbcTemplate {
	private int firstRow;

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

}
