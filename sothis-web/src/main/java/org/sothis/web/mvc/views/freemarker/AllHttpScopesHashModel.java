package org.sothis.web.mvc.views.freemarker;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.Flash;

import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.WrappingTemplateModel;

public class AllHttpScopesHashModel extends WrappingTemplateModel implements TemplateHashModel {

	private static final long serialVersionUID = 1003273414482150521L;

	private final HttpServletRequest request;
	private final TemplateModel actionResult;

	public AllHttpScopesHashModel(HttpServletRequest request, Object actionResult) throws TemplateModelException {
		this.request = request;
		this.actionResult = this.getObjectWrapper().wrap(actionResult);
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if (actionResult instanceof TemplateHashModel) {
			TemplateModel model = ((TemplateHashModel) actionResult).get(key);
			if (model != null) {
				return model;
			}
		}
		// Lookup in request scope
		Object obj = request.getAttribute(key);
		if (obj != null) {
			return this.getObjectWrapper().wrap(obj);
		}
		if ("params".equals(key)) {
			return this.getObjectWrapper().wrap(request.getParameterMap());
		} else if ("session".equals(key)) {
			return new HttpSessionHashModel(request.getSession(false), this.getObjectWrapper());
		} else if ("flash".equals(key)) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				final Flash flash = (Flash) session.getAttribute(ActionContext.FLASH);
				if (null != flash) {
					return new TemplateHashModel() {

						@Override
						public TemplateModel get(String key) throws TemplateModelException {
							return AllHttpScopesHashModel.this.getObjectWrapper().wrap(flash.getAttribute(key));
						}

						@Override
						public boolean isEmpty() throws TemplateModelException {
							return false;
						}
					};
				} else {
					return TemplateModel.NOTHING;
				}
			}
		} else if ("cookie".equals(key)) {
			return new TemplateHashModel() {

				@Override
				public TemplateModel get(String key) throws TemplateModelException {
					for (Cookie cookie : request.getCookies()) {
						if (cookie.getName().equals(key)) {
							return AllHttpScopesHashModel.this.getObjectWrapper().wrap(cookie);
						}
					}
					return null;
				}

				@Override
				public boolean isEmpty() throws TemplateModelException {
					return false;
				}

			};
		}
		return null;
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return false;
	}

}
