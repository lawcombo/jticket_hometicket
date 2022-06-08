package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebPaymentStatusDTO implements Serializable {

	int web_payment_idx;
	String status;
	String message;
	Date time;
	
	String orderNo;
}
