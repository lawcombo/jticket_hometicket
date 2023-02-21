package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.bluecom.common.util.ValidList;

import lombok.Data;

@Data
public class PaymentInfoDTO_noSchedule implements Serializable{

	String content_mst_cd;
	int type;
	int totalCount;
	BigDecimal totalFee;
	ProductGroupDTO_noSchedule productGroup;
	List<ProductDTO> products;

	// bc_ticketvalid에서
	String valid_period;
	
	String schedule_code;
	ScheduleDTO schedule;
	String play_date;
	
	@NotNull
	String visitorType;
	
	ReserverInfoDTO reserver;
	@Valid
	ValidList<VisitorDTO> visitors;
	
	@NotNull(message="결제방법을 선택해 주세요.")
	String payMethod;

	int idx;
	
	boolean agree_1;
	boolean agree_2;
	
	String fee;
	
	
	String loginUserId = "";
	String loginUserNm = "";
	
}
