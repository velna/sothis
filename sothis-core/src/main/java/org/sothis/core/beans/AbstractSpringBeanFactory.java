package org.sothis.core.beans;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;

public abstract class AbstractSpringBeanFactory implements BeanFactory {

	private final ReentrantLock beanRegisterLock = new ReentrantLock();

	protected abstract ApplicationContext getApplicationContext();

	protected abstract BeanDefinitionRegistry getBeanDefinitionRegistry();

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> beanClass) throws BeanInstantiationException {
		try {
			ApplicationContext appContext = this.getApplicationContext();
			BeanDefinitionRegistry beanDefinitionRegistry = getBeanDefinitionRegistry();
			String simpleName = StringUtils.uncapitalize(beanClass.getSimpleName());
			Object bean = null;
			if (appContext.containsBean(simpleName)) {
				bean = doGetBean(simpleName);
			} else if (appContext.containsBean(beanClass.getName())) {
				bean = doGetBean(beanClass.getName());
			}
			if (null == bean) {
				if (!beanDefinitionRegistry.containsBeanDefinition(beanClass.getName())) {
					BeanDefinition definition = createBeanDefinition(beanClass);
					beanRegisterLock.lock();
					try {
						beanDefinitionRegistry.registerBeanDefinition(beanClass.getName(), definition);
					} finally {
						beanRegisterLock.unlock();
					}
					bean = doGetBean(beanClass.getName());
				}
			}
			return (T) bean;
		} catch (Exception e) {
			throw new BeanInstantiationException(e);
		}
	}

	private Object doGetBean(String beanName) {
		return this.getApplicationContext().getBean(beanName);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) throws BeanInstantiationException {
		try {
			return (T) doGetBean(beanName);
		} catch (Exception e) {
			throw new BeanInstantiationException(e);
		}
	}

	private BeanDefinition createBeanDefinition(Class<?> beanClass) {
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(beanClass);
		Bean aBean = beanClass.getAnnotation(Bean.class);
		if (null != aBean) {
			if (aBean.scope() == Scope.PROTOTYPE) {
				definition.setScope(AbstractBeanDefinition.SCOPE_PROTOTYPE);
			} else if (aBean.scope() == Scope.SINGLETON) {
				definition.setScope(AbstractBeanDefinition.SCOPE_SINGLETON);
			} else if (aBean.scope() == Scope.DEFAULT) {
				definition.setScope(AbstractBeanDefinition.SCOPE_DEFAULT);
			}
			if (aBean.autowire() == Autowire.BY_NAME) {
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
			} else if (aBean.autowire() == Autowire.BY_TYPE) {
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			} else if (aBean.autowire() == Autowire.CONSTRUCTOR) {
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
			} else if (aBean.autowire() == Autowire.NO) {
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
			}
			if (StringUtils.isNotEmpty(aBean.initMethod())) {
				definition.setInitMethodName(aBean.initMethod());
			}
		} else {
			definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
			definition.setScope(AbstractBeanDefinition.SCOPE_SINGLETON);
		}
		return definition;
	}

}
