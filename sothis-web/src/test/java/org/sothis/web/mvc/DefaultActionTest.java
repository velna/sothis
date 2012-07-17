package org.sothis.web.mvc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sothis.web.mvc.annotation.Param;
import org.sothis.web.mvc.interceptors.ParametersInterceptorTest.ParamModel;
import org.springframework.beans.BeanUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DefaultActionTest {

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

	@Test(dataProvider = "test")
	public void test(String methodName, Class<?>[] clazz, String expectedName, String expectedActionName) {

		Controller controller = new EmptyController("empty");
		Method method = null;
		DefaultAction defaultAction = null;
		if (methodName != null) {
			if (null == clazz) {
				method = BeanUtils.findMethod(TestController.class, methodName);
			} else {
				method = BeanUtils.findMethod(TestController.class, methodName, clazz);
			}
		}

		try {

			defaultAction = new DefaultAction(method, controller);
			// 接口中方法的测试
			Assert.assertEquals(defaultAction.getName(), expectedName);
			Assert.assertEquals(defaultAction.getActionMethod().getName(), expectedActionName);
			Assert.assertEquals(defaultAction.getController(), controller);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("fail!", e);
		}
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testWithNullMethod() {
		new DefaultAction(null, new EmptyController("empty"));
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testWithNullController() {
		new DefaultAction(BeanUtils.findMethod(TestController.class, "test"), null);
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testWithInvalidActionMethod() {
		new DefaultAction(BeanUtils.findMethod(TestController.class, "test"), new EmptyController("empty"));
	}

	@DataProvider(name = "test")
	public Object[][] dataProvider() {

		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { "testPrimitiveParamAction", new Class[] { Boolean.TYPE, Byte.TYPE }, "testPrimitiveParam",
				"testPrimitiveParamAction" });
		dataList.add(new Object[] { "testMapParamAction", new Class[] { Map.class }, "testMapParam", "testMapParamAction" });
		dataList.add(new Object[] { "testParamAnnotationAction", new Class[] { String.class, String.class },
				"testParamAnnotation", "testParamAnnotationAction" });
		dataList.add(new Object[] { "testBeanParamAction", new Class[] { ParamModel.class }, "testBeanParam",
				"testBeanParamAction" });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}

	public static class TestController {
		public void test() {
		}

		public void test1234455() {
		}

		public void testAction() {
		}

		public void testPrimitiveParamAction(boolean bl, byte b) {
		}

		public void testPrimitiveLikeParamAction(Boolean name, Integer i, String s, Date d) {
		}

		public void testMapParamAction(Map<String, Object[]> m) {
		}

		public void testBeanParamAction(ParamModel model) {
		}

		public void testParamAnnotationAction(@Param(name = "username") String username, @Param(name = "password") String password) {
		}

	}
}
