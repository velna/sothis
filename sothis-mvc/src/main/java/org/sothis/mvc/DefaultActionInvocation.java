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

	public Object invoke() throws ActionInvocationException {
		Object result = null;
		try {
			if (interceptors.hasNext()) {
				Interceptor interceptor = context.getApplicationContext().getBeanFactory().getBean(interceptors.next());
				result = interceptor.intercept(this);
			} else {
				Method method = context.getAction().getActionMethod();
				if (null != method) {
					result = method.invoke(controllerInstance, (Object[]) context.get(ActionContext.ACTION_PARAMS));
				}
			}
		} catch (InvocationTargetException e) {
			throw new ActionInvocationException("error invoke action: " + context.getAction(), e.getTargetException());
		} catch (Exception e) {
			throw new ActionInvocationException("error invoke action: " + context.getAction(), e);
		}
		return result;
	}

	public Object getControllerInstance() {
		return controllerInstance;
	}

}
