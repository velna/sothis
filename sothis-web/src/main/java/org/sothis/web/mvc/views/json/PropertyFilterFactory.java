package org.sothis.web.mvc.views.json;

import java.util.Map;

import net.sf.json.util.PropertyFilter;

public class PropertyFilterFactory {

	private PropertyFilterFactory() {
	}

	public final static PropertyFilter IGNORE_NULL_VALUE_FILTER = new PropertyFilter() {
		public boolean apply(Object source, String name, Object value) {
			return null == value;
		}
	};

	public static PropertyFilter newIncludeFilter(final Map<Class<?>, String[]> includeMap) {
		return new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				if (includeMap.containsKey(source.getClass())) {
					String[] includeNames = includeMap.get(source.getClass());
					for (String includeName : includeNames) {
						if (name.equals(includeName)) {
							return false;
						}
						return true;
					}
				}
				return false;
			}
		};
	}
}
