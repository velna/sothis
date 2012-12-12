package org.sothis.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sothis.core.beans.AbstractSpringBeanFactory;
import org.sothis.core.beans.Autowire;
import org.sothis.core.beans.Bean;
import org.sothis.core.beans.BeanFactory;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.core.beans.BeanPackageAutoProxyCreator;
import org.sothis.core.beans.SothisBeanNameGenerator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.sothis.core.beans.Scope;

/**
 * ClassUtils测试类
 * 
 * @author liupei
 */
public class ClassUtilsTest {
	/**
	 * 测试getClasses方法正常情况
	 * 
	 * @param path
	 * @param expected
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test(dataProvider = "getClasses")
	public void testGetClasses(String path, Class<?>[] expected) throws ClassNotFoundException, IOException {
		Class<?>[] actual = ClassUtils.getClasses(path);
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testGetClassesWithException() throws ClassNotFoundException, IOException {
		ClassUtils.getClasses("123");
	}

	@DataProvider(name = "getClasses")
	public Object[][] getClassesDataProvider() {
		List<Object[]> paramList = new ArrayList<Object[]>();
		paramList.add(new Object[] {
				"org.sothis.core.beans",
				new Class[] { AbstractSpringBeanFactory.class, Autowire.class, Bean.class, BeanFactory.class,
						BeanInstantiationException.class, BeanPackageAutoProxyCreator.class, Scope.class,
						SothisBeanNameGenerator.class } });
		Object[][] result = new Object[paramList.size()][2];
		result = paramList.toArray(result);

		return result;
	}
}
