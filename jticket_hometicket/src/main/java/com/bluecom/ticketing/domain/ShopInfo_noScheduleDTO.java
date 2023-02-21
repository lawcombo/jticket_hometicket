package com.bluecom.ticketing.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ShopInfo_noScheduleDTO implements Serializable {

	//======회차 없는것
	String content_mst_cd;
	String shop_code;
	
	String userId;
	String userName;
}
