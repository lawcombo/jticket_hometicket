package com.bluecom.common.service;

import javax.servlet.http.HttpServletRequest;

import com.bluecom.common.domain.MailVO;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;

public interface MailService {

	boolean sendReserve(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult) throws Exception;

	boolean sendRefund(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO webPayment, WebPaymentPgResultDTO pgResult) throws Exception;
	
	boolean sendChange(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult) throws Exception;
	
}
