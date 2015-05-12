package org.sothis.core.beans;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringClasspathBeanFactory extends AbstractSpringBeanFactory {

	private final BeanFactory beanFactory;
	private final BeanDefinitionRegistry beanDefinitionRegistry;
	private final ConfigurableApplicationContext applicationContext;

	public SpringClasspathBeanFactory(String configLocation) {
		super();
		applicationContext = new ClassPathXmlApplicationContext(configLocation);
		beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
		beanFactory = applicationContext.getBeanFactory();
	}

	@Override
	protected BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanDefinitionRegistry;
	}

	@Override
	protected BeanFactory getBeanFactory() {
		return beanFactory;
	}

}
