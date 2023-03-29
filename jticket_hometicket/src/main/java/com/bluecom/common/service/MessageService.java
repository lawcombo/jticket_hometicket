package com.bluecom.common.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.SaleDTO;
import com.bluecom.ticketing.domain.SaleProductDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.SaleVO_noSchedule;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;

public interface MessageService {

	boolean send(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception;

	boolean sendRefund(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO webPayment, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception;
	
	boolean sendChange(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception;
	
	
	boolean send_noSchedule(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception;
	boolean sendRefund_noSchedule(HttpServletRequest request, SaleVO_noSchedule saleVO, WebPaymentDTO webPayment, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception;
	
}
