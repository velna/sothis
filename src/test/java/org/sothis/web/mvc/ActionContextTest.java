package org.sothis.web.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ActionContextTest {

	private ActionContext context = null;

	@BeforeMethod
	public void beforeMethod() {
		context = ActionContext.getContext();
	}

	@AfterMethod
	public void afterMethod() {
		context = null;
	}

	@Test
	public void get() {
		final String key = "a key";
		Assert.assertNull(context.get(key));
		Object value = new Object();
		Assert.assertNull(context.put(key, value));
		Assert.assertSame(value, context.get(key));
	}

	@Test
	public void getContext() {
		Assert.assertNotNull(context);
		Assert.assertNotNull(ActionContext.getContext());
		Assert.assertSame(context, ActionContext.getContext());
	}

	@Test
	public void getParameters() {
		Assert.assertNull(context.getParameters());
		final Map<String, Object[]> params = new HashMap<String, Object[]>();
		Assert.assertNull(context.setParameters(params));
		Assert.assertSame(params, context.getParameters());
	}

	@Test
	public void getRequest() {
		Assert.assertNull(context.getRequest());
		final HttpServletRequest request = new MockHttpServletRequest();
		Assert.assertNull(context.setRequest(request));
		Assert.assertSame(request, context.getRequest());
	}

	@Test
	public void getResponse() {
		Assert.assertNull(context.getResponse());
		final HttpServletResponse response = new MockHttpServletResponse();
		Assert.assertNull(context.setResponse(response));
		Assert.assertSame(response, context.getResponse());
	}

}
