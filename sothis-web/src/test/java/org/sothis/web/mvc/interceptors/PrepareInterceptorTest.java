package org.sothis.web.mvc.interceptors;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.Assert;

import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocationException;
import org.sothis.web.mvc.ConfigurationException;
import org.sothis.web.mvc.Controller;
import org.sothis.web.mvc.DefaultController;
import org.sothis.web.mvc.MockActionInvocation;
import org.sothis.web.mvc.MockBeanFactory;
import org.sothis.web.mvc.SothisConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 测试PrepareInterceptor
 * 
 * @author liupei
 */
public class PrepareInterceptorTest {

	private ActionContext context = null;

	@BeforeMethod
	public void beforeMethod() throws ConfigurationException, IOException {
		context = ActionContext.getContext();
		SothisConfig.initConfig("sothis.default.properties");
		context.set(ActionContext.SOTHIS_CONFIG, SothisConfig.getConfig());
	}

	@AfterMethod
	public void afterMethod() {
		context.setContextMap(new HashMap<String, Object>());
	}

	/**
	 * PrepareInterceptor实例
	 */
	private PrepareInterceptor pi;

	@BeforeClass
	public void beforeClass() {
		pi = new PrepareInterceptor();
	}

	/**
	 * 测试实现Preparable的Controller 经过PrepareInterceptor拦截器
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIntercept() throws Exception {
		MockBeanFactory factory = new MockBeanFactory();
		MockActionInvocation invocation = new MockActionInvocation(context);

		Controller controller = new DefaultController("", "", TestController.class);

		invocation.setAction(controller.getAction(""));

		Object controllerInstance = factory.getBean(controller.getControllerClass());
		invocation.setControllerInstance(controllerInstance);

		this.pi = new PrepareInterceptor();

		this.pi.intercept(invocation);

		TestController testController = (TestController) invocation.getControllerInstance();

		// 认为会调用TestController.prepare()方法
		Assert.assertEquals(testController.a, true);
	}

	/**
	 * 测试未实现Preparable的Controller 经过PrepareInterceptor拦截器
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIntercept1() throws Exception {
		MockBeanFactory factory = new MockBeanFactory();
		MockActionInvocation invocation = new MockActionInvocation(context);

		Controller controller = new DefaultController("", "", TestController1.class);

		invocation.setAction(controller.getAction(""));

		Object controllerInstance = factory.getBean(controller.getControllerClass());
		invocation.setControllerInstance(controllerInstance);

		PrepareInterceptor pi = new PrepareInterceptor();

		pi.intercept(invocation);

		TestController1 testController = (TestController1) invocation.getControllerInstance();

		// 认为不会调用TestController.prepare()方法
		Assert.assertEquals(testController.a, false);
	}

	/**
	 * 测试实现Preparable的Controller，但抛出ActionInvocationException异常
	 * 经过PrepareInterceptor拦截器
	 * 
	 * @throws Exception
	 */
	@Test(expectedExceptions = ActionInvocationException.class)
	public void testIntercept2() throws Exception {
		MockBeanFactory factory = new MockBeanFactory();
		MockActionInvocation invocation = new MockActionInvocation(context);

		Controller controller = new DefaultController("", "", TestController2.class);

		invocation.setAction(controller.getAction(""));

		Object controllerInstance = factory.getBean(controller.getControllerClass());
		invocation.setControllerInstance(controllerInstance);

		PrepareInterceptor pi = new PrepareInterceptor();

		pi.intercept(invocation);
	}

	/**
	 * 实现Preparable的Controller
	 * 
	 * @author liupei
	 */
	public static class TestController implements Preparable {

		private boolean a = false;

		public void prepare() throws Exception {
			a = true;
		}
	}

	/**
	 * 未实现Preparable的Controller
	 * 
	 * @author liupei
	 */
	public static class TestController1 {

		private boolean a = false;

		public void prepare() throws Exception {
			a = true;
		}
	}

	/**
	 * 实现Preparable的Controller，但抛出异常ActionInvocationException
	 * 
	 * @author liupei
	 */
	public static class TestController2 implements Preparable {

		public void prepare() throws Exception {
			throw new UnsupportedOperationException("throw ActionInvocationException");
		}
	}
}
