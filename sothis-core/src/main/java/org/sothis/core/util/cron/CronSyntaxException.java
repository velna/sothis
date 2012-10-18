package org.sothis.core.util.cron;

public class CronSyntaxException extends RuntimeException {

	public CronSyntaxException() {
		super();
	}

	public CronSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public CronSyntaxException(String message) {
		super(message);
	}

	public CronSyntaxException(Throwable cause) {
		super(cause);
	}

}
