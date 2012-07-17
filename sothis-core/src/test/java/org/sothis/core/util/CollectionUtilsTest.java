package org.sothis.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CollectionUtilsTest {

	private final Closure<Integer, Bean> closure = new Closure<Integer, Bean>() {
		public Integer execute(Bean input) {
			return input.getAge();
		}
	};

	@Test(dataProvider = "groupBy")
	public <K, V> void testGroupBy(Collection<V> list, Closure<K, V> closure, Map<K, List<V>> expected) {
		Map<K, List<V>> map = CollectionUtils.groupBy(list, closure);
		Assert.assertEquals(map, expected);
	}

	@Test(dataProvider = "groupByWithException", expectedExceptions = { IllegalArgumentException.class })
	public <K, V> void testGroupByWithException(Collection<V> list, Closure<K, V> closure) {
		CollectionUtils.groupBy(list, closure);
	}

	@DataProvider(name = "groupBy")
	public Object[][] groupByTestDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { null, closure, Collections.emptyMap() });
		Bean tom = new Bean(20);
		Bean jack = new Bean(20);
		Bean aprile = new Bean(25);
		Bean julie = new Bean(26);
		Bean joe = new Bean(25);
		Bean angle = new Bean(20);

		List<Bean> beanList = new ArrayList<Bean>(3);
		beanList.add(tom);
		Map<Integer, List<Bean>> expected = new HashMap<Integer, List<Bean>>();
		expected.put(tom.getAge(), beanList);
		dataList.add(new Object[] { beanList, closure, expected });

		List<Bean> allBeanList = new ArrayList<Bean>(3);
		beanList = new ArrayList<Bean>(3);
		expected = new HashMap<Integer, List<Bean>>();
		beanList.add(tom);
		beanList.add(jack);
		beanList.add(angle);
		allBeanList.addAll(beanList);
		expected.put(20, beanList);
		dataList.add(new Object[] { allBeanList, closure, expected });

		allBeanList = new ArrayList<Bean>(3);
		expected = new HashMap<Integer, List<Bean>>();
		beanList = new ArrayList<Bean>(3);
		beanList.add(tom);
		beanList.add(jack);
		beanList.add(angle);
		allBeanList.addAll(beanList);
		expected.put(20, beanList);
		beanList = new ArrayList<Bean>(2);
		beanList.add(aprile);
		beanList.add(joe);
		allBeanList.addAll(beanList);
		expected.put(25, beanList);
		beanList = new ArrayList<Bean>(1);
		beanList.add(julie);
		allBeanList.addAll(beanList);
		expected.put(26, beanList);
		dataList.add(new Object[] { allBeanList, closure, expected });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "groupByWithException")
	public Object[][] groupByWithExceptionTestDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { null, null });
		dataList.add(new Object[] { Collections.emptyList(), null });

		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	private static class Bean {
		private int age;

		public Bean(int age) {
			this.age = age;
		}

		public int getAge() {
			return age;
		}

	}
}
