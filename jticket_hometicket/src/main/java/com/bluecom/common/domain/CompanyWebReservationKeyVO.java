package com.bluecom.common.domain;

import java.io.Serializable;

public class CompanyWebReservationKeyVO implements Serializable {

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
	
	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	
	

	public String getIdentification_site_code() {
		return identification_site_code;
	}

	public void setIdentification_site_code(String identification_site_code) {
		this.identification_site_code = identification_site_code;
	}

	public String getIdentification_site_password() {
		return identification_site_password;
	}

	public void setIdentification_site_password(String identification_site_password) {
		this.identification_site_password = identification_site_password;
	}

	public String getPay_merchant_key() {
		return pay_merchant_key;
	}

	public void setPay_merchant_key(String pay_merchant_key) {
		this.pay_merchant_key = pay_merchant_key;
	}

	public String getPay_merchant_id() {
		return pay_merchant_id;
	}

	public void setPay_merchant_id(String pay_merchant_id) {
		this.pay_merchant_id = pay_merchant_id;
	}

	public String getInfo_a_title() {
		return info_a_title;
	}

	public void setInfo_a_title(String info_a_title) {
		this.info_a_title = info_a_title;
	}

	public String getInfo_a() {
		return info_a;
	}

	public void setInfo_a(String info_a) {
		this.info_a = info_a;
	}

	public String getInfo_b_title() {
		return info_b_title;
	}

	public void setInfo_b_title(String info_b_title) {
		this.info_b_title = info_b_title;
	}

	public String getInfo_b() {
		return info_b;
	}

	public void setInfo_b(String info_b) {
		this.info_b = info_b;
	}

	public String getInfo_c_title() {
		return info_c_title;
	}

	public void setInfo_c_title(String info_c_title) {
		this.info_c_title = info_c_title;
	}

	public String getInfo_c() {
		return info_c;
	}

	public void setInfo_c(String info_c) {
		this.info_c = info_c;
	}

	public String getInfo_d_title() {
		return info_d_title;
	}

	public void setInfo_d_title(String info_d_title) {
		this.info_d_title = info_d_title;
	}

	public String getInfo_d() {
		return info_d;
	}

	public void setInfo_d(String info_d) {
		this.info_d = info_d;
	}

	
}
