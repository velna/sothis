package com.velix.sothis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.velix.sothis.util.ClassUtils;

public class SothisFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Controller> controllers;
	private ServletContext servletContext;
	private BeanFactory beanFactory;

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			servletContext = filterConfig.getServletContext();
			controllers = new HashMap<String, Controller>();
			beanFactory = SothisConfig.getConfig().getBeanFactory();
			beanFactory.init(servletContext);
			initControllers();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void initControllers() throws Exception {
		String[] packageNames = SothisConfig.getConfig()
				.getControllerPackages();
		for (String packageName : packageNames) {
			Class<?>[] classes = ClassUtils.getClasses(packageName);
			for (Class<?> c : classes) {
				String name = c.getName();
				String pathName;
				int i = name.lastIndexOf('.');
				if (i > 0) {
					pathName = name.substring(0, i + 1)
							.substring(packageName.length())
							.replaceAll("\\.", "/");
				} else {
					pathName = "/";
				}
				String className = c.getSimpleName();
				i = className.indexOf("Controller");
				String uri;
				if (i > 0) {
					char ch = Character.toLowerCase(className.charAt(0));
					uri = pathName + ch + className.substring(1, i);
				} else if (i == 0) {
					uri = pathName;
				} else {
					continue;
				}
				controllers.put(uri, new Controller(c, beanFactory));
			}
		}
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		try {
			doFilter((HttpServletRequest) req, (HttpServletResponse) resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void doFilter(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionContext context = this.initActionContext(request, response);
		String uri = request.getRequestURI().substring(
				servletContext.getContextPath().length());
		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		String controllerName, actionName;
		int i = uri.lastIndexOf('/');
		if (i > 0) {
			controllerName = uri.substring(0, i);
			actionName = uri.substring(i + 1);
		} else {
			controllerName = "/";
			actionName = uri.substring(1);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("controllerName:{}, actionName:{}", controllerName,
					actionName);
		}
		Controller controller = controllers.get(controllerName);
		if (null != controller) {
			Action action = controller.getAction(actionName);
			if (null != action) {
				Object result = action
						.invoke(controller.newInstance(), context);
				if (result instanceof String) {
					String path = controllerName + "/" + result + ".jsp";
					if (logger.isDebugEnabled()) {
						logger.debug("result path:{}", path);
					}
					request.getRequestDispatcher(path).forward(request,
							response);
				}
			}
		}
	}

	private ActionContext initActionContext(HttpServletRequest request,
			HttpServletResponse response) {
		ActionContext context = ActionContext.getContext();
		context.put(ActionContext.HTTP_REQUEST, request);
		context.put(ActionContext.HTTP_RESPONSE, response);
		context.put(ActionContext.SERVLET_CONTEXT, servletContext);
		return context;
	}

	public void destroy() {

	}

}
