package org.sothis.core.util.bwlist;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.sothis.core.util.bwlist.loaders.FileSourceLoader;
import org.sothis.core.util.bwlist.matchers.EqualsMatcher;
import org.sothis.core.util.bwlist.matchers.ExistsMatcher;
import org.sothis.core.util.bwlist.matchers.WumanberMatcher;

public final class BWList {

	private Lock lock = new ReentrantLock();

	private BWData data = new BWData(this);
	private BWData tmpData = new BWData(this);
	private BWData nextData = new BWData(this);
	private Map<String, Class<? extends Matcher>> matcherRegistry = new HashMap<>();
	private Map<String, SourceLoader> loaderRegistry = new HashMap<>();

	private BWList() {
	}

	public static BWList createDefaultBWList() {
		BWList bwlist = new BWList();
		bwlist.addMatcher(EqualsMatcher.class);
		bwlist.addMatcher(ExistsMatcher.class);
		bwlist.addMatcher(WumanberMatcher.class);
		bwlist.setSourceLoader("file", new FileSourceLoader());
		return bwlist;
	}

	public BWList addMatcher(Class<? extends Matcher> matcherClass) {
		MatcherConf config = matcherClass.getAnnotation(MatcherConf.class);
		if (null == config) {
			throw new IllegalArgumentException("matcher class " + matcherClass.getName() + " must be annotated by "
					+ MatcherConf.class.getName());
		}
		this.matcherRegistry.put(config.name(), matcherClass);
		return this;
	}

	public BWList setSourceLoader(String scheme, SourceLoader loader) {
		this.loaderRegistry.put(scheme, loader);
		return this;
	}

	public SourceLoader findSourceLoader(String str, SourceLoader defaultLoader) throws SourceLoadException {
		int i = str.indexOf("://");
		SourceLoader loader;
		if (i < 0) {
			loader = this.loaderRegistry.get(str);
		} else {
			loader = this.loaderRegistry.get(str.substring(0, i));
		}
		return null == loader ? defaultLoader : loader;
	}

	Matcher newMatcher(String matcherName) throws CompileException {
		Class<? extends Matcher> matcherClass = this.matcherRegistry.get(matcherName);
		if (null == matcherClass) {
			throw new CompileException("no matcher found: " + matcherName);
		}
		try {
			return matcherClass.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
			throw new CompileException("error init matcher " + matcherName, e);
		}
	}

	public Source add(String uri) {
		Source source;
		this.lock.lock();
		try {
			BWData data = this.nextData;
			if (data.isEmpty()) {
				data.copySources(this.data);
			}
			source = new Source(this, uri);
			data.add(source);
		} finally {
			this.lock.unlock();
		}
		return source;
	}

	public BWList remove(int sourceId) {
		this.lock.lock();
		try {
			BWData data = this.nextData;
			if (data.isEmpty()) {
				data.copySources(this.nextData);
			}
			data.remove(sourceId);
		} finally {
			this.lock.unlock();
		}
		return this;
	}

	public BWList compile() throws CompileException {
		this.lock.lock();
		try {
			this.tmpData.clear();
			this.nextData.compile(true);
			BWData data = this.data;
			this.data = this.nextData;
			this.nextData = this.tmpData;
			this.tmpData = data;
		} finally {
			this.lock.unlock();
		}
		return this;
	}

	public boolean check(boolean forceReload) throws CompileException {
		if (forceReload) {
			this.lock.lock();
		} else {
			if (!this.lock.tryLock()) {
				return false;
			}
		}
		try {
			this.tmpData.clear();
			if (this.nextData.isEmpty()) {
				this.tmpData.copySources(this.data);
			} else {
				forceReload = true;
				this.tmpData.copySources(this.nextData);
			}
			this.nextData.clear();
			this.tmpData.compile(forceReload);
			BWData data = this.data;
			this.data = this.tmpData;
			this.tmpData = data;
			return true;
		} finally {
			this.lock.unlock();
		}
	}

	public boolean matches(Map<String, String> targets, BWResult result) throws IllegalStateException {
		return this.data.matches(targets, result);
	}

}
