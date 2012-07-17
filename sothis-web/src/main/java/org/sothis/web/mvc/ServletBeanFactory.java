package org.sothis.web.mvc;

import javax.servlet.ServletContext;

import org.sothis.core.beans.BeanFactory;

/**
 * 基本servlet的BeanFactory<br>
 * 实现了这个接口的BeanFactory，在beanFactory本身被实例化以后，会马上调用{@link #init(ServletContext)}
 * 方法来初始化这个beanFactory
 * 
 * @author velna
 * 
 */
public interface ServletBeanFactory extends BeanFactory {
	/**
	 * 初始化beanFactory
	 * 
	 * @param servletContext
	 *            当前的ServletContext
	 */
	void init(ServletContext servletContext);
}
