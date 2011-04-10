package org.sothis.web.mvc.interceptor;

import org.sothis.web.mvc.ActionInvocation;


public interface Interceptor {
	Object intercept(ActionInvocation invocation) throws Exception;
}
