package org.sothis.mvc.views.freemarker;

import org.sothis.mvc.ActionContext;
import org.sothis.mvc.Flash;
import org.sothis.mvc.Request;
import org.sothis.mvc.Session;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.WrappingTemplateModel;

public class AllScopesHashModel extends WrappingTemplateModel implements TemplateHashModel {

	private final ActionContext actionContext;
	private final TemplateModel actionResult;

	public AllScopesHashModel(ActionContext actionContext, Object actionResult) throws TemplateModelException {
		this.actionContext = actionContext;
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
		Request request = actionContext.getRequest();
		Object obj = request.attributes().get(key);
		if (obj != null) {
			return this.getObjectWrapper().wrap(obj);
		}
		if ("params".equals(key)) {
			return this.getObjectWrapper().wrap(request.parameters().toMap());
		} else if ("session".equals(key)) {
			return new SessionHashModel(request.getSession(false), this.getObjectWrapper());
		} else if ("flash".equals(key)) {
			Session session = request.getSession(false);
			if (session != null) {
				final Flash flash = actionContext.getFlash(false);
				if (null != flash) {
					return new TemplateHashModel() {

						@Override
						public TemplateModel get(String key) throws TemplateModelException {
							return AllScopesHashModel.this.getObjectWrapper().wrap(flash.getAttribute(key));
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
		}
		return null;
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return false;
	}

}