package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.sothis.mvc.Attachment;
import org.sothis.mvc.Headers;
import org.sothis.mvc.ReadOnlyHeaders;

public class CommonsUploadAttachment implements Attachment {

	private final FileItem fileItem;
	private Headers headers;

	public CommonsUploadAttachment(FileItem fileItem) {
		this.fileItem = fileItem;
	}

	@Override
	public Charset getCharset() {
		if (fileItem instanceof DiskFileItem) {
			String myCharsetString = ((DiskFileItem) fileItem).getCharSet();
			if (null != myCharsetString) {
				return Charset.forName(myCharsetString);
			}
		}
		return null;
	}

	@Override
	public String getString(Charset charset) throws IOException {
		Charset myCharset = getCharset();
		if (null == myCharset) {
			myCharset = charset;
		}
		return null == myCharset ? fileItem.getString() : fileItem.getString(myCharset.name());
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return fileItem.getInputStream();
	}

	@Override
	public String getFilename() {
		return fileItem.getName();
	}

	@Override
	public Headers headers() {
		if (null == headers) {
			headers = new ReadOnlyHeaders() {
				@Override
				public Iterator<String> names() {
					return fileItem.getHeaders().getHeaderNames();
				}

				@Override
				public String[] getStrings(String name) {
					List<String> values = new ArrayList<>();
					for (Iterator<String> i = fileItem.getHeaders().getHeaders(name); i.hasNext();) {
						values.add(i.next());
					}
					String[] ret = new String[values.size()];
					return values.toArray(ret);
				}

				@Override
				public boolean contains(String name) {
					return fileItem.getHeaders().getHeader(name) != null;
				}
			};
		}
		return headers;
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
	public long getSize() {
		return fileItem.getSize();
	}

	@Override
	public void delete() throws IOException {
		fileItem.delete();
	}

}
