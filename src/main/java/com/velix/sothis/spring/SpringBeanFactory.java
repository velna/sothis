package com.velix.sothis.spring;

import javax.servlet.ServletContext;

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
		return (T) appContext.getBean(beanClass.getName());
	}

}
