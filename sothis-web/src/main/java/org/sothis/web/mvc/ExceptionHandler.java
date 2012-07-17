package org.sothis.web.mvc;

import java.io.IOException;

import javax.servlet.ServletException;

public interface ExceptionHandler {
	void onExcetion(Throwable e) throws ServletException, IOException;
}
