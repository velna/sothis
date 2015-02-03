package org.sothis.core.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static Date getSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (week == 0) {
			week = 7;
		}
		calendar.add(Calendar.DATE, -week);
		return truncate(calendar.getTime(), Calendar.DATE);
	}
}
