package org.sothis.nios.codec.http;

public interface HttpRequest extends HttpMessage {
	HttpMethod method();

	String uri();
}
