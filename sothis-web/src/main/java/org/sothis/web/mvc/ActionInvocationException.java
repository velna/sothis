package org.sothis.web.mvc;

import javax.servlet.ServletException;

public class ActionInvocationException extends ServletException {

	private static final long serialVersionUID = 4349477086066958198L;

	public ActionInvocationException() {
		super();
	}

	public ActionInvocationException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public ActionInvocationException(String message) {
		super(message);
	}

	public ActionInvocationException(Throwable rootCause) {
		super(rootCause);
	}

}
