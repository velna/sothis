package org.sothis.web.mvc.view;

import org.sothis.web.mvc.ActionInvocation;

public interface Result {
	void execute(ActionInvocation invocation);
}
