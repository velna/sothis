package org.sothis.web.mvc;

import java.io.IOException;
import java.util.ArrayList;
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

public class DefaultControllerTest {

	@Test(dataProvider = "test")
	public void test(String actionName, String expectedActionName) throws ConfigurationException, IOException {

		SothisFactory.getActionContext();
		TestController testController = new TestController();
		String packageName = "org.sothis.web.mvc";
		Controller controller = new DefaultController(packageName, "test", testController.getClass());

		// 接口中的方法
		Assert.assertEquals(controller.getActions().size(), 7);
		Assert.assertEquals(controller.getAction(actionName).getName(), expectedActionName);
		Assert.assertEquals(controller.getControllerClass(), testController.getClass());
		Assert.assertEquals(controller.getPackageName(), packageName);
		Assert.assertEquals(controller.getName(), "test");

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
			// empty
		}

		public void testPrimitiveParamAction(boolean paramBl, byte paramB, int paramI, long paramL, float paramF, double paramD,
				char paramCh, short paramS) {
			// empty
		}

		public void testPrimitiveLikeParamAction(Boolean name, Integer i, String s, Date d) {
			// empty
		}

		public void testEnumParamAction(TestEnum e) {
			// empty
		}

		public void testMapParamAction(Map<String, Object[]> m) {
			// empty
		}

		public void testBeanParamAction(ParamModel model) {
			// empty
		}

		public void testParamAnnotationAction(@Param(name = "username") String username, @Param(name = "password") String password) {
			// empty
		}
	}
}
