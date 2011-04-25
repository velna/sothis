package org.sothis.web.mvc;

public interface ActionInvocation {
	Action getAction();

	ActionContext getActionContext();

	Object invoke() throws Exception;
}
