package org.sothis.web.mvc;

import java.io.IOException;

import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.DefaultApplicationContext;

public class SothisFactory {
	public static WebActionContext initActionContext() throws ConfigurationException, IOException, BeanInstantiationException,
			ClassNotFoundException {
		WebActionContext context = WebActionContext.getContext();
		WebConfiguration config = WebConfiguration.create("sothis.default.properties");
		context.put(WebActionContext.APPLICATION_CONTEXT, new DefaultApplicationContext(getBeanFactory(), config));
		return context;
	}

	public static BeanFactory getBeanFactory() {
		return new BeanFactory() {

			@Override
			public <T> T getBean(Class<T> beanClass) throws BeanInstantiationException {
				try {
					return beanClass.newInstance();
				} catch (InstantiationException e) {
					throw new BeanInstantiationException(e);
				} catch (IllegalAccessException e) {
					throw new BeanInstantiationException(e);
				}
			}

			@Override
			public <T> T getBean(String beanName) throws BeanInstantiationException {
				return null;
			}

			@Override
			public void registerBean(String beanName, Class<?> beanClass) {

			}
		};
	}
}
