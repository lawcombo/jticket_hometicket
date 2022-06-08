package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.bluecom.common.util.ValidList;

import lombok.Data;

@Data
public class WebReservationKeyDTO implements Serializable{

	private String company_code;
	private String identification_site_code;
	private String identification_site_password;
	private String pay_merchant_key;
	private String pay_merchant_id;
	private String info_a_title;
	private String info_a;
	private String info_b_title;
	private String info_b;
	private String info_c_title;
	private String info_c;
	private String info_d_title;
	private String info_d;
}
