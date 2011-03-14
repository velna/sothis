package org.sothis.web.mvc.view;

import org.sothis.web.mvc.ActionInvocation;

public interface ModelAndViewResolver {
	ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation);
}
