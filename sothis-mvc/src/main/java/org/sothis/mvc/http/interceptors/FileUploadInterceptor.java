package org.sothis.mvc.http.interceptors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.Attachment;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.Interceptor;
import org.sothis.mvc.Request;

public class FileUploadInterceptor implements Interceptor {

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		Request request = invocation.getActionContext().getRequest();
		Attachments attachments = request.attachments();
		if (!attachments.isEmpty()) {
			Map<String, Object[]> params = new HashMap<>(
					invocation.getActionContext().getRequest().parameters().toMap());
			for (Map.Entry<String, Collection<Attachment>> entry : attachments) {
				Collection<Attachment> parts = entry.getValue();
				for (Attachment part : parts) {
					if (part.getContentType() != null) {
						append(params, entry.getKey() + "FileName", part.getFilename());
						append(params, entry.getKey() + "InputStream", part.getInputStream());
						append(params, entry.getKey() + "ContentType", part.getContentType());
					} else {
						append(params, entry.getKey(), part.getString(request.getCharset()));
					}
				}
			}
			invocation.getActionContext().setRequestParameters(params);
		}
		return invocation.invoke();
	}

	private void append(Map<String, Object[]> map, String key, Object value) {
		map.put(key, ArrayUtils.add(map.get(key), value));
	}
}
