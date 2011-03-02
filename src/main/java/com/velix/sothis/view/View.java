package com.velix.sothis.view;

import java.util.Map;

import com.velix.sothis.ActionInvocation;

public interface View {
	void render(Map<String, ?> model, ActionInvocation invocation)
			throws Exception;
}
