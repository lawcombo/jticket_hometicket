package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiSocialCancelDTO  implements Serializable {

	@JsonProperty(value = "CONTENT_MST_CD")	
	String CONTENT_MST_CD;
	@JsonProperty(value = "ONLINE_CHANNEL")	
	String ONLINE_CHANNEL;
	@JsonProperty(value = "ORDER_NUM")	
	String ORDER_NUM;
	@JsonProperty(value = "USER_ID")	
	String USER_ID;
	@JsonProperty(value = "TERMINAL_CODE")	
	String TERMINAL_CODE;
	@JsonProperty(value = "TERMINAL_DATETIME")	
	String TERMINAL_DATETIME;
	@JsonProperty(value = "CANCEL_TYPE")	
	String CANCEL_TYPE;
	@JsonProperty(value = "SALE_PRODUCT_LIST")
	List<ApiProductRefundDTO> SALE_PRODUCT_LIST;
}
