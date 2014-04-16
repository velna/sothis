package org.sothis.core.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.sothis.core.util.StringUtils;

public class SimpleBeanFactory implements BeanFactory {
	private final Map<String, Object> beans = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<T> beanClass) throws BeanInstantiationException {
		String simpleName = StringUtils.uncapitalize(beanClass.getSimpleName());
		Object bean = null;
		if (beans.containsKey(simpleName)) {
			bean = beans.get(simpleName);
		} else if (beans.containsKey(beanClass.getName())) {
			bean = beans.get(beanClass.getName());
		}
		if (null == bean) {
			try {
				bean = beanClass.newInstance();
			} catch (InstantiationException e) {
				throw new BeanInstantiationException(e);
			} catch (IllegalAccessException e) {
				throw new BeanInstantiationException(e);
			}
			Bean aBean = beanClass.getAnnotation(Bean.class);
			if (null != aBean) {
				try {
					if (StringUtils.isNotEmpty(aBean.initMethod())) {
						beanClass.getMethod(aBean.initMethod(), (Class<?>[]) null).invoke(bean, (Object[]) null);
					}
					if (StringUtils.isNotEmpty(aBean.destroyMethod())) {
						beanClass.getMethod(aBean.destroyMethod(), (Class<?>[]) null).invoke(bean, (Object[]) null);
					}
				} catch (IllegalAccessException e) {
					throw new BeanInstantiationException(e);
				} catch (IllegalArgumentException e) {
					throw new BeanInstantiationException(e);
				} catch (InvocationTargetException e) {
					throw new BeanInstantiationException(e);
				} catch (NoSuchMethodException e) {
					throw new BeanInstantiationException(e);
				} catch (SecurityException e) {
					throw new BeanInstantiationException(e);
				}
				if (aBean.scope() == Scope.SINGLETON) {
					beans.put(simpleName, bean);
				}
			}
		}
		return (T) bean;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) throws BeanInstantiationException {
		return (T) beans.get(beanName);
	}

	@Override
	public void registerBean(String beanName, Class<?> beanClass) {
		throw new UnsupportedOperationException();
	}

}
