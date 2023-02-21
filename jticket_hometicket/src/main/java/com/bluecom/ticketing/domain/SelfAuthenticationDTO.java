package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class SelfAuthenticationDTO implements Serializable {

	// input
	String shop_code;
	String content_mst_cd;
	String success_url;
	String fail_url;
	
	// result
	String encData;
	String message;
}
