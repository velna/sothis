package org.sothis.web.mvc;

import org.sothis.mvc.Action;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ActionInvocationException;

public class MockActionInvocation implements ActionInvocation {

	private Action action;
	private ActionContext actionContext;
	private Object controllerInstance;

	public MockActionInvocation(ActionContext actionContext) {
		this.actionContext = actionContext;
	}

	public Action getAction() {
		return action;
	}

	public ActionContext getActionContext() {
		return actionContext;
	}

	public Object invoke() throws ActionInvocationException {
		if (null != action.getActionMethod()) {
			try {
				return action.getActionMethod().invoke(controllerInstance,
						(Object[]) actionContext.get(ActionContext.ACTION_PARAMS));
			} catch (Exception e) {
				throw new ActionInvocationException(e);
			}
		}
		return null;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public void setActionContext(ActionContext actionContext) {
		this.actionContext = actionContext;
	}

	public Object getControllerInstance() {
		return controllerInstance;
	}

	public void setControllerInstance(Object controllerInstance) {
		this.controllerInstance = controllerInstance;
	}

	@Override
	public boolean isActionInvoked() {
		return false;
	}

}
