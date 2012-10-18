package org.sothis.core.util.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class AbstractField implements Field {
	protected final String expression;
	protected final List<Matcher> matchers;
	protected final boolean blank;
	protected final int field;
	private final int min;
	private final int max;

	public AbstractField(String expression, int field, int min, int max) {
		this.expression = expression;
		this.field = field;
		this.min = min;
		this.max = max;
		blank = expression.equals("?");
		if (blank && field != Calendar.DAY_OF_MONTH && field != Calendar.DAY_OF_WEEK) {
			throw new IllegalArgumentException("blank(?) is not allowed on this field");
		}
		matchers = blank ? null : this.parse();
	}

	@Override
	public boolean matches(Calendar calendar) {
		for (Matcher matcher : matchers) {
			if (matcher.matches(calendar, field)) {
				return true;
			}
		}
		return false;
	}

	protected List<Matcher> parse() {
		String[] exprs = expression.split(",");
		List<Matcher> matchers = new ArrayList<Matcher>(exprs.length);
		for (String expr : exprs) {
			matchers.add(parseSingle(expr));
		}
		return matchers;
	}

	protected Matcher parseSingle(String expr) {
		Matcher matcher;
		if (expr.equals("*")) {
			matcher = Matchers.MATCH_ALL_MATHCER;
		} else if (expr.indexOf('/') >= 0) {
			String[] repeatPair = expr.split("/");
			if (repeatPair.length != 2) {
				throw new IllegalArgumentException("invalid cron expression: " + expression);
			}
			int repeat = Integer.parseInt(repeatPair[1]);
			if (repeatPair[0].indexOf('-') >= 0) {
				Range range = this.createRange(repeatPair[0]);
				matcher = Matchers.rangeRepeatMatcher(range, repeat);
			} else {
				int offset = repeatPair[0].equals("*") ? 0 : Integer.parseInt(repeatPair[0]);
				matcher = Matchers.offsetRepeatMatcher(offset, repeat);
			}
		} else if (expr.indexOf('-') >= 0) {
			matcher = this.createRange(expr);
		} else {
			matcher = Matchers.valueMatcher(Integer.parseInt(expr));
		}
		return matcher;
	}

	protected Range createRange(String rangeExpr) {
		String[] split = rangeExpr.split("-");
		if (split.length != 2) {
			throw new IllegalArgumentException("invalid field expression: " + expression);
		}
		int start = split[0].length() == 0 ? this.min : Integer.parseInt(split[0]);
		int end = split[1].length() == 0 ? this.max : Integer.parseInt(split[1]);
		return new Range(start, end, this.min, this.max);
	}

	public boolean isBlank() {
		return blank;
	}

	public String getExpression() {
		return expression;
	}

}
