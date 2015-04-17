package org.sothis.mvc.echoapp;

import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.Interceptor;

public class EchoAppInterceptor implements Interceptor {

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		Object result = invocation.invoke();
		return result.toString() + "\n";
	}

}
