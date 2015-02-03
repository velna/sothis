package org.sothis.mvc;

public interface AsyncContext {
	ActionContext getActionContext();

	void complete(Object result) throws Exception;

	boolean isCompleted();
}
