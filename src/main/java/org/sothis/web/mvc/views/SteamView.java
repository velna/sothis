package org.sothis.web.mvc.views;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.sothis.web.mvc.ActionInvocation;

public class SteamView extends AbstractView {

	@Override
	public void render(Object model, ActionInvocation invocation)
			throws Exception {
		InputStream input = null;
		if (model instanceof StreamModel) {
			input = ((StreamModel) model).getInputStream();
		} else if (model instanceof InputStream) {
			input = (InputStream) model;
		} else {
			input = getParam("inputStream");
		}
		HttpServletResponse response = invocation.getActionContext()
				.getResponse();
		response.setContentType((String) getParam("contentType"));
		if (null != input) {
			IOUtils.copy(input, response.getOutputStream());
		}
	}

}
