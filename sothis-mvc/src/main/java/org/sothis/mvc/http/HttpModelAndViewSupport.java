package org.sothis.mvc.http;

import org.sothis.mvc.ModelAndViewSupport;

public class HttpModelAndViewSupport extends ModelAndViewSupport {

	/**
	 * 临时重定向(302)
	 * 
	 * @param location
	 *            需要重定向的action或url
	 * @return
	 */
	public final HttpModelAndViewSupport redirect(String location) {
		this.viewType("redirect");
		this.viewParam("status", HttpResponse.StatusCodes.SC_MOVED_TEMPORARILY);
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
	public final HttpModelAndViewSupport redirectPermanently(String location) {
		this.viewType("redirect");
		this.viewParam("status", HttpResponse.StatusCodes.SC_MOVED_PERMANENTLY);
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
	public final HttpModelAndViewSupport notFound(String path) {
		this.viewParam("status", HttpResponse.StatusCodes.SC_NOT_FOUND);
		this.viewParam("location", path);
		return this;
	}

	/**
	 * 页面未找到(404)，默认404页面的路径为 /404
	 * 
	 * @return
	 */
	public final HttpModelAndViewSupport notFound() {
		return notFound("/404");
	}

	/**
	 * 转向到path指定的页面
	 * 
	 * @param path
	 * @return
	 */
	public final HttpModelAndViewSupport forward(String path) {
		this.viewParam("path", path);
		return this;
	}

}
