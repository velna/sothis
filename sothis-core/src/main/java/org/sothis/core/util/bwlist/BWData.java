package org.sothis.core.util.bwlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.StrTokenizer;

class BWData {

	private final BWList bwlist;
	private int nextSourceId;
	private int nextExprId;
	private boolean compiled = false;
	private final Map<Integer, SourceInfo> sources = new LinkedHashMap<>();
	private final Map<String, Matcher> matchers = new HashMap<>();
	private final Map<String, Integer> exprIdMap = new HashMap<>();

	public BWData(BWList bwlist) {
		this.bwlist = bwlist;
		nextSourceId = 1;
		nextExprId = 1;
	}

	public boolean isEmpty() {
		return this.nextSourceId == 1;
	}

	public void copySources(BWData data) {
		this.nextSourceId = data.nextSourceId;
		this.sources.putAll(data.sources);
	}

	public void clear() {
		nextSourceId = 1;
		nextExprId = 1;
		compiled = false;
		this.sources.clear();
		this.matchers.clear();
		this.exprIdMap.clear();
	}

	public int add(Source source) throws IllegalStateException {
		if (this.compiled) {
			throw new IllegalStateException("can not operate on a compiled bwlist.");
		}
		source.setId(this.nextSourceId++);
		this.sources.put(source.getId(), new SourceInfo(source));
		return source.getId();
	}

	public void remove(int sourceId) throws IllegalStateException {
		if (this.compiled) {
			throw new IllegalStateException("can not operate on a compiled bwlist.");
		}
		this.sources.remove(sourceId);
	}

	private int getExprId(int sourceId, String field, String matcherName, String groupId) {
		String exprIdKey;
		Integer exprId;
		if (null != groupId) {
			exprIdKey = String.format("%d:%s %s %s", sourceId, field, matcherName, groupId);
		} else {
			exprIdKey = String.format("%d:%s %s", sourceId, field, matcherName);
		}
		exprId = this.exprIdMap.get(exprIdKey);
		if (null == exprId) {
			exprId = this.nextExprId++;
			this.exprIdMap.put(exprIdKey, exprId);
		}
		return exprId;
	}

	private Matcher getOrCreateMatcher(String field, String matcherName) throws CompileException {
		String key = field + " " + matcherName;
		Matcher matcher = this.matchers.get(key);
		if (null == matcher) {
			matcher = this.bwlist.newMatcher(matcherName);
			this.matchers.put(key, matcher);
		}
		return matcher;
	}

	private Expression parseLeafExpr(Source source, SourceData sourceData, String field, StrTokenizer tokenizer)
			throws CompileException {
		if (null == field) {
			throw new CompileException("null field at [" + tokenizer.getContent() + "]");
		}
		if (!tokenizer.hasNext()) {
			throw new CompileException("unexpected end of expression at " + tokenizer.getContent());
		}
		String matcherName = tokenizer.nextToken();
		Matcher matcher = getOrCreateMatcher(field, matcherName);
		String varName = null;
		if (matcher.hasValues()) {
			if (!tokenizer.hasNext()) {
				throw new CompileException("unexpected end of expression at " + tokenizer.getContent());
			}
			varName = tokenizer.nextToken();
		}
		int exprId = getExprId(source.getId(), field, matcher.getName(), varName);
		if (matcher.hasValues()) {
			Set<String> values = sourceData.getValues(varName);
			if (null == values) {
				throw new CompileException("undefined var: " + varName);
			}
			matcher.addValues(values, exprId);
		} else {
			matcher.addValues(null, exprId);
		}
		Expression exprNew = new LeafExpression(exprId, field, matcher);
		return exprNew;
	}

	private Expression parseExpr(Source source, SourceData sourceData, StrTokenizer tokenizer, Expression exprLeft)
			throws CompileException {
		if (!tokenizer.hasNext()) {
			throw new CompileException("unexpected end of expression at " + tokenizer.getContent());
		}
		String token = tokenizer.nextToken();
		Logic logic = null;
		Expression exprNew = null;
		if (exprLeft != null) {
			if ("and".equalsIgnoreCase(token) || "&&".equals(token) || "&".equals(token)) {
				logic = Logic.AND;
			} else if ("or".equalsIgnoreCase(token) || "||".equals(token) || "|".equals(token)) {
				logic = Logic.OR;
			} else {
				throw new CompileException("undefined logic " + token + " at [" + tokenizer.getContent() + "]");
			}
			Expression exprRight = parseExpr(source, sourceData, tokenizer, null);
			if (exprRight == null) {
				return null;
			}
			exprNew = new CompositeExpression(logic, exprLeft, exprRight);
		} else {
			if ("(".equals(token)) {
				do {
					exprNew = parseExpr(source, sourceData, tokenizer, exprLeft);
					if (exprNew == null) {
						return null;
					}
					if (")".equals(tokenizer.nextToken())) {
						return exprNew;
					} else {
						tokenizer.previousToken();
						exprLeft = exprNew;
					}
				} while (tokenizer.hasNext());
				throw new CompileException("missing right brace at [" + tokenizer.getContent() + "]");
			} else if ("not".equalsIgnoreCase(token) || "!".equals(token)) {
				Expression exprRight = parseExpr(source, sourceData, tokenizer, null);
				if (null == exprRight) {
					return null;
				}
				exprNew = new CompositeExpression(Logic.NOT, exprRight, null);
			} else {
				exprNew = parseLeafExpr(source, sourceData, token, tokenizer);
			}
		}
		return exprNew;
	}

	private Expression compileSourceData(Source source, SourceData sourceData) throws CompileException {
		Expression exprParent = null;
		Set<String> expressions = sourceData.getExpressions();
		for (String expression : expressions) {
			StrTokenizer tokenizer = new StrTokenizer(expression);
			Expression exprNew = null, expr = null;
			while (tokenizer.hasNext()) {
				exprNew = parseExpr(source, sourceData, tokenizer, expr);
				if (null == exprNew) {
					throw new CompileException("error parse expression: " + expression);
				}
				expr = exprNew;
			}
			if (exprParent != null) {
				expr = new CompositeExpression(sourceData.getLogic(), exprParent, exprNew);
				exprParent = expr;
			} else {
				exprParent = exprNew;
			}
		}
		return exprParent;
	}

	public boolean compile(boolean forceReload) throws CompileException {
		boolean modified = false;
		List<SourceInfo> unmodified = new ArrayList<>();
		for (SourceInfo sourceInfo : this.sources.values()) {
			SourceData sourceData = sourceInfo.source.load(modified ? true : forceReload);
			if (null != sourceData) {
				modified = true;
				sourceInfo.expression = compileSourceData(sourceInfo.source, sourceData);
			} else {
				unmodified.add(sourceInfo);
			}
		}
		if (modified) {
			for (SourceInfo sourceInfo : unmodified) {
				SourceData sourceData = sourceInfo.source.load(true);
				if (null != sourceData) {
					sourceInfo.expression = compileSourceData(sourceInfo.source, sourceData);
				}
			}
		}
		for (Matcher matcher : matchers.values()) {
			matcher.compile();
		}
		this.matchers.clear();
		compiled = true;
		return modified;
	}

	public boolean matches(Map<String, String> targets, BWResult result) throws IllegalStateException {
		if (!this.compiled) {
			throw new IllegalStateException("bwlist not compiled.");
		}
		result.clear();
		boolean matches = false;
		for (SourceInfo sourceInfo : this.sources.values()) {
			if (sourceInfo.expression != null && sourceInfo.expression.matches(targets, result)) {
				result.set(sourceInfo.source.getId());
				matches = true;
			}
		}
		return matches;
	}

	private static class SourceInfo {
		Source source;
		Expression expression;

		public SourceInfo(Source source) {
			this.source = source;
		}
	}
}
