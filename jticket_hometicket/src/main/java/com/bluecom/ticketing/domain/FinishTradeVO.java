package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class FinishTradeVO implements Serializable {
	String work_datetime;
	String total_fee;
	String total_count;
	String pay_method;
	String order_num;
	String sale_date;
	String expire_date;
	String schedule_code;
	String shop_name;
	String member_name;
	String member_tel;
	String member_email;
	String quantity;
	String play_date;
	String start_time;
	String end_time;
	String product_group_kind; 
	
	String play_sequence;
	String product_group_name;
	String product_name;
}
