package org.sothis.web.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sothis.web.mvc.annotation.Param;
import org.sothis.web.mvc.interceptors.ParametersInterceptorTest.ParamModel;
import org.sothis.web.mvc.interceptors.ParametersInterceptorTest.TestEnum;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EmptyControllerTest {

	@Test(dataProvider = "test")
	public void test(String actionName, String expectedActionParams) {
		Controller controller = new EmptyController("test");

		// 接口中的方法

		Assert.assertEquals(controller.getAction(actionName).getName(), expectedActionParams);
		Assert.assertEquals(controller.getActions(), Collections.emptyMap());
		Assert.assertEquals(controller.getControllerClass(), controller.getClass());
		Assert.assertEquals(controller.getPackageName(), "");
		Assert.assertEquals(controller.getName(), "test");
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void testWithException() {
		new EmptyController(null);
	}

	@DataProvider(name = "test")
	public Object[][] dataProvider() {

		List<Object[]> dataList = new ArrayList<Object[]>();

		// 空ActionName
		dataList.add(new Object[] { StringUtils.EMPTY, StringUtils.EMPTY });
		// 任意的ActionName
		dataList.add(new Object[] { "noAction", "noAction" });
		// 给定的ActionName
		dataList.add(new Object[] { "testAction", "testAction" });
		// 给定的ActionName
		dataList.add(new Object[] { "testParamAnnotationAction", "testParamAnnotationAction" });

		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	public static class TestController {

		public void testAction() {
		}

		public void testPrimitiveParamAction(boolean bl, byte b, int i, long l, float f, double d, char ch, short s) {
		}

		public void testPrimitiveLikeParamAction(Boolean name, Integer i, String s, Date d) {
		}

		public void testEnumParamAction(TestEnum e) {
		}

		public void testMapParamAction(Map<String, Object[]> m) {
		}

		public void testBeanParamAction(ParamModel model) {
		}

		public void testParamAnnotationAction(@Param(name = "username") String username, @Param(name = "password") String password) {
		}
	}
}
