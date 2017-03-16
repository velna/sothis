package org.sothis.core.util.bwlist;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class SourceData {

	private final Logic logic;
	private final Map<String, Set<String>> values = new HashMap<>();
	private final Set<String> expressions = new LinkedHashSet<>();

	public SourceData(Logic logic) {
		super();
		if (logic != Logic.AND && logic != Logic.OR) {
			throw new IllegalArgumentException("only AND and OR is allowed.");
		}
		this.logic = logic;
	}

	public Logic getLogic() {
		return logic;
	}

	public void addValues(String varName, Collection<String> values) {
		Set<String> vs = this.values.get(varName);
		if (null == vs) {
			vs = new HashSet<>();
			this.values.put(varName, vs);
		}
		vs.addAll(values);
	}

	public void addValue(String varName, String value) {
		Set<String> vs = this.values.get(varName);
		if (null == vs) {
			vs = new HashSet<>();
			this.values.put(varName, vs);
		}
		vs.add(value);
	}

	public Set<String> getValues(String varName) {
		Set<String> vs = this.values.get(varName);
		if (null == vs) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(vs);
		}
	}

	public void addExpression(String expression) {
		this.expressions.add(normalizeExpr(expression));
	}

	public Set<String> getExpressions() {
		return Collections.unmodifiableSet(expressions);
	}

	private static String normalizeExpr(String expr) {
		StringBuilder sb = new StringBuilder();
		char prev = 0;
		for (int i = 0; i < expr.length(); i++) {
			char ch = expr.charAt(i);
			switch (ch) {
			case '(':
			case ')':
			case '!':
				if (prev != 0 && prev != ' ' && prev != '\t') {
					sb.append(' ');
				}
				break;
			case ' ':
			case '\t':
				break;
			default:
				if (prev != 0 && (prev == '(' || prev == ')' || prev == '!')) {
					sb.append(' ');
				}
				break;
			}
			sb.append(ch == '@' ? ' ' : ch);
			prev = ch;
		}
		return sb.toString().trim();
	}

	public int size() {
		int size = 0;
		for (Set<String> vs : this.values.values()) {
			size += vs.size();
		}
		return size;
	}
}
