package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class BaseDTO_noSchedule implements Serializable{

	String result_code = "";
	String result_message = "";	
	private List<?> DTOList;
	
	public List<?> getDTOList() {
		return DTOList;
	}

	public BaseDTO_noSchedule() {
		super();
	}
	
	public BaseDTO_noSchedule(String result_code, String result_message) {
		super();
		this.result_code = result_code;
		this.result_message = result_message;
	}
	
}
