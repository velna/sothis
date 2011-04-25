package org.sothis.web.mvc.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.Interceptor;


public class FileUploadInterceptor implements Interceptor {

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = invocation.getActionContext();
		HttpServletRequest request = context.getRequest();
		if (ServletFileUpload.isMultipartContent(request)) {
			context.setRequest(new MultipartHttpServletRequest(request,
					invocation));
		}
		return invocation.invoke();
	}

}
