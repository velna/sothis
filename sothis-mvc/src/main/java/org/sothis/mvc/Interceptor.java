package org.sothis.mvc;

/**
 * 拦截器接口
 * 
 * @author velna
 * 
 */
public interface Interceptor {
	/**
	 * 拦截当前的Action调用<br>
	 * 通常实现类需要调用<code>invocation.invoke()</code>
	 * 方法来执行下一个interceptor或action，你也可以选择不执行<code>invocation.invoke()</code>
	 * ，而是返回另一个Object做为本次Action调用的结果
	 * 
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	Object intercept(ActionInvocation invocation) throws Exception;

}
