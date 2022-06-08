package com.bluecom.api.utills;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
public class DataVO implements Serializable {

	// 예매 처리 
	private String rescd;
	private String itemcd;
	private String qrcode;  // qrCode
	
	
	// 예매 취소 - 없음
	
	// 회차 및 입장인원 조회
	private String turncd;  // 회차코드
	private String turnnm;  // 회차이름
	private String enttime;     // 입장시간(시작시간)
	private String enttimeto;       // 퇴장시간(종료시간)
	private int entcnt;      // 입장인원(총 인원인지, 입장한 인원인지는 모름)
	private int unitcnt;    // 현제인원(현제 입장한 인원인가 구매인원인가 모름)
	

	public DataVO() {
		super();
	}

}
