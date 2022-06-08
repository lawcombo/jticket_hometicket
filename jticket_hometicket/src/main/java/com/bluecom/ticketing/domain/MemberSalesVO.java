package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MemberSalesVO implements Serializable{
	Date work_datetime;
	String order_num;
	String sale_code;
	String play_date;
	String schedule_code;
	String start_time;
	String end_time;
	String play_sequence;
	String valid_from;
	String valid_to;
	int quantity;
	int refund;
	String product_group_name;
	BigDecimal fee;
}
