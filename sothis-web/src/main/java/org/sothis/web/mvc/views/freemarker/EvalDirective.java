package org.sothis.web.mvc.views.freemarker;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.web.mvc.WebActionContext;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class EvalDirective implements TemplateDirectiveModel {

	private final static Logger LOGGER = LoggerFactory.getLogger(EvalDirective.class);

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		long start = System.currentTimeMillis();
		String name = (String) params.get("name");
		if (null == name) {
			throw new TemplateException("name is null !", env);
		}
		try {
			StringWriter out = new StringWriter();
			if (body != null) {
				body.render(out);
			}
			Template template = new Template(name, new StringReader(out.toString()), env.getConfiguration());
			template.process(env.getDataModel(), env.getOut());
		} finally {
			long time = System.currentTimeMillis() - start;
			if (time > 100 && LOGGER.isErrorEnabled()) {
				LOGGER.error("type:Performance\ttime:" + time + "\tname:" + name + "\trequest_url:"
						+ WebActionContext.getContext().getRequest().getRequestURL());
			}
		}
	}

}
