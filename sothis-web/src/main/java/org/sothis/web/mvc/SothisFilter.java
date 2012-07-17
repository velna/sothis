package org.sothis.web.mvc;

import java.io.IOException;
import java.util.Collections;
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.config.PropertiesBean;
import org.sothis.core.util.ClassUtils;
import org.sothis.web.mvc.annotation.Ignore;

public class SothisFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SothisFilter.class);

	private final Map<String, Action> actions = new HashMap<String, Action>();
	private ServletContext servletContext;
	private BeanFactory beanFactory;
	private SothisConfig config;

	@SuppressWarnings("unchecked")
	public void init(final FilterConfig filterConfig) throws ServletException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Sothis: initialization started");
		}
		try {
			servletContext = filterConfig.getServletContext();
			String beanFactoryClass = filterConfig.getInitParameter("beanFactoryClass");
			if (null != beanFactoryClass) {
				beanFactory = createBeanFactory((Class<BeanFactory>) Class.forName(beanFactoryClass));
			}
			String configBeanName = filterConfig.getInitParameter("configBeanName");
			if (null != configBeanName) {
				if (null == beanFactory) {
					throw new ServletException("'beanFactoryClass' param must be configured since 'configBeanName' is used.");
				}
				PropertiesBean propertiesBean = beanFactory.getBean(configBeanName);
				config = SothisConfig.initConfig(propertiesBean.getProperties());
			} else {
				String configLocation = filterConfig.getInitParameter("configLocation");
				if (null == configLocation) {
					configLocation = "sothis.properties";
				}
				config = SothisConfig.initConfig(configLocation);
			}
			if (null == beanFactory) {
				if (null == config.getBeanFactoryClass()) {
					throw new ConfigurationException("sothis.beanFactory.class is not set !");
				}
				beanFactory = createBeanFactory(config.getBeanFactoryClass());
			}
			initActions();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Sothis: initialization completed");
			}
		} catch (Exception e) {
			throw new ServletException("sothis init failed: ", e);
		}
	}

	private BeanFactory createBeanFactory(Class<BeanFactory> beanFactoryClass) throws InstantiationException,
			IllegalAccessException {
		BeanFactory beanFactory = beanFactoryClass.newInstance();
		if (beanFactory instanceof ServletBeanFactory) {
			((ServletBeanFactory) beanFactory).init(servletContext);
		}
		return beanFactory;
	}

	private void initActions() throws BeanInstantiationException, ClassNotFoundException, IOException, ConfigurationException {
		final String[] packageNames = config.getControllerPackages();
		for (String packageName : packageNames) {
			if (StringUtils.isEmpty(packageName)) {
				continue;
			}
			final Class<?>[] classes = ClassUtils.getClasses(packageName);
			for (Class<?> c : classes) {
				if (c.isLocalClass() || c.isMemberClass() || c.isAnonymousClass() || c.isAnnotationPresent(Ignore.class)
						|| c.getPackage().isAnnotationPresent(Ignore.class)) {
					continue;
				}
				final String className = c.getName().substring(packageName.length() + 1);
				String subPackageName;
				int dotIndex = className.lastIndexOf('.');
				if (dotIndex > 0) {
					subPackageName = className.substring(0, dotIndex).replaceAll("\\.", "/");
				} else {
					subPackageName = "";
				}
				final String simpleName = c.getSimpleName();
				dotIndex = simpleName.indexOf("Controller");
				String name;
				if (dotIndex > 0) {
					name = StringUtils.uncapitalize(simpleName.substring(0, dotIndex));
				} else if (dotIndex == 0) {
					name = "";
				} else {
					continue;
				}
				Controller controller = new DefaultController(subPackageName, name, c);
				Map<String, Action> controllerActions = controller.getActions();
				for (String actionName : controllerActions.keySet()) {
					Action action = controllerActions.get(actionName);
					ActionMapper actionMapper = beanFactory.getBean(config.getActionMapper());
					String actionKey = actionMapper.map(packageName, c, actionName);
					if (actions.containsKey(actionKey)) {
						throw new ConfigurationException("duplicated action key:" + actionKey + ", which already registered as "
								+ actions.get(actionKey));
					}
					actions.put(actionKey, action);
				}
			}
		}
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		try {
			ActionContext context = ActionContext.getContext();
			context.set(ActionContext.ACTION_MAPPER, beanFactory.getBean(config.getActionMapper()));
			context.set(ActionContext.MODEL_AND_VIEW_RESOLVER, beanFactory.getBean(config.getModelAndViewResolver()));
			context.set(ActionContext.SOTHIS_CONFIG, this.config);
			context.set(ActionContext.ACTIONS, Collections.unmodifiableMap(actions));
			if (null != config.getExceptionHandler()) {
				context.set(ActionContext.EXCEPTION_HANDLER, this.beanFactory.getBean(config.getExceptionHandler()));
			}
			context.setBeanFactory(beanFactory);
			context.setRequest((HttpServletRequest) req);
			context.setResponse((HttpServletResponse) resp);
			context.setServletContext(servletContext);
			Flash flash = context.getFlash(false);
			if (null != flash) {
				flash.flash();
			}
			ActionInvocationHelper.invoke(context);
		} catch (BeanInstantiationException e) {
			throw new ServletException(e);
		} finally {
			ActionContext.getContext().clear();
		}
	}

	public void destroy() {

	}

}
