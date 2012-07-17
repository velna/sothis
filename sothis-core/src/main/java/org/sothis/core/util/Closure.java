package org.sothis.core.util;

public interface Closure<O, I> {
	public O execute(I input);
}
