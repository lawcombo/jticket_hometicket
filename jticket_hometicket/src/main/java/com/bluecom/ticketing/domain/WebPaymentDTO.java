package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebPaymentDTO implements Serializable {

	int idx;
	String shop_code;
	String order_no;
	int reserver_authentication_idx;
	String product_group;
	int total_count;
	BigDecimal total_fee;
	String pay_method;				
	String visitor_type;
	String content_mst_cd;
	String piece_ticket_yn;
	String product_group_kind;
	String schedule_code;
	String play_sequence;
	String play_date;
	
	String shop_name;
	String trade_date;
	String start_time;
	String end_time;
	int valid_period;
	String agree_1;
	String agree_2;
	
	String merchantID;
	String merchantKey;
	String product_group_name;
	String reserverName;
	String reserverPhone;
	String reserverEmail;
	
	String paymentMethodName;
	String product_group_type;
	String org_order_no;
	int org_idx;
	
	String couponFee;
	String fee;
	String coupon; //쿠폰 존재유무
}

