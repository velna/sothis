package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sothis.mvc.Attachments;
import org.sothis.mvc.Attributes;
import org.sothis.mvc.HashMapParameters;
import org.sothis.mvc.Headers;
import org.sothis.mvc.Parameters;
import org.sothis.mvc.Request;
import org.sothis.mvc.RequestParseExecption;
import org.sothis.mvc.Session;

public class ServletHttpRequest implements Request {

	private final HttpServletRequest request;
	private Parameters parameters;
	private Headers headers;
	private Attachments attachments;
	private Session session;
	private Attributes attributes;
	private String uri;
	private Charset charset;

	public ServletHttpRequest(HttpServletRequest request) {
		super();
		this.request = request;
	}

	@Override
	public Charset getCharset() {
		if (null == charset) {
			charset = Charset.forName(request.getCharacterEncoding());
		}
		return charset;
	}

	@Override
	public void setCharset(Charset charset) throws UnsupportedEncodingException {
		this.charset = charset;
		request.setCharacterEncoding(charset.name());
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
	public String getProtocol() {
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
	public Headers headers() {
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

	private boolean isMultipart() {
		String contentType = request.getContentType();
		return null != contentType && contentType.toLowerCase().startsWith("multipart/form-data");
	}

	@Override
	public Parameters parameters() throws RequestParseExecption {
		if (null == parameters) {
			this.parameters = new HashMapParameters((Map) request.getParameterMap());
		}
		return parameters;
	}

	@Override
	public Attachments attachments() throws RequestParseExecption {
		if (null == attachments) {
			if (isMultipart()) {
				ServletContext servletContext = request.getServletContext();
				if (servletContext.getMajorVersion() > 3
						|| (servletContext.getMajorVersion() == 3 && servletContext.getMinorVersion() >= 1)) {
					attachments = Servlet31MultipartParser.parse(request);
				} else {
					attachments = CommonsFileMultipartParser.parse(request);
				}
			} else {
				attachments = Attachments.EMPTY;
			}
		}
		return attachments;
	}

	@Override
	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	@Override
	public int getLocalPort() {
		return request.getLocalPort();
	}

	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	@Override
	public int getRemotePort() {
		return request.getRemotePort();
	}

	@Override
	public String getScheme() {
		return request.getScheme();
	}

}
