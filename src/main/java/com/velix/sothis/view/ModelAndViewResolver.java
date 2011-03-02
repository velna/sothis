package com.velix.sothis.view;

import com.velix.sothis.ActionInvocation;

public interface ModelAndViewResolver {
	ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation);
}
