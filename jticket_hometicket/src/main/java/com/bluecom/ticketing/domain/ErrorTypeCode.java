package com.bluecom.ticketing.domain;

public enum ErrorTypeCode {
	VALIDATION(1),
	PRODUCTGROUPMISMATCH(2)
	;
	
	private final int value;
	private ErrorTypeCode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	
}
