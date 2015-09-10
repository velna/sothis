package org.sothis.mvc;

import java.io.IOException;

/**
 * view接口，所有的view都需要实现这个接口
 * 
 * @author velna
 * 
 */
public interface View {

	/**
	 * 缺省view的类型
	 */
	static final String DEFAULT_VIEW_TYPE = "org.sothis.mvc.views.DEFAULT_VIEW_TYPE";

	/**
	 * null view的类型，该类型的view不执行任何操作。
	 */
	static final String NULL_VIEW_TYPE = "org.sothis.mvc.views.NULL_VIEW_TYPE";

	/**
	 * 使用当前的view渲染请求结果
	 * 
	 * @param mav
	 * @param invocation
	 *            当前的action调用
	 * @throws IOException
	 * @throws ViewRenderException
	 */
	void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException;
}
