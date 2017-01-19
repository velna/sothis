package org.sothis.core.util.bwlist;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BWList {
	private AtomicInteger version = new AtomicInteger(0);
	private Lock lock = new ReentrantLock();
	private Map<String, BWMatcher> matcherMap = new HashMap<>();
	private BWData data;
	private BWData tmpData;
	private BWData nextData;
	private Map<String, Class<? extends BWMatcher>> matcherRegistry = new HashMap<>();

	private BWList() {

	}

	public static BWList createDefaultBWList() {
		return createBWList();
	}

	@SafeVarargs
	public static BWList createBWList(Class<? extends BWMatcher>... matcherClasses) {
		BWList bwlist = new BWList();
		for (Class<? extends BWMatcher> clazz : matcherClasses) {
			bwlist.registerMatcher(clazz);
		}
		return bwlist;
	}

	public void registerMatcher(Class<? extends BWMatcher> matcherClass) {
		BWName aname = matcherClass.getAnnotation(BWName.class);
		if (null == aname) {
			throw new IllegalArgumentException("matcher class " + matcherClass + " must be annotated by " + BWName.class);
		}
		this.matcherRegistry.put(aname.value(), matcherClass);
	}

	public int add(BWSource source) {
		return 0;
	}

	public boolean remove(BWSource source) {
		return false;
	}

	public boolean compile() {
		return false;
	}

	public boolean refresh() {
		return false;
	}

	public int matches(Map<String, String> data, BWResult result) {
		return 0;
	}

	public BWResult newResult() {
		return this.data.newResult();
	}

	public int getVersion() {
		return this.version.get();
	}
}
