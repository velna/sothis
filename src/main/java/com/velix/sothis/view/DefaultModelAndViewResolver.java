package com.velix.sothis.view;

import com.velix.sothis.ActionInvocation;

public class DefaultModelAndViewResolver implements ModelAndViewResolver {

	@Override
	public ResolvedModelAndView resolve(Object actionResult,
			ActionInvocation invocation) {
		return new ResolvedModelAndView(null, new JspView(
				null == actionResult ? invocation.getAction().getName()
						: (String) actionResult));
	}

}
