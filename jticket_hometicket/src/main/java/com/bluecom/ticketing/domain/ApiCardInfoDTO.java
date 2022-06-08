package com.bluecom.ticketing.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiCardInfoDTO  implements Serializable {

	@JsonProperty(value = "PAYMENT_CODE")	
	String PAYMENT_CODE;
	@JsonProperty(value = "PAYMENT_NO")	
	String PAYMENT_NO;
	@JsonProperty(value = "PAYMENT_IDX")	
	int PAYMENT_IDX;
	@JsonProperty(value = "TRANSACTION_DATETIME")	
	String TRANSACTION_DATETIME;
	@JsonProperty(value = "CARD_NO")	
	String CARD_NO;
	@JsonProperty(value = "INSTALL_PERIOD")	
	String INSTALL_PERIOD;
	@JsonProperty(value = "APPROVAL_NO")	
	String APPROVAL_NO;
	@JsonProperty(value = "APPROVAL_AMOUNT")	
	int APPROVAL_AMOUNT;
	@JsonProperty(value = "DISCOUNT_AMOUNT")	
	int DISCOUNT_AMOUNT;
	@JsonProperty(value = "SERVICE_AMOUNT")	
	int SERVICE_AMOUNT;
	@JsonProperty(value = "TAX_AMOUNT")	
	int TAX_AMOUNT;
	@JsonProperty(value = "FEE_AMOUNT")	
	int FEE_AMOUNT;
	@JsonProperty(value = "ORIGINAL_TRANSACTION_DATETIME")	
	String ORIGINAL_TRANSACTION_DATETIME;
	@JsonProperty(value = "CARD_CODE")	
	String CARD_CODE;
	@JsonProperty(value = "CARD_NAME")	
	String CARD_NAME;
	@JsonProperty(value = "PURCHASE_CODE")	
	String PURCHASE_CODE;
	@JsonProperty(value = "PURCHASE_NAME")	
	String PURCHASE_NAME;
	@JsonProperty(value = "STORE_ID")	
	String STORE_ID;
	@JsonProperty(value = "PART_CANCEL_YN")	
	String PART_CANCEL_YN;
	@JsonProperty(value = "CASH_TYPE")	
	String CASH_TYPE;

}
