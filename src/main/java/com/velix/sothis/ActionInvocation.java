package com.velix.sothis;

public interface ActionInvocation {
	Action getAction();

	ActionContext getInvocationContext();

	Object invoke() throws Exception;
}
