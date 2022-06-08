package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class WebPaymentPgResultDTO implements Serializable {

	int idx;
	int web_payment_idx; 
	Date time;
	String auth_result_code;
	String auth_result_msg;
	String next_ap_url;
	String transaction_id;
	String auth_token;
	String pay_method;
	String mid;
	String moid;
	String amt;
	String net_cancel_url;
	String auth_signature_comparison;
	String approval_result_code;
	String approval_result_msg;
	String tid;
	
	// 카드, 계좌이체, 휴대폰
	String auth_code;
	String auth_date;
	
	// 카드일 경우
	String cardNo; // 카드번호
	String cardQuota; // 할부개월
	String cardCode; // 결제 카드사 코드
	String cardName; // 결제 카드사명
	String acquCardCode; // 매입 카드사 코드
	String acquCardName; // 매입 카드사 이름
	
	// 계좌이체
//	String bankCode; // 결제은행코드 // cardCode에 기입
//	String bankName; // 결제은행명
	
	String rcptType; // 현금영수증타입
	
	// 취소
	String visitor_type;
	String name;
	String phone;
	
	String org_order_num;
	
	//취소 승인 기록
	String result_code;
	String result_msg;
	String cancel_amt;
	String cancel_time;
	
	//쿠폰
	String coupon="0";
}
