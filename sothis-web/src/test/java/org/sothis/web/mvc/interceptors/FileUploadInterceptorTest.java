package org.sothis.web.mvc.interceptors;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.Assert;

import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.Controller;
import org.sothis.mvc.DefaultController;
import org.sothis.web.mvc.MockActionInvocation;
import org.sothis.web.mvc.MockBeanFactory;
import org.sothis.web.mvc.SothisFactory;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.interceptors.upload.FileUploadInterceptor;
import org.sothis.web.mvc.interceptors.upload.MultipartHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FileUploadInterceptorTest {

	private WebActionContext context = null;

	@BeforeMethod
	public void beforeMethod() throws ConfigurationException, IOException, BeanInstantiationException, ClassNotFoundException {
		context = SothisFactory.initActionContext();
	}

	@AfterMethod
	public void afterMethod() {
		context.setContextMap(new HashMap<String, Object>());
	}

	@Test
	public void testIntercept() throws Exception {
		MockBeanFactory factory = new MockBeanFactory();
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.setMethod("post");
		request.setContentType("multipart/form-data;boundary=aaa");
		request.setContent("aaaefe".getBytes());
		request.addFile(new MockMultipartFile("myFile", "c:\\myFile.txt", "text/xml", "abc".getBytes()));
		MockActionInvocation invocation = new MockActionInvocation(context);

		Controller controller = new DefaultController(context.getConfiguration(), "", "", TestController.class);
		invocation.setAction(controller.getAction("test"));
		Object controllerInstance = factory.getBean(controller.getControllerClass());
		invocation.setControllerInstance(controllerInstance);

		WebActionContext actionContext = WebActionContext.getContext();
		actionContext.setRequest(request);
		invocation.setActionContext(actionContext);
		FileUploadInterceptor interceptor = new FileUploadInterceptor();
		interceptor.intercept(invocation);

		Assert.assertTrue(actionContext.getRequest() instanceof MultipartHttpServletRequest);
	}

	public static class TestController {
		public void testAction() {

		}
	}
}
