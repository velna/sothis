package org.sothis.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocation;


public class FileUploadInterceptor implements Interceptor {

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = invocation.getInvocationContext();
		HttpServletRequest request = context.getRequest();
		if (ServletFileUpload.isMultipartContent(request)) {
			context.setRequest(new MultipartHttpServletRequest(request,
					invocation));
		}
		return invocation.invoke();
	}

}
