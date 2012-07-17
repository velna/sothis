package org.sothis.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 测试MapUtils类
 * 
 * @author liupei
 */
public class MapUtilsTest {
	/**
	 * 测试MapUtils.containsKey方法
	 * 
	 * @param map
	 * @param key
	 * @param expected
	 */
	@Test(dataProvider = "containsKey")
	public void testContainsKey(Map<?, ?> map, Object key, boolean expected) {
		boolean actual = MapUtils.containsKey(map, key);
		Assert.assertEquals(actual, expected);
	}

	/**
	 * 测试MapUtils.getKeys方法正常情况
	 * 
	 * @param map
	 * @param expected
	 */
	@Test(dataProvider = "getKeys")
	public void testGetKeys(Map<String, Object> map, Set<String> expected) {
		Set<String> actual = MapUtils.getKeys(map, new HashSet<String>());
		Assert.assertEquals(actual, expected);
	}

	/**
	 * 测试MapUtils.getKeys方法有异常情况
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGetKeysWithException() {
		MapUtils.getKeys(null, null);
	}

	@DataProvider(name = "getKeys")
	public Object[][] getKeysDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Set<String> keys1 = new HashSet<String>();
		int keyIndex = 1;

		map1.put("key" + keyIndex, keyIndex);
		keys1.add("key" + keyIndex);
		keyIndex++;
		dataList.add(new Object[] { map1, keys1 });

		Map<String, Object> map2 = new HashMap<String, Object>(map1);
		Set<String> keys2 = new HashSet<String>(keys1);
		Map<String, Object> mapMap = new HashMap<String, Object>();
		for (int i = 0; i < 5; i++) {
			mapMap.put("key" + keyIndex, keyIndex);
			keys2.add("key" + keyIndex);
			keyIndex++;
		}
		map2.put("key" + keyIndex, mapMap);
		keys2.add("key" + keyIndex);
		keyIndex ++;
		dataList.add(new Object[] { map2, keys2 });
		
		Map<String, Object> map3 = new HashMap<String, Object>(map2);
		Set<String> keys3 = new HashSet<String>(keys2);
		@SuppressWarnings("unchecked")
		Map<String, Object>[] arrayMap = new HashMap[5];
		for(int i = 0; i < 5; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("key" + keyIndex, keyIndex);
			arrayMap[i] = m;
			keys3.add("key" + keyIndex);
			keyIndex++;
		}
		map3.put("key" + keyIndex, arrayMap);
		keys3.add("key" + keyIndex);
		keyIndex ++;
		dataList.add(new Object[] { map3, keys3 });
		
		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "containsKey", parallel = true)
	public Object[][] containsKeyDataProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { null, null, false });

		dataList.add(new Object[] { null, "null", false });

		dataList.add(new Object[] { new HashMap<Object, Object>(), null, false });

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(null, "abc");
		dataList.add(new Object[] { map, null, true });
		dataList.add(new Object[] { map, "abc", false });
		dataList.add(new Object[] { map, "abcd", false });

		map = new HashMap<Object, Object>();
		map.put("null", "abc");
		dataList.add(new Object[] { map, null, false });
		dataList.add(new Object[] { map, "null", true });
		dataList.add(new Object[] { map, "abcd", false });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}
}
