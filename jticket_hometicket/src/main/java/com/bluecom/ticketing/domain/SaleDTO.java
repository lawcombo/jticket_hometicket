package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SaleDTO extends BaseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7511908842472893476L;
	@NotNull(message = "예약정보가 없습니다.")
	String member_name = "";
	String member_tel = "";
	String member_email = "";	
	String user_id = "";	
	String shop_code = "";
	String sale_code = "";
	String order_num = "";
	String org_order_num = "";
	String refund_yn = "";		
	String payment_yn = ""; //0:정상 ->환불 처리시 : 1 로 수정
	String payment_case = ""; //B복합결제, M:현금, C:카드, E:기타
	String work_datetime = "";
	
	String memberName = "";
	String memberTel = "";
	String memberEmail = "";	
	String userId = "";	
	String shopCode = "";
	String saleCode = "";
	String orderNum = "";
	String refundYn = "";		
	String paymentYn = ""; //0:정상 ->환불 처리시 : 1 로 수정
	String paymentCase = ""; //B복합결제, M:현금, C:카드, E:기타
	String workDatetime = "";
//	String result_code = "";
//	String result_message = "";
	
	String type;
	String content_mst_cd=""; // 추가 / 2021-09-08 / 조미근
	String product_group_code="";
	
	List<CouponVO> coupon;
	
	private List<SaleDTO> saleDTOList;
	
	public List<SaleDTO> getSaleDTOList() {
		return saleDTOList;
	}
}
