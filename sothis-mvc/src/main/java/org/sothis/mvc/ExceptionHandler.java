package org.sothis.mvc;

public interface ExceptionHandler {
	void exceptionCaught(ActionContext context, Exception e) throws Exception;
}
