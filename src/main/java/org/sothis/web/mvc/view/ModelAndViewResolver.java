package org.sothis.web.mvc.view;

import org.sothis.web.mvc.ActionInvocation;

public interface ModelAndViewResolver {

	void setDefaultView(Class<? extends View> viewClass);

	void register(String typeName, Class<? extends View> viewClass);

	ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation);
}
