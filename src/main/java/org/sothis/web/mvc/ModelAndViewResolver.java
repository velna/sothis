package org.sothis.web.mvc;

public interface ModelAndViewResolver {

	public static final String DEFAULT_VIEW_KEY = "org.sothis.web.mvc.view.DEFAULT_VIEW_KEY";

	void setDefaultView(Class<? extends View> viewClass);

	void register(String typeName, Class<? extends View> viewClass);

	ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation);
}
