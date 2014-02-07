package org.sothis.web.mvc.views.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sothis.core.util.MapUtils;
import org.sothis.core.util.UrlUtils;
import org.sothis.mvc.Action;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.WebConfiguration;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.utility.DeepUnwrap;

/**
 * 用于生成a标签，或url<br>
 * 可选参数：<br>
 * action: 用于生成链接的action名称，默认为当前action的名称<br>
 * controller: 用于生成链接的controller名称，默认为当前controller的名称<br>
 * package: 用于生成链接的pacakge名称，默认为当前package的名称<br>
 * params: 用于生成链接的参数，可以是一个freemaker hash，也可以是一个字符串<br>
 * anchor: 用于生成链接的锚点<br>
 * absolute:
 * 如果为true，则将sothis.serverURL参数的值做为前缀加到链接上，如果sothis.serverURL没有指定则使用http
 * ://localhost:${port}<br>
 * base: 设置用于做为链接前缀的字符串，设置这个值后会忽略absolute值<br>
 * notag: 只生成链接，不包括a标签本身
 * 
 * @author velna
 * 
 */
public class LinkDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Map<String, Object> directiveParams = new HashMap<String, Object>(params);
		WebActionContext context = WebActionContext.getContext();
		Action action = context.getAction();
		WebConfiguration config = context.getConfiguration();

		String actionName = MapUtils.getString(directiveParams, "action", action.getName());

		String controllerName = MapUtils.getString(directiveParams, "controller", action.getController().getName());
		String packageName = MapUtils.getString(directiveParams, "package", action.getController().getPackageName());

		String anchor = MapUtils.getString(directiveParams, "anchor");
		boolean absolute = MapUtils.getBoolean(directiveParams, "absolute", false);
		String base = MapUtils.getString(directiveParams, "base");
		boolean noTag = Boolean.valueOf(String.valueOf(DeepUnwrap.unwrap((TemplateModel) directiveParams.get("notag"))));

		StringBuilder url = new StringBuilder();
		if (base == null && absolute) {
			int serverPort = context.getRequest().getServerPort();
			base = config.get("sothis.serverURL", "http://localhost" + (serverPort == 80 ? "" : (":" + serverPort)));
		}
		if (base != null) {
			url.append(base);
		}
		url.append(context.getServletContext().getContextPath());
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
			UrlUtils.appendParams(url, linkParams, config.getCharacterEncoding());
		} else if (null != linkParamsModel) {
			Object linkParams = DeepUnwrap.unwrap(linkParamsModel);
			url.append('?').append(String.valueOf(linkParams));
		}

		if (StringUtils.isNotEmpty(anchor)) {
			url.append('#').append(anchor);
		}
		String urlString = context.getResponse().encodeURL(url.toString());

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
