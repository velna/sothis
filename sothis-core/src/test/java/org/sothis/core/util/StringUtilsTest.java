package org.sothis.core.util;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StringUtilsTest {

	@Test(dataProvider = "underlined")
	public void testUnderlined(String test, String expect) {
		String result = StringUtils.underlined(test);
		Assert.assertEquals(result, expect);
	}

	@DataProvider(name = "underlined")
	public Object[][] underlinedDataProvider() {
		List<Object[]> paramList = new ArrayList<Object[]>();

		paramList.add(new Object[] { "GetSome", "get_some" });
		paramList.add(new Object[] { "GGetSome", "g_get_some" });
		paramList.add(new Object[] { "GGGetSome", "gg_get_some" });
		paramList.add(new Object[] { "getSome", "get_some" });
		paramList.add(new Object[] { "getSSome", "get_s_some" });
		paramList.add(new Object[] { "GetSSSome", "get_ss_some" });
		paramList.add(new Object[] { "GetSomeD", "get_some_d" });
		paramList.add(new Object[] { "GetSomeDD", "get_some_dd" });
		paramList.add(new Object[] { "Get_Some", "get_some" });
		paramList.add(new Object[] { "Get_Some_value", "get_some_value" });
		paramList.add(new Object[] { "get_some_value", "get_some_value" });
		paramList.add(new Object[] { "__Get_Some", "__get_some" });
		paramList.add(new Object[] { "/abcDef/doSomeAction", "/abc_def/do_some_action" });
		paramList.add(new Object[] { "/AbcDef/DoSomeAction", "/abc_def/do_some_action" });

		Object[][] result = new Object[paramList.size()][2];
		result = paramList.toArray(result);
		return result;
	}
}
