package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiSocialSaleDTO  implements Serializable {

	@JsonProperty(value = "CONTENT_MST_CD")
	String CONTENT_MST_CD;
	@JsonProperty(value = "TERMINAL_CODE")
	String TERMINAL_CODE;
	@JsonProperty(value = "SALE_DATE")
	String SALE_DATE;
	@JsonProperty(value = "TERMINAL_DATETIME")
	String TERMINAL_DATETIME;
	@JsonProperty(value = "MEMBER_YN")
	String MEMBER_YN;
	@JsonProperty(value = "MEMBER_NO")
	String MEMBER_NO;
	@JsonProperty(value = "SALE_KIND_CODE")
	String SALE_KIND_CODE;
	@JsonProperty(value = "ONLINE_CHANNEL")
	String ONLINE_CHANNEL;
	@JsonProperty(value = "WORK_DATETIME")
	String WORK_DATETIME;
	@JsonProperty(value = "ORDER_NUM")
	String ORDER_NUM;
	@JsonProperty(value = "MEMBER_NAME")
	String MEMBER_NAME;
	@JsonProperty(value = "MEMBER_TEL")
	String MEMBER_TEL;
	@JsonProperty(value = "MEMBER_EMAIL")
	String MEMBER_EMAIL;
	@JsonProperty(value = "VALID_FROM")
	String VALID_FROM;
	@JsonProperty(value = "VALID_TO")
	String VALID_TO;
	@JsonProperty(value = "AGREE_1")
	String AGREE_1;
	@JsonProperty(value = "AGREE_2")
	String AGREE_2;
	@JsonProperty(value = "ORG_ORDER_NUM")
	String ORG_ORDER_NUM;
	@JsonProperty(value = "S_CHECK_YN")
	String S_CHECK_YN;
	
	@JsonProperty(value = "SALE_PRODUCT_LIST")
	List<ApiSaleProductDTO> SALE_PRODUCT_LIST;
	@JsonProperty(value = "PAYMENTS_INFO")
	List<ApiPaymentsInfoDTO> PAYMENTS_INFO;
	@JsonProperty(value = "CARD_INFO")
	List<ApiCardInfoDTO> CARD_INFO;
}
