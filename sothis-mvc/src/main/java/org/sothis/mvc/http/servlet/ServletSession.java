package org.sothis.mvc.http.servlet;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.sothis.mvc.Attributes;
import org.sothis.mvc.Session;

public class ServletSession implements Session {

	private final HttpSession session;
	private Attributes attributes;

	public ServletSession(HttpSession session) {
		super();
		this.session = session;
	}

	@Override
	public String getId() {
		return session.getId();
	}

	@Override
	public Date getDateCreated() {
		return new Date(session.getCreationTime());
	}

	@Override
	public Date getLastAccessed() {
		return new Date(session.getLastAccessedTime());
	}

	@Override
	public Attributes attributes() {
		if (null == attributes) {
			attributes = new ServletSessionAttributes(session);
		}
		return attributes;
	}

}
