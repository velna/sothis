package org.sothis.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MapUtils extends org.apache.commons.collections.MapUtils {
	public static boolean containsKey(Map<?, ?> map, Object key) {
		if (null == map) {
			return false;
		}
		return map.containsKey(key);
	}

	/**
	 * 获取map中所有的key
	 * 
	 * @param map
	 * @param keys
	 *            map中所有的key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getKeys(Map<String, Object> map, Set<String> keys) {
		if (org.apache.commons.collections.MapUtils.isEmpty(map)) {
			throw new IllegalArgumentException("map can not be null");
		}
		if (CollectionUtils.isEmpty(keys)) {
			keys = new HashSet<String>();
		}
		for (String key : map.keySet()) {
			if (!keys.contains(key)) {
				keys.add(key);
			}
			Object o = map.get(key);
			if (o instanceof Map) {
				getKeys((Map<String, Object>) o, keys);
			} else if (o instanceof List) {
				for (Object obj : (List<Object>) o) {
					if (obj instanceof Map) {
						getKeys((Map<String, Object>) obj, keys);
					}
				}
			} else if (o instanceof Object[]) {
				for (Object obj : (Object[]) o) {
					if (obj instanceof Map) {
						getKeys((Map<String, Object>) obj, keys);
					}
				}
			}
		}
		return keys;
	}

	public static <O, I> Map<O, List<I>> sortValues(Map<O, List<I>> map, Comparator<I> comparator) {
		if (null == comparator) {
			throw new IllegalArgumentException("comparator must not be null!");
		}
		if (null != map) {
			for (Entry<O, List<I>> entry : map.entrySet()) {
				List<I> colls = entry.getValue();
				Collections.sort(colls, comparator);
			}
		}
		return map;
	}

	public static <O, I> Map<O, List<I>> sortKey(Map<O, List<I>> map, Comparator<O> comparator) {
		if (null == comparator) {
			throw new IllegalArgumentException("comparator must not be null!");
		}
		if (null != map) {
			Map<O, List<I>> ret = new LinkedHashMap<O, List<I>>();
			List<O> keys = new ArrayList<O>(map.size());
			for (Entry<O, List<I>> entry : map.entrySet()) {
				keys.add(entry.getKey());
			}
			Collections.sort(keys, comparator);
			for (int i = 0; i < keys.size(); i++) {
				ret.put(keys.get(i), map.get(keys.get(i)));
			}
			return ret;
		}
		return map;
	}

}
