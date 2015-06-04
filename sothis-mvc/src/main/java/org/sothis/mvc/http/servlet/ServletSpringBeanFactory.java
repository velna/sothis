package org.sothis.mvc.http.servlet;

import javax.servlet.ServletContext;

import org.sothis.core.beans.AbstractSpringBeanFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServletSpringBeanFactory extends AbstractSpringBeanFactory implements ServletBeanFactory {
	private BeanFactory beanFactory;
	private BeanDefinitionRegistry beanDefinitionRegistry;

	public void init(ServletContext servletContext) {
		ConfigurableApplicationContext appContext = (ConfigurableApplicationContext) WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		beanFactory = appContext.getBeanFactory();
		beanDefinitionRegistry = (BeanDefinitionRegistry) appContext.getAutowireCapableBeanFactory();
	}

	@Override
	protected BeanFactory getBeanFactory() {
		return beanFactory;
	}

	@Override
	protected BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanDefinitionRegistry;
	}

}