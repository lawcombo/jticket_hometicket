package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SaleProductDTO extends BaseDTO implements Serializable{

	String sale_shop_code;
	String sale_code;
	String sale_sequence;
	String ticket_control_no;
	String product_shop_code;
	String product_group_code;
	String product_group_name;
	String product_code;
	String product_name;
	String package_yn;
	int package_idx;
	String book_yn;
	String book_no;
	String quantity;
	String play_date;
	String play_sequence;
	BigDecimal product_fee;
	BigDecimal refund_fee;
	String season_code;
	String member_discount_yn;
	String seat_code;
	String schedule_code;
	String schedule_text;
	Date work_datetime;
	String check_in_yn;
	String check_in_user_id;
	String check_in_terminal_code;
	Date check_in_datetime;
	String print_yn;
	String print_user_id;
	String print_terminal_code;
	Date print_datetime;
	String refund_yn;
	String refund_user_id;
	Date refund_datetime;
	String refund_terminal_code;
	String person_name;
	String person_mobile_no;
	String person_jumin;
	Date reg_date;
	Date update_date;
	String boarding_num;
	String unit_price;
	String person_addr;
	String turn_idx;
	String sale_type;
	String online_yn;
	String online_channel;
	
	String gate_no;
	String used_date;
	String start_time;
	String end_time;
	String valid_period;
	String sale_date;
	String valid_from;
	String valid_to;
	
	String order_num;
	int web_payment_idx;
	int actual_quantity;
	
	int real_fee;
	
	// 2022-04-13
	private String remark;
	private String last_sale_date;
	
}
