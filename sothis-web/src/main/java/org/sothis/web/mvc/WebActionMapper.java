package org.sothis.web.mvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.sothis.core.beans.Autowire;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.mvc.Action;
import org.sothis.mvc.ActionMapper;
import org.sothis.mvc.ApplicationContext;
import org.sothis.web.mvc.annotation.Method;

/**
 * ActionMapper接口的默认实现
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON, autowire = Autowire.NO)
public class WebActionMapper implements ActionMapper {

	@Override
	public String map(Action action) {
		return action.getFullName();
	}

	@Override
	public Action resolve(Object req, Object resp, ApplicationContext store) {
		if (null == req || null == resp)
			throw new IllegalArgumentException("null request or response.");

		HttpServletRequest request = (HttpServletRequest) req;
		String key = null;
		if (null != request.getAttribute(WebActionContext.ACTION_URI)) {
			key = request.getAttribute(WebActionContext.ACTION_URI).toString();
		} else {
			key = request.getRequestURI().substring(request.getContextPath().length());
		}
		if (!key.startsWith("/")) {
			key = "/" + key;
		}
		if (key.endsWith("/")) {
			key = key + "index";
		}
		Action action = store.getAction(key);
		if (null != action) {
			Method[] ms = action.getAnnotation(Method.class);
			String method = null;
			for (Method m : ms) {
				if (null != m.value() && StringUtils.isNotEmpty(m.value())) {
					method = m.value();
					break;
				}
			}
			if (null != method && !method.equalsIgnoreCase(request.getMethod())) {
				action = null;
			}
		}
		return action;
	}

}
