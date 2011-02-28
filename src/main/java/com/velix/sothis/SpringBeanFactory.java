package com.velix.sothis;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringBeanFactory implements BeanFactory {

	private WebApplicationContext appContext;

	@Override
	public void init(ServletContext servletContext) {
		appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
	}

	@Override
	public Object getBean(Class<?> beanClass) throws Exception {
		return appContext.getBean(beanClass.getName());
	}

}
