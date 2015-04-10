package org.sothis.mvc;

import java.io.IOException;
import java.io.InputStream;

public interface Request {

	InputStream getInputStream() throws IOException;

	String getString(String name);

	String getString(String name, String defaultValue);

	String[] getStrings(String name);

	Boolean getBoolean(String name);

	Boolean getBoolean(String name, Boolean defaultValue);

	Boolean[] getBooleans(String name);

	Integer getInteger(String name);

	Integer getInteger(String name, Integer defaultValue);

	Integer[] getIntegers(String name);

	Long getLong(String name);

	Long getLong(String name, Long defaultValue);

	Long[] getLongs(String name);

	Double getDouble(String name);

	Double getDouble(String name, Double defaultValue);

	Double[] getDoubles(String name);

}
