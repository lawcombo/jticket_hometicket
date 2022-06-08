package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResultVO  implements Serializable {

	int success;
	String errMsg;

	WebPaymentDTO webPayment;
	List<ApiSocialSaleDTO> socialSales;

	public ApiResultVO(int theSuccess, String theErrMsg) {
		this.success = theSuccess;
		this.errMsg = theErrMsg;
	}
}
