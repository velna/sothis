package org.sothis.core.util.cron;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

public class DayOfWeekField extends AbstractField {

	private final static String[] DAY_OF_WEEK_ALIASES = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
	private final static String[] DAY_OF_WEEKS = { "1", "2", "3", "4", "5", "6", "7" };

	public DayOfWeekField(String expression) {
		super(replaceAlias(expression), Calendar.DAY_OF_WEEK, 1, 7);
	}

	private static String replaceAlias(String expression) {
		return StringUtils.replaceEach(expression, DAY_OF_WEEK_ALIASES, DAY_OF_WEEKS);
	}

	@Override
	protected Matcher parseSingle(String expr) {
		if (expr.indexOf('L') >= 0) {
			if (expr.length() == 1) {
				return Matchers.valueMatcher(Calendar.SATURDAY);
			} else {
				return Matchers.lastMatcher(Integer.parseInt(expr.substring(0, expr.length() - 1)));
			}
		} else if (expr.indexOf('#') >= 0) {
			String[] split = expr.split("#");
			if (split.length != 2) {
				throw new IllegalArgumentException("invalid day of week field:" + expression);
			}
			Matcher matcher = this.parseSingle(split[0]);
			return Matchers.weekMatcher(matcher, Integer.parseInt(split[1]));
		} else {
			return super.parseSingle(expr);
		}
	}
}
