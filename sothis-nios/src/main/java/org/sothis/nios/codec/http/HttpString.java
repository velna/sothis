package org.sothis.nios.codec.http;

public abstract class HttpString {

	private final String str;

	public HttpString(String str) {
		if (null == str) {
			throw new NullPointerException();
		}
		str = str.trim();
		if (str.isEmpty()) {
			throw new IllegalArgumentException("empty str");
		}
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}

	@Override
	public int hashCode() {
		return str.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (null == obj || !(obj instanceof HttpVersion)) {
			return false;
		}
		HttpString other = (HttpString) obj;
		return str.equals(other.str);
	}

}
