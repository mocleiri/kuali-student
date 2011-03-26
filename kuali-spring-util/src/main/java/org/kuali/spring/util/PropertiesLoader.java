package org.kuali.spring.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class PropertiesLoader {
	final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
	public static final boolean DEFAULT_IGNORE_RESOURCE_NOT_FOUND = false;

	PropertiesPersister persister = new DefaultPropertiesPersister();
	PropertiesLogger propertiesLogger;
	PropertiesHelper helper;

	boolean ignoreResourceNotFound = DEFAULT_IGNORE_RESOURCE_NOT_FOUND;
	String fileEncoding;

	public PropertiesLoader() {
		this(null, null);
	}

	public PropertiesLoader(PropertiesLogger loggerSupport, PropertiesHelper helper) {
		super();
		this.propertiesLogger = loggerSupport;
		this.helper = helper;
	}

	public PropertiesLoader(boolean ignoreResourceNotFound, String fileEncoding) {
		super();
		this.ignoreResourceNotFound = ignoreResourceNotFound;
		this.fileEncoding = fileEncoding;
	}

	protected Properties loadProperties(Resource location, InputStream is) throws IOException {
		Properties properties = new Properties();
		if (isXMLFile(location)) {
			this.persister.loadFromXml(properties, is);
			return properties;
		}

		// It is not an xml file
		if (this.fileEncoding == null) {
			this.persister.load(properties, is);
		} else {
			this.persister.load(properties, new InputStreamReader(is, this.fileEncoding));
		}
		return properties;
	}

	protected Properties getProperties(Resource location) throws IOException {
		logger.info("Loading properties from {}", location);
		InputStream is = null;
		try {
			is = location.getInputStream();
			return loadProperties(location, is);
		} catch (IOException e) {
			handleIOException(location, e);
		} finally {
			nullSafeClose(is);
		}
		return new Properties();
	}

	protected boolean isXMLFile(Resource location) {
		String filename = null;
		try {
			filename = location.getFilename();
		} catch (IllegalStateException ex) {
			// resource is not file-based. See SPR-7552.
			return false;
		}
		// May not have thrown an exception, but might still be null
		if (filename == null) {
			return false;
		}
		// It's an XML file
		if (filename.endsWith(PropertiesLoaderSupport.XML_FILE_EXTENSION)) {
			return true;
		} else {
			return false;
		}
	}

	protected void handleIOException(Resource location, IOException e) throws IOException {
		if (!this.ignoreResourceNotFound) {
			throw e;
		}
		logger.warn("Could not load properties from {}: {}", location, e.getMessage());
	}

	protected void nullSafeClose(InputStream is) throws IOException {
		if (is == null) {
			return;
		}
		is.close();
	}

	public void loadProperties(Properties properties, Resource[] locations) throws IOException {
		if (locations == null || locations.length == 0) {
			logger.info("No property locations to load from");
			return;
		}
		for (Resource location : locations) {
			Properties newProps = getProperties(location);
			String source = PropertySource.RESOURCE.toString();
			PropertiesMergeContext context = new PropertiesMergeContext(properties, newProps, true, source, true);
			helper.mergeProperties(context);
		}
	}

	public boolean isIgnoreResourceNotFound() {
		return ignoreResourceNotFound;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public PropertiesPersister getPersister() {
		return persister;
	}

	public void setPersister(PropertiesPersister propertiesPersister) {
		this.persister = propertiesPersister;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}

	public PropertiesLogger getPropertiesLogger() {
		return propertiesLogger;
	}

	public void setPropertiesLogger(PropertiesLogger loggerSupport) {
		this.propertiesLogger = loggerSupport;
	}

	public PropertiesHelper getHelper() {
		return helper;
	}

	public void setHelper(PropertiesHelper helper) {
		this.helper = helper;
	}
}
