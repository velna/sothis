package org.sothis.web.mvc.support;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringBeanFactory implements BeanFactory {

	private WebApplicationContext appContext;

	@Override
	public void init(ServletContext servletContext) {
		appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		org.springframework.beans.factory.BeanFactory factory = appContext.getAutowireCapableBeanFactory();
		System.out.println(factory);
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
