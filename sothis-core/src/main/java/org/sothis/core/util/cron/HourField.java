package org.sothis.core.util.cron;

import java.util.Calendar;

public class HourField extends AbstractField {

	public HourField(String expression) {
		super(expression, Calendar.HOUR_OF_DAY, 0, 23);
	}

}
