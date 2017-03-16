package org.sothis.core.util.bwlist.matchers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.sothis.core.util.Wumanber;
import org.sothis.core.util.bwlist.CompileException;
import org.sothis.core.util.bwlist.Matcher;
import org.sothis.core.util.bwlist.MatcherConf;

@MatcherConf(name = "matches", hasValues = true)
public class WumanberMatcher extends Matcher {

	private Wumanber<Integer> wm = new Wumanber<>();

	public WumanberMatcher() throws CompileException {
		super();
	}

	@Override
	public Set<Integer> matches(String target) {
		if (null == target) {
			return Collections.emptySet();
		}
		WumanberMatchHandler handler = new WumanberMatchHandler();
		wm.matches(target, handler);
		return handler.matchIds;
	}

	@Override
	public void addValues(Set<String> values, int matchId) {
		for (String value : values) {
			wm.addPattern(value, matchId);
		}
	}

	@Override
	public void compile() throws CompileException {
		wm.compile();
	}

	@Override
	public int size() {
		return wm.size();
	}

	private static class WumanberMatchHandler implements Wumanber.MatchHandler<Integer> {

		private final Set<Integer> matchIds = new HashSet<>();

		@Override
		public boolean onMatch(Wumanber<Integer> wm, String str, String pattern, int matchOptions, Iterator<Integer> users) {
			while (users.hasNext()) {
				matchIds.add(users.next());
			}
			return true;
		}

	}

}
