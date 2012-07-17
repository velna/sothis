package org.sothis.web.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sothis.web.mvc.views.freemarker.FreemarkerView;
import org.sothis.web.mvc.views.json.JsonView;
import org.sothis.web.mvc.views.jsp.JspView;
import org.sothis.web.mvc.views.redirect.RedirectView;
import org.sothis.web.mvc.views.stream.StreamView;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DefaultModelAndViewResolverTest {

	DefaultModelAndViewResolver dmavr = new DefaultModelAndViewResolver();

	// resolve失败，依赖于beanFactory为null
	@Test(dataProvider = "resolve", expectedExceptions = { ViewCreationException.class })
	public void resolveErr0(Object actionResult, ActionInvocation invocation, Class<? extends View> clazz) throws Exception {
		ActionContext.getContext().setBeanFactory(null);
		dmavr.resolve(actionResult, invocation);
	}

	// resolve成功
	@Test(dataProvider = "resolve")
	public void resolveOK(Object actionResult, ActionInvocation invocation, Class<? extends View> clazz) throws Exception {
		Assert.assertTrue(clazz.isAssignableFrom(dmavr.resolve(actionResult, invocation).getView().getClass()));
	}

	@DataProvider(name = "registerErr")
	public Object[][] registerErrProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { null, JspView.class });
		dataList.add(new Object[] { "", JspView.class });
		dataList.add(new Object[] { " ", JspView.class });
		dataList.add(new Object[] { " ", this.getClass() });
		dataList.add(new Object[] { "jsp", null });
		dataList.add(new Object[] { null, null });
		dataList.add(new Object[] { "", null });
		dataList.add(new Object[] { " ", null });

		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "registerOk")
	public Object[][] registerOkProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { "jsp", JspView.class });
		dataList.add(new Object[] { "ftl", FreemarkerView.class });
		dataList.add(new Object[] { "json", JsonView.class });
		dataList.add(new Object[] { "redirect", RedirectView.class });
		dataList.add(new Object[] { "stream", StreamView.class });

		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "defaultErr")
	public Object[][] defaultViewErrProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { null });
		dataList.add(new Object[] { "" });
		dataList.add(new Object[] { " " });
		dataList.add(new Object[] { "aaaa" });

		Object[][] ret = new Object[dataList.size()][1];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "defaultOk")
	public Object[][] defaultViewOkProvider() {
		List<Object[]> dataList = new ArrayList<Object[]>();

		dataList.add(new Object[] { "jsp", JspView.class });
		dataList.add(new Object[] { "ftl", FreemarkerView.class });
		dataList.add(new Object[] { "json", JsonView.class });
		dataList.add(new Object[] { "redirect", RedirectView.class });
		dataList.add(new Object[] { "stream", StreamView.class });

		Object[][] ret = new Object[dataList.size()][2];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "resolve")
	public Object[][] resolveProvider() throws ConfigurationException, IOException {
		List<Object[]> dataList = new ArrayList<Object[]>();

		MockActionInvocation invocation = new MockActionInvocation(SothisFactory.getActionContext());
		invocation.setAction(new MockAction());

		dataList.add(new Object[] { null, invocation, JspView.class });
		dataList.add(new Object[] { "", invocation, JspView.class });
		dataList.add(new Object[] { " ", invocation, JspView.class });
		dataList.add(new Object[] { 12, invocation, JspView.class });
		dataList.add(new Object[] { 12.12, invocation, JspView.class });

		dataList.add(new Object[] { null, invocation, JspView.class });
		dataList.add(new Object[] { "", invocation, JspView.class });
		dataList.add(new Object[] { " ", invocation, JspView.class });
		dataList.add(new Object[] { 12, invocation, JspView.class });
		dataList.add(new Object[] { 12.12, invocation, JspView.class });

		MockModel model0 = new MockModel();
		dataList.add(new Object[] { model0, invocation, JspView.class });

		MockModel model1 = new MockModel();
		model1.setViewType("jsp");
		dataList.add(new Object[] { model1, invocation, JspView.class });

		MockModel model2 = new MockModel();
		model2.setViewType("ftl");
		dataList.add(new Object[] { model2, invocation, FreemarkerView.class });

		MockModel model3 = new MockModel();
		model3.setViewType("json");
		dataList.add(new Object[] { model3, invocation, JsonView.class });

		MockModel model4 = new MockModel();
		model4.setViewType("redirect");
		dataList.add(new Object[] { model4, invocation, RedirectView.class });

		MockModel model5 = new MockModel();
		model5.setViewType("stream");
		dataList.add(new Object[] { model5, invocation, StreamView.class });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}

	@DataProvider(name = "resolve2")
	public Object[][] resolveProvider2() throws ConfigurationException, IOException {
		List<Object[]> dataList = new ArrayList<Object[]>();

		MockActionInvocation invocation = new MockActionInvocation(SothisFactory.getActionContext());

		dataList.add(new Object[] { null, invocation, StreamView.class });
		dataList.add(new Object[] { "", invocation, StreamView.class });
		dataList.add(new Object[] { " ", invocation, StreamView.class });
		dataList.add(new Object[] { 12, invocation, StreamView.class });
		dataList.add(new Object[] { 12.12, invocation, StreamView.class });

		dataList.add(new Object[] { null, invocation, StreamView.class });
		dataList.add(new Object[] { "", invocation, StreamView.class });
		dataList.add(new Object[] { " ", invocation, StreamView.class });
		dataList.add(new Object[] { 12, invocation, StreamView.class });
		dataList.add(new Object[] { 12.12, invocation, StreamView.class });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}
}
