package org.sothis.web.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionContext {

	public final static String HTTP_REQUEST = "org.sothis.web.mvc.servlet.HttpServletRequest";
	public final static String HTTP_REQUEST_PARAMETERS = "org.sothis.web.mvc.servlet.HttpServletRequestParameters";
	public final static String HTTP_RESPONSE = "org.sothis.web.mvc.servlet.HttpServletResponse";
	public final static String SERVLET_CONTEXT = "org.sothis.web.mvc.servlet.ServeltContext";
	public final static String ACTION = "org.sothis.web.mvc.action.Action";
	public final static String INTERCEPTORS = "org.sothis.web.mvc.Interceptors";
	public final static String ACTION_PARAMS = "org.sothis.web.mvc.action.Params";
	public final static String LOCALE = "org.sothis.web.mvc.LOCALE";

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

	public HttpServletRequest setRequest(HttpServletRequest request) {
		return (HttpServletRequest) context.put(HTTP_REQUEST, request);
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) get(HTTP_RESPONSE);
	}

	public HttpServletResponse setResponse(HttpServletResponse response) {
		return (HttpServletResponse) context.put(HTTP_RESPONSE, response);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object[]> getParameters() {
		return (Map<String, Object[]>) get(HTTP_REQUEST_PARAMETERS);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object[]> setParameters(Map<String, Object[]> parameters) {
		return (Map<String, Object[]>) context.put(HTTP_REQUEST_PARAMETERS,
				parameters);
	}

}
