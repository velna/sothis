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
	 * 将calendar的当前字段设置为下一个匹配的时间
	 * 
	 * @param calendar
	 * @return 返回更新的量
	 */
	int next(Calendar calendar);

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

	/**
	 * 本字段最小值
	 * 
	 * @return
	 */
	int getMin();

	/**
	 * 本字段最大值
	 * 
	 * @return
	 */
	int getMax();

	/**
	 * 得到Calendar的field值
	 * 
	 * @return
	 */
	int getField();
}
