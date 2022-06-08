package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class BookOpenVO implements Serializable {

	String shop_code = "";
	String shopCode = "";
	String play_date = "";
	String start_date = "";
	String book_yn = "";
	String date = "";
	String content_mst_cd = "";
	String product_group_code;
	
	private List<BookOpenVO> bookOpenVOList;
	
	public List<BookOpenVO> getBookOpenVOList() {
		return bookOpenVOList;
	}
}
