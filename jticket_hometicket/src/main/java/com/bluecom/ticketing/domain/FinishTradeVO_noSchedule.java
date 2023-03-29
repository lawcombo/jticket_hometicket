package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class FinishTradeVO_noSchedule implements Serializable {
	String work_datetime;
	String total_fee;
	String total_count;
	String pay_method;
	String order_num;
	String sale_date;
	String expire_date;
	String valid_from;
	String valid_to;
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
	String product_group_name;
	String product_names_counts;
	String category;
	String product_fee;
	String book_no;
	String product_name;
	String product_code;
	
	
	
	
	//--------------------- 소금산 연동 컬럼 ---------------------------------
	String saleShopCode = "";
	String saleOrderNum = "";
	String saleBookNo = "";
	String saleValidFrom = "";
	String saleValidTo = "";
	String saleProductCode = "";
	String saleProductName = "";
	String saleUniPrice = "";
	String saleQuantity = "";
	String saleProductFee = "";
	String saleSaleDate = "";
	String saleWorkDateTime = "";
	
}
