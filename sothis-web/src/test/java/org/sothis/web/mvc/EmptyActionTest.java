package org.sothis.web.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EmptyActionTest {

	@Test(dataProvider = "test")
	public void test(Object expectedResult, Object[] params) {

		Controller controller = new EmptyController("emptyContorller");
		String actionName = "empty";
		Action emptyAction = new EmptyAction(actionName, controller);
		// 接口中方法
		Assert.assertEquals(emptyAction.getActionMethod(), null);
		Assert.assertEquals(emptyAction.getController(), controller);
		Assert.assertEquals(emptyAction.getName(), actionName);

	}

	@Test(dataProvider = "testWithException", expectedExceptions = { IllegalArgumentException.class })
	public void testWithException(String actionName, Controller controller) {
		new EmptyAction(actionName, controller);
	}

	@DataProvider(name = "testWithException")
	public Object[][] testWithExceptionDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { null, null });
		dataList.add(new Object[] { null, new EmptyController("emptyContorller") });
		dataList.add(new Object[] { "empty", null });

		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "test")
	public Object[][] dataProvider() {

		List<Object[]> dataList = new ArrayList<Object[]>();
		// params is null
		dataList.add(new Object[] { null, null });
		// params 为空
		dataList.add(new Object[] { null, new Object[] { Collections.EMPTY_MAP } });

		dataList.add(new Object[] { null, new Object[] { "1", 1, 3, "1233" } });

		dataList.add(new Object[] { null, new Object[] { "1", 1, 3, new ArrayList<String>() } });

		dataList.add(new Object[] { null,
				new Object[] { "1", 1, 3, new ArrayList<String>(), TestEnum.E1, new HashMap<Object, Object>() } });

		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	public static enum TestEnum {
		E1, E2
	}

	public static class TestController {

		public void testAction() {
		}

	}
}
