package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.sothis.mvc.Attachment;
import org.sothis.mvc.Headers;
import org.sothis.mvc.ReadOnlyHeaders;

public class Servlet31Attachment implements Attachment {

	private final Part part;

	public Servlet31Attachment(Part part) {
		super();
		this.part = part;
	}

	@Override
	public String getFilename() {
		return part.getSubmittedFileName();
	}

	@Override
	public Headers headers() {
		if (null == part.getHeaderNames()) {
			return ReadOnlyHeaders.EMPTY;
		}
		return new ReadOnlyHeaders() {

			@Override
			public Iterator<String> names() {
				return part.getHeaderNames().iterator();
			}

			@Override
			public String[] getStrings(String name) {
				Collection<String> headers = part.getHeaders(name);
				String[] ret = new String[headers.size()];
				return part.getHeaders(name).toArray(ret);
			}

			@Override
			public boolean contains(String name) {
				return part.getHeaderNames().contains(name);
			}

		};
	}

	@Override
	public Charset getCharset() {
		return null;
	}

	@Override
	public String getString(Charset charset) throws IOException {
		return IOUtils.toString(part.getInputStream(), charset);
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
	public void delete() throws IOException {
		part.delete();
	}

}
