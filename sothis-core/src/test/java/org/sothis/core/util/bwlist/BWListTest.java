package org.sothis.core.util.bwlist;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BWListTest {

	@Test(dataProvider = "matches")
	public void testMatches(URI[] uris, Map<String, String> target, int[] matchIndexes) throws CompileException {
		BWList bwlist = BWList.createDefaultBWList();
		List<Source> sources = new ArrayList<>(uris.length);
		for (URI uri : uris) {
			sources.add(bwlist.add(uri));
		}

		bwlist.compile();

		BWResult result = new BWResult();
		bwlist.matches(target, result);

		Set<Integer> matchIndexSet = new HashSet<>();
		for (int idx : matchIndexes) {
			matchIndexSet.add(idx);
		}

		Set<Integer> resultSet = new HashSet<>();
		for (Integer sid : result) {
			for (int i = 0; i < sources.size(); i++) {
				if (sources.get(i).getId() == sid) {
					resultSet.add(i);
					break;
				}
			}
		}

		Assert.assertEquals(resultSet, matchIndexSet);
	}

	@DataProvider(name = "matches")
	public Object[][] matchesData() throws Exception {
		List<Object[]> paramList = new ArrayList<Object[]>();

		Map<String, String> target1 = new HashMap<>();
		target1.put("host", "www.baidu.com");
		target1.put("url", "www.baidu.com/s");
		target1.put("user-agent", "Chrome");

		Map<String, String> target2 = new HashMap<>();
		target2.put("host", "www.hao123.com");
		target2.put("url", "www.baidu.com/s");
		target2.put("user-agent", "Chrome");

		Map<String, String> target3 = new HashMap<>();
		target3.put("host", "www.google.com");

		URI test1 = this.getClass().getClassLoader().getResource("bwlist/test1.bwl").toURI();
		URI test2 = this.getClass().getClassLoader().getResource("bwlist/test2.bwl").toURI();
		URI test3 = this.getClass().getClassLoader().getResource("bwlist/test3.bwl").toURI();
		URI empty = this.getClass().getClassLoader().getResource("bwlist/empty.bwl").toURI();

		paramList.add(new Object[] { new URI[] { test1 }, target1, new int[] { 0 } });
		paramList.add(new Object[] { new URI[] { test1, empty }, target1, new int[] { 0 } });
		paramList.add(new Object[] { new URI[] { test2 }, target2, new int[] { 0 } });
		paramList.add(new Object[] { new URI[] { test2 }, target3, new int[] {} });
		paramList.add(new Object[] { new URI[] { test1, test2 }, target3, new int[] {} });
		paramList.add(new Object[] { new URI[] { test1, test2 }, target2, new int[] { 0, 1 } });
		paramList.add(new Object[] { new URI[] { test1, empty, test2 }, target2, new int[] { 0, 2 } });

		// test3 is same as test2
		paramList.add(new Object[] { new URI[] { test3 }, target2, new int[] { 0 } });
		paramList.add(new Object[] { new URI[] { test3 }, target3, new int[] {} });
		paramList.add(new Object[] { new URI[] { test1, test3 }, target3, new int[] {} });
		paramList.add(new Object[] { new URI[] { test1, test3 }, target2, new int[] { 0, 1 } });
		paramList.add(new Object[] { new URI[] { test1, empty, test3 }, target2, new int[] { 0, 2 } });

		return paramList.toArray(new Object[paramList.size()][4]);
	}

	public void testEmptyBwlist() throws Exception {
		BWList bwlist = BWList.createDefaultBWList();
		bwlist.add(this.getClass().getClassLoader().getResource("bwlist/empty.bwl").toURI());
		bwlist.compile();
	}

	@Test(dataProvider = "compileException", expectedExceptions = { CompileException.class })
	public void testCompileException(String uri) throws CompileException, URISyntaxException {
		BWList bwlist = BWList.createDefaultBWList();
		bwlist.add(this.getClass().getClassLoader().getResource(uri).toURI());
		bwlist.compile();
	}

	@DataProvider(name = "compileException")
	public Object[][] compileExceptionData() throws Exception {
		List<Object[]> paramList = new ArrayList<Object[]>();

		paramList.add(new Object[] { "bwlist/error1.bwl" });
		paramList.add(new Object[] { "bwlist/error2.bwl" });
		paramList.add(new Object[] { "bwlist/error3.bwl" });
		paramList.add(new Object[] { "bwlist/error4.bwl" });
		paramList.add(new Object[] { "bwlist/error5.bwl" });
		paramList.add(new Object[] { "bwlist/error6.bwl" });

		return paramList.toArray(new Object[paramList.size()][1]);
	}
}
