package org.sothis.core.beans;

import java.util.concurrent.locks.ReentrantLock;

import org.sothis.core.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public abstract class AbstractSpringBeanFactory implements BeanFactory {

	private final ReentrantLock beanRegisterLock = new ReentrantLock();

	protected abstract org.springframework.beans.factory.BeanFactory getBeanFactory();

	protected abstract BeanDefinitionRegistry getBeanDefinitionRegistry();

	public <T> T getBean(Class<T> beanClass) throws BeanInstantiationException {
		return getBean(beanClass.getName());
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) throws BeanInstantiationException {
		try {
			return (T) this.getBeanFactory().getBean(beanName);
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
			if (StringUtils.isNotEmpty(aBean.destroyMethod())) {
				definition.setDestroyMethodName(aBean.destroyMethod());
			}
		} else {
			definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
			definition.setScope(AbstractBeanDefinition.SCOPE_SINGLETON);
		}
		return definition;
	}

	@Override
	public void registerBean(String beanName, Class<?> beanClass) {
		try {
			beanRegisterLock.lock();
			try {
				BeanDefinitionRegistry beanDefinitionRegistry = getBeanDefinitionRegistry();
				if (!beanDefinitionRegistry.containsBeanDefinition(beanName)) {
					BeanDefinition definition = createBeanDefinition(beanClass);
					beanDefinitionRegistry.registerBeanDefinition(beanName, definition);
				}
			} finally {
				beanRegisterLock.unlock();
			}
		} catch (Exception e) {
			throw new BeanInstantiationException(e);
		}
	}

}
