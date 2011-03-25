package org.sothis.web.mvc;

import java.lang.annotation.Annotation;

class EmptyAction implements Action {
	private final Controller controller;
	private final String name;

	public EmptyAction(String name, Controller controller) {
		this.controller = controller;
		this.name = name;
	}

	@Override
	public Object invoke(ActionContext context, Object... params)
			throws Exception {
		return (Void) null;
	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return new Class<?>[0];
	}

	@Override
	public Annotation[][] getParameterAnnotations() {
		return new Annotation[0][0];
	}

	@Override
	public Annotation[] getAnnotations() {
		return new Annotation[0];
	}

	@Override
	public String getName() {
		return name;
	}

}
