package org.sothis.mvc;

import java.util.Map;

public interface ActionContext {
	public final static String REQUEST = "org.sothis.mvc.ACTION_REQUEST";
	public final static String RESPONSE = "org.sothis.mvc.ACTION_RESPONSE";
	public final static String ACTION_PARAMS = "org.sothis.mvc.ACTION_PARAMS";
	public final static String ACTION = "org.sothis.mvc.ACTION";
	public final static String MODEL_AND_VIEW_RESOLVER = "org.sothis.mvc.MODEL_AND_VIEW_RESOLVER";
	public final static String ACTION_MAPPER = "org.sothis.mvc.ACTION_MAPPER";
	public final static String APPLICATION_CONTEXT = "org.sothis.mvc.APPLICATION_CONTEXT";

	Object getRequest();

	void setRequest(Object request);

	Object getResponse();

	void setResponse(Object response);

	Action getAction();

	void setAction(Action action);

	Object put(String key, Object value);

	Object get(String key);

	public Map<String, Object> getContextMap();

	Map<String, Object> setContextMap(Map<String, Object> context);

	void clear();

	ApplicationContext getApplicationContext();

}
