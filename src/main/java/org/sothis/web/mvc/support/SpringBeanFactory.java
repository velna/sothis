package org.sothis.web.mvc.support;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringBeanFactory implements BeanFactory {
	private WebApplicationContext appContext;
	private BeanDefinitionRegistry beanDefinitionRegistry;

	@Override
	public void init(ServletContext servletContext) {
		appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		beanDefinitionRegistry = (BeanDefinitionRegistry) appContext
				.getAutowireCapableBeanFactory();

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<T> beanClass) throws Exception {
		String simpleName = StringUtils.uncapitalize(beanClass.getSimpleName());
		Object bean = null;
		if (appContext.containsBean(simpleName)) {
			bean = appContext.getBean(simpleName);
		} else if (appContext.containsBean(beanClass.getName())) {
			bean = appContext.getBean(beanClass.getName());
		}
		if (null == bean) {
			if (!beanDefinitionRegistry.containsBeanDefinition(beanClass
					.getName())) {
				GenericBeanDefinition definition = new GenericBeanDefinition();
				definition.setBeanClass(beanClass);
				definition
						.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
				definition.setScope(BeanDefinition.SCOPE_SINGLETON);
				beanDefinitionRegistry.registerBeanDefinition(
						beanClass.getName(), definition);
				bean = appContext.getBean(beanClass.getName());
			}
		}
		return (T) bean;
	}

}
