package com.velix.sothis;

import javax.servlet.ServletContext;

public interface BeanFactory {
	void init(ServletContext servletContext);

	<T> T getBean(Class<T> beanClass) throws Exception;
}
