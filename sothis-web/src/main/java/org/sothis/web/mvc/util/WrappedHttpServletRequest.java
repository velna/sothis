package org.sothis.web.mvc.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class WrappedHttpServletRequest extends HttpServletRequestWrapper {
	private Map<String, String[]> parameterMap;

	public WrappedHttpServletRequest(HttpServletRequest request, Map<String, String[]> parameterMap) {
		super(request);
		this.parameterMap = parameterMap;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

}
