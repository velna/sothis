package org.sothis.core.util;

import org.slf4j.Logger;

public class LoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		return org.slf4j.LoggerFactory.getLogger(clazz);
	}

	public static Logger getLogger(String name) {
		return org.slf4j.LoggerFactory.getLogger(name);
	}

	public static Logger getPerformanceLogger(Class<?> clazz) {
		return org.slf4j.LoggerFactory.getLogger("performance." + clazz.getName());
	}
}
