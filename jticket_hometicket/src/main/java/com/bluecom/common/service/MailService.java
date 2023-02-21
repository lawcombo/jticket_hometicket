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

	//제주맥주 , 다이아몬드베이 예매 성공 안내 메일
	boolean sendReserve(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult) throws Exception;

	//제주맥주 예매 취소 안내 메일
	boolean sendRefund(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO webPayment, WebPaymentPgResultDTO pgResult) throws Exception;
	
	//다이아몬드베이 예매 취소 안내 메일
	boolean sendRefundOfDiamondbay(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO webPayment, WebPaymentPgResultDTO pgResult) throws Exception;
	
	boolean sendChange(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult) throws Exception;
	
	
	
	//noSchedule
	boolean sendReserve(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception;
	
}
