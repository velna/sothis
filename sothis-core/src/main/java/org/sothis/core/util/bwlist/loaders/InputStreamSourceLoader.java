package org.sothis.core.util.bwlist.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.sothis.core.util.bwlist.Source;
import org.sothis.core.util.bwlist.SourceData;
import org.sothis.core.util.bwlist.SourceLoadException;
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

	protected void loadFromStream(Source source, SourceData sourceData, InputStream in) throws IOException {
		String line, name = null, varName = null;
		int ln = 0, nextVarId = 0;
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(in));
		try {
			while ((line = reader.readLine()) != null) {
				ln++;
				if (line.length() == 0 || line.charAt(0) == '#') {
					continue;
				}
				char ch = line.charAt(0);
				if (Character.isWhitespace(ch)) {
					if (name == null || varName == null) {
						throw new SourceLoadException("bwlist syntax error, no group found at line " + ln + ": [" + line + "]");
					}
					sourceData.addValue(varName, line.trim());
				} else {
					StringTokenizer tokenizer = new StringTokenizer(line);
					if (!tokenizer.hasMoreTokens()) {
						throw new SourceLoadException("bwlist syntax error, group name expected at line " + ln + ": [" + line
								+ "]");
					}
					name = tokenizer.nextToken();
					if (name.charAt(0) == '@') {
						sourceData.addExpression(line.trim());
					} else {
						if (name.charAt(0) == '$') {
							varName = name;
						} else {
							if (!tokenizer.hasMoreTokens()) {
								throw new SourceLoadException("bwlist syntax error, group type expected at line " + ln + ": ["
										+ line + "]");
							}
							varName = "#$var_" + (++nextVarId);
							sourceData.addExpression("@" + name + " " + tokenizer.nextToken() + " " + varName);
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
