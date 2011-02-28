package com.velix.sothis;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionContext {

	public final static String HTTP_REQUEST = "com.velix.sothis.servlet.HttpServletRequest";
	public final static String HTTP_RESPONSE = "com.velix.sothis.servlet.HttpServletResponse";
	public final static String SERVLET_CONTEXT = "com.velix.sothis.servlet.ServeltContext";

	private final static ThreadLocal<ActionContext> actionContext = new ThreadLocal<ActionContext>() {
		@Override
		protected ActionContext initialValue() {
			return new ActionContext();
		}
	};

	private Map<String, Object> context = new HashMap<String, Object>();

	private ActionContext() {
	}

	public static ActionContext getContext() {
		return actionContext.get();
	}

	public Object put(String key, Object value) {
		return context.put(key, value);
	}

	public Object get(String key) {
		return context.get(key);
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) get(HTTP_REQUEST);
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) get(HTTP_RESPONSE);
	}

}
