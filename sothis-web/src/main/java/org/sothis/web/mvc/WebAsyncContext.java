package org.sothis.web.mvc;

import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.AsyncContext;

public class WebAsyncContext implements AsyncContext {
	private final WebActionContext actionContext;
	private final javax.servlet.AsyncContext asyncContext;
	private boolean completed;

	public WebAsyncContext(WebActionContext actionContext) {
		this.actionContext = actionContext;
		asyncContext = this.actionContext.getRequest().startAsync(this.actionContext.getRequest(),
				this.actionContext.getResponse());
	}

	@Override
	public WebActionContext getActionContext() {
		return actionContext;
	}

	@Override
	public void complete(Object result) throws Exception {
		if (completed) {
			return;
		}
		WebActionContext old = WebActionContext.setContext(actionContext);
		completed = true;
		ActionInvocation invocation = (ActionInvocation) this.actionContext.get(ActionContext.ACTION_INVOCATION);
		ActionInvocationHelper.render(invocation, result);
		asyncContext.complete();
		WebActionContext.setContext(old);
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}

}
