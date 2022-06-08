package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class ShopPaymentsaleVO implements Serializable{

	String shop_code;
	String sale_code;
	String payment_code;
	String payment_no;
	String payment_idx;
	BigDecimal payment_fee;
	BigDecimal penalty_fee;
	String group_payment_code;
	String payment_kind;
	String payment_user_id;
	String payment_terminal_code;
	String payment_date;
	String decide_date;
	String decide_id;
	String bill_yn;
	String bill_datetime;
	String work_datetime;
	String turn_idx;
}
