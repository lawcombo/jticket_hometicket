package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class BookOpenDTO implements Serializable {

	String shop_code = "";
	String play_date = "";
	String start_date = "";
	String book_yn = "";	
	String contentMstCd = "";
}
