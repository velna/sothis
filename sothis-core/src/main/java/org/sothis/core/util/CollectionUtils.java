package org.sothis.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class CollectionUtils {

	/**
	 * 对一个列表进行分组
	 * 
	 * @param <K>
	 * @param <V>
	 * @param list
	 * @param closure
	 * @return
	 */
	public static <K, V> Map<K, List<V>> groupBy(Collection<V> list, Closure<K, V> closure) {
		if (null == closure) {
			throw new IllegalArgumentException("closure must not be null!");
		}
		Map<K, List<V>> ret = new HashMap<K, List<V>>();
		if (null != list) {
			for (V v : list) {
				K k = closure.execute(v);
				if (!ret.containsKey(k)) {
					ret.put(k, new ArrayList<V>());
				}
				ret.get(k).add(v);
			}
		}
		return ret;
	}

	/**
	 * 对一个列表进行分组
	 * 
	 * @param <K>
	 * @param <V>
	 * @param list
	 * @param closure
	 * @return
	 */
	public static <K, V> Map<K, V> overrideGroupBy(Collection<V> list, Closure<K, V> closure) {
		if (null == closure) {
			throw new IllegalArgumentException("closure must not be null!");
		}
		Map<K, V> ret = new HashMap<K, V>();
		if (null != list) {
			for (V v : list) {
				K k = closure.execute(v);
				ret.put(k, v);
			}
		}
		return ret;
	}

	/**
	 * 将一个列表转换成另一个列表
	 * 
	 * @param <O>
	 * @param <I>
	 * @param list
	 * @param closure
	 * @return
	 */
	public static <O, I> List<O> collect(Collection<I> list, Closure<O, I> closure) {
		if (null == closure) {
			throw new IllegalArgumentException("closure must not be null!");
		}
		if (null == list) {
			return Collections.emptyList();
		}
		List<O> ret = new ArrayList<O>(list.size());
		for (I i : list) {
			ret.add(closure.execute(i));
		}
		return ret;
	}

	/**
	 * 将一个列表转换成另一个列表。如果closure的execute方法返回null则丢弃当前结果
	 * 
	 * @param <O>
	 * @param <I>
	 * @param list
	 * @param closure
	 * @return
	 */
	public static <O, I> List<O> collectSome(Collection<I> list, Closure<O, I> closure) {
		if (null == closure) {
			throw new IllegalArgumentException("closure must not be null!");
		}
		if (null == list) {
			return Collections.emptyList();
		}
		List<O> ret = new ArrayList<O>(list.size());
		for (I i : list) {
			O o = closure.execute(i);
			if (null != o) {
				ret.add(o);
			}
		}
		return ret;
	}

	public static <O, I> Set<O> collectUnique(Collection<I> list, Closure<O, I> closure) {
		if (null == closure) {
			throw new IllegalArgumentException("closure must not be null!");
		}
		if (null == list) {
			return Collections.emptySet();
		}
		Set<O> ret = new HashSet<O>();
		for (I i : list) {
			ret.add(closure.execute(i));
		}
		return ret;
	}

	public static <V> List<V> subList(List<V> list, int startIndex, int count) {
		if (null == list || startIndex >= list.size() || count <= 0) {
			return Collections.emptyList();
		}
		int endIndex = Math.min(startIndex + count, list.size());
		return list.subList(startIndex, endIndex);
	}

	/**
	 * Merge the given Properties instance into the given Map, copying all
	 * properties (key-value pairs) over.
	 * <p>
	 * Uses <code>Properties.propertyNames()</code> to even catch default
	 * properties linked into the original Properties instance.
	 * 
	 * @param props
	 *            the Properties instance to merge (may be <code>null</code>)
	 * @param map
	 *            the target Map to merge the properties into
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void mergePropertiesIntoMap(Properties props, Map map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.getProperty(key);
				if (value == null) {
					// Potentially a non-String value...
					value = props.get(key);
				}
				map.put(key, value);
			}
		}
	}

}
