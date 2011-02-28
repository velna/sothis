package com.velix.sothis;

import javax.servlet.ServletContext;

public class SimpleBeanFactory implements BeanFactory {

	@Override
	public void init(ServletContext servletContext) {
		// empty
	}

	@Override
	public Object getBean(Class<?> beanClass) throws Exception {
		return beanClass.newInstance();
	}

}
