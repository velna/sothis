package org.sothis.web.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DefaultActionMapperTest {

	@Test(dataProvider = "mapErr", expectedExceptions = { IllegalArgumentException.class })
	public void mapErr(Action action, Object expected) {

		DefaultActionMapper dam = new DefaultActionMapper();
		Assert.assertEquals(dam.map(action), expected);

	}

	@Test(dataProvider = "mapOk")
	public void mapOk(Action action, Object expected) {

		DefaultActionMapper dam = new DefaultActionMapper();
		Assert.assertEquals(dam.map(action), expected);

	}

	@Test(dataProvider = "resolveErr", expectedExceptions = { IllegalArgumentException.class }, expectedExceptionsMessageRegExp = "null request or response.")
	public void resolveErr(MockHttpServletRequest request, MockHttpServletResponse response, ActionStore store, Action expected) {

		DefaultActionMapper dam = new DefaultActionMapper();
		Assert.assertEquals(dam.resolve(request, response, store), expected);

	}

	@Test(dataProvider = "resolveOk")
	public void resolveOk(MockHttpServletRequest request, MockHttpServletResponse response, ActionStore store, Action expected) {

		DefaultActionMapper dam = new DefaultActionMapper();
		Assert.assertEquals(dam.resolve(request, response, store), expected);

	}

	@DataProvider(name = "mapErr")
	public Object[][] mapErrDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		// 1个入参为null
		dataList.add(new Object[] { null, MockController.class, "list", null });

		dataList.add(new Object[] { "org.sothis.web.mvc", null, "list", null });

		dataList.add(new Object[] { "org.sothis.web.mvc", MockController.class, null, null });

		// 1个入参为null，其他入参有空串
		dataList.add(new Object[] { null, MockController.class, "", null });
		dataList.add(new Object[] { null, MockController.class, " ", null });

		dataList.add(new Object[] { "", null, "list", null });
		dataList.add(new Object[] { " ", null, "list", null });

		dataList.add(new Object[] { "org.sothis.web.mvc", null, "", null });
		dataList.add(new Object[] { "org.sothis.web.mvc", null, " ", null });

		dataList.add(new Object[] { "", null, "", null });
		dataList.add(new Object[] { " ", null, "", null });
		dataList.add(new Object[] { "", null, " ", null });
		dataList.add(new Object[] { " ", null, " ", null });

		dataList.add(new Object[] { "", MockController.class, null, null });
		dataList.add(new Object[] { " ", MockController.class, null, null });

		// 2个入参为null
		dataList.add(new Object[] { "org.sothis.web.mvc", null, null, null });

		dataList.add(new Object[] { null, MockController.class, null, null });

		dataList.add(new Object[] { null, null, "list", null });

		// 2个入参为null，其他入参有空串
		dataList.add(new Object[] { "", null, null, null });
		dataList.add(new Object[] { " ", null, null, null });

		dataList.add(new Object[] { null, null, "", null });
		dataList.add(new Object[] { null, null, " ", null });

		// 3个入参为null
		dataList.add(new Object[] { null, null, null, null });

		dataList.add(new Object[] { "org.sothis.web.mvcXXXXXXX", MockController.class, "list", null });
		dataList.add(new Object[] { "XXXXorg.sothis.web.mvc", MockController.class, "list", null });
		dataList.add(new Object[] { "org.sothis.dal.mvc", MockController.class, "list", null });
		dataList.add(new Object[] { "org.sothis.web.mvc", DefaultActionMapperTest.class, "list", null });

		Object[][] ret = new Object[dataList.size()][4];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "mapOk")
	public Object[][] mapOkDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { "org.sothis.web.mvc", MockController.class, "list", "/mock/list" });

		Object[][] ret = new Object[dataList.size()][4];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "resolveErr")
	public Object[][] resolveErrDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		dataList.add(new Object[] { null, null, null });

		dataList.add(new Object[] { request, null, null });

		dataList.add(new Object[] { null, response, null });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "resolveOk")
	public Object[][] resolveOkDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		dataList.add(new Object[] { request, response, "/index" });

		request = new MockHttpServletRequest();
		request.setContextPath("/fangjia-coresite");
		request.setRequestURI("/fangjia-coresite");
		dataList.add(new Object[] { request, response, "/index" });

		request = new MockHttpServletRequest();
		request.setContextPath("/fangjia-coresite");
		request.setRequestURI("/fangjia-coresite/");
		dataList.add(new Object[] { request, response, "/index" });

		request = new MockHttpServletRequest();
		request.setContextPath("/fangjia-coresite");
		request.setRequestURI("/fangjia-coresite/ershoufang");
		dataList.add(new Object[] { request, response, "/ershoufang" });

		request = new MockHttpServletRequest();
		request.setContextPath("/fangjia-coresite");
		request.setRequestURI("/fangjia-coresite/ershoufang/");
		dataList.add(new Object[] { request, response, "/ershoufang/index" });

		request = new MockHttpServletRequest();
		request.setContextPath("/fangjia-coresite");
		request.setRequestURI("/fangjia-coresite/ershoufang/list");
		dataList.add(new Object[] { request, response, "/ershoufang/list" });

		request = new MockHttpServletRequest();
		request.setContextPath("/fangjia-coresite");
		request.setRequestURI("/fangjia-coresite/ershoufang/list?a=1");
		dataList.add(new Object[] { request, response, "/ershoufang/list?a=1" });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}

}
