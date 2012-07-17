package org.sothis.web.mvc;

import java.util.Iterator;
import java.util.List;

public class DefaultInterceptorStack implements InterceptorStack {
	private final List<Class<Interceptor>> interceptors;

	public DefaultInterceptorStack(List<Class<Interceptor>> interceptors) {
		if (null == interceptors) {
			throw new NullPointerException();
		}
		this.interceptors = interceptors;
	}

	@Override
	public Iterator<Class<Interceptor>> iterator() {
		return interceptors.iterator();
	}
}
