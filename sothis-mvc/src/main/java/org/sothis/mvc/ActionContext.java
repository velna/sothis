package org.sothis.mvc;

import java.util.Map;

public interface ActionContext {
	public final static String REQUEST = "org.sothis.mvc.ACTION_REQUEST";
	public final static String RESPONSE = "org.sothis.mvc.ACTION_RESPONSE";
	public final static String ACTION_PARAMS = "org.sothis.mvc.ACTION_PARAMS";
	public final static String ACTION = "org.sothis.mvc.ACTION";
	public final static String MODEL_AND_VIEW_RESOLVER = "org.sothis.mvc.MODEL_AND_VIEW_RESOLVER";
	public final static String ACTION_INVOCATION = "org.sothis.mvc.ACTION_INVOCATION";
	public final static String ACTION_MAPPER = "org.sothis.mvc.ACTION_MAPPER";
	public final static String APPLICATION_CONTEXT = "org.sothis.mvc.APPLICATION_CONTEXT";

	Request getRequest();

	void setRequest(Request request);

	Response getResponse();

	void setResponse(Response response);

	Action getAction();

	void setAction(Action action);

	<T> T put(String key, T value);

	<T> T get(String key);

	public Map<String, Object> getContextMap();

	Map<String, Object> setContextMap(Map<String, Object> context);

	void clear();

	ApplicationContext getApplicationContext();

	boolean isAsyncStarted();

	AsyncContext getAsyncContext();

	AsyncContext startAsync();
}
