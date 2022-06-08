package com.bluecom.ticketing.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiSaleProductDTO implements Serializable{

	@JsonProperty(value = "SALE_SEQUENCE")
	String SALE_SEQUENCE;
	@JsonProperty(value = "TICKET_CONTROL_NO")
	String TICKET_CONTROL_NO;
	@JsonProperty(value = "PRODUCT_CONTENT_MST_CD")
	String PRODUCT_CONTENT_MST_CD;
	@JsonProperty(value = "PRODUCT_CODE")
	String PRODUCT_CODE;
	@JsonProperty(value = "PRODUCT_NAME")
	String PRODUCT_NAME;
	@JsonProperty(value = "PACKAGE_YN")
	String PACKAGE_YN;
	@JsonProperty(value = "PACKAGE_IDX")
	int PACKAGE_IDX;
	@JsonProperty(value = "BOOK_YN")
	String BOOK_YN;
	@JsonProperty(value = "BOOK_NO")
	String BOOK_NO;
	@JsonProperty(value = "UNIT_PRICE")
	int UNIT_PRICE;
	@JsonProperty(value = "QUANTITY")
	int QUANTITY;
	@JsonProperty(value = "PLAY_DATE")
	String PLAY_DATE;
	@JsonProperty(value = "PLAY_SEQUENCE")
	String PLAY_SEQUENCE;
	@JsonProperty(value = "PRODUCT_FEE")
	int PRODUCT_FEE;
	@JsonProperty(value = "REFUND_FEE")
	int REFUND_FEE;
	@JsonProperty(value = "SEASON_CODE")
	String SEASON_CODE;
	@JsonProperty(value = "MEMBER_DISCOUNT_YN")
	String MEMBER_DISCOUNT_YN;
	@JsonProperty(value = "SEAT_CODE")
	String SEAT_CODE;
	@JsonProperty(value = "SCHEDULE_CODE")
	String SCHEDULE_CODE;
	@JsonProperty(value = "PERSON_NAME")
	String PERSON_NAME;
	@JsonProperty(value = "PERSON_MOBILE_NO")
	String PERSON_MOBILE_NO;
	@JsonProperty(value = "PERSON_JUMIN")
	String PERSON_JUMIN;
	@JsonProperty(value = "PERSON_ADDR")
	String PERSON_ADDR;
}
