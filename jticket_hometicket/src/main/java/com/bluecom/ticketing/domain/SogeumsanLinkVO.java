/**
* Comment  : NiceResLink VO
* @version : 1.0
* @date    : "2022-12-19"
* @author  : hoon
*/

package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter

public class SogeumsanLinkVO implements Serializable{
	
	
	
	//============================= 예매 주문 START =======================================
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
    public static class ReqNappleToNiceOfRes  {
		
		@JsonProperty("Param")
		private socialSaleHeader Param;
		
	}
	
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
	public static class socialSaleHeader {
		
		//관광지(시설)연동코드 ( 프로방스 : CDPROVENCE_0_1 )
		@JsonProperty("CONTENT_MST_CD")	
		private String CONTENT_MST_CD 		= "";
		
		//거래 일자
		@JsonProperty("SALE_DATE")	
		private String SALE_DATE 			= "";
				
		//단말기 거래 일시
		@JsonProperty("TERMINAL_DATETIME")	
		private String TERMINAL_DATETIME 	= "";
				
		//사용자(판매 소셜ID)
		@JsonProperty("USER_ID")	
		private String USER_ID 				= "";
		
		@JsonProperty("USER_NM")	
		private String USER_NM 				= "";
		
		@JsonProperty("USER_TEL")	
		private String USER_TEL 			= "";
		
		//대표주문번호
		@JsonProperty("ORDER_NUM")	
		private String ORDER_NUM 			= "";
		
		//사용가능 시작 날짜
		@JsonProperty("VALID_FROM")	
		private String VALID_FROM 			= "";
		
		//사용가능 종료 날짜
		@JsonProperty("VALID_TO")	
		private String VALID_TO 			= "";
		
		@JsonProperty("STATUS_FLAG")	
		private String STATUS_FLAG 			= "";	// sale:예매, refund:예매취소, ticketing:발권
		
		//상품정보 LIST
		@JsonProperty("SALE_PRODUCT_LIST")	
		private List<socialSaleProductList> SALE_PRODUCT_LIST;
		
		//판매정보 LIST
		//@JsonProperty("PAYMENTS_INFO")	
		//private List<socialSalePaymentInfoList> PAYMENTS_INFO;
		
		//카드정보 LIST
		//@JsonProperty("CARD_INFO")	
		//private List<socialSaleCardInfoList> CARD_INFO;
		
	}
	
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
	public static class socialSaleProductList {
		
		//상품코드 ( 나이스에서 채번한 상품코드 맵핑 )
		@JsonProperty("PRODUCT_CODE")	
		private String PRODUCT_CODE 			= "";
		
		//상품명
		@JsonProperty("PRODUCT_NAME")	
		private String PRODUCT_NAME 			= "";
		
		//개별예매번호 (개별바코드)
		@JsonProperty("BOOK_NO")	
		private String BOOK_NO 					= "";
		
		//단가
		@JsonProperty("UNIT_PRICE")	
		private int UNIT_PRICE;
		
		//수량
		@JsonProperty("QUANTITY")	
		private int QUANTITY;
		
		//입장일 ( 예매할때 선택한 입장일 )
		@JsonProperty("PLAY_DATE")	
		private String PLAY_DATE 				= "";
		
		//상품금액 ( 수량 * 단가 )
		@JsonProperty("PRODUCT_FEE")	
		private int PRODUCT_FEE;

		//좌성코드 ( 사용안함 )
		@JsonProperty("SEAT_CODE")	
		private String SEAT_CODE 				= "";
		
		//회차코드 ( 회차 상품을 판매시, 나이스에서 채번한 회차코드를 기입 )
		@JsonProperty("SCHEDULE_CODE")	
		private String SCHEDULE_CODE 			= "";
		
	}
	
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
	public static class socialSalePaymentInfoList {
		
		@JsonProperty("PAYMENT_CODE")	
		private String PAYMENT_CODE 		= "";
		
		@JsonProperty("PAYMENT_NO")	
		private String PAYMENT_NO			= "";
		
		@JsonProperty("PAYMENT_IDX")	
		private int PAYMENT_IDX;
		
		@JsonProperty("PAYMENT_FEE")	
		private int PAYMENT_FEE;
		
		@JsonProperty("PENALTY_FEE")	
		private int PENALTY_FEE;
		
		@JsonProperty("PAYMENT_KIND")	
		private String PAYMENT_KIND			= "";
		
		@JsonProperty("PAYMENT_USER_ID")	
		private String PAYMENT_USER_ID		= "napple";
		
		@JsonProperty("PAYMENT_DATE")	
		private String PAYMENT_DATE			= "";
		
		@JsonProperty("DECIDE_DATE")	
		private String DECIDE_DATE			= "";
		
		@JsonProperty("DECIDE_ID")	
		private String DECIDE_ID			= " ";
		
		@JsonProperty("BILL_YN")	
		private String BILL_YN				= "1";
		
		@JsonProperty("BILL_DATETIME")	
		private String BILL_DATETIME		= "";
		
	}
	
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
	public static class socialSaleCardInfoList {
		
		
		@JsonProperty("PAYMENT_CODE")	
		private String PAYMENT_CODE 		= "";
		
		@JsonProperty("PAYMENT_NO")	
		private String PAYMENT_NO			= "";
		
		@JsonProperty("PAYMENT_IDX")	
		private int PAYMENT_IDX;
		
		
		@JsonProperty("VAN_CODE")	
		private String VAN_CODE					= "";
		
		
		@JsonProperty("TRANSACTION_DATETIME")	
		private String TRANSACTION_DATETIME		= "";
		
		
		@JsonProperty("APPROVAL_AMOUNT")	
		private int APPROVAL_AMOUNT;
		
		
		@JsonProperty("DISCOUNT_AMOUNT")	
		private int DISCOUNT_AMOUNT;
		
		
		@JsonProperty("SERVICE_AMOUNT")	
		private int SERVICE_AMOUNT;
		
		@JsonProperty("TAX_AMOUNT")	
		private int TAX_AMOUNT;
		
		@JsonProperty("FEE_AMOUNT")	
		private int FEE_AMOUNT;
		
		
		@JsonProperty("ORIGINAL_TRANSACTION_DATETIME")	
		private String ORIGINAL_TRANSACTION_DATETIME		= "";
		
		//부분환불 가능여부 0:불가능 / 1:가능
		@JsonProperty("PART_CANCEL_YN")	
		private String PART_CANCEL_YN		= "";
		
	}
	
	//============================== 예매 주문 END =========================================
	
	
	
	
	//=============================== 예매 주문 취소 START ====================================
	
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
    public static class ReqNappleToNiceOfResRefund  {
		
		@JsonProperty("Param")
		private socialRefundHeader Param;
		
	}
	
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
	public static class socialRefundHeader {
		
		//관광지(시설)연동코드 ( 프로방스 : CDPROVENCE_0_1 )
		@JsonProperty("CONTENT_MST_CD")	
		private String CONTENT_MST_CD 		= "";
		
		// 온라인 판매 채널 ( 네이플 : 0095 )
		@JsonProperty("ONLINE_CHANNEL")	
		private String ONLINE_CHANNEL 		= "";
		
		// 주문번호
		@JsonProperty("ORDER_NUM")	
		private String ORDER_NUM 			= "";
		
		// 사용자
		@JsonProperty("USER_ID")	
		private String USER_ID 				= "";
		
		// 단말기 코드
		@JsonProperty("TERMINAL_CODE")	
		private String TERMINAL_CODE 		= "";
		
		// 취소 단말기 거래 일시
		@JsonProperty("TERMINAL_DATETIME")	
		private String TERMINAL_DATETIME 	= "";
		
		// 전체 취소인지 부분취소인지
		// A: 주문번호 기준 ( 전체 )
		// E: 예매번호 기준 ( 부분 )
		@JsonProperty("CANCEL_TYPE")	
		private String CANCEL_TYPE 			= "";
		
		
		//상품정보 LIST
		@JsonProperty("SALE_PRODUCT_LIST")	
		private List<socialRefundProductList> SALE_PRODUCT_LIST;
		
	}
	

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
	public static class socialRefundProductList {
		
		//개별예매번호 ( 개별 바코드 )
		@JsonProperty("BOOK_NO")	
		private String BOOK_NO = "";
		
	}
	
	
}
