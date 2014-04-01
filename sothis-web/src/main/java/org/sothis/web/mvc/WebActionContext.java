package org.sothis.web.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sothis.mvc.Action;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ApplicationContext;
import org.sothis.mvc.AsyncContext;

public class WebActionContext implements ActionContext {

	public final static String FLASH = "org.sothis.web.mvc.FLAH";
	public final static String SERVLET_CONTEXT = "org.sothis.web.mvc.ServeltContext";
	public final static String ACTION_URI = "org.sothis.web.mvc.ACTION_URI";
	public final static String REQUEST_PARAM = "org.sothis.web.mvc.REQUEST_PARAM";

	protected final static ThreadLocal<WebActionContext> ACTION_CONTEXT = new ThreadLocal<WebActionContext>() {
		@Override
		protected WebActionContext initialValue() {
			return new WebActionContext();
		}
	};

	private WebActionContext() {
	}

	protected final Map<String, Object> context = new ConcurrentHashMap<String, Object>();
	private AsyncContext asyncContext;

	/**
	 * 得到当前的上下文
	 * 
	 * @return
	 */
	public static WebActionContext getContext() {
		return ACTION_CONTEXT.get();
	}

	/**
	 * 设置当前线程的上下文
	 * 
	 * @param context
	 * @return 返回原始的上下文
	 */
	public static WebActionContext setContext(WebActionContext context) {
		WebActionContext ret = ACTION_CONTEXT.get();
		ACTION_CONTEXT.set(context);
		return ret;
	}

	/**
	 * 得到当前上下文的HttpServletRequest对象
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) get(REQUEST);
	}

	/**
	 * 设置当前上下文的HttpServletRequest对象
	 * 
	 * @param request
	 * @return 返回原始值，没有则返回null
	 */
	public void setRequest(Object request) {
		context.put(REQUEST, request);
	}

	/**
	 * 得到当前上下文的HttpServletResponse对象
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return (HttpServletResponse) get(RESPONSE);
	}

	/**
	 * 设置当前上下文的HttpServletResponse对象
	 * 
	 * @param response
	 * @return 返回原始值，没有则返回null
	 */
	public void setResponse(Object response) {
		context.put(RESPONSE, response);
	}

	/**
	 * 得到当前上下文的ServletContext对象
	 * 
	 * @return
	 */
	public ServletContext getServletContext() {
		return (ServletContext) get(SERVLET_CONTEXT);
	}

	/**
	 * 设置当前上下文的ServletContext对象
	 * 
	 * @param servletContext
	 * @return 返回原始值，没有则返回null
	 */
	public void setServletContext(ServletContext servletContext) {
		put(SERVLET_CONTEXT, servletContext);
	}

	/**
	 * 得到当前上下文的所有请求参数
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> getParameters() {
		return (Map<String, Object[]>) get(REQUEST_PARAM);
	}

	/**
	 * 设置当前上下文的的所有请求参数
	 * 
	 * @param parameters
	 * @return 返回原始值，没有则返回null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> setParameters(Map<String, Object[]> parameters) {
		return (Map<String, Object[]>) context.put(REQUEST_PARAM, parameters);
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
		HttpSession session = this.getRequest().getSession(create);
		if (session != null) {
			flash = (Flash) session.getAttribute(FLASH);
			if (null == flash && create) {
				flash = this.getApplicationContext().getBeanFactory().getBean(this.getConfiguration().getFlash());
				session.setAttribute(FLASH, flash);
			}
		}
		return flash;
	}

	@Override
	public Action getAction() {
		return (Action) get(ACTION);
	}

	@Override
	public void setAction(Action action) {
		put(ACTION, action);
	}

	@Override
	public Object put(String key, Object value) {
		return context.put(key, value);
	}

	@Override
	public Object get(String key) {
		return context.get(key);
	}

	@Override
	public Map<String, Object> getContextMap() {
		return Collections.unmodifiableMap(this.context);
	}

	@Override
	public Map<String, Object> setContextMap(Map<String, Object> context) {
		if (null == context) {
			throw new IllegalArgumentException("context can not be null!");
		}
		Map<String, Object> ret = new HashMap<String, Object>(this.context);
		this.context.clear();
		this.context.putAll(context);
		return ret;
	}

	@Override
	public void clear() {
		ACTION_CONTEXT.remove();
	}

	public WebConfiguration getConfiguration() {
		return (WebConfiguration) this.getApplicationContext().getConfiguration();
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return (ApplicationContext) get(APPLICATION_CONTEXT);
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
		asyncContext = new WebAsyncContext(this);
		return asyncContext;
	}
}
