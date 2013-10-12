package org.sothis.mvc;

/**
 * 用于映射和解析action<br>
 * 
 * @author velna
 * 
 */
public interface ActionMapper {

	/**
	 * 根据action生成action的唯一ID
	 * 
	 * @param action
	 *            需要映射的action
	 * 
	 * @return 返回action的唯一ID，返回null将导致NullPointerException
	 * @throws IllegalArgumentException
	 *             参数为null或空字符串时，抛出该异常
	 */
	Object map(Action action);

	/**
	 * 根据请求从actionStore中找到对应的action
	 * 
	 * @param request
	 * @param response
	 * @param store
	 * @return 返回null则表示无法解析，将导致404，除非response已经被commit
	 * @throws NullPointerException
	 *             request或response为null时，抛出该异常
	 */
	Action resolve(Object request, Object response, ApplicationContext store);
}
