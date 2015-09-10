package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class Servlet31Attachments extends ServletAttachments {

	private final Map<String, Object> attachments;

	public Servlet31Attachments(HttpServletRequest request) throws IOException, ServletException {
		this.attachments = new HashMap<>();
		for (Part part : request.getParts()) {
			this.attachments.put(part.getName(), new Servlet31PartWrapper(part));
		}
	}

	@Override
	protected Map<String, Object> getAttachments() {
		return attachments;
	}

	private class Servlet31PartWrapper implements Servlet31Part {

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

	}
}
