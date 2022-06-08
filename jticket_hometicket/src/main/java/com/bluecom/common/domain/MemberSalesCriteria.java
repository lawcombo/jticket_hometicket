package com.bluecom.common.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberSalesCriteria extends SearchCriteria implements Serializable{

	String contentMstCd;
	String memberCode;
	String startDate;
	String endDate;
}
