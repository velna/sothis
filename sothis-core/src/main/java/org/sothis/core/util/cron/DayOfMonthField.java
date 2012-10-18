package org.sothis.core.util.cron;

import java.util.Calendar;

public class DayOfMonthField extends AbstractField {

	public DayOfMonthField(String expression) {
		super(expression, Calendar.DAY_OF_MONTH, 1, 31);
	}

	@Override
	protected Matcher parseSingle(String expr) {
		if (expr.equals("LW") || expr.equals("WL")) {
			return Matchers.LAST_WORK_DAY_MATCHER;
		} else if (expr.indexOf('L') >= 0) {
			if (expr.length() == 1) {
				return Matchers.lastMatcher(1);
			} else {
				return Matchers.lastMatcher(Integer.parseInt(expr.substring(0, expr.length() - 1)));
			}
		} else if (expr.indexOf('W') >= 0) {
			return Matchers.nearestWorkDayMatcher(Integer.parseInt(expr.substring(0, expr.length() - 1)));
		} else {
			return super.parseSingle(expr);
		}
	}

}
