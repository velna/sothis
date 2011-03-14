package org.sothis.web.mvc;

public interface ActionInvocation {
	Action getAction();

	ActionContext getInvocationContext();

	Object invoke() throws Exception;
}
