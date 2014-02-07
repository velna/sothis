/**
 * copyright (c) by fangjia 2011
 */
package org.sothis.core.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.sothis.core.config.PropertiesConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * <p>
 * 测试PropertiesConfiguration
 * </p>
 * 
 * @author：liupei
 * @date：2011-6-17
 */
public class PropertiesConfigurationTest {
	private PropertiesConfiguration pc;

	/**
	 * <p>
	 * 预定义属性及初始化PropertiesConfiguration
	 * </p>
	 */
	@BeforeClass
	public void beforeClass() {
		Properties properties = new Properties();

		properties.setProperty("org.sothis.core.util.classutils.class", "org.sothis.core.util.ClassUtils");
		properties.setProperty("org.sothis.core.maputils.class", "org.sothis.core.MapUtils");
		properties.setProperty("sothis.interceptors.stack.default", "upload,params");
		properties.setProperty("org.sothis.core.util.testboolean.boolean", "true");
		properties.setProperty("org.sothis.core.util.testint.integer", "123");
		properties.setProperty("org.sothis.core.util.testlong.long", "123456789");
		properties.setProperty("org.sothis.core.util.testfloat.float", "123.456");
		properties.setProperty("org.sothis.core.util.testdouble.double", "123.456789");
		String file = Thread.currentThread().getContextClassLoader()
				.getResource(PropertiesConfigurationTest.class.getName().replace(".", "/") + ".class").toString().substring(6);
		properties.setProperty("org.sothis.core.util.testfile.file", file);
		properties.setProperty("org.sothis.core.util.testurl.url", "http://sh.fangjia.com");
		properties.setProperty("org.sothis.core.util.testturl.url", "http://bj.fangjia.com");

		properties.setProperty("sothis.views.default", "jsp");

		this.pc = new PropertiesConfiguration(properties);
	}

	/**
	 * <p>
	 * 正常情况下的测试
	 * </p>
	 * 
	 * @param pattern
	 * @param valueClass
	 * @param expected
	 * @throws Exception
	 */
	@Test(dataProvider = "getAsGroup")
	public void testGetAsGroup(Pattern pattern, Class<?> valueClass, Map<String, ?> expected) throws Exception {
		Map<String, ?> actual = this.pc.getAsGroup(pattern, valueClass);

		Assert.assertEquals(actual, expected);
	}

	/**
	 * <p>
	 * 测试抛出IllegalArgumentException异常
	 * </p>
	 * 
	 * @throws Exception
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetAsGroupWithIllegalArgumentException() throws Exception {
		this.pc.getAsGroup(Pattern.compile("sothis\\.interceptors\\.stack\\.(\\w+)"), PropertiesConfigurationTest.class);
	}

	@Test(expectedExceptions = ClassNotFoundException.class)
	public void testGetAsGroupWithClassNotFoundException() throws Exception {
		this.pc.getAsGroup(Pattern.compile("org\\.sothis\\.core\\.(\\w+)\\.class"), Class.class);
	}

	/**
	 * <p>
	 * 提供各种正常情况的参数
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	@DataProvider(name = "getAsGroup")
	public Object[][] getAsGroupDataProvider() throws Exception {
		Map<String, Object> expected = new HashMap<String, Object>();

		List<Object[]> paramList = new ArrayList<Object[]>();

		// String.class
		expected.put("default", "upload,params");
		paramList.add(new Object[] { Pattern.compile("sothis\\.interceptors\\.stack\\.(\\w+)"), String.class, expected });

		// Class.class
		expected = new HashMap<String, Object>();
		expected.put("classutils", ClassUtils.class);
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.class"), Class.class, expected });

		// Boolean.class
		expected = new HashMap<String, Object>();
		expected.put("testboolean", new Boolean(true));
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.boolean"), Boolean.class, expected });

		// Integer.class
		expected = new HashMap<String, Object>();
		expected.put("testint", new Integer(123));
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.integer"), Integer.class, expected });

		// Long.class
		expected = new HashMap<String, Object>();
		expected.put("testlong", new Long(123456789));
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.long"), Long.class, expected });

		// Float.class
		expected = new HashMap<String, Object>();
		expected.put("testfloat", new Float(123.456));
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.float"), Float.class, expected });

		// Double.class
		expected = new HashMap<String, Object>();
		expected.put("testdouble", new Double(123.456789));
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.double"), Double.class, expected });

		// File.class
		expected = new HashMap<String, Object>();
		expected.put(
				"testfile",
				new File(Thread.currentThread().getContextClassLoader()
						.getResource(PropertiesConfigurationTest.class.getName().replace(".", "/") + ".class").toString()
						.substring(6)));
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.file"), File.class, expected });

		// URL.class
		expected = new HashMap<String, Object>();
		expected.put("testurl", new URL("http://sh.fangjia.com"));
		expected.put("testturl", new URL("http://bj.fangjia.com"));
		paramList.add(new Object[] { Pattern.compile("org\\.sothis\\.core\\.util\\.(\\w+)\\.url"), URL.class, expected });

		Object[][] params = new Object[paramList.size()][3];

		params = paramList.toArray(params);

		return params;
	}
}
