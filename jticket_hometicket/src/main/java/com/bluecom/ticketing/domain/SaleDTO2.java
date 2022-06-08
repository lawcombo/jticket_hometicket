package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SaleDTO2 extends BaseDTO implements Serializable{
	
	String member_name2 = "";
	String member_tel2 = "";
	String order_num2 = "";
	
	String type2;
	String content_mst_cd2=""; // 추가 / 2021-09-08 / 조미근
	String product_group_code2="";
	String sale_code2;

}
