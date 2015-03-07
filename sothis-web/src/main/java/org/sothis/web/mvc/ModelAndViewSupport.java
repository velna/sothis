package org.sothis.web.mvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sothis.core.util.Pager;

public class ModelAndViewSupport extends BaseModelAndView {

	private static final long serialVersionUID = 2142558258767601308L;

	private Pager pager;
	private Collection<String> actionMessages;
	private Map<String, Collection<String>> fieldMessages;
	private Collection<String> actionErrors;
	private Map<String, Collection<String>> fieldErrors;

	// public final Flash flash() {
	// return WebActionContext.getContext().getFlash();
	// }

	public final void addActionError(String message) {
		if (null == this.actionErrors) {
			this.actionErrors = new ArrayList<String>(4);
		}
		this.actionErrors.add(message);
	}

	public final Collection<String> getActionErrors() {
		return null == this.actionErrors ? null : Collections.unmodifiableCollection(this.actionErrors);
	}

	public final void clearActionErrors() {
		if (null != this.actionErrors) {
			this.actionErrors.clear();
		}
	}

	public final boolean hasActionErrors() {
		return (this.actionErrors != null && this.actionErrors.size() > 0);
	}

	public final void addFieldError(String fieldName, String message) {
		if (null == this.fieldErrors) {
			this.fieldErrors = new HashMap<String, Collection<String>>();
		}
		Collection<String> errors = this.fieldErrors.get(fieldName);
		if (null == errors) {
			errors = new ArrayList<String>(4);
			this.fieldErrors.put(fieldName, errors);
		}
		errors.add(message);
	}

	public final Map<String, Collection<String>> getFieldErrors() {
		return null == this.fieldErrors ? null : Collections.unmodifiableMap(this.fieldErrors);
	}

	public final void clearFieldErrors() {
		if (null != this.fieldErrors) {
			this.fieldErrors.clear();
		}
	}

	public final boolean hasFieldErrors() {
		return (this.fieldErrors != null && this.fieldErrors.size() > 0);
	}

	public final boolean hasFieldError(String field) {
		return (this.fieldErrors != null && this.fieldErrors.containsKey(field));
	}

	public final boolean hasErrors() {
		return hasActionErrors() || hasFieldErrors();
	}

	public final void addActionMessage(String message) {
		if (null == this.actionMessages) {
			this.actionMessages = new ArrayList<String>(4);
		}
		this.actionMessages.add(message);
	}

	public final Collection<String> getActionMessages() {
		return null == this.actionMessages ? null : Collections.unmodifiableCollection(this.actionMessages);
	}

	public final void clearActionMessages() {
		if (null != this.actionMessages) {
			this.actionMessages.clear();
		}
	}

	public final boolean hasActionMessages() {
		return (this.actionMessages != null && this.actionMessages.size() > 0);
	}

	public final void addFieldMessage(String fieldName, String message) {
		if (null == this.fieldMessages) {
			this.fieldMessages = new HashMap<String, Collection<String>>();
		}
		Collection<String> errors = this.fieldMessages.get(fieldName);
		if (null == errors) {
			errors = new ArrayList<String>(4);
			this.fieldMessages.put(fieldName, errors);
		}
		errors.add(message);
	}

	public final Map<String, Collection<String>> getFieldMessages() {
		return null == this.fieldMessages ? null : Collections.unmodifiableMap(this.fieldMessages);
	}

	public final void clearFieldMessages() {
		if (null != this.fieldMessages) {
			this.fieldMessages.clear();
		}
	}

	public final boolean hasFieldMessages() {
		return (this.fieldMessages != null && this.fieldMessages.size() > 0);
	}

	public final boolean hasFieldMessage(String field) {
		return (this.fieldMessages != null && this.fieldMessages.containsKey(field));
	}

	public final boolean hasMessages() {
		return hasActionMessages() || hasFieldMessages();
	}

	public final void setPager(Pager pager) {
		this.pager = pager;
	}

	public final Pager getPager() {
		return pager;
	}

	/**
	 * 分页器，初始为每页<code>pageSize</code>条记录
	 * 
	 * @param pageSize
	 * @return
	 */
	public final Pager getPager(int pageSize) {
		if (null == pager) {
			pager = new Pager(pageSize);
		} else {
			pager.setPageSize(pageSize);
		}
		return pager;
	}

}
