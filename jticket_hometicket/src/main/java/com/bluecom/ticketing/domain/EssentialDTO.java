package com.bluecom.ticketing.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EssentialDTO implements Serializable {

	@NotNull(message = "시설정보가 없습니다")
	@Size(min = 1, message = "시설정보가 없습니다.")
	String content_mst_cd;
	String product_group_code;

	//원주 소금산벨리 예매버튼 클릭시 넘겨받는 회원정보
	String userId 	= "";
	String userName = "";
	
	
}
