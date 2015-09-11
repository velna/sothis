package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

public class Servlet31Attachments extends ServletAttachments {

	private final Map<String, Object> attachments;

	public Servlet31Attachments(HttpServletRequest request) throws IOException, ServletException {
		this.attachments = new HashMap<>();
		for (Part part : request.getParts()) {
			List<ServletPart> parts = (List<ServletPart>) this.attachments.get(part.getName());
			if (null == parts) {
				parts = new ArrayList<ServletPart>();
				this.attachments.put(part.getName(), parts);
			}
			parts.add(new Servlet31PartWrapper(part));
		}
	}

	@Override
	protected Map<String, Object> getAttachments() {
		return attachments;
	}

	private class Servlet31PartWrapper implements ServletPart {

		private final Part part;

		public Servlet31PartWrapper(Part part) {
			super();
			this.part = part;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return part.getInputStream();
		}

		@Override
		public String getContentType() {
			return part.getContentType();
		}

		@Override
		public String getName() {
			return part.getName();
		}

		@Override
		public long getSize() {
			return part.getSize();
		}

		@Override
		public void write(String fileName) throws IOException {
			part.write(fileName);
		}

		@Override
		public void delete() throws IOException {
			part.delete();
		}

		@Override
		public String getHeader(String name) {
			return part.getHeader(name);
		}

		@Override
		public Collection<String> getHeaders(String name) {
			return part.getHeaders(name);
		}

		@Override
		public Collection<String> getHeaderNames() {
			return part.getHeaderNames();
		}

		@Override
		public String getSubmittedFileName() {
			return part.getSubmittedFileName();
		}

		@Override
		public boolean isFormField() {
			return part.getContentType() == null;
		}

		@Override
		public String getString(String charset) throws IOException {
			return IOUtils.toString(part.getInputStream(), charset);
		}

	}
}
