package org.sothis.web.mvc;


public interface ModelAndViewResolver {

	void setDefaultView(Class<? extends View> viewClass);

	void register(String typeName, Class<? extends View> viewClass);

	ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation);
}
