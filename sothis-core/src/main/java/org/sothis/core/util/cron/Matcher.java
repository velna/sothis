package org.sothis.core.util.cron;

import java.util.Calendar;

/**
 * 字段内部的匹配器，匹配以逗号分隔的单个组
 * 
 * @author velna
 * 
 */
public interface Matcher {
	/**
	 * 判断这个组是否和某一时间的某个字段相匹配
	 * 
	 * @param calendar
	 * @param field
	 *            Calendar的字段
	 * @return
	 */
	boolean matches(Calendar calendar, int field);
}
