package com.bluecom.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bluecom.common.domain.AlimTalkDTO;
import com.bluecom.common.domain.AlimTalkTemplateDTO;
import com.bluecom.common.service.MessageService;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSocialSaleDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("bgfAlimTalk")
public class BgfAlimTalkServiceImpl extends EgovAbstractServiceImpl implements MessageService {

	@Autowired
	private BgfAlimTalkDAO alimTalkMapper;

	@Override
	public boolean send(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail)
			throws Exception {
		ApiSocialSaleDTO sale = apiResult.getSocialSales().get(0);
		WebPaymentDTO payment = apiResult.getWebPayment();
		
		AlimTalkTemplateDTO template = getScheduleReservationMessage(request, sale, payment, pgResult, shopDetail);
		
		AlimTalkDTO alimTalk = new AlimTalkDTO();
		alimTalk.setMsg_type(template.getMsg_type());
		alimTalk.setDstaddr(payment.getReserverPhone());
		alimTalk.setCallback(template.getCallback());
		alimTalk.setSubject(template.getSubject());
		alimTalk.setText(template.getText());
		alimTalk.setText_type(template.getText_type());
		alimTalk.setExt_col1(template.getExt_col1());
		alimTalk.setSender_key(template.getSender_key());
		alimTalk.setTemplate_code(template.getTemplate_code());
		alimTalk.setChangeflag(template.getChangeflag());
		
		alimTalkMapper.insertAlimTalk(alimTalk);
		return true;
	}

	private AlimTalkTemplateDTO getScheduleReservationMessage(HttpServletRequest request, ApiSocialSaleDTO sale, WebPaymentDTO payment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception{

		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		
		if(payment.getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			searchTemplate.setShop_code("JEJUBEER");
		}
		else if(payment.getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			searchTemplate.setShop_code("DIAMONDBAY");
		}
		
		searchTemplate.setType("RESERVE");
//		searchTemplate.setProduct_group_kind("S");		
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
//		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
//		String productName = sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME();
//		int productCount = sale.getSALE_PRODUCT_LIST().size();
		int productCount = payment.getTotal_count();
//		if(productCount > 1) {
//			productName += " 등 "; 
//		}
//		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", payment.getPlay_date());
		text = text.replace("#{6}", payment.getStart_time());
		text = text.replace("#{7}", payment.getReserverName());
		Date workDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.getWORK_DATETIME());
		text = text.replace("#{8}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(workDatetime));	
		text = text.replace("#{9}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
//		text = text.replace("#{10}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/termsOfUse?content_mst_cd=" + payment.getContent_mst_cd());
//		text = text.replace("#{11}", StringUtils.hasText(shopDetail.getWeekday_use_time()) ? shopDetail.getWeekday_use_time() : "");	
//		text = text.replace("#{12}", StringUtils.hasText(shopDetail.getWeekend_use_time()) ? shopDetail.getWeekend_use_time() : "");	
//		text = text.replace("#{13}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/refundPolicy?content_mst_cd=" + payment.getContent_mst_cd());
//		text = text.replace("#{14}", StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");	
		template.setText(text);
		
		// 알림톡 키가 업을 때에		
		if (StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		//template.setTemplate_code("jejubeer1001");		
		
		
		if(payment.getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			template.setTemplate_code("jejubeer1001");
		}
		else if(payment.getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			template.setTemplate_code("diamond0001");
		}
		
		
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
		
		return template;
	}
	
	@Override
	public boolean sendRefund(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO webPayment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception {
		AlimTalkTemplateDTO template = getScheduleRefundMessage(request, saleVO, webPayment, pgResult, shopDetail);
		
		AlimTalkDTO alimTalk = new AlimTalkDTO();
		alimTalk.setMsg_type(template.getMsg_type());
		alimTalk.setDstaddr(saleVO.getMember_tel());
		alimTalk.setCallback(template.getCallback());
		alimTalk.setSubject(template.getSubject());
		alimTalk.setText(template.getText());
		alimTalk.setText_type(template.getText_type());
		alimTalk.setExt_col1(template.getExt_col1());
		alimTalk.setSender_key(template.getSender_key());
		alimTalk.setTemplate_code(template.getTemplate_code());
		alimTalk.setChangeflag(template.getChangeflag());
		
		alimTalkMapper.insertAlimTalk(alimTalk);
		
		return true;
	}
	
	private AlimTalkTemplateDTO getScheduleRefundMessage(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO payment, WebPaymentPgResultDTO pgResult,
			ShopDetailVO shopDetail) throws Exception{

		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		
		
		if(payment.getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			searchTemplate.setShop_code("JEJUBEER");
		}
		else if(payment.getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			searchTemplate.setShop_code("DIAMONDBAY");
		}
		
		searchTemplate.setType("REFUND");
		searchTemplate.setProduct_group_kind("S");
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
//		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
		String productName = saleVO.getSaleProducts().get(0).getProduct_name();
//		int productCount = saleVO.getSaleProducts().size();
//		if(productCount > 1) {
//			productName += " 등 "; 
//		}
		int productCount = payment.getTotal_count();
		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", payment.getPlay_date());
		text = text.replace("#{6}", payment.getStart_time());
		text = text.replace("#{7}", payment.getReserverName());
		text = text.replace("#{8}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(saleVO.getReg_Date()));
		text = text.replace("#{9}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
//		text = text.replace("#{14}", StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		
		template.setText(text);
		
		// 알림톡 키가 업을 때에		
		if (StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		template.setTemplate_code("jejubeer1002");		
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
				
		return template;
	}
	
	@Override
	
	public boolean sendChange(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception {
		ApiSocialSaleDTO sale = apiResult.getSocialSales().get(0);
		WebPaymentDTO payment = apiResult.getWebPayment();
		
		AlimTalkTemplateDTO template = getScheduleChangeMessage(request, sale, payment, pgResult, shopDetail);
		
		AlimTalkDTO alimTalk = new AlimTalkDTO();
		alimTalk.setMsg_type(template.getMsg_type());
		alimTalk.setDstaddr(payment.getReserverPhone());
		alimTalk.setCallback(template.getCallback());
		alimTalk.setSubject(template.getSubject());
		alimTalk.setText(template.getText());
		alimTalk.setText_type(template.getText_type());
		alimTalk.setExt_col1(template.getExt_col1());
		alimTalk.setSender_key(template.getSender_key());
		alimTalk.setTemplate_code(template.getTemplate_code());
		alimTalk.setChangeflag(template.getChangeflag());
		
		alimTalkMapper.insertAlimTalk(alimTalk);
		
		return true;
	}
	
	private AlimTalkTemplateDTO getScheduleChangeMessage(HttpServletRequest request, ApiSocialSaleDTO sale, WebPaymentDTO payment, WebPaymentPgResultDTO pgResult,
			ShopDetailVO shopDetail) throws Exception{

		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		searchTemplate.setShop_code("JEJUBEER");
		searchTemplate.setType("CHANGE");
		searchTemplate.setProduct_group_kind("S");
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
//		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
//		String productName = sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME();
//		int productCount = sale.getSALE_PRODUCT_LIST().size();
//		if(productCount > 1) {
//			productName += " 등 "; 
//		}
		int productCount = payment.getTotal_count();
//		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", payment.getPlay_date());
		text = text.replace("#{6}", payment.getStart_time());
		text = text.replace("#{7}", payment.getReserverName());
		Date workDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.getWORK_DATETIME());
		text = text.replace("#{8}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(workDatetime));
		text = text.replace("#{9}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
//		text = text.replace("#{14}", StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		
		template.setText(text);
		
		// 알림톡 키가 업을 때에		
		if (StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		template.setTemplate_code("jejubeer1003");		
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
				
		return template;
	}
}