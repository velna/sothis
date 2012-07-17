package org.sothis.core.beans;

/**
 * Bean工厂<br>
 * 
 * @author velna
 * 
 */
public interface BeanFactory {
	/**
	 * 根据bean的class来得到bean<br>
	 * bean查找顺序如下：<br>
	 * 1.根据beanClass的simple name查找bean，即{@link Class#getSimpleName()}，simple
	 * name的首字母会先转换成小写<br>
	 * 2.根据完整的class name查找bean，即{@link Class#getName()} <br>
	 * 3.根据{@link Bean}注解的声明注册这个beanClass，如果没有{@link Bean}注解，则使用{@link Bean}
	 * 中的默认值
	 * 
	 * @param <T>
	 * @param beanClass
	 * @return
	 * @throws BeanInstantiationException
	 *             如果在bean创建过程发生任何异常
	 */
	<T> T getBean(Class<T> beanClass) throws BeanInstantiationException;

	/**
	 * 得到name为beanName的bean，不会进行bean注册动作
	 * 
	 * @param <T>
	 * @param beanName
	 * @return 如果未找到则返回null
	 * @throws BeanInstantiationException
	 *             如果在bean创建过程发生任何异常
	 */
	<T> T getBean(String beanName) throws BeanInstantiationException;
}
