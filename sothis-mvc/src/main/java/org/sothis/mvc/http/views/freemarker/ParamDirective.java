package org.sothis.mvc.http.views.freemarker;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import org.sothis.mvc.ActionContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ParamDirective implements TemplateDirectiveModel {

	private static final String NAME = "name";
	private static final String VALUE = "value";

	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if (params != null && params.containsKey(NAME) && params.containsKey(VALUE)) {
			String name = params.get(NAME).toString();
			String value = params.get(VALUE).toString();
			env.setCustomAttribute(name,
					URLEncoder.encode(value, ActionContext.getContext().getRequest().getCharset().name()));
		}
	}
}