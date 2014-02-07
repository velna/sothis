package org.sothis.web.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.beans.BeanFactory;
import org.sothis.core.config.PropertiesBean;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.ApplicationContext;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.DefaultApplicationContext;

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
public class SothisFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SothisFilter.class);

	private ApplicationContext applicationContext;
	private ServletContext servletContext;

	@SuppressWarnings("unchecked")
	public void init(final FilterConfig filterConfig) throws ServletException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Sothis: initialization started");
		}
		try {
			BeanFactory beanFactory = null;
			WebConfiguration config;
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
				config = WebConfiguration.create(propertiesBean.getProperties());
			} else {
				String configLocation = filterConfig.getInitParameter("configLocation");
				if (null == configLocation) {
					configLocation = "sothis.properties";
				}
				InputStream input = WebConfiguration.class.getClassLoader().getResourceAsStream(configLocation);
				if (null == input) {
					if (LOGGER.isWarnEnabled()) {
						LOGGER.warn("can not find sothi config file : {}, using sothis.default.properties as default.", configLocation);
					}
					input = WebConfiguration.class.getClassLoader().getResourceAsStream("sothis.default.properties");
				}
				Properties properties = new Properties();
				properties.load(new InputStreamReader(input, "UTF-8"));
				input.close();
				config = WebConfiguration.create(properties);
			}
			if (null == beanFactory) {
				if (null == config.getBeanFactoryClass()) {
					throw new ConfigurationException("sothis.beanFactory.class is not set !");
				}
				beanFactory = createBeanFactory(config.getBeanFactoryClass());
			}

			applicationContext = new DefaultApplicationContext(beanFactory, config);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Sothis: initialization completed");
			}
		} catch (Exception e) {
			throw new ServletException("sothis init failed: ", e);
		}
	}

	private BeanFactory createBeanFactory(Class<BeanFactory> beanFactoryClass) throws InstantiationException, IllegalAccessException {
		BeanFactory beanFactory = beanFactoryClass.newInstance();
		if (beanFactory instanceof ServletBeanFactory) {
			((ServletBeanFactory) beanFactory).init(servletContext);
		}
		return beanFactory;
	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		WebActionContext context = WebActionContext.getContext();
		try {
			BeanFactory beanFactory = applicationContext.getBeanFactory();
			WebConfiguration config = (WebConfiguration) applicationContext.getConfiguration();

			context.put(WebActionContext.ACTION_MAPPER, beanFactory.getBean(config.getActionMapper()));
			context.put(WebActionContext.MODEL_AND_VIEW_RESOLVER, beanFactory.getBean(config.getModelAndViewResolver()));
			context.put(WebActionContext.APPLICATION_CONTEXT, applicationContext);
			context.setRequest(req);
			context.setResponse((HttpServletResponse) resp);
			context.setServletContext(servletContext);
			context.setParameters(new HashMap<String, Object[]>(req.getParameterMap()));

			Flash flash = context.getFlash(false);
			if (null != flash) {
				flash.flash();
			}
			ActionInvocationHelper.invoke(context);
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			context.clear();
		}
		chain.doFilter(req, resp);
	}

	public void destroy() {

	}

}
