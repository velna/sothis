package org.sothis.web.mvc;

import java.lang.reflect.Method;

/**
 * Action接口的默认实现
 * 
 * @author velna
 * 
 */
public class DefaultAction extends AbstractActionBase implements Action {
	private final Method method;
	private final Controller controller;
	private final String name;
	private final String fullName;

	public DefaultAction(final Method method, final Controller controller) {
		super(controller, method);
		if (null == method) {
			throw new IllegalArgumentException("method is null");
		}
		if (null == controller) {
			throw new IllegalArgumentException("controller is null");
		}
		this.method = method;
		this.controller = controller;

		if (method.getName().endsWith("Action")) {
			this.name = method.getName().substring(0, method.getName().length() - 6);
		} else {
			throw new IllegalArgumentException("method's must last with 'Action' ");
		}
		this.fullName = new StringBuilder().append(this.controller.getFullName()).append(this.name).toString();
	}

	public Controller getController() {
		return controller;
	}

	public String getName() {
		return this.name;
	}

	public Method getActionMethod() {
		return method;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public String toString() {
		return getFullName();
	}

}
