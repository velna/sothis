package org.sothis.nios.codec.http;

public class DefaultHttpRequest implements HttpRequest {
	private final HttpVersion version;
	private final HttpHeaders headers;
	private final HttpMethod method;
	private final String uri;

	public DefaultHttpRequest(HttpMethod method, String uri, HttpVersion version) {
		this.version = version;
		this.method = method;
		this.uri = uri;
		this.headers = new HttpHeaders();
	}

	@Override
	public HttpVersion version() {
		return version;
	}

	@Override
	public HttpHeaders headers() {
		return headers;
	}

	@Override
	public HttpMethod method() {
		return method;
	}

	@Override
	public String uri() {
		return uri;
	}

}
