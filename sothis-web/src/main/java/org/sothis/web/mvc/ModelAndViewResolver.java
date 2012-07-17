package org.sothis.web.mvc;

import org.sothis.core.beans.BeanFactory;

/**
 * model和view的解析器<br>
 * 根据当前的action执行结果，解析出对应的model和view
 * 
 * @author velna
 * 
 */
public interface ModelAndViewResolver {

	/**
	 * 缺省view的类型
	 */
	static final String DEFAULT_VIEW_TYPE = "org.sothis.web.mvc.view.DEFAULT_VIEW_TYPE";

	/**
	 * 解析model和view，view必须使用{@link BeanFactory#getBean(Class)}方法创建
	 * 
	 * @param actionResult
	 * @param invocation
	 * @return
	 * @throws ViewCreationException
	 *             beanFactory初始化异常、没有注册view类型、
	 *             没有设置defaultView且actionResult为null时，抛出该异常
	 */
	ResolvedModelAndView resolve(Object actionResult, ActionInvocation invocation) throws ViewCreationException;

	/**
	 * 根据typeName得到对应的view实例
	 * 
	 * @param typeName
	 * @return
	 */
	View getView(String typeName, ActionInvocation invocation) throws ViewCreationException;
}
