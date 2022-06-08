package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class CouponVO implements Serializable {

	int idx;
	String company_code;
	int web_payment_idx;
	
	String cpm_num;
	String product_group_code;
	
	String cpm_cpn_code;
	String cpn_name;
	String cpn_sale_cost;
	String cpn_use_product;
	Date cpm_from_date;
	Date cpm_to_date;
	String cpm_use_date="";
	String cpm_use_info;
	String cpm_cancel_date="";
	
	String msg="";
	String use_yn="1";
	String couponFee;
	
	String content_mst_cd;
	
	String cpn_sale_type;
}
