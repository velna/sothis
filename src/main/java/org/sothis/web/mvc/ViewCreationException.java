package org.sothis.web.mvc;

public class ViewCreationException extends RuntimeException {

	private static final long serialVersionUID = 7846046903061809981L;

	public ViewCreationException() {
		super();
	}

	public ViewCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ViewCreationException(String message) {
		super(message);
	}

	public ViewCreationException(Throwable cause) {
		super(cause);
	}

}
