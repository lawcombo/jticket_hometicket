package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.bluecom.common.util.ValidList;

import lombok.Data;

@Data
public class PaymentInfoDTO implements Serializable{

	int totalCount;
	BigDecimal totalFee;
	ProductGroupDTO productGroup;
	List<ProductDTO> products;
	List<ProductDTO> productsOrderedByPrice;

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
	
	String order_num;
	String member_name;
	String member_tel;
	
	List<CouponVO> coupon;
	
	String couponFee;
	String fee;
}
