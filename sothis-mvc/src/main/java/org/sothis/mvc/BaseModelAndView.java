package org.sothis.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseModelAndView implements ModelAndView {

	private String viewType = ModelAndViewResolver.DEFAULT_VIEW_TYPE;

	private Map<String, Object> viewParams;

	@Override
	public Object model() {
		return this;
	}

	@Override
	public String viewType() {
		return viewType;
	}

	public final void viewType(final String viewType) {
		this.viewType = viewType;
	}

	@Override
	public Map<String, Object> viewParams() {
		if (null == viewParams) {
			return Collections.emptyMap();
		} else {
			return viewParams;
		}
	}

	public final void viewParams(final Map<String, Object> viewParams) {
		this.viewParams = viewParams;
	}

	public final void viewParam(final String paramName, Object paramValue) {
		if (null == viewParams) {
			viewParams = new HashMap<String, Object>();
		}
		viewParams.put(paramName, paramValue);
	}

	public final Object viewParam(final String paramName) {
		if (null == viewParams) {
			return null;
		}
		return viewParams.get(paramName);
	}

}
