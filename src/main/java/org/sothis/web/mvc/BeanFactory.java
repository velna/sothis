package org.sothis.web.mvc;

import javax.servlet.ServletContext;

public interface BeanFactory {
	void init(ServletContext servletContext);

	<T> T getBean(Class<T> beanClass) throws Exception;

	<T> void registerBean(Class<T> beanClass, BeanDefinition beanDefinition);
}
