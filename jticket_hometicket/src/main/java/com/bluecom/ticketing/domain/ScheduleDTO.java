package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ScheduleDTO  implements Serializable{

	String shop_code = "";
	String schedule_code = "";
	String product_group_code = "";
	String play_sequence = "";
	String start_time = "";
	String end_time = "";
	String subject_text = "";
	String sumCout = "";
	String contentMstCd = "";
	String play_date = "";
	String total_count=""; // 추가 / 2021-09-06 / 조미근
	String play_date_from;
	String play_date_to;
	
	private List<ScheduleDTO> scheduleDTOList;
	
	public List<ScheduleDTO> getScheduleDTOList() {
		return scheduleDTOList;
	}
}
