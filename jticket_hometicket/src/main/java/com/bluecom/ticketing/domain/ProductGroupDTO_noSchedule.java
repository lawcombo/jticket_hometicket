package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductGroupDTO_noSchedule implements Serializable {

	@NotNull(message = "시설정보가 없습니다.")
	@Size(min = 1, message = "시설정보가 없습니다.")
	String shop_code = "";
	@NotNull(message = "상품그룹정보가 없습니다.")
	@Size(min = 1, message = "상품그룹정보가 없습니다.")
	String product_group_code = "";	
	String product_group_name = "";
	String product_group_eng_name = "";
//	String product_group_kind = "";
	String play_date = "";
//	int group_minimum_count = 0;
//	int order_no = 0;
//	String window_yn = "";
	String web_book_yn = "";
//	String mobile_book_yn = "";
//	String kiosk_yn = "";
//	String delete_yn = "";
//	String remark = "";
//	String update_id = "";
//	Date update_datetime = null;
//	String work_id = "";
//	Date work_datetime = null;
	
	int product_min;
	String content_mst_cd;
//	String contentMstCd = "";
	int valid_period;
	String piece_ticket_yn;

	// bc_product에서 일반티켓(common_yn = 1), 회차티켓(schedule_yn = 2)
	int type;
}
