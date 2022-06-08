package com.bluecom.ticketing.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReserverInfoDTO implements Serializable {

	int idx;
	String name = "";
	String phone = "";
	String email = "";
	String code = "";
	String authNo = "";
	String returnCode = "";
	String responseSEQ = "";

}
