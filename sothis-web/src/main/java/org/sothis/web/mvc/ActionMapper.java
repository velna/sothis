package org.sothis.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于映射和解析action<br>
 * 
 * @author velna
 * 
 */
public interface ActionMapper {
	/**
	 * 根据package名称、controller名称和action名称生成action的唯一ID
	 * 
	 * @param packageName
	 *            并不是完整的包名，是去掉包前缀后剩余的部分
	 * @param controllerName
	 *            去掉后缀Controller后的部分
	 * @param actionName
	 *            去掉Action后缀后的部分
	 * @return 返回action的唯一ID，返回null将导致NullPointerException
	 * @throws IllegalArgumentException
	 *             参数为null或空字符串时，抛出该异常
	 */
	String map(String controllerPackageName, Class<?> controllerClass, String actionName);

	/**
	 * 根据请求生成action的唯一ID
	 * 
	 * @param request
	 * @param response
	 * @return 返回null则表示无法解析，将导致404，除非response已经被commit
	 * @throws NullPointerException
	 *             request或response为null时，抛出该异常
	 */
	String resolve(HttpServletRequest request, HttpServletResponse response);
}
