package org.sothis.mvc.http.servlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.sothis.mvc.Headers;

public class ServletRequestHttpHeaders extends Headers {
	private final HttpServletRequest request;

	public ServletRequestHttpHeaders(HttpServletRequest request) {
		super();
		this.request = request;
	}

	@Override
	public Iterator<String> names() {
		return new EnumerationIterator<String>(request.getHeaderNames());
	}

	@Override
	public String[] getStrings(String name) {
		Enumeration<String> e = this.request.getHeaders(name);
		List<String> values = new ArrayList<String>(1);
		while (e.hasMoreElements()) {
			values.add(e.nextElement());
		}
		String[] ret = new String[values.size()];
		values.toArray(ret);
		return ret;
	}

	@Override
	public void addString(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setString(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStrings(String name, String[] value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(String name) {
		return request.getHeader(name) != null;
	}

}
