package org.sothis.core.util.bwlist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class BWResult {

	private final Map<Integer, Field> matches = new HashMap<>();
	private final Set<Integer> sources = new HashSet<>();

	public BWResult() {
	}

	void setExpressionResult(int expressionId, boolean matchResult, String fieldName, String target) {
		this.matches.put(expressionId, matchResult ? new Field(fieldName, target) : Field.NULL);
	}

	Boolean getExpressionResult(int expressionId) {
		Field field = this.matches.get(expressionId);
		return null == field ? null : (field == Field.NULL ? Boolean.FALSE : Boolean.TRUE);
	}

	void set(int sourceId) {
		this.sources.add(sourceId);
	}

	public boolean contains(int sourceId) {
		return this.sources.contains(sourceId);
	}

	public boolean isEmpty() {
		return sources.isEmpty();
	}

	public static class Field {
		private final String name;
		private final String value;

		public static final Field NULL = new Field(null, null);

		public Field(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

	}

	void clear() {
		this.matches.clear();
		this.sources.clear();
	}
}
