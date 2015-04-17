package org.sothis.mvc;

public interface ActionContextFactory {
	ActionContext createActionContext();

	AsyncContext createAsyncContext(ActionContext actionContext);
}
