package org.sothis.mvc.http.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.sothis.mvc.Attachment;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.HashMapAttachments;
import org.sothis.mvc.RequestParseExecption;

class Servlet31MultipartParser {
	static Attachments parse(HttpServletRequest request) throws RequestParseExecption {
		try {
			Collection<Part> parts = request.getParts();
			if (parts.isEmpty()) {
				return Attachments.EMPTY;
			}
			Map<String, Collection<Attachment>> attachments = new HashMap<>();
			for (Part part : request.getParts()) {
				Collection<Attachment> atts = attachments.get(part.getName());
				if (null == atts) {
					atts = new ArrayList<Attachment>();
					attachments.put(part.getName(), atts);
				}
				atts.add(new Servlet31Attachment(part));
			}
			return new HashMapAttachments(attachments);
		} catch (IOException | ServletException ex) {
			throw new RequestParseExecption(ex);
		}
	}
}
