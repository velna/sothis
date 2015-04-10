package org.sothis.web.mvc.interceptors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.sothis.core.beans.BeanInstantiationException;
import org.sothis.mvc.ConfigurationException;
import org.sothis.mvc.Controller;
import org.sothis.mvc.DefaultController;
import org.sothis.mvc.Request;
import org.sothis.mvc.Response;
import org.sothis.web.mvc.MockActionInvocation;
import org.sothis.web.mvc.SothisFactory;
import org.sothis.web.mvc.WebActionContext;
import org.sothis.web.mvc.WebRequest;
import org.sothis.web.mvc.WebResponse;
import org.sothis.web.mvc.interceptors.param.HttpServletRequestAware;
import org.sothis.web.mvc.interceptors.param.HttpServletResponseAware;
import org.sothis.web.mvc.interceptors.param.Param;
import org.sothis.web.mvc.interceptors.param.ParametersInterceptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ParametersInterceptorTest {

	private WebActionContext context = null;

	@BeforeMethod
	public void beforeMethod() throws ConfigurationException, IOException, BeanInstantiationException, ClassNotFoundException {
		context = SothisFactory.initActionContext();
	}

	@AfterMethod
	public void afterMethod() {
		context.setContextMap(new HashMap<String, Object>());
	}

	@Test(dataProvider = "test")
	public void testIntercept(Map<String, Object[]> parameters, String actionName, Object[] expectedActionParams) throws Exception {

		MockActionInvocation invocation = new MockActionInvocation(context);
		invocation.setActionContext(context);

		context.setParameters(parameters);
		Controller controller = new DefaultController(context.getConfiguration(), "", "test", TestController.class);
		invocation.setAction(controller.getAction(actionName));

		Object controllerInstance = context.getApplicationContext().getBeanFactory()
				.getBean(controller.getAction(actionName).getController().getControllerClass());
		invocation.setControllerInstance(controllerInstance);

		ParametersInterceptor interceptor = new ParametersInterceptor();
		interceptor.intercept(invocation);
		Object[] actionParams = (Object[]) invocation.getActionContext().get(WebActionContext.ACTION_PARAMS);
		Assert.assertNotNull(actionParams);
		Assert.assertEquals(actionParams, expectedActionParams);
	}

	@Test
	public void testRequestAndResponse() throws Exception {
		final Request request = new WebRequest(new MockHttpServletRequest());
		context.setRequest(request);
		Response response = new WebResponse(new MockHttpServletResponse());
		context.setResponse(response);

		MockActionInvocation invocation = new MockActionInvocation(context);
		invocation.setActionContext(context);

		Map<String, Object[]> parameters = new HashMap<String, Object[]>();
		context.setParameters(parameters);
		Controller controller = new DefaultController(context.getConfiguration(), "", "test", TestController.class);
		String actionName = "testRequestResponse";
		invocation.setAction(controller.getAction(actionName));

		Object controllerInstance = context.getApplicationContext().getBeanFactory()
				.getBean(controller.getAction(actionName).getController().getControllerClass());
		invocation.setControllerInstance(controllerInstance);

		ParametersInterceptor interceptor = new ParametersInterceptor();
		interceptor.intercept(invocation);
		Object[] actionParams = (Object[]) invocation.getActionContext().get(WebActionContext.ACTION_PARAMS);
		Assert.assertNotNull(actionParams);
		Assert.assertEquals(actionParams, new Object[] { request, response });
	}

	@Test
	public void testRequestAndResponseAware() throws Exception {
		final Request request = new WebRequest(new MockHttpServletRequest());
		context.setRequest(request);
		Response response = new WebResponse(new MockHttpServletResponse());
		context.setResponse(response);

		MockActionInvocation invocation = new MockActionInvocation(context);
		invocation.setActionContext(context);

		Map<String, Object[]> parameters = new HashMap<String, Object[]>();
		context.setParameters(parameters);
		Controller controller = new DefaultController(context.getConfiguration(), "", "test", TestController.class);
		String actionName = "testBeanParam2";
		invocation.setAction(controller.getAction(actionName));

		Object controllerInstance = context.getApplicationContext().getBeanFactory()
				.getBean(controller.getAction(actionName).getController().getControllerClass());
		invocation.setControllerInstance(controllerInstance);

		ParametersInterceptor interceptor = new ParametersInterceptor();
		interceptor.intercept(invocation);
		Object[] actionParams = (Object[]) invocation.getActionContext().get(WebActionContext.ACTION_PARAMS);
		Assert.assertNotNull(actionParams);
		ParamModel2 model = new ParamModel2();
//		model.request = request;
//		model.response = response;
		Assert.assertEquals(actionParams, new Object[] { model });
	}

	@DataProvider(name = "test")
	public Object[][] interceptDataProvider() {

		Map<String, Object[]> params = new HashMap<String, Object[]>();
		List<Object[]> dataList = new ArrayList<Object[]>();

		// 无任何参数
		dataList.add(new Object[] { Collections.EMPTY_MAP, "test", new Object[0] });

		// 原始类型
		dataList.add(new Object[] { Collections.EMPTY_MAP, "testPrimitiveParam", new Object[] { false, (byte) 0, 0, 0L, 0F, 0D, (char) 0, (short) 0 } });

		// 类原始类型
		dataList.add(new Object[] { Collections.EMPTY_MAP, "testPrimitiveLikeParam", new Object[4] });

		// 枚举类型为null
		dataList.add(new Object[] { Collections.EMPTY_MAP, "testEnumParam", new Object[1] });

		// 枚举类型不为null

		params.put("E1", new Object[] { "" });
		dataList.add(new Object[] { params, "testEnumParam", new Object[] { TestEnum.E1 } });

		// Map参数为null
		dataList.add(new Object[] { Collections.EMPTY_MAP, "testMapParam", new Object[] { Collections.EMPTY_MAP } });

		// Map参数不为null
		params = new HashMap<String, Object[]>();
		params.put("E1", new String[] { "" });
		dataList.add(new Object[] { params, "testMapParam", new Object[] { params } });

		// 嵌套map
		params = new HashMap<String, Object[]>();
		params.put("params.name", new String[] { "123", "234" });
		params.put("params.bob.friends", new String[] { "tom", "jack" });
		params.put("model.intArray", new String[] { "123", "234" });
		params.put("intArray", new String[] { "123", "234" });
		params.put("enum1", new String[] { "E1" });
		params.put("int1", new String[] { "1234" });
		params.put("int2", new String[] { "12345" });
		params.put("string1", new String[] { "string122" });
		params.put("names", new String[] { "abc", "def" });
		params.put("titles", new String[] { "deff", "abc" });
		params.put("date", new String[] { "2011-05-05" });
		params.put("booleanValue", new String[] { "true" });
		params.put("booleanObject", new String[] { "false" });
		params.put("longValue", new String[] { "123" });
		params.put("longObject", new String[] { "456" });
		params.put("floatValue", new String[] { "5.36" });
		params.put("floatObject", new String[] { "63.6" });
		params.put("doubleValue", new String[] { "2.2250738585072012e-8" });
		params.put("doubleObject", new String[] { "26.33" });
		params.put("byteValue", new String[] { "127" });
		params.put("byteObject", new String[] { "1" });
		params.put("charValue", new String[] { "c" });
		params.put("charObject", new String[] { "我的" });
		params.put("shortValue", new String[] { "152" });
		params.put("shortObject", new String[] { "-523" });

		ParamModel expected = new ParamModel();
		expected.enum1 = TestEnum.E1;
		expected.int1 = 1234;
		expected.int2 = 12345;
		expected.enum1 = TestEnum.E1;
		expected.string1 = "string122";
		expected.intArray = new int[] { 123, 234 };
		expected.params = new HashMap<String, String[]>();
		expected.params.put("name", new String[] { "123", "234" });
		expected.params.put("bob.friends", new String[] { "tom", "jack" });
		expected.names = new ArrayList<String>();
		expected.names.add("abc");
		expected.names.add("def");
		expected.titles = new HashSet<String>();
		expected.titles.add("abc");
		expected.titles.add("deff");
		expected.date = new Date(2011 - 1900, 5 - 1, 5);
		expected.booleanValue = true;
		expected.booleanObject = false;
		expected.longValue = 123;
		expected.longObject = 456L;
		expected.floatValue = 5.36f;
		expected.floatObject = 63.6f;
		expected.doubleValue = 0;
		expected.doubleObject = 26.33d;
		expected.byteValue = 127;
		expected.byteObject = 1;
		expected.charValue = 'c';
		expected.charObject = '我';
		expected.shortValue = 152;
		expected.shortObject = -523;

		ParamModel nestModel = new ParamModel();
		nestModel.intArray = new int[] { 123, 234 };
		expected.model = nestModel;

		dataList.add(new Object[] { params, "testBeanParam", new Object[] { expected } });

		// 异常数据
		params = new HashMap<String, Object[]>();
		params.put("params.name", new String[] { "123", "234" });
		params.put("model.intArray", new String[] { "sdf", "234" });
		params.put("intArray", new String[] { "123", "qqq" });
		params.put("int1", new String[] { "qad" });
		params.put("int2", new String[] { "4r4r" });
		params.put("enum1", new String[] { "eee" });
		params.put("string1", new String[] { "string122" });
		params.put("names", new String[] { "abc", "def" });
		params.put("titles", new String[] { "deff", "abc" });
		params.put("date", new String[] { "qqqq" });
		params.put("booleanValue", new String[] { "qqq" });
		params.put("booleanObject", new String[] { "fff" });
		params.put("longValue", new String[] { "sss" });
		params.put("longObject", new String[] { "ttt" });
		params.put("floatValue", new String[] { "uuu" });
		params.put("floatObject", new String[] { "ppo" });
		params.put("doubleValue", new String[] { "26ss" });
		params.put("doubleObject", new String[] { "26.33ff" });
		params.put("byteValue", new String[] { "212" });
		params.put("byteObject", new String[] { "aa" });
		params.put("charValue", new String[] { "" });

		expected = new ParamModel();
		expected.enum1 = null;
		expected.string1 = "string122";
		expected.intArray = new int[] { 123 };
		expected.int1 = 0;
		expected.int2 = null;
		expected.params = new HashMap<String, String[]>();
		expected.params.put("name", new String[] { "123", "234" });
		expected.names = new ArrayList<String>();
		expected.names.add("abc");
		expected.names.add("def");
		expected.titles = new HashSet<String>();
		expected.titles.add("abc");
		expected.titles.add("deff");
		expected.date = null;
		expected.booleanValue = false;
		expected.booleanObject = false;
		expected.longValue = 0;
		expected.longObject = null;
		expected.floatValue = 0f;
		expected.floatObject = null;
		expected.doubleValue = 0;
		expected.doubleObject = null;
		expected.byteValue = 0;
		expected.byteObject = null;
		expected.charValue = 0;
		expected.charObject = null;

		nestModel = new ParamModel();
		nestModel.intArray = new int[] { 234 };
		expected.model = nestModel;

		dataList.add(new Object[] { params, "testBeanParam", new Object[] { expected } });

		// @param 注解方式
		params = new HashMap<String, Object[]>();
		params.put("username", new String[] { "123" });
		params.put("password", new String[] { "456" });
		dataList.add(new Object[] { params, "testParamAnnotation", new Object[] { "123", "456" } });

		// @param 注解方式 参数值为null
		params = new HashMap<String, Object[]>();
		params.put("username", new String[] { null });
		params.put("password", new String[] { null });
		dataList.add(new Object[] { params, "testParamAnnotation", new Object[] { null, null } });

		// @param 注解方式参数为null
		dataList.add(new Object[] { Collections.EMPTY_MAP, "testParamAnnotation", new Object[2] });

		// @param 注解方式
		dataList.add(new Object[] { Collections.EMPTY_MAP, "testParamAnnotationPrimitive", new Object[] { 0, false } });

		Object[][] ret = new Object[dataList.size()][3];
		ret = dataList.toArray(ret);
		return ret;
	}

	public static enum TestEnum {
		E1, E2
	}

	public static class ParamModel2 implements HttpServletResponseAware, HttpServletRequestAware {
		private HttpServletResponse response;
		private HttpServletRequest request;

		public HttpServletResponse getResponse() {
			return response;
		}

		public void setResponse(HttpServletResponse response) {
			this.response = response;
		}

		public HttpServletRequest getRequest() {
			return request;
		}

		public void setRequest(HttpServletRequest request) {
			this.request = request;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != getClass()) {
				return false;
			}
			ParamModel2 other = (ParamModel2) obj;
			return new EqualsBuilder().append(this.request, other.request).append(this.response, other.response).isEquals();
		}

	}

	public static class ParamModel {
		private String string1;
		private int[] intArray;
		private int int1;
		private Integer int2;
		private TestEnum enum1;
		private Map<String, String[]> params;
		private List<String> names;
		private Set<String> titles;
		private ParamModel model;
		private Date date;
		private boolean booleanValue;
		private Boolean booleanObject;
		private long longValue;
		private Long longObject;
		private float floatValue;
		private Float floatObject;
		private double doubleValue;
		private Double doubleObject;
		private byte byteValue;
		private Byte byteObject;
		private char charValue;
		private Character charObject;
		private short shortValue;
		private Short shortObject;

		public String getString1() {
			return string1;
		}

		public void setString1(String string1) {
			this.string1 = string1;
		}

		public int getInt1() {
			return int1;
		}

		public void setInt1(int int1) {
			this.int1 = int1;
		}

		public Integer getInt2() {
			return int2;
		}

		public void setInt2(Integer int2) {
			this.int2 = int2;
		}

		public TestEnum getEnum1() {
			return enum1;
		}

		public void setEnum1(TestEnum enum1) {
			this.enum1 = enum1;
		}

		public Map<String, String[]> getParams() {
			return params;
		}

		public void setParams(Map<String, String[]> params) {
			this.params = params;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != getClass()) {
				return false;
			}
			ParamModel other = (ParamModel) obj;
			EqualsBuilder ret = new EqualsBuilder();
			ret.append(this.int1, other.int1);
			ret.append(this.enum1, other.enum1);
			ret.append(this.int2, other.int2);
			ret.append(this.string1, other.string1);
			ret.append(this.intArray, other.intArray);
			ret.append(this.model, other.model);
			ret.append(this.names, other.names);
			ret.append(this.titles, other.titles);
			ret.append(this.date, other.date);
			ret.append(this.booleanValue, other.booleanValue);
			ret.append(this.booleanObject, other.booleanObject);
			ret.append(this.longValue, other.longValue);
			ret.append(this.longObject, other.longObject);
			ret.append(this.floatValue, other.floatValue);
			ret.append(this.floatObject, other.floatObject);
			ret.append(this.doubleValue, other.doubleValue);
			ret.append(this.doubleObject, other.doubleObject);
			ret.append(this.byteValue, other.byteValue);
			ret.append(this.byteObject, other.byteObject);
			ret.append(this.charValue, other.charValue);
			ret.append(this.charObject, other.charObject);
			ret.append(this.shortValue, other.shortValue);
			ret.append(this.shortObject, other.shortObject);
			if (!ret.isEquals()) {
				return false;
			}
			if (null == this.params && null == other.params) {
				return true;
			}
			if (null == this.params || null == other.params) {
				return false;
			}
			if (this.params.size() != other.params.size()) {
				return false;
			}
			for (String key : this.params.keySet()) {
				if (!other.params.containsKey(key)) {
					return false;
				}
				String[] value = this.params.get(key);
				String[] otherValue = other.params.get(key);
				if (!ArrayUtils.isEquals(value, otherValue)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public String toString() {
			StringBuilder ret = new StringBuilder();
			ret.append("int1=").append(this.int1);
			ret.append("\nint2=").append(this.int2);
			ret.append("\nintArray=").append(ArrayUtils.toString(this.intArray));
			ret.append("\nstring1=").append(this.string1);
			ret.append("\nenum1=").append(this.enum1);
			ret.append("\nparams=").append(this.params);
			ret.append("\nnames=").append(names);
			ret.append("\ntitles=").append(titles);
			ret.append("\nmodel=").append(this.model);
			ret.append("\ndate=").append(this.date).toString();
			ret.append("\nbooleanValue=").append(this.booleanValue).toString();
			ret.append("\nbooleanObject=").append(this.booleanObject).toString();
			ret.append("\nlongValue=").append(this.longValue).toString();
			ret.append("\nlongObject=").append(this.longObject).toString();
			ret.append("\nfloatValue=").append(this.floatValue).toString();
			ret.append("\nfloatObject=").append(this.floatObject).toString();
			ret.append("\ndoubleValue=").append(this.doubleValue).toString();
			ret.append("\ndoubleObject=").append(this.doubleObject).toString();
			ret.append("\nbyteValue=").append(this.byteValue).toString();
			ret.append("\nbyteObject=").append(this.byteObject).toString();
			ret.append("\ncharValue=").append(this.charValue).toString();
			ret.append("\ncharObject=").append(this.charObject).toString();
			ret.append("\nshortValue=").append(this.shortValue).toString();
			ret.append("\nshortObject=").append(this.shortObject).toString();
			return ret.toString();
		}

		public int[] getIntArray() {
			return intArray;
		}

		public void setIntArray(int[] intArray) {
			this.intArray = intArray;
		}

		public ParamModel getModel() {
			return model;
		}

		public void setModel(ParamModel model) {
			this.model = model;
		}

		public List<String> getNames() {
			return names;
		}

		public void setNames(List<String> names) {
			this.names = names;
		}

		public Set<String> getTitles() {
			return titles;
		}

		public void setTitles(Set<String> titles) {
			this.titles = titles;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public boolean isBooleanValue() {
			return booleanValue;
		}

		public void setBooleanValue(boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

		public Boolean getBooleanObject() {
			return booleanObject;
		}

		public void setBooleanObject(Boolean booleanObject) {
			this.booleanObject = booleanObject;
		}

		public long getLongValue() {
			return longValue;
		}

		public void setLongValue(long longValue) {
			this.longValue = longValue;
		}

		public Long getLongObject() {
			return longObject;
		}

		public void setLongObject(Long longObject) {
			this.longObject = longObject;
		}

		public float getFloatValue() {
			return floatValue;
		}

		public void setFloatValue(float floatValue) {
			this.floatValue = floatValue;
		}

		public Float getFloatObject() {
			return floatObject;
		}

		public void setFloatObject(Float floatObject) {
			this.floatObject = floatObject;
		}

		public double getDoubleValue() {
			return doubleValue;
		}

		public void setDoubleValue(double doubleValue) {
			this.doubleValue = doubleValue;
		}

		public Double getDoubleObject() {
			return doubleObject;
		}

		public void setDoubleObject(Double doubleObject) {
			this.doubleObject = doubleObject;
		}

		public byte getByteValue() {
			return byteValue;
		}

		public void setByteValue(byte byteValue) {
			this.byteValue = byteValue;
		}

		public Byte getByteObject() {
			return byteObject;
		}

		public void setByteObject(Byte byteObject) {
			this.byteObject = byteObject;
		}

		public char getCharValue() {
			return charValue;
		}

		public void setCharValue(char charValue) {
			this.charValue = charValue;
		}

		public Character getCharObject() {
			return charObject;
		}

		public void setCharObject(Character charObject) {
			this.charObject = charObject;
		}

		public short getShortValue() {
			return shortValue;
		}

		public void setShortValue(short shortValue) {
			this.shortValue = shortValue;
		}

		public Short getShortObject() {
			return shortObject;
		}

		public void setShortObject(Short shortObject) {
			this.shortObject = shortObject;
		}

	}

	public static class TestController {

		public void testAction() {
		}

		public void testPrimitiveParamAction(boolean bl, byte b, int i, long l, float f, double d, char ch, short s) {
		}

		public void testPrimitiveLikeParamAction(Boolean name, Integer i, String s, Date d) {
		}

		public void testEnumParamAction(TestEnum e) {
		}

		public void testRequestResponseAction(HttpServletRequest request, HttpServletResponse response) {
		}

		public void testMapParamAction(Map<String, Object[]> m) {
		}

		public void testBeanParam2Action(ParamModel2 model) {
		}

		public void testBeanParamAction(ParamModel model) {
		}

		public void testParamAnnotationAction(@Param(name = "username") String username, @Param(name = "password") String password) {
		}

		public void testParamAnnotationPrimitiveAction(@Param(name = "id") int id, @Param(name = "flag") boolean flag) {
		}
	}

}
