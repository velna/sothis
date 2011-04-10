package org.sothis.web.mvc;

import javax.servlet.ServletContext;

public class SimpleBeanFactory implements BeanFactory {

	@Override
	public void init(ServletContext servletContext) {
		// empty
	}

	@Override
	public <T> T getBean(Class<T> beanClass) throws Exception {
		return beanClass.newInstance();
	}

	@Override
	public <T> void registerBean(Class<T> beanClass,
			BeanDefinition beanDefinition) {
		// empty
	}

}
