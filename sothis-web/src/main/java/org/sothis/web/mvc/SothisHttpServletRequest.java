package org.sothis.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class SothisHttpServletRequest extends HttpServletRequestWrapper {
	private final String contextPath;

	public SothisHttpServletRequest(HttpServletRequest request) {
		super(request);
		String suffix = SothisConfig.getConfig().getContextSuffix();
		if (suffix != null) {
			contextPath = request.getContextPath() + suffix;
		} else {
			contextPath = request.getContextPath();
		}
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

}
