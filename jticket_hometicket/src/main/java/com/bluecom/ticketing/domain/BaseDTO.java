package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class BaseDTO implements Serializable{

	String result_code = "";
	String result_message = "";	
	String contentMstCd = "";
	String shopCode = "";
	private List<?> DTOList;
	
	public List<?> getDTOList() {
		return DTOList;
	}
	
}
