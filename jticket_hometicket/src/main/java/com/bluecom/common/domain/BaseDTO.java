package com.bluecom.common.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO implements Serializable{

	protected int resultCode;
	protected String resultMessage;
	
}
