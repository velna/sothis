package com.velix.sothis;

import javax.servlet.ServletContext;

public interface BeanFactory {
	void init(ServletContext servletContext);

	Object getBean(Class<?> beanClass) throws Exception;
}
