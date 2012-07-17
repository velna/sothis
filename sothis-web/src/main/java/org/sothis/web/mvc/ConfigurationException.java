package org.sothis.web.mvc;

public class ConfigurationException extends Exception {

	private static final long serialVersionUID = 4793255495109136609L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(final String message) {
		super(message);
	}

	public ConfigurationException(final Throwable cause) {
		super(cause);
	}

	public ConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
