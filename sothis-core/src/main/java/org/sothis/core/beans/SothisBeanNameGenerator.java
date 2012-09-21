package org.sothis.core.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

public class SothisBeanNameGenerator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		String className = definition.getBeanClassName();
		int i = className.lastIndexOf('.');
		String beanName = className.substring(i + 1);
		if (beanName.endsWith("Impl")) {
			beanName = beanName.substring(0, beanName.length() - 4);
		}
		beanName = Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);
		return beanName;
	}

}
