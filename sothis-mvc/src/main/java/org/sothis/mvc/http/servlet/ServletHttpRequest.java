package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sothis.mvc.Attributes;
import org.sothis.mvc.HashMapParameters;
import org.sothis.mvc.Parameters;
import org.sothis.mvc.Session;
import org.sothis.mvc.http.HttpHeaders;
import org.sothis.mvc.http.HttpRequest;

public class ServletHttpRequest implements HttpRequest {

	private final HttpServletRequest request;
	private Parameters parameters;
	private HttpHeaders headers;
	private Session session;
	private Attributes attributes;
	private String uri;

	public ServletHttpRequest(HttpServletRequest request) {
		super();
		this.request = request;
	}

	@Override
	public String getCharset() {
		return request.getCharacterEncoding();
	}

	@Override
	public void setCharset(String charset) throws UnsupportedEncodingException {
		request.setCharacterEncoding(charset);
	}

	@Override
	public Parameters parameters() {
		if (null == parameters) {
			parameters = new HashMapParameters(request.getParameterMap());
		}
		return parameters;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	@Override
	public Session getSession() {
		return getSession(true);
	}

	@Override
	public Session getSession(boolean create) {
		if (null == session) {
			HttpSession httpSession = request.getSession(create);
			if (null == httpSession) {
				return null;
			}
			session = new ServletSession(httpSession);
		}
		return session;
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public String getProtocolVersion() {
		return request.getProtocol();
	}

	@Override
	public String getUri() {
		if (null == uri) {
			uri = request.getRequestURI();
			if (request.getQueryString() != null) {
				uri = uri + "?" + request.getQueryString();
			}
		}
		return uri;
	}

	@Override
	public HttpHeaders headers() {
		if (null == headers) {
			headers = new ServletRequestHttpHeaders(request);
		}
		return headers;
	}

	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	@Override
	public Attributes attributes() {
		if (null == attributes) {
			attributes = new ServletRequestAttributes(request);
		}
		return attributes;
	}

	@Override
	public String getUriPath() {
		return request.getRequestURI();
	}

}
