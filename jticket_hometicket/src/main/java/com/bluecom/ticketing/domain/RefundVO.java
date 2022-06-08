package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class RefundVO  implements Serializable {

	String shop_code;
	String order_num;
	Date sale_date;
	String member_name;
	String member_tel;
	Date play_date;
	String refund_yn;
	String valid_period;
	String ticket_control_no;
	int used_count;
	Date expire_date;
	String start_time;
	String product_group_kind;
	Date play_datetime;
}
