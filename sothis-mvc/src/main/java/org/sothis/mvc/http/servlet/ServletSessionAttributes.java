package org.sothis.mvc.http.servlet;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

public class ServletSessionAttributes extends ServletAttributes {
	private final HttpSession session;

	public ServletSessionAttributes(HttpSession session) {
		super();
		this.session = session;
	}

	@Override
	public Object get(String name) {
		return session.getAttribute(name);
	}

	@Override
	public void set(String name, Object value) {
		session.setAttribute(name, value);
	}

	@Override
	public void remove(String name) {
		session.removeAttribute(name);
	}

	@Override
	protected Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}

}
