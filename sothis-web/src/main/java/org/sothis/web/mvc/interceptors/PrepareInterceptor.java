package org.sothis.web.mvc.interceptors;

import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.ActionInvocationException;
import org.sothis.web.mvc.Interceptor;

public class PrepareInterceptor implements Interceptor {

	public Object intercept(ActionInvocation invocation) throws ActionInvocationException {
		Object controller = invocation.getControllerInstance();
		if (controller instanceof Preparable) {
			try {
				((Preparable) controller).prepare();
			} catch (Exception e) {
				throw new ActionInvocationException("error invoke preparable method: ", e);
			}
		}
		return invocation.invoke();
	}

}
