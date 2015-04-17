package org.sothis.mvc.http.servlet;

import java.util.Enumeration;

import javax.servlet.ServletRequest;

public class ServletRequestAttributes extends ServletAttributes {
	private final ServletRequest request;

	public ServletRequestAttributes(ServletRequest request) {
		super();
		this.request = request;
	}

	@Override
	public Object get(String name) {
		return request.getAttribute(name);
	}

	@Override
	public void set(String name, Object value) {
		request.setAttribute(name, value);
	}

	@Override
	public void remove(String name) {
		request.removeAttribute(name);
	}

	@Override
	protected Enumeration<String> getAttributeNames() {
		return request.getAttributeNames();
	}

}
