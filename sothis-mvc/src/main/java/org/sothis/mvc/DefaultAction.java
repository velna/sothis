package org.sothis.mvc;

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

	public DefaultAction(Configuration config, final Method method, final Controller controller) {
		super(config, method, controller.getControllerClass(), controller.getControllerClass().getPackage());
		this.method = method;
		this.controller = controller;

		if (method.getName().endsWith(ACTION_SUFFIX)) {
			this.name = method.getName().substring(0, method.getName().length() - ACTION_SUFFIX.length());
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
