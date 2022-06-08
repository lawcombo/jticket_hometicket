package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProductDTO implements Serializable, Comparable {

	@NotNull(message = "시설정보가 없습니다.")
	@Size(min = 1, message = "시설정보가 없습니다.")
	String shop_code = "";
	@NotNull(message = "상품그룹정보가 없습니다.")
	@Size(min = 1, message = "상품그룹정보가 없습니다.")
	String product_group_code = "";	
	@NotNull(message = "상품정보가 없습니다.")
	@Size(min = 1, message = "상품정보가 없습니다.")
	String product_code = "";
//	String product_kind = "";
	String product_name = "";
	String product_eng_name = "";
	@NotNull(message = "상품가격정보가 없습니다.")
	BigDecimal product_fee;
//	BigDecimal product_discount_fee;
//	BigDecimal product_premium_fee;
//	BigDecimal product_peak_fee;
//	BigDecimal product_member_fee;
	@NotNull(message = "부가세비율정보가 없습니다.")
	BigDecimal vat_rates;
	String variable_fee_yn = "";
//	String group_yn = "";
//	String window_yn = "";
	String web_yn = "";
//	String mobile_yn = "";
//	String kiosk_yn = "";
//	int order_no = 0;
//	String use_type_code = "";
//	String delete_yn = "";
	String remark = "";
//	String update_id = "";
//	Date update_datetime = null;
//	String work_id = "";
//	Date work_datetime = null;
	
	/**
	 * 유효기간
	 */
	String valid_period;
	/**
	 * 티켓종류
	 */
	String product_kind_name;

	/**
	 * 매수
	 */
	@Size(min = 0, max = 10, message = "상품 수량 범위가 맞지 않습니다.")
	int count;
	String contentMstCd;
	
	
	
	
	public ProductDTO() {
		
	}
	
	@Override
	public int compareTo(Object o) {
		return compareTo((ProductDTO)o);
	}
	
	public int compareTo(ProductDTO product) {
		return this.product_fee.compareTo(product.product_fee);
	}
}
