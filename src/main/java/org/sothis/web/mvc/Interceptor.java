package org.sothis.web.mvc;

public interface Interceptor {
	Object intercept(ActionInvocation invocation) throws Exception;
}
