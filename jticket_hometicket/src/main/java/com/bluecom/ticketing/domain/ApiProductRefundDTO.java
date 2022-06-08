package com.bluecom.ticketing.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiProductRefundDTO implements Serializable{

	@JsonProperty(value = "BOOK_NO")
	String BOOK_NO;
	@JsonProperty(value = "REFUND_FEE")
	int REFUND_FEE;

}
