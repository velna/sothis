package org.sothis.core.util.bwlist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BWSourceData {
	private int id;
	private Object attachment;
	private Map<String, List<String>> itemGroups = new HashMap<>();
	private Set<String> exprMap = new HashSet<>();
}
