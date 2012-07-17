package org.sothis.web.mvc;

import java.io.IOException;

import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;

public class SothisFactory {
	public static ActionContext getActionContext() throws ConfigurationException, IOException {
		ActionContext context = ActionContext.getContext();
		SothisConfig.initConfig("sothis.default.properties");
		context.set(ActionContext.SOTHIS_CONFIG, SothisConfig.getConfig());
		context.setBeanFactory(getBeanFactory());
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
		};
	}
}
