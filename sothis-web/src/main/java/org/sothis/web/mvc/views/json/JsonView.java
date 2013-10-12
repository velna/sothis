package org.sothis.web.mvc.views.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.core.util.MapUtils;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.ModelAndView;
import org.sothis.mvc.View;
import org.sothis.mvc.ViewRenderException;
import org.sothis.web.mvc.WebActionContext;

@Bean(scope = Scope.SINGLETON)
public class JsonView implements View {
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private final static PropertyFilter IGNORE_NULL_VALUE_FILTER = new PropertyFilter() {
		public boolean apply(Object source, String name, Object value) {
			return null == value;
		}
	};

	private final static JsonValueProcessor DATE_VALUE_PROCESSOR = new JsonValueProcessor() {
		public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
			if (value == null) {
				return "";
			} else if (value instanceof Date)
				return DATE_FORMAT.format((Date) value);
			else {
				return value.toString();
			}
		}

		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
			return null;
		}
	};

	private final static JsonConfig DEFAULT_JSON_CONFIG = new JsonConfig();
	static {
		DEFAULT_JSON_CONFIG.setJsonPropertyFilter(IGNORE_NULL_VALUE_FILTER);
		DEFAULT_JSON_CONFIG.registerJsonValueProcessor(Date.class, DATE_VALUE_PROCESSOR);
	}

	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ViewRenderException {
		Object model = mav.model();
		Map<String, Object> params = mav.viewParams();
		JSON json;
		JsonConfig config = (JsonConfig) MapUtils.getObject(params, "config");
		if (null == config) {
			config = DEFAULT_JSON_CONFIG;
		}
		if (null != model && (model instanceof Enum || JSONUtils.isArray(model))) {
			json = JSONArray.fromObject(model, config);
		} else {
			json = JSONObject.fromObject(model, config);
		}
		WebActionContext context = (WebActionContext) invocation.getActionContext();
		HttpServletResponse response = context.getResponse();
		response.setContentType(MapUtils.getString(params, "contentType", "text/plain;charset=" + context.getConfiguration().getCharacterEncoding()));
		json.write(response.getWriter());
	}
}
