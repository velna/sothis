package org.sothis.core.beans;

public class BeanInstantiationException extends RuntimeException {

	private static final long serialVersionUID = 2583283563268973241L;

	public BeanInstantiationException() {
		super();
	}

	public BeanInstantiationException(final String message, Throwable cause) {
		super(message, cause);
	}

	public BeanInstantiationException(final String message) {
		super(message);
	}

	public BeanInstantiationException(final Throwable cause) {
		super(cause);
	}

}
