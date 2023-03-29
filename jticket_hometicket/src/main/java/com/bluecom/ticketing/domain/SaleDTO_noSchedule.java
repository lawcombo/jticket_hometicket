package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SaleDTO_noSchedule extends BaseDTO implements Serializable{

	@NotNull(message = "예약정보가 없습니다.")
	String member_name = "";
	String member_tel = "";
	String member_email = "";	
	String user_id = "";	
	String shop_code = "";
	String sale_code = "";
	String order_num = "";
	String refund_yn = "";		
	String payment_yn = ""; //0:정상 ->환불 처리시 : 1 로 수정
	String payment_case = ""; //B복합결제, M:현금, C:카드, E:기타
	String work_datetime = "";
	
//	String memberName = "";
//	String memberTel = "";
//	String memberEmail = "";	
//	String userId = "";	
//	String shopCode = "";
//	String saleCode = "";
//	String orderNum = "";
//	String refundYn = "";		
//	String paymentYn = ""; //0:정상 ->환불 처리시 : 1 로 수정
//	String paymentCase = ""; //B복합결제, M:현금, C:카드, E:기타
//	String workDatetime = "";
//	String result_code = "";
//	String result_message = "";
	
	String type;
	
	private List<SaleDTO> saleDTOList;
	
	public List<SaleDTO> getSaleDTOList() {
		return saleDTOList;
	}

	/**
	 * 검색 타입 SS가 있을 경우 selectSaleProduct쿼리에서 SALE_TYPE의 조건이 SS인것들만 들고옴
	 */
	String saleProductSearchType;
	
	// 예매조회/취소를 위한 변수
	String content_mst_cd;
	String today;
	int list_type;
	
	
}
