package org.sothis.web.mvc;

import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;

public class MockBeanFactory implements BeanFactory {

	public <T> T getBean(Class<T> beanClass) throws BeanInstantiationException {
		try {
			return beanClass.newInstance();
		} catch (Exception e) {
			throw new BeanInstantiationException(e);
		}
	}

	public <T> T getBean(String beanName) throws BeanInstantiationException {
		throw new AbstractMethodError("getBean(String beanName)");
	}

}
