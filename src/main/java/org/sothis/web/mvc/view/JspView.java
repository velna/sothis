package org.sothis.web.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.ActionInvocation;

public class JspView implements View {

	private String jspPathName;

	@Override
	public void render(Object model, ActionInvocation invocation)
			throws Exception {
		HttpServletRequest request = invocation.getInvocationContext()
				.getRequest();
		Map<String, ?> attrMap = (Map<String, ?>) model;
		if (null != model) {
			for (String key : attrMap.keySet()) {
				request.setAttribute(key, attrMap.get(key));
			}
		}
		String path = invocation.getAction().getController().getName() + "/"
				+ jspPathName;
		request.getRequestDispatcher(path).forward(request,
				invocation.getInvocationContext().getResponse());
	}

	@Override
	public void setParams(Object... params) throws IllegalArgumentException {
		String path = params[0].toString();

		if (StringUtils.isEmpty(path)) {
			throw new IllegalArgumentException("invalid jsp path name: \""
					+ path + "\"");
		}
		this.jspPathName = path + ".jsp";
	}

}
