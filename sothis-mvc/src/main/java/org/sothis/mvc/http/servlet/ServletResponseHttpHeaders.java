package org.sothis.mvc.http.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.sothis.mvc.http.HttpHeaders;

public class ServletResponseHttpHeaders extends HttpHeaders {
	private final HttpServletResponse response;

	public ServletResponseHttpHeaders(HttpServletResponse response) {
		super();
		this.response = response;
	}

	@Override
	public Iterator<String> names() {
		return Collections.unmodifiableCollection(response.getHeaderNames()).iterator();
	}

	@Override
	public String[] getStrings(String name) {
		Collection<String> headers = this.response.getHeaders(name);
		String[] ret = new String[headers.size()];
		headers.toArray(ret);
		return ret;
	}

	@Override
	public void addString(String name, String value) {
		response.addHeader(name, value);
	}

	@Override
	public void setString(String name, String value) {
		response.setHeader(name, value);
	}

	@Override
	public void remove(String name) {
		response.setHeader(name, null);
	}

	@Override
	public boolean contains(String name) {
		return response.containsHeader(name);
	}

}
