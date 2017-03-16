package org.sothis.core.util.bwlist.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.sothis.core.util.bwlist.CompileException;
import org.sothis.core.util.bwlist.MatcherConf;
import org.sothis.core.util.bwlist.Source;
import org.sothis.core.util.bwlist.SourceData;
import org.sothis.core.util.bwlist.SourceLoader;

public abstract class InputStreamSourceLoader implements SourceLoader {

	protected Set<String> loadValuesFromStream(InputStream in) throws IOException {
		Set<String> values = new HashSet<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty()) {
					values.add(line.trim());
				}
			}
		} finally {
			reader.close();
		}
		return values;
	}

	protected void loadFromStream(Source source, SourceData sourceData, InputStream in) throws IOException, CompileException {
		String line, name = null, varName = null;
		int ln = 0, nextVarId = 0;
		BufferedReader reader = null;
		MatcherConf matcherConf = null;
		reader = new BufferedReader(new InputStreamReader(in));
		try {
			while ((line = reader.readLine()) != null) {
				ln++;
				if (line.trim().length() == 0 || line.charAt(0) == '#') {
					continue;
				}
				char ch = line.charAt(0);
				if (Character.isWhitespace(ch)) {
					if (varName == null) {
						if (null != matcherConf && !matcherConf.hasValues()) {
							throw new CompileException("bwlist syntax error, matcher of type \"" + matcherConf.name()
									+ "\" can not have values: [" + line + "] at line " + ln);
						} else {
							throw new CompileException("bwlist syntax error: [" + line + "] at line " + ln);
						}
					}
					sourceData.addValue(varName, line.trim());
				} else if (ch == '@') {
					sourceData.addExpression(line.trim());
				} else {
					StringTokenizer tokenizer = new StringTokenizer(line);
					if (!tokenizer.hasMoreTokens()) {
						throw new CompileException("bwlist syntax error: [" + line + "] at line " + ln);
					}
					name = tokenizer.nextToken();
					matcherConf = null;
					varName = null;
					if (name.charAt(0) == '$') {
						varName = name;
						if (varName.length() == 1) {
							throw new CompileException("bwlist syntax error, empty variable name: [" + line + "] at line " + ln);
						}
					} else {
						if (!tokenizer.hasMoreTokens()) {
							throw new CompileException("bwlist syntax error, matcher type expected: [" + line + "] at line " + ln);
						}
						String matcherName = tokenizer.nextToken();
						matcherConf = source.getBwList().getMatcherConf(matcherName);
						if (matcherConf.hasValues()) {
							varName = "#$var_" + (++nextVarId);
							sourceData.addExpression("@" + name + " " + matcherName + " " + varName);
						} else {
							sourceData.addExpression("@" + name + " " + matcherName);
						}
					}
					if (tokenizer.hasMoreTokens()) {
						String uri = tokenizer.nextToken();
						Set<String> values = source.getBwList().findSourceLoader(uri, this).loadValues(source, uri);
						sourceData.addValues(varName, values);
					}
				}
			}
		} finally {
			reader.close();
		}
	}
}
