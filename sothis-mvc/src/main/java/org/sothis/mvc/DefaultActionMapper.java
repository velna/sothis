package org.sothis.mvc;

import org.sothis.core.beans.Autowire;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;

/**
 * ActionMapper接口的默认实现<br>
 * 可选参数：<br>
 * uriStyle: underlined|default
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON, autowire = Autowire.NO)
public class DefaultActionMapper implements ActionMapper {

	private String uriStyle;

	@Override
	public String map(ApplicationContext appContext, Action action) {
		if ("underlined".equals(uriStyle)) {
			return org.sothis.core.util.StringUtils.underlined(action.getFullName());
		} else {
			return action.getFullName();
		}
	}

	@Override
	public Action resolve(ActionContext context) {
		Request request = context.getRequest();
		String key = request.getUriPath().substring(context.getApplicationContext().getContextPath().length());
		if (!key.startsWith("/")) {
			key = "/" + key;
		}
		if (key.endsWith("/")) {
			key = key + "index";
		}
		Action action = context.getApplicationContext().getAction(key);
		return action;
	}

	public void setUriStyle(String uriStyle) {
		this.uriStyle = uriStyle;
	}

}
