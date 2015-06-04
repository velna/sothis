package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

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
import org.sothis.core.beans.BeanFactory;
import org.sothis.core.config.PropertiesBean;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.ApplicationContext;
import org.sothis.mvc.Configuration;
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
			Configuration config;
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
				config = new Configuration(propertiesBean.getProperties());
			} else {
				String configLocation = filterConfig.getInitParameter("configLocation");
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

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		try {
			if (!ActionInvocationHelper.invoke(applicationContext, new ServletHttpRequest((HttpServletRequest) req),
					new ServletHttpResponse((HttpServletResponse) resp, ((HttpServletRequest) req).getProtocol()))
					&& !resp.isCommitted()) {
				chain.doFilter(req, resp);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void destroy() {

	}

}