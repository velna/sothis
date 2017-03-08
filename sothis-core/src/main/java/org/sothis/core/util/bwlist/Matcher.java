package org.sothis.core.util.bwlist;

import java.util.Set;

public abstract class Matcher {

	private final MatcherConf conf;

	public Matcher() throws CompileException {
		conf = this.getClass().getAnnotation(MatcherConf.class);
		if (null == conf) {
			throw new CompileException("no annotation of class " + MatcherConf.class.getName() + " found on "
					+ this.getClass().getName());
		}
	}

	abstract public Set<Integer> matches(String target);

	abstract public void addValues(Set<String> values, int matchId);

	abstract public void compile() throws CompileException;

	abstract public int size();

	public final String getName() {
		return conf.name();
	}

	public final boolean hasValues() {
		return conf.hasValues();
	}
}
