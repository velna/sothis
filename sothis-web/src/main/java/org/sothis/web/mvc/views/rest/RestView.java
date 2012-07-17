package org.sothis.web.mvc.views.rest;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.sothis.core.beans.Bean;
import org.sothis.core.beans.Scope;
import org.sothis.web.mvc.ActionContext;
import org.sothis.web.mvc.ActionInvocation;
import org.sothis.web.mvc.ModelAndView;
import org.sothis.web.mvc.ModelAndViewResolver;
import org.sothis.web.mvc.View;
import org.sothis.web.mvc.ViewCreationException;

@Bean(scope = Scope.SINGLETON)
public class RestView implements View {
	public void render(ModelAndView mav, ActionInvocation invocation) throws IOException, ServletException {
		Map<String, Object> viewParams = mav.viewParams();
		ActionContext context = invocation.getActionContext();
		ModelAndViewResolver mavResolver = (ModelAndViewResolver) context.get(ActionContext.MODEL_AND_VIEW_RESOLVER);
		int code = 200;
		if (null != viewParams && null != viewParams.get("code")) {
			code = (Integer) viewParams.get("code");
		}
		String format = getRestForamt(context);
		context.getResponse().setStatus(code);
		try {
			mavResolver.getView(format, invocation).render(mav, invocation);
		} catch (ViewCreationException e) {
			throw new ServletException(e);
		}
		// Class<?> c = model.getClass();
		// Field[] fields = c.getDeclaredFields();
		// List<String> needColumns =
		// exportColumns((String)viewParams.get("columns"));
		// if (model instanceof List) {
		// List<Object> l = (List<Object>) model;
		// if (CollectionUtils.isNotEmpty(l)) {
		// c = l.get(0).getClass();
		// fields = c.getDeclaredFields();
		// }
		// if (CollectionUtils.isNotEmpty(needColumns)) {
		// for (Object o : l) {
		// doWithoutFieldEmpty(c, fields, needColumns, o);
		// }
		// }
		// }else{
		// if (CollectionUtils.isNotEmpty(needColumns)){
		// doWithoutFieldEmpty(c, fields, needColumns, model);
		// }
		// }
	}

	private String getRestForamt(ActionContext context) {
		String uri = context.getRequest().getRequestURI();
		String format = "json";
		int index = uri.lastIndexOf('.');
		if (index > 0 && index < uri.length() - 1) {
			format = uri.substring(index + 1);
			if (!"json".equalsIgnoreCase(format) && !"xml".equalsIgnoreCase(format)) {
				format = "json";
			}
		}
		return format;
	}

	// private void doWithoutFieldEmpty(Class<?> c, Field[] fields, List<String>
	// neideColumns, Object o)
	// throws IllegalAccessException, InvocationTargetException {
	// for (Field field : fields) {
	// if ("serialVersionUID".equals(field.getName()))
	// continue;
	// if (!neideColumns.contains(field.getName())) {
	// Method setMethod = BeanUtils.findMethod(c, "set" +
	// StringUtils.capitalize(field.getName()),
	// new Class[] { field.getType() });
	// setMethod.invoke(o, new Object[] { null });
	// }
	// }
	// }
	//
	// @SuppressWarnings("unchecked")
	// private List<String> exportColumns(String columns) {
	// if (StringUtils.isNotBlank(columns)) {
	// List<String> exportColumns = Arrays.asList(StringUtils.split(columns,
	// ","));
	// exportColumns = (List<String>) CollectionUtils.collect(exportColumns, new
	// Transformer() {
	// public Object transform(Object input) {
	// return String.valueOf(input).trim();
	// }
	// });
	// return exportColumns;
	// } else {
	// return Collections.EMPTY_LIST;
	// }
	//
	// }
}
