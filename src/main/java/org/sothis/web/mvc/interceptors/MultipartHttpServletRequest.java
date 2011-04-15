package org.sothis.web.mvc.interceptors;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.ArrayUtils;
import org.sothis.web.mvc.ActionInvocation;


public class MultipartHttpServletRequest extends HttpServletRequestWrapper {

	private final Map<String, String[]> paramMap = new HashMap<String, String[]>();
	private final Map<String, Object[]> allParamMap = new HashMap<String, Object[]>();

	@SuppressWarnings("unchecked")
	public MultipartHttpServletRequest(HttpServletRequest request,
			ActionInvocation invocation) throws Exception {
		super(request);
		if (!ServletFileUpload.isMultipartContent(request)) {
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<DiskFileItem> items = upload.parseRequest(request);
		String encoding = request.getCharacterEncoding();
		for (Iterator<DiskFileItem> i = items.iterator(); i.hasNext();) {
			DiskFileItem item = i.next();
			String fieldName = item.getFieldName();
			if (item.isFormField()) {
				if (paramMap.containsKey(fieldName)) {
					paramMap.put(fieldName, (String[]) ArrayUtils.add(
							paramMap.get(fieldName), item.getString(encoding)));
				} else {
					paramMap.put(fieldName,
							new String[] { item.getString(encoding) });
				}
			} else {
				File file = null;
				if (item.isInMemory()) {
					file = File.createTempFile("sothis_upload_", ".tmp");
					item.write(file);
				} else {
					file = item.getStoreLocation();
				}
				append(allParamMap, fieldName, file);
				append(allParamMap, fieldName + "FileName", item.getName());
				append(allParamMap, fieldName + "InputStream",
						item.getInputStream());
				append(allParamMap, fieldName + "ContentType",
						item.getContentType());
			}
		}
		allParamMap.putAll(paramMap);
	}

	private void append(Map<String, Object[]> map, String key, Object value) {
		if (map.containsKey(key)) {
			map.put(key, ArrayUtils.add(map.get(key), value));
		} else {
			Object array = Array.newInstance(Object.class, 1);
			Array.set(array, 0, value);
			map.put(key, (Object[]) array);
		}
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> ret = new HashMap<String, String[]>(
				paramMap.size());
		for (String name : paramMap.keySet()) {
			ret.put(name, paramMap.get(name).clone());
		}
		return Collections.unmodifiableMap(ret);
	}

	@Override
	public String getParameter(String name) {
		if (paramMap.containsKey(name)) {
			return paramMap.get(name)[0];
		} else {
			return null;
		}
	}

	public Map<String, Object[]> getAllParameterMap() {
		Map<String, Object[]> ret = new HashMap<String, Object[]>(
				allParamMap.size());
		for (String name : allParamMap.keySet()) {
			ret.put(name, allParamMap.get(name).clone());
		}
		return Collections.unmodifiableMap(ret);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return new ParamEnumeration(this.paramMap.keySet().iterator());
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.paramMap.get(name).clone();
	}

	private class ParamEnumeration implements Enumeration<String> {
		private Iterator<String> i;

		public ParamEnumeration(Iterator<String> i) {
			this.i = i;
		}

		@Override
		public boolean hasMoreElements() {
			return i.hasNext();
		}

		@Override
		public String nextElement() {
			return i.next();
		}

	}

}
