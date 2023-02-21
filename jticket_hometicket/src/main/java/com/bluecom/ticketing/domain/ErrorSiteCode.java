package com.bluecom.ticketing.domain;

public enum ErrorSiteCode {
	
	HOMETICKET(1);
	
	private final int value;
	private ErrorSiteCode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
