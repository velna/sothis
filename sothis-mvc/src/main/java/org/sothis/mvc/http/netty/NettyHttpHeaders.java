package org.sothis.mvc.http.netty;

import java.util.Iterator;
import java.util.List;

import org.sothis.mvc.Headers;

public class NettyHttpHeaders extends Headers {

	private final io.netty.handler.codec.http.HttpHeaders headers;

	public NettyHttpHeaders(io.netty.handler.codec.http.HttpHeaders headers) {
		super();
		this.headers = headers;
	}

	@Override
	public Iterator<String> names() {
		return headers.names().iterator();
	}

	@Override
	public String[] getStrings(String name) {
		List<String> values = headers.getAll(name);
		String[] ret = new String[values.size()];
		values.toArray(ret);
		return ret;
	}

	@Override
	public void addString(String name, String value) {
		headers.add(name, value);
	}

	@Override
	public void setString(String name, String value) {
		headers.set(name, value);
	}

	@Override
	public void remove(String name) {
		headers.remove(name);
	}

	@Override
	public boolean contains(String name) {
		return headers.contains(name);
	}

}
