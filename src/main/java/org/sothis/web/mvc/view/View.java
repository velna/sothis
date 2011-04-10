package org.sothis.web.mvc.view;

import org.sothis.web.mvc.ActionInvocation;

public interface View {
	void render(Object model, ActionInvocation invocation) throws Exception;

	void setParams(Object... params) throws IllegalArgumentException;
}
