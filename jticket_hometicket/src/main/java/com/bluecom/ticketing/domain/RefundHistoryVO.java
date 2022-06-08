package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class RefundHistoryVO implements Serializable{

	String shop_code;
	String sale_code;
	int count;
	String fee;
	String book_no;
	String work_datetime;
	String work_id;
	
}
