package org.sothis.web.mvc.view;

import java.util.Map;

import org.sothis.web.mvc.ActionInvocation;


public interface View {
	void render(Map<String, ?> model, ActionInvocation invocation)
			throws Exception;
}
