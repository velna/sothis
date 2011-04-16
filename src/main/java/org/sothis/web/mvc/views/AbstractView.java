package org.sothis.web.mvc.views;

import java.util.Collections;
import java.util.Map;

import org.sothis.web.mvc.View;

public abstract class AbstractView implements View {

	private Map<String, Object> params = Collections.emptyMap();

	@Override
	public void setParams(Map<String, Object> params) {
		if (null != params) {
			this.params = params;
		}
	}

	protected <T> T getParam(String paramName) {
		return getParam(paramName, null);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getParam(String paramName, T defaultValue) {
		Object ret = params.get(paramName);
		if (null == ret) {
			ret = defaultValue;
		}
		return (T) ret;
	}

	protected boolean containsParam(String paramName) {
		return params.containsKey(paramName);
	}
}
