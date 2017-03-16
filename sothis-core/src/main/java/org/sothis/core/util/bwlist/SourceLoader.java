package org.sothis.core.util.bwlist;

import java.util.Set;

public interface SourceLoader {
	/**
	 * 加载uri对应的bwlist内容，如果forceReload为false则可以检查uri的
	 * 
	 * @param bwList
	 * @param uri
	 * @param forceReload
	 * @return
	 */
	SourceData load(Source source, boolean forceReload) throws CompileException;

	Set<String> loadValues(Source source, String uri) throws CompileException;

}
