package org.sothis.nios.codec.http;

public class HttpVersion extends HttpString {

	public final static HttpVersion HTTP_1_1 = new HttpVersion("HTTP/1.1");
	public final static HttpVersion HTTP_1_0 = new HttpVersion("HTTP/1.0");

	public HttpVersion(String value) {
		super(value);
	}

	public static HttpVersion valueOf(String s) {
		if (HTTP_1_1.toString().equals(s)) {
			return HTTP_1_1;
		} else if (HTTP_1_0.toString().equals(s)) {
			return HTTP_1_0;
		} else {
			return new HttpVersion(s);
		}
	}
}
