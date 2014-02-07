/**
 * copyright (c) by fangjia 2011
 */
package org.sothis.core.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * <p>
 * 测试UrlUtils工具类
 * </p>
 * 
 * @author：liupei
 * @date：2011-6-17
 */
public class UrlUtilsTest {

	/**
	 * <p>
	 * appendParams()方法测试正常情况
	 * </p>
	 * 
	 * @param url
	 * @param params
	 * @param expected
	 * @throws Exception
	 */
	@Test(dataProvider = "appendParams")
	public void testAppendParams(String url, Map<Object, Object> params, String expected) throws Exception {
		String str = UrlUtils.appendParams(url, params, "UTF-8");
		System.out.println("url:" + url + ", params:" + String.valueOf(params) + "     = result:" + str);

		Assert.assertEquals(str, expected);
	}

	/**
	 * <p>
	 * appendParams()方法测试有异常情况
	 * </p>
	 * 
	 * @param url
	 * @param params
	 * @throws Exception
	 */
	@Test(dataProvider = "appendParamsWithException", expectedExceptions = { UnsupportedEncodingException.class })
	public void testAppendParamsWithException(String url, Map<Object, Object> params) throws Exception {
		UrlUtils.appendParams("/test/testAction", params, "UF-8");
	}

	/**
	 * 测试encodePath()方法
	 * 
	 * @param fileName
	 * @param withRoot
	 * @param expected
	 */
	@Test(dataProvider = "encodePath")
	public void testEncodePath(String fileName, boolean withRoot, String expected) {
		String str = UrlUtils.encodePath(fileName, withRoot);
		Assert.assertEquals(str, expected);
	}

	@DataProvider(name = "encodePath")
	public Object[][] encodePathDataProvider() {
		List<Object[]> paramList = new ArrayList<Object[]>();
		paramList.add(new Object[] { "/res/district_pics/外景图/admin18/comm/art/000/000/198/31375_s.jpg", true,
				"/res/district_pics/%E5%A4%96%E6%99%AF%E5%9B%BE/admin18/comm/art/000/000/198/31375_s.jpg" });
		paramList.add(new Object[] { "/res/district_pics/外景图/admin18/comm/art/000/000/198/31375_s.jpg", false,
				"/res/district_pics/%E5%A4%96%E6%99%AF%E5%9B%BE/admin18/comm/art/000/000/198/31375_s.jpg" });
		paramList.add(new Object[] { "res/district_pics/外景图/admin18/comm/art/000/000/198/31375_s.jpg", true,
				"/res/district_pics/%E5%A4%96%E6%99%AF%E5%9B%BE/admin18/comm/art/000/000/198/31375_s.jpg" });
		paramList.add(new Object[] { "res/district_pics/外景图/admin18/comm/art/000/000/198/31375_s.jpg", false,
				"res/district_pics/%E5%A4%96%E6%99%AF%E5%9B%BE/admin18/comm/art/000/000/198/31375_s.jpg" });

		Object[][] params = new Object[paramList.size()][3];

		params = paramList.toArray(params);

		return params;
	}

	@DataProvider(name = "appendParams")
	public Object[][] appendParamsDataProvider() {
		List<Object[]> paramList = new ArrayList<Object[]>();

		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("testLong", new Long(123));
		paramList.add(new Object[] { "/test/testAction", params, "/test/testAction?testLong=123" });

		params = new HashMap<Object, Object>();
		params.put("testString", new String("房价网"));
		paramList.add(new Object[] { "/test/testAction", params, "/test/testAction?testString=%E6%88%BF%E4%BB%B7%E7%BD%91" });

		params = new HashMap<Object, Object>();
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		params.put("testList", list);
		paramList.add(new Object[] { "/test/testAction", params, "/test/testAction?testList=1&testList=2&testList=3" });

		params = new HashMap<Object, Object>();
		String[] strs = new String[] { "a", "b", "c" };
		params.put("testArray", strs);
		paramList.add(new Object[] { "/test/testAction", params, "/test/testAction?testArray=a&testArray=b&testArray=c" });

		paramList.add(new Object[] { "/test/testAction", null, "/test/testAction" });

		Object[][] result = new Object[paramList.size()][3];

		result = paramList.toArray(result);

		return result;
	}

	@DataProvider(name = "appendParamsWithException")
	public Object[][] appendParamsWithUnsupportedEncodingExceptionDataProvider() {
		List<Object[]> paramList = new ArrayList<Object[]>();

		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("testLong", new Long(123));
		paramList.add(new Object[] { "/test/testAction", params });

		params = new HashMap<Object, Object>();
		params.put("testString", new String("房价网"));
		paramList.add(new Object[] { "/test/testAction", params });

		params = new HashMap<Object, Object>();
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		params.put("testList", list);
		paramList.add(new Object[] { "/test/testAction", params });

		params = new HashMap<Object, Object>();
		String[] strs = new String[] { "a", "b", "c" };
		params.put("testArray", strs);
		paramList.add(new Object[] { "/test/testAction", params });

		params = new HashMap<Object, Object>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "1");
		params.put("testMap", map);
		paramList.add(new Object[] { "/test/testAction", params });

		Object[][] result = new Object[paramList.size()][2];

		result = paramList.toArray(result);

		return result;
	}
}
