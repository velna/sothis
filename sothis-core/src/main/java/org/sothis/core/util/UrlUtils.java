package org.sothis.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * url工具类
 * </p>
 */
public final class UrlUtils {
	private UrlUtils() {
	}

	/**
	 * 判断一个url字符串是否返回200的响应状态码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isUrlOK(String str) {
		URL url;
		try {
			url = new URL(str);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
				return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * <p>
	 * 将提供的url与params参数按照默认(UTF-8)编码格式，组装成一个新的url
	 * </p>
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String appendParams(String url, Map<?, ?> params) throws UnsupportedEncodingException {
		return appendParams(url, params, "UTF-8");
	}

	/**
	 * <p>
	 * 将提供的url与params参数按照charset编码格式，组装成一个新的url
	 * </p>
	 * 
	 * <p>
	 * url:/test/testAction, params:{testLong=123} =
	 * /test/testAction?testLong=123<br>
	 * 
	 * url:/test/testAction, params:{testString=房价网} =
	 * /test/testAction?testString=%E6%88%BF%E4%BB%B7%E7%BD%91<br>
	 * 
	 * url:/test/testAction, params:{testList=[1, 2, 3]} =
	 * /test/testAction?testList=1&testList=2&testList=3<br>
	 * 
	 * url:/test/testAction, params:{testArray=[Ljava.lang.String;@1ad77a7} =
	 * /test/testAction?testArray=a&testArray=b&testArray=c<br>
	 * 
	 * url:/test/testAction, params:null = /test/testAction<br>
	 * </p>
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static StringBuilder appendParams(StringBuilder url, Map<?, ?> params, String charset)
			throws UnsupportedEncodingException {

		if (null == params || params.isEmpty()) {
			return url;
		}
		if (url.indexOf("?") < 0) {
			url.append('?');
		}

		for (Iterator<?> i = params.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			String paramName = String.valueOf(key);
			if (StringUtils.isEmpty(paramName)) {
				continue;
			}
			Object paramValue = params.get(key);
			if (null == paramValue) {
				url.append(paramName).append("=");
			} else if (paramValue instanceof Collection) {
				Collection<?> c = (Collection<?>) paramValue;
				for (Iterator<?> ii = c.iterator(); ii.hasNext();) {
					url.append(paramName).append("=");
					Object v = ii.next();
					if (null != v) {
						url.append(URLEncoder.encode(String.valueOf(v), charset));
					}
					if (ii.hasNext()) {
						url.append('&');
					}
				}
			} else if (paramValue.getClass().isArray()) {
				int length = Array.getLength(paramValue);
				for (int j = 0; j < length; j++) {
					url.append(paramName).append("=");
					Object v = Array.get(paramValue, j);
					if (null != v) {
						url.append(URLEncoder.encode(String.valueOf(v), charset));
					}
					if (j < length - 1) {
						url.append('&');
					}
				}
			} else {
				url.append(paramName).append("=").append(URLEncoder.encode(String.valueOf(paramValue), charset));
			}
			if (i.hasNext()) {
				url.append('&');
			}
		}
		return url;

	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String appendParams(String url, Map<?, ?> params, String charset) throws UnsupportedEncodingException {
		return appendParams(new StringBuilder(url), params, charset).toString();
	}

	/**
	 * 把文件名encode成utf-8格式
	 * 如输入的文件名为：/res/district_pics/外景图/admin18/comm/art/000/000/198/31375_s.jpg
	 * 
	 * @param fileName
	 * @return
	 */
	public static String encodePath(String fileName, boolean withRoot) {
		fileName = fileName.replaceAll("\\\\", "/");
		StringBuilder ret = new StringBuilder();
		String[] fileNames = fileName.split("/");
		for (int index = 0; index < fileNames.length; index++) {
			if (!fileNames[index].equals("")) {
				if (withRoot || index != 0) {
					ret.append("/");
				}
				try {
					ret.append(URLEncoder.encode(fileNames[index], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return ret.toString();
	}
}
