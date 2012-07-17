package org.sothis.web.mvc;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * ActionInvocation接口的默认实现
 * 
 * @author velna
 * 
 */
public class DefaultActionInvocation implements ActionInvocation {

	private final Action action;
	private final ActionContext context;
	private final Object controllerInstance;
	private final Iterator<Class<Interceptor>> interceptors;

	public DefaultActionInvocation(final Action action, final Object controllerInstance, final ActionContext context) {
		this.action = action;
		this.controllerInstance = controllerInstance;
		this.context = context;
		interceptors = action.getInterceptorStack().iterator();
	}

	public Action getAction() {
		return action;
	}

	public ActionContext getActionContext() {
		return context;
	}

	public Object invoke() throws ActionInvocationException {
		Object result = null;
		try {
			if (interceptors.hasNext()) {
				Interceptor interceptor = context.getBeanFactory().getBean(interceptors.next());
				result = interceptor.intercept(this);
			} else {
				if (null != action.getActionMethod()) {
					result = action.getActionMethod().invoke(controllerInstance,
							(Object[]) context.get(ActionContext.ACTION_PARAMS));
				}
			}
		} catch (InvocationTargetException e) {
			throw new ActionInvocationException("error invoke action: " + action, e.getTargetException());
		} catch (Exception e) {
			throw new ActionInvocationException("error invoke action: " + action, e);
		}
		return result;
	}

	public Object getControllerInstance() {
		return controllerInstance;
	}

}
