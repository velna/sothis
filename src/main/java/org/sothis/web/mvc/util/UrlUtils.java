package org.sothis.web.mvc.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class UrlUtils {
	private UrlUtils() {
	}

	public static String appendParams(String url, Map<?, ?> params)
			throws UnsupportedEncodingException {
		return appendParams(url, params, "UTF-8");
	}

	public static String appendParams(String url, Map<?, ?> params,
			String charset) throws UnsupportedEncodingException {
		StringBuilder ret = new StringBuilder(url);
		if (url.indexOf('?') < 0) {
			ret.append('?');
		}
		for (Iterator<?> i = params.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			String paramName = String.valueOf(key);
			if (StringUtils.isEmpty(paramName)) {
				continue;
			}
			Object paramValue = params.get(key);
			if (null == paramValue) {
				ret.append(paramName).append("=");
			} else if (paramValue instanceof Collection) {
				Collection<?> c = (Collection<?>) paramValue;
				for (Iterator<?> ii = c.iterator(); ii.hasNext();) {
					ret.append(paramName).append("=");
					Object v = ii.next();
					if (null != v) {
						ret.append(URLEncoder.encode(String.valueOf(v), "UTF-8"));
					}
					if (ii.hasNext()) {
						ret.append('&');
					}
				}
			} else if (paramValue.getClass().isArray()) {
				int length = Array.getLength(paramValue);
				for (int j = 0; j < length; j++) {
					ret.append(paramName).append("=");
					Object v = Array.get(paramValue, j);
					if (null != v) {
						ret.append(URLEncoder.encode(String.valueOf(v), "UTF-8"));
					}
					if (j < length) {
						ret.append('&');
					}
				}
			} else {
				ret.append(paramName).append("=").append(paramValue);
			}
			if (i.hasNext()) {
				ret.append('&');
			}
		}
		return ret.toString();

	}
}
