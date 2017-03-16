package org.sothis.core.util.bwlist;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class Source {
	private int id;
	private final URI uri;
	private final BWList bwList;
	private Map<Object, Object> attributes = new HashMap<>();

	public Source(BWList bwList, URI uri) {
		super();
		this.bwList = bwList;
		this.uri = uri;
	}

	SourceData load(boolean forceReload) throws CompileException {
		SourceLoader loader = bwList.findSourceLoader(uri.getScheme(), null);
		if (null == loader) {
			throw new CompileException("can not find loader of type: " + uri.getScheme());
		}
		return loader.load(this, forceReload);
	}

	final void setId(int id) {
		this.id = id;
	}

	public final int getId() {
		return id;
	}

	public URI getUri() {
		return uri;
	}

	public BWList getBwList() {
		return bwList;
	}

	public Object getAttribute(Object key) {
		return this.attributes.get(key);
	}

	public Object setAttribute(Object key, Object value) {
		return this.attributes.put(key, value);
	}

}
