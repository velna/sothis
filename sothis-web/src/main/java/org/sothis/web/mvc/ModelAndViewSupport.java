package org.sothis.web.mvc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.sothis.web.mvc.annotation.Ignore;

public class ModelAndViewSupport implements ModelAndView, Serializable {

	private static final long serialVersionUID = 2142558258767601308L;
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

	/**
	 * 临时重定向(302)
	 * 
	 * @param location
	 *            需要重定向的action或url
	 * @return
	 */
	public final ModelAndViewSupport redirect(String location) {
		this.setViewType("redirect");
		this.setViewParam("location", location);
		return this;
	}

	/**
	 * 永久重定向(301)
	 * 
	 * @param location
	 *            需要重定向的action或url
	 * @return
	 */
	public final ModelAndViewSupport redirectPermanently(String location) {
		this.setViewParam("statusCode", HttpServletResponse.SC_MOVED_PERMANENTLY);
		this.setViewType("redirect");
		this.setViewParam("location", location);
		return this;
	}

	/**
	 * 页面未找到(404)
	 * 
	 * @param path
	 *            404页面的路径
	 * @return
	 */
	public final ModelAndViewSupport notFound(String path) {
		this.setViewParam("status", HttpServletResponse.SC_NOT_FOUND);
		this.setViewParam("path", path);
		return this;
	}

	/**
	 * 页面未找到(404)，默认404页面的路径为 /404
	 * 
	 * @return
	 */
	public final ModelAndViewSupport notFound() {
		return notFound("/404");
	}

	/**
	 * 转向到path指定的页面
	 * 
	 * @param path
	 * @return
	 */
	public final ModelAndViewSupport forward(String path) {
		this.setViewParam("path", path);
		return this;
	}

	public final Flash flash() {
		return ActionContext.getContext().getFlash();
	}

	@Ignore
	public final void setViewType(final String viewType) {
		this.viewType = viewType;
	}

	@Ignore
	public final void setViewParams(final Map<String, Object> viewParams) {
		this.viewParams = viewParams;
	}

	public final void setViewParam(final String paramName, Object paramValue) {
		initViewParams();
		viewParams.put(paramName, paramValue);
	}

	public final Object getViewParam(final String paramName) {
		if (null == viewParams) {
			return null;
		}
		return viewParams.get(paramName);
	}

	private synchronized void initViewParams() {
		if (null == viewParams) {
			viewParams = new HashMap<String, Object>();
		}
	}
}
