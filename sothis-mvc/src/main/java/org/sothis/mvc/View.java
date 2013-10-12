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
