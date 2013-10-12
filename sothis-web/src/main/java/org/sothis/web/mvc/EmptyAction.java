package org.sothis.web.mvc;

import java.lang.reflect.Method;

/**
 * ¿ÕµÄAction
 * 
 * @author velna
 * 
 */
class EmptyAction extends AbstractActionBase implements Action {
	private final Controller controller;
	private final String name;
	private final String fullName;

	public EmptyAction(final String name, final Controller controller) {
		super(controller.getControllerClass(), controller.getControllerClass().getPackage());
		if (null == name) {
			throw new IllegalArgumentException("method is null");
		}

		this.controller = controller;
		this.name = name;
		this.fullName = new StringBuilder().append(this.controller.getFullName()).append(this.name).toString();
	}

	public Controller getController() {
		return controller;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	public Method getActionMethod() {
		return null;
	}

	@Override
	public String toString() {
		return getFullName();
	}

}
