package org.sothis.web.mvc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.sothis.mvc.Ignore;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.ModelAndViewResolver;

public class BaseModelAndView implements ModelAndView, Serializable {

	private static final long serialVersionUID = 1555542804341386440L;

	private String viewType = ModelAndViewResolver.DEFAULT_VIEW_TYPE;

	private Map<String, Object> viewParams;

	public Object model() {
		return this;
	}

	public String viewType() {
		return viewType;
	}

	public Map<String, Object> viewParams() {
		return viewParams;
	}

	@Ignore
	public final void viewType(final String viewType) {
		this.viewType = viewType;
	}

	/**
	 * 临时重定向(302)
	 * 
	 * @param location
	 *            需要重定向的action或url
	 * @return
	 */
	public final BaseModelAndView redirect(String location) {
		this.viewType("redirect");
		this.viewParam("location", location);
		return this;
	}

	/**
	 * 永久重定向(301)
	 * 
	 * @param location
	 *            需要重定向的action或url
	 * @return
	 */
	public final BaseModelAndView redirectPermanently(String location) {
		this.viewParam("statusCode", HttpServletResponse.SC_MOVED_PERMANENTLY);
		this.viewType("redirect");
		this.viewParam("location", location);
		return this;
	}

	/**
	 * 页面未找到(404)
	 * 
	 * @param path
	 *            404页面的路径
	 * @return
	 */
	public final BaseModelAndView notFound(String path) {
		this.viewParam("status", HttpServletResponse.SC_NOT_FOUND);
		this.viewParam("path", path);
		return this;
	}

	/**
	 * 页面未找到(404)，默认404页面的路径为 /404
	 * 
	 * @return
	 */
	public final BaseModelAndView notFound() {
		return notFound("/404");
	}

	/**
	 * 转向到path指定的页面
	 * 
	 * @param path
	 * @return
	 */
	public final BaseModelAndView forward(String path) {
		this.viewParam("path", path);
		return this;
	}

	@Ignore
	public final void viewParams(final Map<String, Object> viewParams) {
		this.viewParams = viewParams;
	}

	public final void viewParam(final String paramName, Object paramValue) {
		if (null == viewParams) {
			viewParams = new HashMap<String, Object>();
		}
		viewParams.put(paramName, paramValue);
	}

	public final Object viewParam(final String paramName) {
		if (null == viewParams) {
			return null;
		}
		return viewParams.get(paramName);
	}

}
