package org.sothis.web.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;

/**
 * Action的上下文，内部是在ThreadLocal里放入了本次Action执行过程中需要的各个对象
 * 
 * @author velna
 * 
 */
public final class ActionContext {

	public final static String HTTP_REQUEST = "org.sothis.web.mvc.servlet.HttpServletRequest";
	public final static String HTTP_REQUEST_PARAM = "org.sothis.web.mvc.servlet.HttpServletRequestParameters";
	public final static String HTTP_RESPONSE = "org.sothis.web.mvc.servlet.HttpServletResponse";
	public final static String SERVLET_CONTEXT = "org.sothis.web.mvc.servlet.ServeltContext";
	public final static String ACTION = "org.sothis.web.mvc.action.Action";
	public final static String ACTION_PARAMS = "org.sothis.web.mvc.action.Params";
	public final static String LOCALE = "org.sothis.web.mvc.LOCALE";
	public final static String BEAN_FACTORY = "org.sothis.web.mvc.BEAN_FACTORY";
	public final static String SOTHIS_CONFIG = "org.sothis.web.mvc.SOTHIS_CONFIG";
	public final static String MODEL_AND_VIEW_RESOLVER = "org.sothis.web.mvc.MODEL_AND_VIEW_RESOLVER";
	public final static String ACTION_MAPPER = "org.sothis.web.mvc.ACTION_MAPPER";
	public final static String ACTIONS = "org.sothis.web.mvc.ACTIONS";
	public final static String ACTION_URI = "org.sothis.web.mvc.ActionUri";
	public final static String EXCEPTION_HANDLER = "org.sothis.web.mvc.ExceptionHandler";
	public final static String FLASH = "org.sothis.web.mvc.FLAH";

	private final static ThreadLocal<ActionContext> ACTION_CONTEXT = new ThreadLocal<ActionContext>() {
		@Override
		protected ActionContext initialValue() {
			return new ActionContext();
		}
	};

	private final Map<String, Object> context = new HashMap<String, Object>();

	private ActionContext() {
	}

	/**
	 * 清除当前的上下文
	 */
	public void clear() {
		ACTION_CONTEXT.remove();
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
	 * 设置key对应的上下文内容
	 * 
	 * @param key
	 * @param value
	 * @return 返回原始值，没有则返回null
	 */
	public Object set(String key, Object value) {
		return context.put(key, value);
	}

	/**
	 * 获取key对应的上下文内容
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return context.get(key);
	}

	/**
	 * 得到当前上下文的HttpServletRequest对象
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) get(HTTP_REQUEST);
	}

	/**
	 * 设置当前上下文的HttpServletRequest对象
	 * 
	 * @param request
	 * @return 返回原始值，没有则返回null
	 */
	public HttpServletRequest setRequest(HttpServletRequest request) {
		return (HttpServletRequest) context.put(HTTP_REQUEST, request);
	}

	/**
	 * 得到当前上下文的HttpServletResponse对象
	 * 
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return (HttpServletResponse) get(HTTP_RESPONSE);
	}

	/**
	 * 设置当前上下文的HttpServletResponse对象
	 * 
	 * @param response
	 * @return 返回原始值，没有则返回null
	 */
	public HttpServletResponse setResponse(HttpServletResponse response) {
		return (HttpServletResponse) context.put(HTTP_RESPONSE, response);
	}

	/**
	 * 得到当前上下文的所有请求参数
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> getParameters() {
		return (Map<String, Object[]>) get(HTTP_REQUEST_PARAM);
	}

	/**
	 * 设置当前上下文的的所有请求参数
	 * 
	 * @param parameters
	 * @return 返回原始值，没有则返回null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> setParameters(Map<String, Object[]> parameters) {
		return (Map<String, Object[]>) context.put(HTTP_REQUEST_PARAM, parameters);
	}

	/**
	 * 得到当前上下文的BeanFactory对象
	 * 
	 * @return
	 */
	public BeanFactory getBeanFactory() {
		return (BeanFactory) get(BEAN_FACTORY);
	}

	/**
	 * 设置当前上下文的BeanFactory对象
	 * 
	 * @param beanFactory
	 * @return 返回原始值，没有则返回null
	 */
	public BeanFactory setBeanFactory(BeanFactory beanFactory) {
		return (BeanFactory) set(BEAN_FACTORY, beanFactory);
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
	public ServletContext setServletContext(ServletContext servletContext) {
		return (ServletContext) set(SERVLET_CONTEXT, servletContext);
	}

	/**
	 * 得到当前上下文的Action对象
	 * 
	 * @return
	 */
	public Action getAction() {
		return (Action) get(ACTION);
	}

	/**
	 * 设置当前上下文的ServletContext对象
	 * 
	 * @param servletContext
	 * @return 返回原始值，没有则返回null
	 */
	public Action setAction(Action action) {
		return (Action) set(ACTION, action);
	}

	/**
	 * 得到当前上下文的内容
	 * 
	 * @return
	 */
	public Map<String, Object> getContextMap() {
		return Collections.unmodifiableMap(new HashMap<String, Object>(this.context));
	}

	/**
	 * 设置当前上下文的内容
	 * 
	 * @param context
	 * @return 返回原上下文的内容
	 */
	public Map<String, Object> setContextMap(Map<String, Object> context) {
		if (null == context) {
			throw new IllegalArgumentException("context can not be null!");
		}
		Map<String, Object> ret = new HashMap<String, Object>(this.context);
		this.context.clear();
		this.context.putAll(context);
		return ret;
	}

	public ExceptionHandler getExceptionHandler() {
		return (ExceptionHandler) get(EXCEPTION_HANDLER);
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
				try {
					flash = this.getBeanFactory().getBean(SothisConfig.getConfig().getFlash());
				} catch (BeanInstantiationException e) {
					throw new RuntimeException("error create flash bean", e);
				}
				session.setAttribute(FLASH, flash);
			}
		}
		return flash;
	}
}
