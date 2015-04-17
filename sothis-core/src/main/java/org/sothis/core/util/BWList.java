package org.sothis.core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class BWList<E extends Enum<E>> {

	private final EnumMap<E, EnumMap<MatchType, Group<E>>> groups;
	private final Class<E> fieldType;

	public static enum MatchType {
		EQUALS, WUMANBER, EXISTS, REGEX
	}

	private BWList(Class<E> fieldType) {
		this.fieldType = fieldType;
		this.groups = new EnumMap<E, EnumMap<MatchType, Group<E>>>(fieldType);
	}

	public BWList(String uri, Class<E> fieldType) throws IOException {
		this.fieldType = fieldType;
		this.groups = new EnumMap<E, EnumMap<MatchType, Group<E>>>(fieldType);
		loadFromFile(uri);
		compile();
	}

	public static class Builder<E extends Enum<E>> {
		final BWList<E> bwlist;
		Group<E> currentGroup;

		public Builder(Class<E> fieldType) {
			bwlist = new BWList<E>(fieldType);
		}

		public Builder<E> group(E field, MatchType type) {
			currentGroup = bwlist.getGroup(field, type);
			return this;
		}

		public Builder<E> addItem(String item) {
			this.currentGroup.addItem(item);
			return this;
		}

		public BWList<E> compile() {
			bwlist.compile();
			return bwlist;
		}
	}

	private void compile() {
		for (Map.Entry<E, EnumMap<MatchType, Group<E>>> entry : groups.entrySet()) {
			for (Map.Entry<MatchType, Group<E>> subEntry : entry.getValue().entrySet()) {
				subEntry.getValue().compile();
			}
		}
	}

	Group<E> getGroup(E field, MatchType type) {
		EnumMap<MatchType, Group<E>> mg = this.groups.get(field);
		if (null == mg) {
			mg = new EnumMap<MatchType, Group<E>>(MatchType.class);
			this.groups.put(field, mg);
		}
		Group<E> group = mg.get(type);
		if (null == group) {
			switch (type) {
			case EQUALS:
				group = new EqualsGroup<E>(field, type);
				break;
			case EXISTS:
				group = new ExistsGroup<E>(field, type);
				break;
			case WUMANBER:
				group = new WumanberGroup<E>(field, type);
				break;
			case REGEX:
				group = new RegexGroup<E>(field, type);
				break;
			default:
				throw new IllegalArgumentException("unsupported group match type: " + type);
			}
			mg.put(type, group);
		}
		return group;
	}

	private Group<E> parseGroupLine(String line, int lineno) {
		String[] split = line.trim().split("\\s");
		if (split.length != 2) {
			throw new IllegalArgumentException("invalid group line at line " + lineno + ": " + line);
		}
		try {
			E field = Enum.valueOf(this.fieldType, split[0]);
			MatchType type = MatchType.valueOf(split[1].toUpperCase());
			return getGroup(field, type);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("invalid group line at line " + lineno + ": " + line, e);
		}
	}

	private void loadFromFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		int lineno = 0;
		Group<E> group = null;
		try {
			while ((line = reader.readLine()) != null) {
				lineno++;
				if (line.isEmpty()) {
					continue;
				}
				char ch = line.charAt(0);
				switch (ch) {
				case '#':
				case '\r':
				case '\n':
					continue;
				case ' ':
				case '\t':
					if (null == group) {
						throw new IllegalArgumentException("invalid group format at line " + lineno + ": " + line);
					}
					group.addItem(line.trim());
					break;
				default:
					group = this.parseGroupLine(line, lineno);
					break;
				}
			}
		} finally {
			reader.close();
		}
	}

	public int matches(EnumMap<E, String> source, boolean matchAll) {
		int sizeSource = source.size();
		int sizeGroup = this.groups.size();
		int ret = 0;
		if (sizeSource < sizeGroup) {
			for (Map.Entry<E, String> entry : source.entrySet()) {
				String item = entry.getValue();
				EnumMap<MatchType, Group<E>> mg = this.groups.get(entry.getKey());
				if (null != mg) {
					for (Map.Entry<MatchType, Group<E>> mgEntry : mg.entrySet()) {
						ret += mgEntry.getValue().matches(item, matchAll);
						if (ret > 0 && !matchAll) {
							return ret;
						}
					}
				}
			}
		} else {
			for (Map.Entry<E, EnumMap<MatchType, Group<E>>> entry : this.groups.entrySet()) {
				String item = source.get(entry.getKey());
				if (null != item) {
					EnumMap<MatchType, Group<E>> mg = entry.getValue();
					for (Map.Entry<MatchType, Group<E>> mgEntry : mg.entrySet()) {
						ret += mgEntry.getValue().matches(item, matchAll);
						if (ret > 0 && !matchAll) {
							return ret;
						}
					}
				}
			}
		}
		return ret;
	}

	static abstract class Group<G extends Enum<G>> {
		G field;
		MatchType type;

		public Group(G field, MatchType type) {
			super();
			this.field = field;
			this.type = type;
		}

		abstract int matches(String string, boolean matchAll);

		abstract void addItem(String item);

		void compile() {

		}
	}

	static class EqualsGroup<G extends Enum<G>> extends Group<G> {
		Set<String> items = new HashSet<String>();

		public EqualsGroup(G field, MatchType type) {
			super(field, type);
		}

		@Override
		int matches(String string, boolean matchAll) {
			return items.contains(string) ? 1 : 0;
		}

		@Override
		void addItem(String item) {
			items.add(item);
		}

	}

	static class WumanberGroup<G extends Enum<G>> extends Group<G> {
		Wumanber wumanber = new Wumanber();

		public WumanberGroup(G field, MatchType type) {
			super(field, type);
		}

		@Override
		int matches(String string, boolean matchAll) {
			return wumanber.matches(string, matchAll);
		}

		@Override
		void addItem(String item) {
			if (!wumanber.addPattern(item)) {
				throw new IllegalArgumentException("invalid wumanber item: " + item);
			}
		}

		@Override
		void compile() {
			wumanber.compile();
		}

	}

	static class ExistsGroup<G extends Enum<G>> extends Group<G> {

		public ExistsGroup(G field, MatchType type) {
			super(field, type);
		}

		@Override
		int matches(String string, boolean matchAll) {
			return 1;
		}

		@Override
		void addItem(String item) {
			throw new IllegalArgumentException("exists group not allow items.");
		}

	}

	static class RegexGroup<G extends Enum<G>> extends Group<G> {

		List<Pattern> patterns = new LinkedList<Pattern>();

		public RegexGroup(G field, MatchType type) {
			super(field, type);
		}

		@Override
		int matches(String string, boolean matchAll) {
			int ret = 0;
			for (Pattern p : patterns) {
				if (p.matcher(string).matches()) {
					ret++;
				}
				if (ret > 0 && !matchAll) {
					break;
				}
			}
			return ret;
		}

		@Override
		void addItem(String item) {
			patterns.add(Pattern.compile(item));
		}

	}
}
