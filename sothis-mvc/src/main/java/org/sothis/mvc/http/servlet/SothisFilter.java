package org.sothis.mvc.http.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SothisFilter implements Filter {

	private ServletApplication app;

	public void init(final FilterConfig filterConfig) throws ServletException {
		app = new ServletApplication(filterConfig);
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		try {
			if (!app.execute((HttpServletRequest) req, (HttpServletResponse) resp)) {
				chain.doFilter(req, resp);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void destroy() {

	}

}