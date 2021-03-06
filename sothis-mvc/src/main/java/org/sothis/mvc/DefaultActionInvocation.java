package org.sothis.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * ActionInvocation接口的默认实现
 * 
 * @author velna
 * 
 */
public class DefaultActionInvocation implements ActionInvocation {

	private final ActionContext context;
	private final Object controllerInstance;
	private final Iterator<Class<Interceptor>> interceptors;

	private Object result;
	private boolean actionInvoked;

	public DefaultActionInvocation(final Object controllerInstance, final ActionContext context) {
		this.controllerInstance = controllerInstance;
		this.context = context;
		interceptors = context.getAction().getInterceptors().iterator();
	}

	public Action getAction() {
		return context.getAction();
	}

	public ActionContext getActionContext() {
		return context;
	}

	public Object invoke() throws Exception {
		try {
			if (interceptors.hasNext()) {
				Interceptor interceptor = context.getApplicationContext().getBeanFactory().getBean(interceptors.next());
				result = interceptor.intercept(this);
			} else if (!actionInvoked) {
				actionInvoked = true;
				Method method = context.getAction().getActionMethod();
				if (null != method) {
					result = method.invoke(controllerInstance, context.getActionParams());
				}
			}
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Exception) {
				throw (Exception) e.getCause();
			} else {
				throw e;
			}
		}
		return result;
	}

	public Object getControllerInstance() {
		return controllerInstance;
	}

	public boolean isActionInvoked() {
		return actionInvoked;
	}

}
