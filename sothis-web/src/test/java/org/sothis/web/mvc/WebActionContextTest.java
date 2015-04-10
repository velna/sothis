package org.sothis.web.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.mvc.Action;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.Request;
import org.sothis.mvc.Response;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WebActionContextTest {

	private WebActionContext context = null;

	@BeforeMethod
	public void beforeMethod() throws ConfigurationException, IOException, BeanInstantiationException, ClassNotFoundException {
		context = SothisFactory.initActionContext();
	}

	@AfterMethod
	public void afterMethod() {
		context.setContextMap(new HashMap<String, Object>());
	}

	@Test
	public void testGetAndPut() {
		final String key = "a key";
		Assert.assertNull(context.get(key));
		Object value = new Object();
		Assert.assertNull(context.put(key, value));
		Assert.assertSame(value, context.get(key));
	}

	@Test
	public void testGetAndSetContext() {
		Assert.assertNotNull(context);
		Assert.assertNotNull(WebActionContext.getContext());
		Assert.assertSame(context, WebActionContext.getContext());
	}

	@Test
	public void testGetAndSetParameters() {
		Assert.assertNull(context.getParameters());
		final Map<String, Object[]> params = new HashMap<String, Object[]>();
		Assert.assertNull(context.setParameters(params));
		Assert.assertSame(params, context.getParameters());
	}

	@Test
	public void testGetAndSetRequest() {
		Assert.assertNull(context.getRequest());
		final Request request = new WebRequest(new MockHttpServletRequest());
		context.setRequest(request);
		Assert.assertSame(request, context.getRequest());
	}

	@Test
	public void testGetAndSetResponse() {
		Assert.assertNull(context.getResponse());
		final Response response = new WebResponse(new MockHttpServletResponse());
		context.setResponse(response);
		Assert.assertSame(response, context.getResponse());
	}

	@Test
	public void testGetAndSetServletContext() {
		Assert.assertNull(context.getServletContext());
		final ServletContext servletContext = new MockServletContext();
		context.setServletContext(servletContext);
		Assert.assertSame(servletContext, context.getServletContext());
	}

	@Test
	public void testGetAndSetAction() {
		Assert.assertNull(context.getAction());
		final Action action = new MockAction(context.getConfiguration());
		context.setAction(action);
		Assert.assertSame(action, context.getAction());
	}

	@Test
	public void testGetFlash() {
		final Request request = new WebRequest(new MockHttpServletRequest());
		context.setRequest(request);
		Assert.assertNull(context.getFlash(false));
		Flash flash = context.getFlash(true);
		Assert.assertNotNull(flash);
		Assert.assertSame(flash, context.getFlash());
		Assert.assertSame(flash, context.getFlash(true));
		Assert.assertSame(flash, context.getFlash(false));
	}

}
