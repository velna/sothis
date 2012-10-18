package org.sothis.core.util.cron;

import java.util.Calendar;

public interface Field {

	/**
	 * 判断本字段和一个日期是否匹配
	 * 
	 * @param calendar
	 * @return true为匹配，false为不匹配
	 */
	boolean matches(Calendar calendar);

	/**
	 * 本字段是否未设置
	 * 
	 * @return
	 */
	boolean isBlank();

	/**
	 * 本字段的表达式
	 * 
	 * @return
	 */
	String getExpression();
}
