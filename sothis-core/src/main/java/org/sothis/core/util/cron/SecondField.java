package org.sothis.core.util.cron;

import java.util.Calendar;

public class SecondField extends AbstractField {

	public SecondField(String expression) {
		super(expression, Calendar.SECOND, 0, 59);
	}

}
