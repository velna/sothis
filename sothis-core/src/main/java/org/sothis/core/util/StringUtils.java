package org.sothis.core.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static String underlined(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		StringBuilder ret = new StringBuilder();
		int last = 0;
		for (int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);
			if (ch == '_') {
				ret.append(Character.toLowerCase(ch));
				last = -1;
			} else if (!Character.isLetter(ch)) {
				ret.append(ch);
				last = -1;
			} else if (Character.isUpperCase(ch)) {
				if (i > 0 && (last == 0 || (last != -1 && i < strLen - 1 && Character.isLowerCase(str.charAt(i + 1))))) {
					ret.append('_');
				}
				ret.append(Character.toLowerCase(ch));
				last = 1;
			} else {
				ret.append(ch);
				last = 0;
			}
		}
		return ret.toString();
	}
}
