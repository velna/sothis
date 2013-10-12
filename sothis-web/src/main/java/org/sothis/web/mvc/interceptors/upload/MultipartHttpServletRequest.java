package org.sothis.web.mvc.interceptors.upload;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.ArrayUtils;
import org.sothis.web.mvc.WebActionContext;

public class MultipartHttpServletRequest extends HttpServletRequestWrapper {

	private final Map<String, Object[]> paramMap = new HashMap<String, Object[]>();

	public MultipartHttpServletRequest(HttpServletRequest request) throws HttpMultipartException {
		super(request);
		try {
			init(request);
		} catch (FileUploadException e) {
			throw new HttpMultipartException("error parset http multipart request: ", e);
		} catch (IOException e) {
			throw new HttpMultipartException("error parset http multipart request: ", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void init(HttpServletRequest request) throws FileUploadException, IOException {
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
					paramMap.put(fieldName, (String[]) ArrayUtils.add(paramMap.get(fieldName), item.getString(encoding)));
				} else {
					paramMap.put(fieldName, new String[] { item.getString(encoding) });
				}
			} else {
				File file = null;
				if (item.isInMemory()) {
					file = File.createTempFile("sothis_upload_", ".tmp");
					try {
						item.write(file);
					} catch (Exception e) {
						throw new IOException("error write upload file:" + file, e);
					}
				} else {
					file = item.getStoreLocation();
				}
				append(paramMap, fieldName, file);
				append(paramMap, fieldName + "FileName", item.getName());
				append(paramMap, fieldName + "InputStream", item.getInputStream());
				append(paramMap, fieldName + "ContentType", item.getContentType());
			}
		}
		WebActionContext.getContext().setParameters(paramMap);
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

}
