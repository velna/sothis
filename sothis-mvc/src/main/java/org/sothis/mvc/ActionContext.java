package org.sothis.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActionContext {
	public final static String REQUEST = "org.sothis.mvc.ACTION_REQUEST";
	public final static String RESPONSE = "org.sothis.mvc.ACTION_RESPONSE";
	public final static String FLASH = "org.sothis.web.mvc.FLAH";
	public final static String ACTION_PARAMS = "org.sothis.mvc.ACTION_PARAMS";
	public final static String REQUEST_PARAMS = "org.sothis.mvc.REQUEST_PARAMS";
	public final static String ACTION = "org.sothis.mvc.ACTION";
	public final static String MODEL_AND_VIEW_RESOLVER = "org.sothis.mvc.MODEL_AND_VIEW_RESOLVER";
	public final static String ACTION_MAPPER = "org.sothis.mvc.ACTION_MAPPER";
	public final static String APPLICATION_CONTEXT = "org.sothis.mvc.APPLICATION_CONTEXT";
	public final static String EXCEPTION_HANDLER = "org.sothis.mvc.EXCEPTION_HANDLER";

	protected final Map<String, Object> context = new ConcurrentHashMap<String, Object>();
	protected AsyncContext asyncContext;

	protected static ActionContextFactory CONTEXT_FACTORY = new ActionContextFactory() {

		@Override
		public AsyncContext createAsyncContext(ActionContext actionContext) {
			throw new UnsupportedOperationException("async context is not supported.");
		}

		@Override
		public ActionContext createActionContext() {
			return new ActionContext();
		}
	};

	protected final static ThreadLocal<ActionContext> ACTION_CONTEXT = new ThreadLocal<ActionContext>() {
		@Override
		protected ActionContext initialValue() {
			return CONTEXT_FACTORY.createActionContext();
		}
	};

	protected ActionContext() {
	}

	/**
	 * 得到当前的上下文
	 * 
	 * @return
	 */
	public static ActionContext getContext() {
		return ACTION_CONTEXT.get();
	}

	/**
	 * 设置当前线程的上下文
	 * 
	 * @param context
	 * @return 返回原始的上下文
	 */
	public static ActionContext setContext(ActionContext context) {
		ActionContext ret = ACTION_CONTEXT.get();
		ACTION_CONTEXT.set(context);
		return ret;
	}

	protected static void setActionContextFactory(ActionContextFactory factory) {
		if (null == factory) {
			throw new IllegalArgumentException("factory can not be null.");
		}
		CONTEXT_FACTORY = factory;
	}

	public Request getRequest() {
		return get(REQUEST);
	}

	public void setRequest(Request request) {
		put(REQUEST, request);
	}

	public Map<String, Object[]> getRequestParameters() {
		return get(REQUEST_PARAMS);
	}

	public void setRequestParameters(Map<String, Object[]> params) {
		put(REQUEST_PARAMS, params);
	}

	public Response getResponse() {
		return get(RESPONSE);
	}

	public void setResponse(Response response) {
		put(RESPONSE, response);
	}

	public Action getAction() {
		return get(ACTION);
	}

	public void setAction(Action action) {
		put(ACTION, action);
	}

	public ActionMapper getActionMapper() {
		return get(ACTION_MAPPER);
	}

	public void setActionMapper(ActionMapper actionMapper) {
		put(ACTION_MAPPER, actionMapper);
	}

	public ModelAndViewResolver getModelAndViewResolver() {
		return get(MODEL_AND_VIEW_RESOLVER);
	}

	public void setModelAndViewResolver(ModelAndViewResolver modelAndViewResolver) {
		put(MODEL_AND_VIEW_RESOLVER, modelAndViewResolver);
	}

	public Object[] getActionParams() {
		return get(ACTION_PARAMS);
	}

	public void setActionParams(Object... actionParams) {
		put(ACTION_PARAMS, actionParams);
	}

	@SuppressWarnings("unchecked")
	public <T> T put(String key, T value) {
		return (T) context.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) context.get(key);
	}

	public Map<String, Object> getContextMap() {
		return new HashMap<>(this.context);
	}

	public Map<String, Object> setContextMap(Map<String, Object> context) {
		if (null == context) {
			throw new IllegalArgumentException("context can not be null!");
		}
		Map<String, Object> ret = new HashMap<String, Object>(this.context);
		this.context.clear();
		this.context.putAll(context);
		return ret;
	}

	public void clear() {
		this.context.clear();
	}

	public ApplicationContext getApplicationContext() {
		return get(APPLICATION_CONTEXT);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		put(APPLICATION_CONTEXT, applicationContext);
	}

	public boolean isAsyncStarted() {
		return asyncContext != null;
	}

	public AsyncContext getAsyncContext() {
		if (asyncContext == null) {
			throw new IllegalStateException("async context is not started yet.");
		}
		return asyncContext;
	}

	public AsyncContext startAsync() {
		asyncContext = CONTEXT_FACTORY.createAsyncContext(this);
		return asyncContext;
	}

	/**
	 * 得到当前上下文中的Flash对象，没有则会创建一个新的Flash对象
	 * 
	 * @return
	 */
	public Flash getFlash() {
		return getFlash(true);
	}

	/**
	 * 得到当前上下文中的Flash对象
	 * 
	 * @param create
	 *            当当前上下文中没有Flash对象时是否创建一个新的对象，true为创建，false为不创建
	 * @return
	 */
	public Flash getFlash(boolean create) {
		Flash flash = null;
		Session session = this.getRequest().getSession(create);
		if (session != null) {
			flash = (Flash) session.attributes().get(FLASH);
			if (null == flash && create) {
				ApplicationContext appContext = this.getApplicationContext();
				flash = appContext.getBeanFactory().getBean(appContext.getConfiguration().getFlash());
				session.attributes().set(FLASH, flash);
			}
		}
		return flash;
	}

	public ExceptionHandler getExceptionHandler() {
		return get(EXCEPTION_HANDLER);
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		put(EXCEPTION_HANDLER, exceptionHandler);
	}
}
