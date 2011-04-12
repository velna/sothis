package org.sothis.web.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.sothis.util.ClassUtils;
import org.sothis.web.mvc.views.JspView;

public class SothisFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Controller> controllers;
	private ServletContext servletContext;
	private BeanFactory beanFactory;
	private SothisConfig config;

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			config = SothisConfig.getConfig();
			servletContext = filterConfig.getServletContext();
			controllers = new HashMap<String, Controller>();
			beanFactory = config.getBeanFactory();
			beanFactory.init(servletContext);
			initControllers();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void initControllers() throws Exception {
		String[] packageNames = config.getControllerPackages();
		for (String packageName : packageNames) {
			Class<?>[] classes = ClassUtils.getClasses(packageName);
			for (Class<?> c : classes) {
				String className = c.getName();
				String pathName;
				int i = className.lastIndexOf('.');
				if (i > 0) {
					pathName = className.substring(0, i + 1)
							.substring(packageName.length())
							.replaceAll("\\.", "/");
				} else {
					pathName = "/";
				}
				String simpleName = c.getSimpleName();
				i = simpleName.indexOf("Controller");
				String name;
				if (i > 0) {
					char ch = Character.toLowerCase(simpleName.charAt(0));
					name = pathName + ch + simpleName.substring(1, i);
				} else if (i == 0) {
					name = pathName;
				} else {
					continue;
				}
				controllers.put(name, new DefaultController(name, c,
						beanFactory));
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
		if (null == request.getCharacterEncoding()) {
			request.setCharacterEncoding(config.getCharacterEncoding());
		}
		ActionInvocation invocation = prepareActionInvocation(request, response);
		if (null != invocation) {
			Object result = invocation.invoke();
			ModelAndViewResolver resolver = this.beanFactory.getBean(config
					.getViewResolverClass());
			resolver.setDefaultView(JspView.class);
			ResolvedModelAndView mav = resolver.resolve(result, invocation);
			mav.getView().render(mav.getModel(), invocation);
		}
	}

	private ActionInvocation prepareActionInvocation(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		if ("".equals(actionName)) {
			actionName = "index";
		}
		if (logger.isDebugEnabled()) {
			logger.debug("controllerName:{}, actionName:{}", controllerName,
					actionName);
		}
		ActionContext context = ActionContext.getContext();
		context.put(ActionContext.HTTP_REQUEST, request);
		context.put(ActionContext.HTTP_RESPONSE, response);
		context.put(ActionContext.SERVLET_CONTEXT, servletContext);
		context.put(ActionContext.INTERCEPTORS,
				getBeans(config.getDefaultInterceptorStackClasses()));

		Controller controller = controllers.get(controllerName);
		if (null == controller) {
			controller = new EmptyController(controllerName);
		}
		context.put(ActionContext.ACTION, controller.getAction(actionName));
		return new DefaultActionInvocation(context);
	}

	private <T> T getBean(Class<? extends T> beanClass) throws Exception {
		return this.beanFactory.getBean(beanClass);
	}

	private <T> List<? extends T> getBeans(List<Class<? extends T>> beanClasses)
			throws Exception {
		List<T> beans = new ArrayList<T>(beanClasses.size());
		for (Class<? extends T> beanClass : beanClasses) {
			beans.add(getBean(beanClass));
		}
		return beans;
	}

	public void destroy() {

	}

}
