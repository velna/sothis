package org.sothis.core.util;

import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ThreadSafeDateFormat extends DateFormat {
	private static final long serialVersionUID = 3786090697869963812L;

	private final String dateFormat;
	private final Locale locale;
	private final TimeZone timeZone;

	public ThreadSafeDateFormat(String dateFormat) {
		this(dateFormat, Locale.getDefault(), TimeZone.getDefault());
	}

	public ThreadSafeDateFormat(String dateFormat, Locale locale) {
		this(dateFormat, locale, TimeZone.getDefault());
	}

	public ThreadSafeDateFormat(String dateFormat, Locale locale, TimeZone timeZone) {
		this.dateFormat = dateFormat;
		this.locale = locale;
		this.timeZone = timeZone;
	}

	private final ThreadLocal<SoftReference<SimpleDateFormat>> cache = new ThreadLocal<SoftReference<SimpleDateFormat>>() {
		public SoftReference<SimpleDateFormat> get() {
			SoftReference<SimpleDateFormat> softRef = (SoftReference<SimpleDateFormat>) super.get();
			if (softRef == null || softRef.get() == null) {
				SimpleDateFormat fromat = new SimpleDateFormat(dateFormat, locale);
				fromat.setTimeZone(timeZone);
				softRef = new SoftReference<SimpleDateFormat>(fromat);
				super.set(softRef);
			}
			return softRef;
		}
	};

	private DateFormat getDateFormat() {
		return cache.get().get();
	}

	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		return getDateFormat().format(date, toAppendTo, fieldPosition);
	}

	public Date parse(String source, ParsePosition pos) {
		return getDateFormat().parse(source, pos);
	}
}