package org.sothis.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Wumanber {

	private static final int WM_MATCH_ALL = 0;
	private static final int WM_MATCH_PREFIX = 1;
	private static final int WM_MATCH_SUFFIX = 2;
	private static final int WM_MATCH_FULL = 3;

	private static final int WM_MIN_BLOCK = 2;
	private static final int WM_TABLE_SIZE = 65536;

	private final List<WMPattern> patternList = new ArrayList<WMPattern>();
	private int minLen = Integer.MAX_VALUE;
	private int[] shift = new int[WM_TABLE_SIZE];
	private int[] hash = new int[WM_TABLE_SIZE];
	private boolean compiled;

	public boolean addPattern(String pattern) {
		if (compiled) {
			throw new IllegalStateException("Wumanber is compiled already.");
		}
		if (StringUtils.length(pattern) < WM_MIN_BLOCK) {
			return false;
		}
		char c0 = pattern.charAt(0);
		char cn = pattern.charAt(pattern.length() - 1);
		int pb = 0, plen = pattern.length(), opt = 0;
		switch (c0) {
		case '^':
			pb++;
			plen--;
			switch (cn) {
			case '$':
				opt |= WM_MATCH_FULL;
				plen--;
				break;
			case '*':
				opt |= WM_MATCH_PREFIX;
				plen--;
				break;
			default:
				opt |= WM_MATCH_PREFIX;
				break;
			}
			break;
		case '*':
			pb++;
			plen--;
			switch (cn) {
			case '$':
				opt |= WM_MATCH_SUFFIX;
				plen--;
				break;
			case '*':
				opt |= WM_MATCH_ALL;
				plen--;
				break;
			default:
				opt |= WM_MATCH_ALL;
				break;
			}
			break;
		default:
			switch (cn) {
			case '$':
				opt |= WM_MATCH_SUFFIX;
				plen--;
				break;
			case '*':
				opt |= WM_MATCH_ALL;
				plen--;
				break;
			default:
				opt |= WM_MATCH_ALL;
				break;
			}
			break;
		}
		if (plen < WM_MIN_BLOCK) {
			return false;
		}
		WMPattern p = new WMPattern();
		p.pattern = pattern.substring(pb, plen);
		p.options = opt;
		p.prefix = hash(p.pattern, 0);
		p.orgPattern = pattern;
		this.minLen = this.minLen < p.pattern.length() ? this.minLen : p.pattern.length();
		this.patternList.add(p);
		return true;
	}

	private int hash(String s, int i) {
		return (s.charAt(i) << 8) | s.charAt(i + 1);
	}

	private void sort() {
		for (WMPattern p : this.patternList) {
			p.hash = hash(p.pattern, this.minLen - WM_MIN_BLOCK);
		}
		Collections.sort(this.patternList, new Comparator<WMPattern>() {

			@Override
			public int compare(WMPattern a, WMPattern b) {
				return b.hash - a.hash;
			}

		});
	}

	private void calcShift() {
		for (int i = 0; i < this.shift.length; i++) {
			this.shift[i] = this.minLen - WM_MIN_BLOCK + 1;
		}
		for (WMPattern p : this.patternList) {
			for (int i = 0; i < this.minLen - WM_MIN_BLOCK + 1; i++) {
				int shift = this.minLen - i - WM_MIN_BLOCK;
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
	}

	public int matches(String str, boolean matchAll) {
		if (str.length() < this.minLen) {
			return 0;
		}
		int p = this.minLen - WM_MIN_BLOCK;
		int pEnd = str.length();
		int shift, c = 0, hash, idx, p0, prefix;
		boolean ok;
		WMPattern pattern;
		while (p < pEnd - 1) {
			while ((shift = this.shift[hash(str, p)]) > 0) {
				p += shift;
				if (p >= pEnd - 1) {
					return c;
				}
			}
			hash = hash(str, p);
			if ((idx = this.hash[hash]) == -1) {
				continue;
			}
			p0 = p - this.minLen + WM_MIN_BLOCK;
			prefix = hash(str, p0);
			pattern = this.patternList.get(idx);
			while (pattern != null && hash == pattern.hash) {
				if (prefix != pattern.prefix) {
					if (++idx < this.patternList.size()) {
						pattern = this.patternList.get(idx);
						continue;
					} else {
						break;
					}
				}
				ok = true;
				switch (pattern.options) {
				case WM_MATCH_ALL:
					break;
				case WM_MATCH_PREFIX:
					if (p0 != 0 || pattern.pattern.length() > pEnd) {
						ok = false;
					}
					break;
				case WM_MATCH_SUFFIX:
					if (p0 + pattern.pattern.length() != pEnd || pattern.pattern.length() > pEnd) {
						ok = false;
					}
					break;
				case WM_MATCH_FULL:
					if (p0 != 0 || pattern.pattern.length() != pEnd) {
						ok = false;
					}
					break;
				}
				if (ok && subStringCompare(str, p0, pattern.pattern) == 0) {
					System.out.println("matches " + pattern.orgPattern);
					if (!matchAll) {
						return 1;
					}
					c++;
				}
				if (++idx < this.patternList.size()) {
					pattern = this.patternList.get(idx);
				} else {
					pattern = null;
				}
			}
			p++;
		}
		return c;
	}

	private int subStringCompare(String str, int i, String cmp) {
		int k = 0;
		int len1 = str.length() - i;
		int len2 = cmp.length();
		int limit = Math.min(len1, len2);
		while (k < limit) {
			char c1 = str.charAt(k + i);
			char c2 = cmp.charAt(k);
			if (c1 != c2) {
				return c1 - c2;
			}
			k++;
		}
		return len1 - len2;
	}

	private static class WMPattern {
		String pattern;
		String orgPattern;
		int options;
		int hash;
		int prefix;
	}

	public static void main(String[] args) {
		Wumanber wumanber = new Wumanber();
		wumanber.addPattern("baidu.com");
		wumanber.addPattern("^baidu.com$");
		wumanber.addPattern("baidu.com$");
		wumanber.addPattern("baidu");
		wumanber.addPattern("^baidu");
		wumanber.addPattern("^com");
		wumanber.addPattern("^baidu$");

		wumanber.compile();

		System.out.println(wumanber.matches("www.baidu.com", true));
	}
}
