package org.sothis.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExceptionHandler implements ExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	@Override
	public void exceptionCaught(ActionContext context, Exception e) throws Exception {
		LOG.error("error process request: " + context.getRequest().getUriPath(), e);
	}

}
