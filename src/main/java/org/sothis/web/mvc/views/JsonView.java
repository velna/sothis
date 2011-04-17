package org.sothis.web.mvc.views;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.sothis.web.mvc.ActionInvocation;

public class JsonView extends AbstractView {

	@Override
	public void render(Object model, ActionInvocation invocation)
			throws Exception {
		JSON json;
		JsonConfig config = getParam("config");
		if (null == config) {
			config = new JsonConfig();
		}
		if (null != model
				&& (model instanceof Enum || JSONUtils.isArray(model))) {
			json = JSONArray.fromObject(model, config);
		} else {
			json = JSONObject.fromObject(model, config);
		}
		HttpServletResponse response = invocation.getInvocationContext()
				.getResponse();
		response.setContentType(getParam("contentType",
				"text/json;charset=UTF-8"));
		json.write(response.getWriter());
	}

}
