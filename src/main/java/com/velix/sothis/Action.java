package com.velix.sothis;

import java.lang.reflect.Method;

public class Action {
	private final Method method;
	private final Controller controller;

	public Action(Method method, Controller controller) {
		this.method = method;
		this.controller = controller;
	}

	public Object invoke(ActionContext context, Object... params)
			throws Exception {
		return this.method.invoke(controller.newInstance(), params);
	}

	public Controller getController() {
		return controller;
	}

	public Class<?>[] getParameterTypes() {
		return this.method.getParameterTypes();
	}
}
