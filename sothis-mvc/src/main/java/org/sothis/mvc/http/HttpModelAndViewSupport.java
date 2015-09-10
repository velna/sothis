package org.sothis.mvc.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ModelAndViewSupport;
import org.sothis.mvc.View;

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

	/**
	 * 不进行view渲染
	 * 
	 * @return
	 */
	public final HttpModelAndViewSupport noView() {
		this.viewType(View.NULL_VIEW_TYPE);
		return this;
	}

	/**
	 * 同download(fileName, fileSize, null);
	 * 
	 * @param fileName
	 * @param fileSize
	 * @return
	 * @throws IOException
	 */
	public final HttpModelAndViewSupport download(String fileName, long fileSize) throws IOException {
		return download(fileName, fileSize, null);
	}

	/**
	 * 生成一个下载文件的响应
	 * 
	 * @param fileName
	 *            文件名
	 * @param contentType
	 *            文件类型
	 * @param fileSize
	 *            文件大小。可以为0。
	 * @param fileStream
	 *            文件流，为null则不实际写入文件数据。
	 * @return
	 * @throws IOException
	 */
	public final HttpModelAndViewSupport download(String fileName, long fileSize, InputStream fileStream) throws IOException {
		this.viewType(View.NULL_VIEW_TYPE);
		HttpResponse resp = (HttpResponse) ActionContext.getContext().getResponse();
		resp.headers().addString("Content-Disposition",
				"attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
		if (fileSize > 0) {
			resp.headers().addString("Content-Length", String.valueOf(fileSize));
		}
		ServletContext servletContext = (ServletContext) ActionContext.getContext().getApplicationContext().getNativeContext();
		String contentType = servletContext.getMimeType(fileName);
		resp.headers().setString(HttpHeaders.Names.CONTENT_TYPE, null == contentType ? "application/octet-stream" : contentType);
		if (null != fileStream) {
			IOUtils.copy(new BufferedInputStream(fileStream), resp.getOutputStream());
		}
		return this;
	}

}
