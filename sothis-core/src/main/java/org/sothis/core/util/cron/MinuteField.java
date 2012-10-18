package org.sothis.core.util.cron;

import java.util.Calendar;

public class MinuteField extends AbstractField {

	public MinuteField(String expression) {
		super(expression, Calendar.MINUTE, 0, 59);
	}

}
