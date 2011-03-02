package com.velix.sothis.interceptor;

import org.apache.commons.beanutils.BeanUtils;

import com.velix.sothis.ActionContext;
import com.velix.sothis.ActionInvocation;
import com.velix.sothis.HttpServletRequestAware;
import com.velix.sothis.HttpServletResponseAware;

public class ParametersInterceptor implements Interceptor {

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		Class<?>[] paramTypes = invocation.getAction().getParameterTypes();
		if (paramTypes.length == 1) {
			ActionContext context = invocation.getInvocationContext();
			Object param = paramTypes[0].newInstance();
			BeanUtils.populate(param, context.getRequest().getParameterMap());
			if (param instanceof HttpServletRequestAware) {
				((HttpServletRequestAware) param).setRequest(context
						.getRequest());
			}
			if (param instanceof HttpServletResponseAware) {
				((HttpServletResponseAware) param).setResponse(context
						.getResponse());
			}
			context.put(ActionContext.ACTION_PARAMS, new Object[] { param });
		} else {
			// TODO:
		}
		return invocation.invoke();
	}

}
