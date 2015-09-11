package org.sothis.mvc.http.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class CommonsUploadAttachments extends ServletAttachments {

	private final Map<String, Object> attachments;

	public CommonsUploadAttachments(HttpServletRequest request) throws IOException, ServletException {
		this.attachments = new HashMap<>();
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			for (Iterator<FileItem> i = items.iterator(); i.hasNext();) {
				FileItem item = i.next();
				List<ServletPart> parts = (List<ServletPart>) this.attachments.get(item.getFieldName());
				if (null == parts) {
					parts = new ArrayList<ServletPart>();
					this.attachments.put(item.getFieldName(), parts);
				}
				parts.add(new CommonsFileUploadPart(item));
			}
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected Map<String, Object> getAttachments() {
		return attachments;
	}

	private class CommonsFileUploadPart implements ServletPart {
		private final FileItem fileItem;

		public CommonsFileUploadPart(FileItem fileItem) {
			this.fileItem = fileItem;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return fileItem.getInputStream();
		}

		@Override
		public String getContentType() {
			return fileItem.getContentType();
		}

		@Override
		public String getName() {
			return fileItem.getFieldName();
		}

		@Override
		public String getSubmittedFileName() {
			return fileItem.getName();
		}

		@Override
		public long getSize() {
			return fileItem.getSize();
		}

		@Override
		public void write(String fileName) throws IOException {
			try {
				fileItem.write(new File(fileName));
			} catch (Exception e) {
				throw new IOException(e);
			}
		}

		@Override
		public void delete() throws IOException {
			fileItem.delete();
		}

		@Override
		public String getHeader(String name) {
			return fileItem.getHeaders().getHeader(name);
		}

		@Override
		public Collection<String> getHeaders(String name) {
			List<String> headers = new ArrayList<>();
			Iterator<String> i = fileItem.getHeaders().getHeaders(name);
			while (i.hasNext()) {
				headers.add(i.next());
			}
			return headers;
		}

		@Override
		public Collection<String> getHeaderNames() {
			List<String> names = new ArrayList<>();
			Iterator<String> i = fileItem.getHeaders().getHeaderNames();
			while (i.hasNext()) {
				names.add(i.next());
			}
			return names;
		}

		@Override
		public boolean isFormField() {
			return fileItem.isFormField();
		}

		@Override
		public String getString(String charset) throws IOException {
			return fileItem.getString(charset);
		}

	}

}
