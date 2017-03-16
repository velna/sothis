package org.sothis.core.util.bwlist.loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sothis.core.util.bwlist.CompileException;
import org.sothis.core.util.bwlist.Logic;
import org.sothis.core.util.bwlist.Source;
import org.sothis.core.util.bwlist.SourceData;

public class FileSourceLoader extends InputStreamSourceLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileSourceLoader.class);

	@Override
	public SourceData load(Source source, boolean forceReload) throws CompileException {
		try {
			File file = new File(source.getUri());
			long lm = file.lastModified();
			Long lastModified = (Long) source.getAttribute(source.getUri());
			if (!forceReload && lastModified != null && lastModified == lm) {
				return null;
			}
			SourceData sourceData = new SourceData(Logic.OR);
			this.loadFromStream(source, sourceData, new FileInputStream(file));
			source.setAttribute(source.getUri(), lm);
			LOGGER.info("load {} items from file {}", sourceData.size(), file);
			return sourceData;
		} catch (IOException e) {
			throw new CompileException(e);
		}
	}

	@Override
	public Set<String> loadValues(Source source, String uri) throws CompileException {
		try {
			return loadValuesFromStream(new FileInputStream(new File(source.getUri().resolve(uri))));
		} catch (IOException e) {
			throw new CompileException("error load values from uri: " + uri, e);
		}
	}

}
