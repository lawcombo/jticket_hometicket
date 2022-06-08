package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReserverDTO implements Serializable {

	/**
	 * 1: Process1, 2: Process2
	 */
	int type;
	String name;
	String phoneNumber;
	String orderNumber;
}
