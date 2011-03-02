package com.velix.sothis;

import java.util.Iterator;
import java.util.List;

import com.velix.sothis.interceptor.Interceptor;

public class DefaultActionInvocation implements ActionInvocation {

	private final Iterator<Interceptor> interceptors;
	private final Action action;
	private final ActionContext context;

	@SuppressWarnings("unchecked")
	public DefaultActionInvocation(ActionContext context) {
		this.context = context;
		this.interceptors = ((List<Interceptor>) context
				.get(ActionContext.INTERCEPTORS)).iterator();
		this.action = (Action) context.get(ActionContext.ACTION);
	}

	@Override
	public Action getAction() {
		return action;
	}

	@Override
	public ActionContext getInvocationContext() {
		return context;
	}

	@Override
	public Object invoke() throws Exception {
		if (interceptors.hasNext()) {
			return interceptors.next().intercept(this);
		} else {
			return action.invoke(context,
					(Object[]) context.get(ActionContext.ACTION_PARAMS));
		}
	}

}
