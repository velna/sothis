package org.sothis.core.util.bwlist;

import java.util.Map;

public interface Expression {
	boolean matches(Map<String, String> targets, BWResult result);
}
