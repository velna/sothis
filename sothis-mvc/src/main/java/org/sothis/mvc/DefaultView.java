package org.sothis.mvc;

import java.io.IOException;

public class DefaultView implements View {

	@Override
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		if (null != mav.model()) {
			invocation.getActionContext().getResponse().getOutputStream().write(String.valueOf(mav.model()).getBytes());
		}
	}

}
