package org.sothis.web.mvc;

import java.lang.annotation.Annotation;

public class EmptyController implements Controller {

	private final String name;

	public EmptyController(String name) {
		this.name = name;
	}

	@Override
	public Action getAction(String actionName) {
		return new EmptyAction(actionName, this);
	}

	@Override
	public Object newInstance() throws Exception {
		return this;
	}

	@Override
	public Annotation[] getAnnotations() {
		return new Annotation[0];
	}

	@Override
	public Class<?> getControllerClass() {
		return this.getClass();
	}

	@Override
	public String getName() {
		return name;
	}

}
