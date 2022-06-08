package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class WebPaymentVisitorDTO implements Serializable {

	int web_payment_idx;
	String name;
	String phone;
	String jumin;
	String product_code;
	String addr;

}
