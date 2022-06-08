package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class WebPaymentProductDTO implements Serializable{

	int web_payment_idx;
	String product_code;
	String product_name;
	BigDecimal product_fee;
	int count;
	
	String name;
	String phone;
	String jumin;
	String addr;
}
