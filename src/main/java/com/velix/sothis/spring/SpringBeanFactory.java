package com.velix.sothis.spring;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.velix.sothis.BeanFactory;

public class SpringBeanFactory implements BeanFactory {

	private WebApplicationContext appContext;

	@Override
	public void init(ServletContext servletContext) {
		appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<T> beanClass) throws Exception {
		String simpleName = StringUtils.uncapitalize(beanClass.getSimpleName());
		Object bean = null;
		if (appContext.containsBean(simpleName)) {
			bean = appContext.getBean(simpleName);
		} else if (appContext.containsBean(beanClass.getName())) {
			bean = appContext.getBean(beanClass.getName());
		}
		if (null == bean) {
			throw new NoSuchBeanDefinitionException(beanClass.getName());
		} else {
			return (T) bean;
		}
	}

}
