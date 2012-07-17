package org.sothis.web.mvc;

/**
 * Action调用的封装，包括了interceptors的执行和action的执行<br/>
 * ActionInvocation目前不会经过BeanFactory实例化，而只是使用了DefaultActionInvocation类
 * 
 * @author velna
 * 
 */
public interface ActionInvocation {
	/**
	 * 得到当前Action对象
	 * 
	 * @return
	 */
	Action getAction();

	/**
	 * 得到当前的Action上下文
	 * 
	 * @return
	 */
	ActionContext getActionContext();

	/**
	 * 调用下一个interceptor，如果interceptor全部执行完毕，则调用下一个Action
	 * 
	 * @return
	 * @throws ActionInvocationException
	 */
	Object invoke() throws ActionInvocationException;

	/**
	 * 得到当前的controller实例
	 * 
	 * @return
	 */
	Object getControllerInstance();
}
