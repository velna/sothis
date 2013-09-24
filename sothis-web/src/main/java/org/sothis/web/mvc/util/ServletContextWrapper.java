package org.sothis.web.mvc.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletContextWrapper implements ServletContext {
	private final ServletContext servletContext;

	public ServletContextWrapper(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public String getContextPath() {
		return servletContext.getContextPath();
	}

	@Override
	public ServletContext getContext(String uripath) {
		return servletContext.getContext(uripath);
	}

	@Override
	public int getMajorVersion() {
		return servletContext.getMajorVersion();
	}

	@Override
	public int getMinorVersion() {
		return servletContext.getMinorVersion();
	}

	@Override
	public String getMimeType(String file) {
		return servletContext.getMimeType(file);
	}

	@Override
	public Set getResourcePaths(String path) {
		return servletContext.getResourcePaths(path);
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return servletContext.getResource(path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return servletContext.getResourceAsStream(path);
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return servletContext.getRequestDispatcher(path);
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		return servletContext.getNamedDispatcher(name);
	}

	@Override
	public Servlet getServlet(String name) throws ServletException {
		return servletContext.getServlet(name);
	}

	@Override
	public Enumeration getServlets() {
		return servletContext.getServlets();
	}

	@Override
	public Enumeration getServletNames() {
		return servletContext.getServletNames();
	}

	@Override
	public void log(String msg) {
		servletContext.log(msg);
	}

	@Override
	public void log(Exception exception, String msg) {
		servletContext.log(exception, msg);
	}

	@Override
	public void log(String message, Throwable throwable) {
		servletContext.log(message, throwable);
	}

	@Override
	public String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	@Override
	public String getServerInfo() {
		return servletContext.getServerInfo();
	}

	@Override
	public String getInitParameter(String name) {
		return servletContext.getInitParameter(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return servletContext.getInitParameterNames();
	}

	@Override
	public Object getAttribute(String name) {
		return servletContext.getAttribute(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return servletContext.getAttributeNames();
	}

	@Override
	public void setAttribute(String name, Object object) {
		servletContext.setAttribute(name, object);
	}

	@Override
	public void removeAttribute(String name) {
		servletContext.removeAttribute(name);
	}

	@Override
	public String getServletContextName() {
		return servletContext.getServletContextName();
	}

}
