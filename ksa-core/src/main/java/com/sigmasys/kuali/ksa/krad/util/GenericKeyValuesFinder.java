package com.sigmasys.kuali.ksa.krad.util;

import org.kuali.rice.krad.keyvalues.KeyValuesBase;

@SuppressWarnings("all")
public abstract class GenericKeyValuesFinder extends KeyValuesBase {

	private boolean blankOption;

	/**
	 * @return the blankOption
	 */
	public boolean isBlankOption() {
		return this.blankOption;
	}

	/**
	 * @param blankOption the blankOption to set
	 */
	public void setBlankOption(boolean blankOption) {
		this.blankOption = blankOption;
	}
}
