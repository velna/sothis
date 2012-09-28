package org.sothis.web.mvc.views.rest;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.ModelAndView;
import org.sothis.web.mvc.ModelAndViewResolver;
import org.sothis.web.mvc.View;
import org.sothis.web.mvc.ViewCreationException;

@Bean(scope = Scope.SINGLETON)
public class RestView implements View {
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ServletException {
		Map<String, Object> viewParams = mav.viewParams();
		ActionContext context = invocation.getActionContext();
		ModelAndViewResolver mavResolver = (ModelAndViewResolver) context.get(ActionContext.MODEL_AND_VIEW_RESOLVER);
		int code = 200;
		if (null != viewParams && null != viewParams.get("code")) {
			code = (Integer) viewParams.get("code");
		}
		String format = getRestForamt(context);
		context.getResponse().setStatus(code);
		try {
			mavResolver.getView(format, invocation).render(mav, invocation);
		} catch (ViewCreationException e) {
			throw new ServletException(e);
		}
	}

	private String getRestForamt(ActionContext context) {
		String uri = context.getRequest().getRequestURI();
		String format = "json";
		int index = uri.lastIndexOf('.');
		if (index > 0 && index < uri.length() - 1) {
			format = uri.substring(index + 1);
			if (!"json".equalsIgnoreCase(format) && !"xml".equalsIgnoreCase(format)) {
				format = "json";
			}
		}
		return format;
	}

}
