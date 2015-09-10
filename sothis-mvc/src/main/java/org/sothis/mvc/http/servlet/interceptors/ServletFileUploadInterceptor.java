package org.sothis.mvc.http.servlet.interceptors;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.Interceptor;
import org.sothis.mvc.http.servlet.Servlet31Part;
import org.sothis.mvc.interceptors.param.ParametersInterceptor;

public class ServletFileUploadInterceptor implements Interceptor {

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		Attachments attachments = invocation.getActionContext().getRequest().attachments();
		if (!attachments.isEmpty()) {
			Map<String, Object[]> params = new HashMap<>(invocation.getActionContext().getRequest().parameters().toMap());
			for (Map.Entry<String, Object> entry : attachments) {
				Servlet31Part part = (Servlet31Part) entry.getValue();
				if (part.getContentType() != null) {
					append(params, entry.getKey(), part.getSubmittedFileName());
					append(params, entry.getKey() + "InputStream", part.getInputStream());
					append(params, entry.getKey() + "ContentType", part.getContentType());
				}
			}
			invocation.getActionContext().put(ParametersInterceptor.PARAMETER_KEY, params);
		}
		return invocation.invoke();
	}

	private void append(Map<String, Object[]> map, String key, Object value) {
		if (map.containsKey(key)) {
			map.put(key, ArrayUtils.add(map.get(key), value));
		} else {
			Object array = Array.newInstance(Object.class, 1);
			Array.set(array, 0, value);
			map.put(key, (Object[]) array);
		}
	}
}
