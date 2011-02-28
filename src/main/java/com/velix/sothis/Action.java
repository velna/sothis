package com.velix.sothis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;

public class Action {
	private final Method method;
	private final Class<?> paramClass;

	public Action(Method method) {
		this.method = method;
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length == 0) {
			paramClass = null;
		} else if (paramTypes.length == 1) {
			paramClass = paramTypes[0];
		} else {
			throw new RuntimeException("invalid params type length: "
					+ paramTypes.length);
		}
	}

	public Object invoke(Object controllerInstance, ActionContext context)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		if (paramClass != null) {
			Object param = paramClass.newInstance();
			BeanUtils.populate(param, context.getRequest().getParameterMap());
			if (param instanceof HttpServletRequestAware) {
				((HttpServletRequestAware) param).setRequest(context
						.getRequest());
			}
			if (param instanceof HttpServletResponseAware) {
				((HttpServletResponseAware) param).setResponse(context
						.getResponse());
			}
			return this.method.invoke(controllerInstance,
					new Object[] { param });
		} else {
			return this.method.invoke(controllerInstance, (Object[]) null);
		}
	}
}
