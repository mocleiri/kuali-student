package org.kuali.spring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.util.ObjectUtils;

public class DefaultPropertiesConverter implements PropertiesConverter {
	public static final boolean DEFAULT_IS_SORT = true;
	boolean sort = DEFAULT_IS_SORT;

	/**
	 * Perform any conversion on the supplied Properties as needed.
	 * <p>
	 * The default implementation invokes {@link #getConvertedValue(String,String)} for each property value and replaces
	 * the original with the converted value if the converted value is different
	 * 
	 * @param properties
	 *            the Properties to convert
	 */
	@Override
	public void convert(Properties properties) {
		// Get a handle to the property names
		List<String> names = new ArrayList<String>(properties.stringPropertyNames());
		// Sort if needed
		if (isSort()) {
			Collections.sort(names);
		}
		// Iterate through the properties
		for (String name : names) {
			// Extract the current value
			String oldValue = properties.getProperty(name);
			// Get the converted value
			String newValue = getConvertedValue(name, oldValue);
			// Check them for equality
			if (!ObjectUtils.nullSafeEquals(newValue, oldValue)) {
				// The converted value is different, update our properties object
				properties.setProperty(name, newValue);
			}
		}
	}

	/**
	 * Convert the given property from the properties source to the value which should be used.
	 * <p>
	 * The default implementation calls {@link #getConvertedValue(String)}.
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param propertyValue
	 *            the original value from the properties source
	 * 
	 * @return the converted value, to be used for processing
	 * @see #getConvertedValue(String)
	 */
	protected String getConvertedValue(String propertyName, String propertyValue) {
		return getConvertedValue(propertyValue);
	}

	/**
	 * Convert the given property value from the properties source to the value which should be used.
	 * <p>
	 * The default implementation simply returns the original value. Can be overridden in subclasses, for example to
	 * detect encrypted values and decrypt them accordingly.
	 * 
	 * @param originalValue
	 *            the original value from the properties source
	 * 
	 * @return the converted value, to be used for processing
	 * 
	 * @see #getConvertedValue(String, String)
	 */
	protected String getConvertedValue(String originalValue) {
		return originalValue;
	}

	public boolean isSort() {
		return sort;
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

}
