package org.sothis.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.sothis.core.beans.Autowire;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;

/**
 * ActionMapper接口的默认实现
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON, autowire = Autowire.NO)
public class DefaultActionMapper implements ActionMapper {

	public String map(String controllerPackageName, Class<?> controllerClass, String actionName) {

		if (StringUtils.isBlank(controllerPackageName))
			throw new IllegalArgumentException("empty or null controller package name is not allowed: " + controllerPackageName);

		if (StringUtils.isBlank(actionName))
			throw new IllegalArgumentException("empty or null action name is not allowed: " + actionName);

		if (null == controllerClass)
			throw new IllegalArgumentException("null controller class is not allowed.");

		String packageName = controllerClass.getPackage().getName();
		if (packageName.indexOf(controllerPackageName) != 0 || packageName.length() < controllerPackageName.length())
			throw new IllegalArgumentException(controllerClass + " is not under the package: " + controllerPackageName);

		String controllerPackage = packageName.substring(controllerPackageName.length()).replaceAll("\\.", "/");

		String simpleName = controllerClass.getSimpleName();
		if (!simpleName.endsWith("Controller"))
			throw new IllegalArgumentException("controller class should end with 'Controller'");

		String controllerName = StringUtils.uncapitalize(simpleName.substring(0, simpleName.length() - "Controller".length()));

		StringBuilder ret = new StringBuilder();
		ret.append(controllerPackage);
		if (StringUtils.isNotEmpty(controllerName)) {
			ret.append('/').append(controllerName);
		}
		ret.append('/').append(actionName);
		return ret.toString();
	}

	public String resolve(HttpServletRequest request, HttpServletResponse response) {
		if (null == request || null == response)
			throw new IllegalArgumentException("null request or response.");
		
		String uri = null;
		if (null != request.getAttribute(ActionContext.ACTION_URI)) {
			uri = request.getAttribute(ActionContext.ACTION_URI).toString();
		} else {
			uri = request.getRequestURI().substring(request.getContextPath().length());
		}
		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		if (uri.endsWith("/")) {
			uri = uri + "index";
		}
		return uri;
	}

}
