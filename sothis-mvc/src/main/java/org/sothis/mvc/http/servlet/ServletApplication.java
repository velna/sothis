package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.beans.BeanFactory;
import org.sothis.core.config.PropertiesBean;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.ApplicationContext;
import org.sothis.mvc.Configuration;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.DefaultApplicationContext;
import org.sothis.mvc.Flash;
import org.sothis.mvc.Request;
import org.sothis.mvc.Response;

/**
 * @param beanFactoryClass
 *            如果配置了configBeanName，则必须同时配置beanFactoryClass
 * @param configBeanName
 *            在beanFactory中定义的config bean名称
 * @param configLocation
 *            如果没有配置configBeanName，则使用configLocation指身的classpath路径查找配置文件，如果没有配置，
 *            则默认使用sothis.properties
 * @author velna
 * 
 */
public class ServletApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServletApplication.class);

	private ApplicationContext applicationContext;
	private ServletContext servletContext;

	public ServletApplication(final ServletConfig servletConfig) throws ServletException {
		init(new Config() {

			@Override
			public String getName() {
				return servletConfig.getServletName();
			}

			@Override
			public ServletContext getServletContext() {
				return servletConfig.getServletContext();
			}

			@Override
			public String getInitParameter(String name) {
				return servletConfig.getInitParameter(name);
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				return servletConfig.getInitParameterNames();
			}
		});
	}

	public ServletApplication(final FilterConfig filterConfig) throws ServletException {
		init(new Config() {

			@Override
			public String getName() {
				return filterConfig.getFilterName();
			}

			@Override
			public ServletContext getServletContext() {
				return filterConfig.getServletContext();
			}

			@Override
			public String getInitParameter(String name) {
				return filterConfig.getInitParameter(name);
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				return filterConfig.getInitParameterNames();
			}
		});
	}

	private void init(Config servletConfig) throws ServletException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Sothis: initialization started");
		}
		try {
			BeanFactory beanFactory = null;
			Configuration config;
			servletContext = servletConfig.getServletContext();

			String beanFactoryClass = servletConfig.getInitParameter("beanFactoryClass");
			if (null != beanFactoryClass) {
				beanFactory = createBeanFactory((Class<BeanFactory>) Class.forName(beanFactoryClass));
			}
			String configBeanName = servletConfig.getInitParameter("configBeanName");
			if (null != configBeanName) {
				if (null == beanFactory) {
					throw new ServletException("'beanFactoryClass' param must be configured since 'configBeanName' is used.");
				}
				PropertiesBean propertiesBean = beanFactory.getBean(configBeanName);
				config = new Configuration(propertiesBean.getProperties());
			} else {
				String configLocation = servletConfig.getInitParameter("configLocation");
				if (null == configLocation) {
					configLocation = "sothis.properties";
				}
				InputStream input = Configuration.class.getClassLoader().getResourceAsStream(configLocation);
				if (null == input) {
					if (LOGGER.isWarnEnabled()) {
						LOGGER.warn("can not find sothi config file : {}, using sothis.default.properties as default.",
								configLocation);
					}
					input = Configuration.class.getClassLoader().getResourceAsStream("sothis.default.properties");
				}
				Properties properties = new Properties();
				properties.load(new InputStreamReader(input, "UTF-8"));
				input.close();
				config = new Configuration(properties);
			}
			if (null == beanFactory) {
				if (null == config.getBeanFactoryClass()) {
					throw new ConfigurationException("sothis.beanFactory.class is not set !");
				}
				beanFactory = createBeanFactory(config.getBeanFactoryClass());
			}

			applicationContext = new DefaultApplicationContext(servletContext.getContextPath(), beanFactory, config,
					servletContext);

			registerBeans(config, beanFactory);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Sothis: initialization completed");
			}
		} catch (Exception e) {
			throw new ServletException("sothis init failed: ", e);
		}

	}

	private void registerBeans(Configuration config, BeanFactory beanFactory) {
		beanFactory.registerBean(config.getFlash().getName(), config.getFlash());
	}

	private BeanFactory createBeanFactory(Class<BeanFactory> beanFactoryClass) throws InstantiationException,
			IllegalAccessException {
		BeanFactory beanFactory = beanFactoryClass.newInstance();
		if (beanFactory instanceof ServletBeanFactory) {
			((ServletBeanFactory) beanFactory).init(servletContext);
		}
		return beanFactory;
	}

	public boolean execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			ActionContext context = ActionContext.getContext();
			Request request = new ServletHttpRequest((HttpServletRequest) req);
			Response response = new ServletHttpResponse((HttpServletResponse) resp, ((HttpServletRequest) req).getProtocol());
			context.setRequest(request);
			context.setResponse(response);
			context.setApplicationContext(applicationContext);
			Flash flash = context.getFlash(false);
			if (null != flash) {
				flash.flash();
			}
			return ActionInvocationHelper.invoke(context, applicationContext, request, response) || resp.isCommitted();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private static interface Config {
		public String getName();

		public ServletContext getServletContext();

		public String getInitParameter(String name);

		public Enumeration<String> getInitParameterNames();
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}
