package org.sothis.web.mvc;

import java.util.Map;


public interface View {
	void render(Object model, ActionInvocation invocation) throws Exception;

	/**
	 * 设置 参数，params可能为null
	 * 
	 * @param params
	 * @throws IllegalArgumentException
	 */
	void setParams(Map<String, Object> params);
}
