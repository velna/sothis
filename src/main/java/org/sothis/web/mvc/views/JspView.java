package org.sothis.web.mvc.views;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.sothis.web.mvc.ActionInvocation;

public class JspView extends AbstractView {

	@Override
	public void render(Object model, ActionInvocation invocation)
			throws Exception {
		HttpServletRequest request = invocation.getInvocationContext()
				.getRequest();
		if (null != model) {
			Map<?, ?> attrMap;
			if (model instanceof Map) {
				attrMap = (Map<?, ?>) model;
			} else {
				attrMap = PropertyUtils.describe(model);
			}
			for (Object key : attrMap.keySet()) {
				request.setAttribute(String.valueOf(key), attrMap.get(key));
			}
		}
		request.getRequestDispatcher(getPath(invocation)).forward(request,
				invocation.getInvocationContext().getResponse());
	}

	private String getPath(ActionInvocation invocation) {
		StringBuilder ret = new StringBuilder();
		if (containsParam("path")) {
			String prefix = String.valueOf(getParam("path"));
			if (prefix.charAt(0) == '/') {
				ret.append(prefix).append(".jsp");
			} else {
				ret.append(invocation.getAction().getController().getName())
						.append('/').append(prefix).append(".jsp");
			}
		} else {
			ret.append(invocation.getAction().getController().getName())
					.append('/').append(invocation.getAction().getName())
					.append(".jsp");
		}

		return ret.toString();
	}

}
