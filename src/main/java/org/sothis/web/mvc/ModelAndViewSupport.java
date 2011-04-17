package org.sothis.web.mvc;

import java.util.HashMap;
import java.util.Map;

public class ModelAndViewSupport implements ModelAndView {

	private String viewType = ModelAndViewResolver.DEFAULT_VIEW_KEY;
	private Map<String, Object> viewParams = new HashMap<String, Object>();

	@Override
	public Object model() {
		return this;
	}

	@Override
	public String viewType() {
		return viewType;
	}

	@Override
	public Map<String, Object> viewParams() {
		return viewParams;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public void setViewParams(Map<String, Object> viewParams) {
		this.viewParams = viewParams;
	}

	public void setViewParam(String paramName, Object paramValue) {
		viewParams.put(paramName, paramValue);
	}

	public Object getViewParam(String paramName) {
		return viewParams.get(paramName);
	}
}
