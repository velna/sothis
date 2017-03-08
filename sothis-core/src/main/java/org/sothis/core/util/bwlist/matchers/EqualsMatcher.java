package org.sothis.core.util.bwlist.matchers;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.sothis.core.util.bwlist.CompileException;
import org.sothis.core.util.bwlist.Matcher;
import org.sothis.core.util.bwlist.MatcherConf;

@MatcherConf(name = "equals", hasValues = true)
public class EqualsMatcher extends Matcher {

	private final Map<String, Set<Integer>> matchMap = new HashMap<>();

	public EqualsMatcher() throws CompileException {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Integer> matches(String target) {
		Set<Integer> matchIds = this.matchMap.get(target);
		return null == matchIds ? Collections.EMPTY_SET : matchIds;
	}

	@Override
	public void addValues(Set<String> values, int matchId) {
		for (String value : values) {
			Set<Integer> matchIds = this.matchMap.get(value);
			if (null == matchIds) {
				matchIds = new LinkedHashSet<>();
				this.matchMap.put(value, matchIds);
			}
			matchIds.add(matchId);
		}
	}

	@Override
	public void compile() throws CompileException {

	}

	@Override
	public int size() {
		return this.matchMap.size();
	}

}
