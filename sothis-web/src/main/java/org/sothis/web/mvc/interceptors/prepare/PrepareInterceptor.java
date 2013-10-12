package org.sothis.web.mvc.interceptors.prepare;

import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ActionInvocationException;
import org.sothis.mvc.Interceptor;

public class PrepareInterceptor implements Interceptor {

	public Object intercept(ActionInvocation invocation) throws Exception {
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
