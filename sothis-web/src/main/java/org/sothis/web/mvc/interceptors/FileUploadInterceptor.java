package org.sothis.web.mvc.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.ActionInvocationException;
import org.sothis.web.mvc.Interceptor;

/**
 * 文件上传拦截器
 * 
 * @author velna
 * 
 */
@Bean(scope = Scope.SINGLETON)
public class FileUploadInterceptor implements Interceptor {

	public Object intercept(ActionInvocation invocation) throws ActionInvocationException {
		ActionContext context = invocation.getActionContext();
		HttpServletRequest request = context.getRequest();
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				context.setRequest(new MultipartHttpServletRequest(request));
			} catch (HttpMultipartException e) {
				throw new ActionInvocationException("error process multipart reqeust: ", e);
			}
		}
		return invocation.invoke();
	}

}
