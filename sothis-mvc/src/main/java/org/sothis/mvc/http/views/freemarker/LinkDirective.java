package org.sothis.mvc.http.views.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.sothis.core.util.UrlUtils;
import org.sothis.mvc.Action;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.Configuration;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;

public class LinkDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		Map<String, Object> directiveParams = new HashMap<String, Object>(params);
		ActionContext context = ActionContext.getContext();
		Action action = context.getAction();
		Configuration config = context.getApplicationContext().getConfiguration();

		String actionName = MapUtils.getString(directiveParams, "action", action.getName());

		String controllerName = MapUtils.getString(directiveParams, "controller", action.getController().getName());
		String packageName = MapUtils.getString(directiveParams, "package", action.getController().getPackageName());

		String anchor = MapUtils.getString(directiveParams, "anchor");
		boolean absolute = MapUtils.getBoolean(directiveParams, "absolute", false);
		String base = MapUtils.getString(directiveParams, "base");
		boolean noTag = Boolean
				.valueOf(String.valueOf(DeepUnwrap.unwrap((TemplateModel) directiveParams.get("notag"))));

		StringBuilder url = new StringBuilder();
		if (base == null && absolute) {
			base = config.get("sothis.serverURL", "");
		}
		if (base != null) {
			url.append(base);
		}
		url.append(context.getApplicationContext().getContextPath());
		if (StringUtils.isNotEmpty(packageName)) {
			url.append('/').append(packageName);
		}
		if (StringUtils.isNotEmpty(controllerName)) {
			url.append('/').append(controllerName);
		}
		if (StringUtils.isNotEmpty(actionName)) {
			url.append('/').append(actionName);
		}

		TemplateModel linkParamsModel = (TemplateModel) directiveParams.get("params");
		if (linkParamsModel instanceof TemplateHashModelEx) {
			Map<String, Object> linkParams = (Map<String, Object>) DeepUnwrap.unwrap(linkParamsModel);
			UrlUtils.appendParams(url, linkParams, context.getRequest().getCharset().name());
		} else if (null != linkParamsModel) {
			Object linkParams = DeepUnwrap.unwrap(linkParamsModel);
			url.append('?').append(String.valueOf(linkParams));
		}

		if (StringUtils.isNotEmpty(anchor)) {
			url.append('#').append(anchor);
		}
		// String urlString = context.getResponse().encodeURL(url.toString());
		String urlString = url.toString();

		directiveParams.remove("action");
		directiveParams.remove("controller");
		directiveParams.remove("package");
		directiveParams.remove("params");
		directiveParams.remove("anchor");
		directiveParams.remove("absolute");
		directiveParams.remove("base");
		directiveParams.remove("notag");

		Writer out = env.getOut();
		if (noTag) {
			out.write(urlString);
		} else {
			out.write("<a href=\"");
			out.write(urlString);
			out.write('"');
			for (Map.Entry<String, Object> entry : directiveParams.entrySet()) {
				out.write(' ');
				out.write(entry.getKey());
				out.write("=\"");
				out.write(String.valueOf(entry.getValue()));
				out.write('"');
			}
			out.write('>');
			body.render(out);
			out.write("</a>");
		}
	}
}
