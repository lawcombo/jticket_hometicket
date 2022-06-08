package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class VerificationKeyVO implements Serializable{
	String company_code;
	String identification_site_code;
	String identification_site_password;
	String pay_merchant_key;
	String pay_merchant_id;
}
