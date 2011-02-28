package com.velix.sothis;

public interface ActionInvocation {
	Object getController();

	ActionContext getInvocationContext();

	Object invoke();
}
