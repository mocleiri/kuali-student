package com.sigmasys.kuali.ksa.model;

/**
 * This enumeration represents possible values of KeyPair class and subclass types. 
 * According to the current design, "K" for KeyPairs and "P" for PeriodKeyPairs.
 * 
 * @author Sergey
 *
 */
public enum KeyPairType {

	KEYPAIR("K"),
	PERIOD_KEYPAIR("P");
	
	public static final String KEYPAIR_TYPE_CODE = "K";
	public static final String PERIOD_KEYPAIR_TYPE_CODE = "P";
	
	private String typeCode;
	
	private KeyPairType(String typeCode) {
		this.typeCode = typeCode;
	}
	
	public String getTypeCode() {
		return typeCode;
	}
}
