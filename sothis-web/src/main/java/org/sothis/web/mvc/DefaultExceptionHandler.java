package org.sothis.web.mvc;

import java.io.IOException;

import javax.servlet.ServletException;

public class DefaultExceptionHandler implements ExceptionHandler {

	public void onExcetion(Throwable e) throws ServletException, IOException {
		if (e instanceof ServletException) {
			throw (ServletException) e;
		} else if (e instanceof IOException) {
			throw (IOException) e;
		}
		throw new ServletException(e);
	}
}
