package com.velix.sothis;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionContext {

	public final static String HTTP_REQUEST = "com.velix.sothis.servlet.HttpServletRequest";
	public final static String HTTP_RESPONSE = "com.velix.sothis.servlet.HttpServletResponse";
	public final static String SERVLET_CONTEXT = "com.velix.sothis.servlet.ServeltContext";
	public final static String ACTION = "com.velix.sothis.action.Action";
	public final static String INTERCEPTORS = "com.velix.sothis.Interceptors";
	public final static String ACTION_PARAMS = "com.velix.sothis.action.Params";
	public final static String LOCALE = "com.velix.sothis.LOCALE";

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

	public Locale getLocale() {
		Locale locale = (Locale) get(LOCALE);
		if (locale == null) {
			locale = Locale.getDefault();
			put(LOCALE, locale);
		}
		return locale;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getParameters() {
		return (Map<String, Object>) get(ACTION_PARAMS);
	}
}
