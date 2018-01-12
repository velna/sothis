package org.sothis.mvc.http.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sothis.mvc.Attachment;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.HashMapAttachments;
import org.sothis.mvc.RequestParseExecption;

public class CommonsFileMultipartParser {
	private static final DiskFileItemFactory DISK_FILE_ITEM_FACTORY = new DiskFileItemFactory();

	static Attachments parse(HttpServletRequest request) throws RequestParseExecption {
		Map<String, Collection<Attachment>> attachments = new HashMap<>();
		ServletFileUpload upload = new ServletFileUpload(DISK_FILE_ITEM_FACTORY);
		List<FileItem> items;
		try {
			items = upload.parseRequest(request);
			for (Iterator<FileItem> i = items.iterator(); i.hasNext();) {
				FileItem item = i.next();
				Collection<Attachment> parts = attachments.get(item.getFieldName());
				if (null == parts) {
					parts = new ArrayList<Attachment>();
					attachments.put(item.getFieldName(), parts);
				}
				parts.add(new CommonsUploadAttachment(item));
			}
			return new HashMapAttachments(attachments);
		} catch (FileUploadException e) {
			throw new RequestParseExecption(e);
		}
	}
}
