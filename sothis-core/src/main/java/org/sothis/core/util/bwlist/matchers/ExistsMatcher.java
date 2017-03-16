package org.sothis.core.util.bwlist.matchers;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.sothis.core.util.bwlist.CompileException;
import org.sothis.core.util.bwlist.Matcher;
import org.sothis.core.util.bwlist.MatcherConf;

@MatcherConf(name = "exists", hasValues = false)
public class ExistsMatcher extends Matcher {

	private final Set<Integer> matchIds = new LinkedHashSet<>();

	public ExistsMatcher() throws CompileException {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Integer> matches(String target) {
		return null == target ? Collections.EMPTY_SET : matchIds;
	}

	@Override
	public void addValues(Set<String> values, int matchId) {
		matchIds.add(matchId);
	}

	@Override
	public void compile() throws CompileException {

	}

	@Override
	public int size() {
		return 1;
	}

}
