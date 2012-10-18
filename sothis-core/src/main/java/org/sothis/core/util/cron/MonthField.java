package org.sothis.core.util.cron;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

public class MonthField extends AbstractField {

	private final static String[] MONTHS_ALIASES = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	private final static String[] MONTHS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" };

	public MonthField(String expression) {
		super(replaceAlias(expression), Calendar.MONTH, 0, 11);
	}

	private static String replaceAlias(String expression) {
		return StringUtils.replaceEach(expression, MONTHS_ALIASES, MONTHS);
	}
}
