package org.sothis.core.test.testng;

import org.sothis.core.beans.AbstractSpringBeanFactory;
import org.sothis.core.beans.BeanInstantiationException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.IObjectFactory2;

public class TestNGObjectFactory extends AbstractSpringBeanFactory implements IObjectFactory2 {

	private static final long serialVersionUID = 8994787145950177968L;
	private final ApplicationContext applicationContext;
	private BeanDefinitionRegistry beanDefinitionRegistry;

	public TestNGObjectFactory() {
		applicationContext = new ClassPathXmlApplicationContext(System.getProperty("sothis.test.springContextFile",
				"classpath:spring-dal-test.xml"));
		beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
	}

	public Object newInstance(Class<?> cls) {
		try {
			return this.getBean(cls);
		} catch (BeanInstantiationException e) {
			throw new RuntimeException("error init bean: " + cls, e);
		}
	}

	@Override
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	protected BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanDefinitionRegistry;
	}

	@Override
	public void registerBean(String beanName, Class<?> beanClass) {

	}
}
