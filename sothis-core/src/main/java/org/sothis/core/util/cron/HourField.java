package org.sothis.core.util.cron;

import java.util.Calendar;

public class HourField extends AbstractField {

	public HourField(String expression) {
		super(expression, Calendar.HOUR, 0, 23);
	}

}
