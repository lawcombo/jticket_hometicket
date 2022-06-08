package com.bluecom.ticketing.domain;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class VisitorDTO implements Serializable {

	int web_payment_idx;
	
	@Valid
	@NotNull(message= "방문자 코드가 없습니다")
	String product_code;
	
	@Valid
	@NotNull(message= "방문자 이름은 필수입력항목입니다")
	String name;
	
	@Valid
	@NotNull(message= "방문자 전화번호는 필수입력항목입니다")
	String phone;
	
	@Valid
	@NotNull
	@Length(min=6, max=6, message= "방문자 주민번호 자리수가 맞지 않습니다.")
	String jumin1;
	
	@Valid
	@NotNull(message= "방문자 주민번호는 필수입력항목입니다")
	@Length(min=1, max=1)
	String jumin2;
	
	@Valid
	@NotNull
	String addr;
	
	
	public String getJumin() {
		return jumin1 + jumin2;
	}
	
}
