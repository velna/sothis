package org.sothis.mvc.views.freemarker;

import java.io.Serializable;

import org.sothis.mvc.Session;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public final class SessionHashModel implements TemplateHashModel, Serializable {
	private static final long serialVersionUID = 1L;
	private transient Session session;
	private transient final ObjectWrapper wrapper;

	/**
	 * Use this constructor when the session already exists.
	 * 
	 * @param session
	 *            the session
	 * @param wrapper
	 *            an object wrapper used to wrap session attributes
	 */
	public SessionHashModel(Session session, ObjectWrapper wrapper) {
		this.session = session;
		this.wrapper = wrapper;

	}

	public TemplateModel get(String key) throws TemplateModelException {
		return wrapper.wrap(session != null ? session.attributes().get(key) : null);
	}

	public boolean isEmpty() throws TemplateModelException {
		return session == null || !session.attributes().names().hasNext();
	}
}