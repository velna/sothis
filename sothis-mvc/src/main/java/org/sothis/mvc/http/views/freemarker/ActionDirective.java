package org.sothis.mvc.http.views.freemarker;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.sothis.mvc.AbstractRequest;
import org.sothis.mvc.ActionContext;
import org.sothis.mvc.ActionInvocationHelper;
import org.sothis.mvc.ApplicationContext;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.HashMapHeaders;
import org.sothis.mvc.HashMapParameters;
import org.sothis.mvc.Headers;
import org.sothis.mvc.Parameters;
import org.sothis.mvc.Request;
import org.sothis.mvc.Response;
import org.sothis.mvc.Session;
import org.sothis.mvc.http.HttpConstants;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ActionDirective implements TemplateDirectiveModel {

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		ActionContext actionContext = ActionContext.getContext();
		if (StringUtils.isBlank(actionContext.getAction().getName())) {
			throw new IllegalArgumentException("d_action actionName is null!");
		}
		String controller = MapUtils.getString(params, "controller", actionContext.getAction().getController().getName());
		if (StringUtils.isNotBlank(controller)) {
			controller = "/" + controller;
		}
		String action = MapUtils.getString(params, "action", actionContext.getAction().getName());

		Writer out = env.getOut();
		if (body != null) {
			body.render(out);
		}
		Map<String, Object[]> myParams = new HashMap<String, Object[]>();
		String[] attrs = env.getCustomAttributeNames();
		if (attrs != null) {
			for (int i = 0; i < attrs.length; i++) {
				myParams.put(attrs[i],
						new String[] { URLDecoder.decode(String.valueOf(env.getCustomAttribute(attrs[i])), "UTF-8") });
				env.removeCustomAttribute(attrs[i]);
			}
		}
		Map<String, Object> orgContext = actionContext.getContextMap();
		StringResponse response = new StringResponse(actionContext.getRequest().getProtocol(), actionContext.getRequest()
				.getCharset());
		ActionRequest myRequest = new ActionRequest(actionContext.getApplicationContext().getContextPath() + controller + "/"
				+ action, actionContext.getRequest(), myParams);
		ApplicationContext appContext = actionContext.getApplicationContext();
		try {
			actionContext.clear();
			ActionInvocationHelper.invoke(actionContext, appContext, myRequest, response);
			env.getOut().write(response.getContentAsString());
		} catch (Exception e) {
			throw new TemplateException("error invoke action directive:" + controller + "/" + action, e, env);
		} finally {
			actionContext.setContextMap(orgContext);
		}
	}

	private static class ActionRequest extends AbstractRequest {
		private final Request request;
		private final String uri;
		private final Parameters parameters;
		private Headers headers;

		public ActionRequest(String uri, Request request, Map<String, Object[]> myParams) {
			super();
			this.uri = uri;
			this.request = request;
			this.parameters = new HashMapParameters(myParams);
		}

		@Override
		public String getMethod() {
			return request.getMethod();
		}

		@Override
		public String getUri() {
			return uri;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public Session getSession() {
			return request.getSession();
		}

		@Override
		public Session getSession(boolean create) {
			return request.getSession(create);
		}

		@Override
		public String getLocalAddr() {
			return request.getLocalAddr();
		}

		@Override
		public int getLocalPort() {
			return request.getLocalPort();
		}

		@Override
		public String getRemoteAddr() {
			return request.getRemoteAddr();
		}

		@Override
		public int getRemotePort() {
			return request.getRemotePort();
		}

		@Override
		public Headers headers() {
			if (null == headers) {
				headers = new HashMapHeaders();
			}
			return headers;
		}

		@Override
		public String getProtocol() {
			return request.getProtocol();
		}

		@Override
		public Attachments attachments() throws IOException {
			return null;
		}

		@Override
		public Parameters parameters() {
			return this.parameters;
		}

		@Override
		public String getScheme() {
			return request.getScheme();
		}

	}

	private static class StringResponse implements Response {

		private ByteArrayOutputStream content;
		private PrintWriter writer;
		private String protocolVersion;
		private String charset;
		private int status;
		private Headers headers;

		public StringResponse(String protocolVersion, String charset) {
			super();
			this.protocolVersion = protocolVersion;
			this.charset = charset;
			this.status = HttpConstants.StatusCodes.SC_OK;
		}

		public String getContentAsString() throws IOException {
			if (null != writer) {
				writer.close();
			}
			if (null != content) {
				return new String(content.toByteArray(), charset);
			} else {
				return "";
			}
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			if (null == content) {
				content = new ByteArrayOutputStream();
			}
			return content;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			if (null == writer) {
				writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getOutputStream(), charset)));
			}
			return writer;
		}

		@Override
		public Headers headers() {
			if (null == headers) {
				headers = new HashMapHeaders();
			}
			return headers;
		}

		@Override
		public String getProtocolVersion() {
			return protocolVersion;
		}

		@Override
		public void setProtocolVersion(String protocolVersion) {
			this.protocolVersion = protocolVersion;
		}

		@Override
		public String getCharset() {
			return charset;
		}

		@Override
		public void setCharset(String charset) throws UnsupportedEncodingException {
			this.charset = charset;
		}

		@Override
		public int getStatus() {
			return status;
		}

		@Override
		public void setStatus(int status) {
			this.status = status;
		}

		@Override
		public boolean isCommitted() {
			return false;
		}

		@Override
		public void reset() {
			if (null != this.content) {
				this.content.reset();
			}
		}

	}
}