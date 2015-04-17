package org.sothis.core.util;

import java.util.EnumMap;

import org.sothis.core.util.BWList.MatchType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BWListTest {

	private static enum Fields {
		host, url, query
	}

	@Test
	public void testBWList() {
		BWList.Builder<Fields> builder = new BWList.Builder<BWListTest.Fields>(Fields.class);
		builder.group(Fields.host, MatchType.EQUALS);
		builder.addItem("abc.com").addItem("def.com");
		builder.group(Fields.url, MatchType.EXISTS);
		builder.group(Fields.query, MatchType.WUMANBER);
		builder.addItem("^defdef$").addItem("ccc$").addItem("^fff").addItem("c.c");
		BWList<Fields> bwlist = builder.compile();

		EnumMap<Fields, String> source = new EnumMap<Fields, String>(Fields.class);
		source.put(Fields.host, "abc.com");
		assertEquals(bwlist.matches(source, false), 1);
	}
}
