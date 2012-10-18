package org.sothis.core.util.cron;

import java.util.Calendar;

public class YearField extends AbstractField {

	public YearField(String expression) {
		super(expression, Calendar.YEAR, 1970, 2099);
	}

}
