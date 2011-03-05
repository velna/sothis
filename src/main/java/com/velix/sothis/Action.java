package com.velix.sothis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Action {
	private final Method method;
	private final Controller controller;
	private final String name;

	public Action(Method method, Controller controller) {
		this.method = method;
		this.controller = controller;
		this.name = method.getName()
				.substring(0, method.getName().length() - 6);
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

	public Annotation[][] getParameterAnnotations() {
		return this.method.getParameterAnnotations();
	}

	public Annotation[] getAnnotations() {
		return this.method.getAnnotations();
	}

	public String getName() {
		return this.name;
	}
}
