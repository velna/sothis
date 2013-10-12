package org.sothis.web.mvc.views.rest;

import java.io.IOException;
import java.util.Map;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.ModelAndViewResolver;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewCreationException;
import org.sothis.mvc.ViewRenderException;
import org.sothis.web.mvc.WebActionContext;

@Bean(scope = Scope.SINGLETON)
public class RestView implements View {
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Map<String, Object> viewParams = mav.viewParams();
		WebActionContext context = (WebActionContext) invocation.getActionContext();
		ModelAndViewResolver mavResolver = (ModelAndViewResolver) context.get(WebActionContext.MODEL_AND_VIEW_RESOLVER);
		int code = 200;
		if (null != viewParams && null != viewParams.get("code")) {
			code = (Integer) viewParams.get("code");
		}
		String format = getRestForamt(context);
		context.getResponse().setStatus(code);
		try {
			mavResolver.getView(format, invocation).render(mav, invocation);
		} catch (ViewCreationException e) {
			throw new ViewRenderException(e);
		}
	}

	private String getRestForamt(WebActionContext context) {
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
