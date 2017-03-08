package org.sothis.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class Wumanber<T> {

	public interface MatchHandler<T> {
		/**
		 * 
		 * @param wm
		 * @param str
		 * @param pattern
		 * @param matchOptions
		 * @param users
		 * @return false for stop matching, else return true
		 */
		boolean onMatch(Wumanber<T> wm, String str, String pattern, int matchOptions, Iterator<T> users);
	}

	public static final int CONTAINS = 1;
	public static final int PREFIX = 2;
	public static final int SUFFIX = 4;
	public static final int FULL = 8;

	private static final int MIN_BLOCK = 2;
	private static final int TABLE_SIZE = 65536;

	private final List<WMPattern<T>> patternList = new ArrayList<WMPattern<T>>();
	private int minLen = Integer.MAX_VALUE;
	private int[] shift = new int[TABLE_SIZE];
	private int[] hash = new int[TABLE_SIZE];
	private Map<String, WMPattern<T>> pMap = new HashMap<>();
	private boolean compiled;

	public boolean addPattern(String pattern, T user) {
		if (compiled) {
			throw new IllegalStateException("Wumanber is compiled already.");
		}
		if (StringUtils.length(pattern) < MIN_BLOCK) {
			return false;
		}
		char c0 = pattern.charAt(0);
		char cn = pattern.charAt(pattern.length() - 1);
		int pb = 0, plen = pattern.length(), opt = 0;
		switch (c0) {
		case '^':
			pb++;
			switch (cn) {
			case '$':
				opt |= FULL;
				plen--;
				break;
			case '*':
				opt |= PREFIX;
				plen--;
				break;
			default:
				opt |= PREFIX;
				break;
			}
			break;
		case '*':
			pb++;
			switch (cn) {
			case '$':
				opt |= SUFFIX;
				plen--;
				break;
			case '*':
				opt |= CONTAINS;
				plen--;
				break;
			default:
				opt |= CONTAINS;
				break;
			}
			break;
		default:
			switch (cn) {
			case '$':
				opt |= SUFFIX;
				plen--;
				break;
			case '*':
				opt |= CONTAINS;
				plen--;
				break;
			default:
				opt |= CONTAINS;
				break;
			}
			break;
		}
		if (plen < MIN_BLOCK) {
			return false;
		}

		String ps = pattern.substring(pb, plen);
		WMPattern<T> p = this.pMap.get(ps);
		if (null == p) {
			p = new WMPattern<T>();
			p.pattern = ps;
			p.prefix = hash(p.pattern, 0);
			this.pMap.put(ps, p);
			this.patternList.add(p);
		}
		if (null == p.users[opt]) {
			p.users[opt] = new HashSet<>();
		}
		p.users[opt].add(user);
		p.options |= opt;
		this.minLen = this.minLen < p.pattern.length() ? this.minLen : p.pattern.length();
		return true;
	}

	private int hash(String s, int i) {
		return ((s.charAt(i) << 8) | s.charAt(i + 1)) & 0xffff;
	}

	private void sort() {
		for (WMPattern<T> p : this.patternList) {
			p.hash = hash(p.pattern, this.minLen - MIN_BLOCK);
		}
		Collections.sort(this.patternList, new Comparator<WMPattern<T>>() {

			@Override
			public int compare(WMPattern<T> a, WMPattern<T> b) {
				return b.hash - a.hash;
			}

		});
	}

	private void calcShift() {
		for (int i = 0; i < this.shift.length; i++) {
			this.shift[i] = this.minLen - MIN_BLOCK + 1;
		}
		for (WMPattern<T> p : this.patternList) {
			for (int i = 0; i < this.minLen - MIN_BLOCK + 1; i++) {
				int shift = this.minLen - i - MIN_BLOCK;
				int k = hash(p.pattern, i);
				if (shift < this.shift[k]) {
					this.shift[k] = shift;
				}
			}
		}
	}

	private void calcHash() {
		for (int i = 0; i < this.hash.length; i++) {
			this.hash[i] = -1;
		}
		for (int i = this.patternList.size() - 1; i >= 0; i--) {
			this.hash[this.patternList.get(i).hash] = i;
		}
	}

	public void compile() {
		if (compiled) {
			throw new IllegalStateException("Wumanber is compiled already.");
		}
		sort();
		calcShift();
		calcHash();
		this.compiled = true;
		pMap.clear();
		pMap = null;
	}

	public int matches(String str, MatchHandler<T> matchHandler) {
		if (str.length() < this.minLen) {
			return 0;
		}
		int p = this.minLen - MIN_BLOCK;
		int pEnd = str.length();
		int sEnd = pEnd - MIN_BLOCK + 1;
		int shift, c = 0, hash, idx, p0, prefix;
		int ok;
		WMPattern<T> pattern;
		while (p < sEnd) {
			while ((shift = this.shift[hash(str, p)]) > 0) {
				p += shift;
				if (p >= sEnd) {
					return c;
				}
			}
			hash = hash(str, p);
			if ((idx = this.hash[hash]) == -1) {
				continue;
			}
			p0 = p - this.minLen + MIN_BLOCK;
			prefix = hash(str, p0);
			while (idx < this.patternList.size() && (pattern = this.patternList.get(idx)).hash == hash) {
				if (prefix != pattern.prefix) {
					idx++;
					continue;
				}
				ok = 0;

				if ((pattern.options & CONTAINS) != 0) {
					ok |= CONTAINS;
				}
				if ((pattern.options & PREFIX) != 0) {
					if (p0 == 0 && pattern.pattern.length() <= pEnd) {
						ok |= PREFIX;
					}
				}
				if ((pattern.options & SUFFIX) != 0) {
					if (p0 + pattern.pattern.length() == pEnd && pattern.pattern.length() <= pEnd) {
						ok |= SUFFIX;
					}
				}
				if ((pattern.options & FULL) != 0) {
					if (p0 == 0 && pattern.pattern.length() == pEnd) {
						ok |= FULL;
					}
				}
				if (ok != 0 && str.startsWith(pattern.pattern, p0)) {
					c++;
					if (null != matchHandler) {
						if (!matchHandler.onMatch(this, str, pattern.pattern, ok, new UsersIterator<T>(pattern, ok))) {
							return c;
						}
					} else {
						return c;
					}
				}
				idx++;
			}
			p++;
		}
		return c;
	}

	private static class UsersIterator<T> implements Iterator<T> {
		final WMPattern<T> pattern;
		final int matchOptions;
		int i;
		Iterator<T> nextIterator;

		public UsersIterator(WMPattern<T> pattern, int matchOptions) {
			super();
			this.pattern = pattern;
			this.i = 1;
			this.matchOptions = matchOptions;
		}

		@Override
		public boolean hasNext() {
			if (null == nextIterator) {
				while (i < 16) {
					if ((matchOptions & i) == 0 || this.pattern.users[i] == null || this.pattern.users[i].isEmpty()) {
						i <<= 1;
					} else {
						nextIterator = this.pattern.users[i].iterator();
						break;
					}
				}
			}
			return null != nextIterator && nextIterator.hasNext();
		}

		@Override
		public T next() {
			if (hasNext()) {
				T ret = nextIterator.next();
				if (!nextIterator.hasNext()) {
					nextIterator = null;
					i <<= 1;
				}
				return ret;
			} else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public int size() {
		return this.patternList.size();
	}

	private static class WMPattern<T> {
		String pattern;
		int options;
		int hash;
		int prefix;
		Set<T>[] users = new HashSet[16];
	}

	public static void main(String[] args) {
		Wumanber<Integer> wumanber = new Wumanber<>();
		wumanber.addPattern("baidu.com", 1);
		wumanber.addPattern("^baidu.com$", 2);
		wumanber.addPattern("baidu.com$", 3);
		wumanber.addPattern("baidu", 4);
		wumanber.addPattern("^baidu", 5);
		wumanber.addPattern("^com", 6);
		wumanber.addPattern("^baidu$", 7);

		wumanber.compile();

		System.out.println(wumanber.matches("com", new MatchHandler<Integer>() {

			@Override
			public boolean onMatch(Wumanber<Integer> wm, String str, String pattern, int matchOptions, Iterator<Integer> users) {
				System.out.format("str=%s, pattern=%s, options=0x%x\n", str, pattern, matchOptions);
				while (users.hasNext()) {
					System.out.println("\t" + users.next());
				}
				return true;
			}

		}) + " matches");
	}
}
