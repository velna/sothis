package com.velix.sothis.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.velix.sothis.ActionContext;
import com.velix.sothis.ActionInvocation;

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
