package com.bluecom.ticketing.domain;

public enum ErrorPathCode {
	
	SELECTTICKET(1);
	
	private final int value;
	private ErrorPathCode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
