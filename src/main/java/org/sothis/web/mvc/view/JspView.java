package org.sothis.web.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sothis.web.mvc.ActionInvocation;


public class JspView implements View {

	private final String jspPathName;

	public JspView(String jspPathName) {
		this.jspPathName = jspPathName + ".jsp";
	}

	@Override
	public void render(Map<String, ?> model, ActionInvocation invocation)
			throws Exception {
		HttpServletRequest request = invocation.getInvocationContext()
				.getRequest();
		if (null != model) {
			for (String key : model.keySet()) {
				request.setAttribute(key, model.get(key));
			}
		}
		String path = invocation.getAction().getController().getName() + "/"
				+ jspPathName;
		request.getRequestDispatcher(path).forward(request,
				invocation.getInvocationContext().getResponse());
	}

}
