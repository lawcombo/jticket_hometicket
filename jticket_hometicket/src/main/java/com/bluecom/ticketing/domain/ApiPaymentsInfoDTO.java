package com.bluecom.ticketing.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiPaymentsInfoDTO  implements Serializable {
	
	@JsonProperty(value = "PAYMENT_CODE")	
	String PAYMENT_CODE;
	@JsonProperty(value = "PAYMENT_NO")	
	String PAYMENT_NO;
	@JsonProperty(value = "PAYMENT_IDX")	
	int PAYMENT_IDX;
	@JsonProperty(value = "PAYMENT_FEE")	
	int PAYMENT_FEE;
	@JsonProperty(value = "PENALTY_FEE")	
	int PENALTY_FEE;
	@JsonProperty(value = "PAYMENT_USER_ID")	
	String PAYMENT_USER_ID;
	@JsonProperty(value = "PAYMENT_DATE")	
	String PAYMENT_DATE;
	@JsonProperty(value = "DECIDE_DATE")	
	String DECIDE_DATE;
	@JsonProperty(value = "DECIDE_ID")	
	String DECIDE_ID;
	@JsonProperty(value = "BILL_YN")	
	String BILL_YN;
	@JsonProperty(value = "BILL_DATETIME")	
	String BILL_DATETIME;

}
