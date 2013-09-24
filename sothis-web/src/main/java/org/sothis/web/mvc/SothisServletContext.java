package org.sothis.web.mvc;

import javax.servlet.ServletContext;

import org.sothis.web.mvc.util.ServletContextWrapper;

public class SothisServletContext extends ServletContextWrapper {
	private final String contextPath;

	public SothisServletContext(ServletContext servletContext) {
		super(servletContext);
		String suffix = SothisConfig.getConfig().getContextSuffix();
		if (suffix != null) {
			contextPath = servletContext.getContextPath() + suffix;
		} else {
			contextPath = servletContext.getContextPath();
		}
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

}
