package org.sothis.web.mvc.support;

import javax.servlet.ServletContext;

import org.sothis.core.beans.AbstractSpringBeanFactory;
import org.sothis.web.mvc.ServletBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringBeanFactory extends AbstractSpringBeanFactory implements ServletBeanFactory {
	private ApplicationContext appContext;
	private BeanDefinitionRegistry beanDefinitionRegistry;

	public void init(ServletContext servletContext) {
		appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		beanDefinitionRegistry = (BeanDefinitionRegistry) appContext.getAutowireCapableBeanFactory();

	}

	@Override
	protected ApplicationContext getApplicationContext() {
		return appContext;
	}

	@Override
	protected BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanDefinitionRegistry;
	}

}