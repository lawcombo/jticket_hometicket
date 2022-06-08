package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SaleVO implements Serializable{
	
	String shop_code;
	String sale_code;
	String terminal_code;
	String sale_Date;
	String user_id;
	String member_yn;
	String member_no;
	String sale_kind_code;
	String online_channel;
	Date terminal_Datetime;
	String account_Date;
	Date work_Datetime;
	String payment_yn;
	String order_num;
	String member_name;
	String member_tel;
	String member_email;
	Date reg_Date;
	String payment_case;
	String sale_type;
	String valid_from;
	String valid_to;
	String agree_1;
	String agree_2;
	
	List<SaleProductDTO> saleProducts;
}
