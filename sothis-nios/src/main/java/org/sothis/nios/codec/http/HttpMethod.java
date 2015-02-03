package org.sothis.nios.codec.http;

import java.util.HashMap;
import java.util.Map;

public class HttpMethod extends HttpString {

	public final static HttpMethod GET = new HttpMethod("GET");
	public final static HttpMethod OPTIONS = new HttpMethod("OPTIONS");
	public final static HttpMethod HEAD = new HttpMethod("HEAD");
	public final static HttpMethod POST = new HttpMethod("POST");
	public final static HttpMethod PUT = new HttpMethod("PUT");
	public final static HttpMethod PATCH = new HttpMethod("PATCH");
	public final static HttpMethod DELETE = new HttpMethod("DELETE");
	public final static HttpMethod TRACE = new HttpMethod("TRACE");
	public final static HttpMethod CONNECT = new HttpMethod("CONNECT");

	private static final Map<String, HttpMethod> methodMap = new HashMap<String, HttpMethod>();

	static {
		methodMap.put(OPTIONS.toString(), OPTIONS);
		methodMap.put(GET.toString(), GET);
		methodMap.put(HEAD.toString(), HEAD);
		methodMap.put(POST.toString(), POST);
		methodMap.put(PUT.toString(), PUT);
		methodMap.put(PATCH.toString(), PATCH);
		methodMap.put(DELETE.toString(), DELETE);
		methodMap.put(TRACE.toString(), TRACE);
		methodMap.put(CONNECT.toString(), CONNECT);
	}

	public HttpMethod(String str) {
		super(str);
	}

	public static HttpMethod valueOf(String name) {
		HttpMethod result = methodMap.get(name);
		if (result != null) {
			return result;
		} else {
			return new HttpMethod(name);
		}
	}
}
