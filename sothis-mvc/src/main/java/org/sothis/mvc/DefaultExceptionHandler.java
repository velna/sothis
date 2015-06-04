package org.sothis.mvc;

public class DefaultExceptionHandler implements ExceptionHandler {

	@Override
	public void exceptionCaught(ActionContext context, Exception e) throws Exception {
		throw e;
	}

}
