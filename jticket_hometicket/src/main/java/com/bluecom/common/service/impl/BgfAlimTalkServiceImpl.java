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
import com.bluecom.common.util.DateHelper;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSocialSaleDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.SaleVO_noSchedule;
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
		
		AlimTalkTemplateDTO template = null;
		
		if (payment.getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			template = getScheduleReservationMessage(request, sale, payment, pgResult, shopDetail);
		}
		else if(payment.getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			template = getScheduleReservationMessageOfDiamondBay(request, sale, payment, pgResult, shopDetail);
		}
		
		//AlimTalkTemplateDTO template = getScheduleReservationMessage(request, sale, payment, pgResult, shopDetail);
		
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
		
		searchTemplate.setShop_code("JEJUBEER");
		
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
		
		//템플릿내용 변경으로 템플릿 코드 수정 ( 1001 -> 1006 -> 1010)
		template.setTemplate_code("jejubeer1010");
		
		
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
		
		return template;
	}
	
	/**
	 * 다이아몬드베이 알림톡
	 * @param request
	 * @param sale
	 * @param payment
	 * @param pgResult
	 * @param shopDetail
	 * @return
	 * @throws Exception
	 */
	private AlimTalkTemplateDTO getScheduleReservationMessageOfDiamondBay(HttpServletRequest request, ApiSocialSaleDTO sale, WebPaymentDTO payment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception{

		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		
		searchTemplate.setShop_code("DIAMONDBAY");
		searchTemplate.setType("RESERVE");
//		searchTemplate.setProduct_group_kind("S");		
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
		
		
		String productName = "";
		if(sale.getSALE_PRODUCT_LIST().size() > 1)
		{//상품여러개
			for(int i=0; i<sale.getSALE_PRODUCT_LIST().size(); i++)
			{
				if(i==0)
				{
					productName += sale.getSALE_PRODUCT_LIST().get(i).getPRODUCT_NAME() + "(" + sale.getSALE_PRODUCT_LIST().get(i).getQUANTITY() + "매)";
				}
				else
				{
					productName += ", " + sale.getSALE_PRODUCT_LIST().get(i).getPRODUCT_NAME() + "(" + sale.getSALE_PRODUCT_LIST().get(i).getQUANTITY() + "매)";
				}
			}
		}else
		{
			productName = sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME();
		}
		
		//String productName = sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME();
//		int productCount = sale.getSALE_PRODUCT_LIST().size();
		int productCount = payment.getTotal_count();
//		if(productCount > 1) {
//			productName += " 등 "; 
//		}
		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", payment.getPlay_date() +"("+payment.getStart_time()+"~"+payment.getEnd_time()+")");
//		text = text.replace("#{6}", payment.getStart_time());
		text = text.replace("#{6}", payment.getReserverName());
		Date workDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.getWORK_DATETIME());
		text = text.replace("#{7}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(workDatetime));	
		text = text.replace("#{8}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
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
		
		
		template.setTemplate_code("diamond0001");
		
		
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
		
		return template;
	}
	
	@Override
	public boolean sendRefund(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO webPayment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception {
		
		
		AlimTalkTemplateDTO template = getScheduleRefundMessage(request, saleVO, webPayment, pgResult, shopDetail);
		
		if (webPayment.getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			template = getScheduleRefundMessage(request, saleVO, webPayment, pgResult, shopDetail);
		}
		else if(webPayment.getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			template = getScheduleRefundMessageOfDiamondBay(request, saleVO, webPayment, pgResult, shopDetail);
		}
		
		
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
		
		searchTemplate.setShop_code("JEJUBEER");
		
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
		
		//template.setTemplate_code("jejubeer1002");
		//템플릿내용 변경으로 템플릿 코드 수정 ( 1002 -> 1007 )
		template.setTemplate_code("jejubeer1007");
		
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
				
		return template;
	}
	
	private AlimTalkTemplateDTO getScheduleRefundMessageOfDiamondBay(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO payment, WebPaymentPgResultDTO pgResult,
			ShopDetailVO shopDetail) throws Exception{
		
		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		
		searchTemplate.setShop_code("DIAMONDBAY");
		
		searchTemplate.setType("REFUND");
		searchTemplate.setProduct_group_kind("2");
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
		
		
		
		String productName = "";
		if(saleVO.getSaleProducts().size() > 1)
		{//상품여러개
			for(int i=0; i<saleVO.getSaleProducts().size(); i++)
			{
				if(i==0)
				{
					productName += saleVO.getSaleProducts().get(i).getProduct_name() + "(" + saleVO.getSaleProducts().get(i).getQuantity() + "매)";
				}
				else
				{
					productName += ", " + saleVO.getSaleProducts().get(i).getProduct_name() + "(" + saleVO.getSaleProducts().get(i).getQuantity() + "매)";
				}
			}
		}else
		{
			productName = saleVO.getSaleProducts().get(0).getProduct_name();
		}
		
//		String productName = saleVO.getSaleProducts().get(0).getProduct_name();
//		int productCount = saleVO.getSaleProducts().size();
//		if(productCount > 1) {
//			productName += " 등 "; 
//		}
		int productCount = payment.getTotal_count();
		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", payment.getPlay_date() +"("+payment.getStart_time()+"~"+payment.getEnd_time()+")");
		//text = text.replace("#{6}", payment.getStart_time());
		text = text.replace("#{7}", payment.getReserverName());
		
		text = text.replace("#{8}", DateHelper.getTimeStamp(12) + " " + DateHelper.getTimeStamp(13) + ":" + DateHelper.getTimeStamp(14));
		text = text.replace("#{9}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
		
		template.setText(text);
		
		// 알림톡 키가 업을 때에		
		if (StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		template.setTemplate_code("diamond0003");		
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
		
		//템플릿내용 변경으로 템플릿 코드 수정 ( 1003 -> 1008 )
		//template.setTemplate_code("jejubeer1003");	
		template.setTemplate_code("jejubeer1008");	
		
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
				
		return template;
	}
	
	
	@Override
	public boolean sendRefund_noSchedule(HttpServletRequest request, SaleVO_noSchedule saleVO, WebPaymentDTO webPayment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception {
		AlimTalkTemplateDTO template = null;
		if(webPayment.getProduct_group_kind().equals("1")) {
			template = getNormalRefundMessage(request, saleVO, webPayment, pgResult, shopDetail);
		} else if(webPayment.getProduct_group_kind().equals("2")) {
			template = getScheduleRefundMessage_noSchedule(request, saleVO, webPayment, pgResult, shopDetail);
		} else {
			return false;
		}
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
	
	private AlimTalkTemplateDTO getNormalRefundMessage(HttpServletRequest request, SaleVO_noSchedule saleVO, WebPaymentDTO payment, WebPaymentPgResultDTO pgResult,
			ShopDetailVO shopDetail) throws Exception{
		
		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		searchTemplate.setShop_code("MCYCAMPING");
		searchTemplate.setType("REFUND");
		searchTemplate.setProduct_group_kind("1");		

		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
		String productName = saleVO.getSaleProducts().get(0).getProduct_name();
		int productCount = saleVO.getSaleProducts().size();
		if(productCount > 1) {
			productName += " 등 "; 
		}
		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", saleVO.getValid_from());
		text = text.replace("#{6}", saleVO.getValid_to());
		text = text.replace("#{7}", payment.getReserverName());
		text = text.replace("#{8}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(saleVO.getWork_Datetime()));
		text = text.replace("#{9}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/mcycamping/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("#{14}", StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		text = text.replace("#{15}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		template.setText(text);

		// 알림톡 키가 업을 때에		
		if (StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() + "0002" : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
		
		return template;
	}
	
	
	private AlimTalkTemplateDTO getScheduleRefundMessage_noSchedule(HttpServletRequest request, SaleVO_noSchedule saleVO, WebPaymentDTO payment, WebPaymentPgResultDTO pgResult,
			ShopDetailVO shopDetail) throws Exception{

		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		searchTemplate.setShop_code("MCYCAMPING");
		searchTemplate.setType("REFUND");
		searchTemplate.setProduct_group_kind("2");
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
		String productName = saleVO.getSaleProducts().get(0).getProduct_name();
		int productCount = saleVO.getSaleProducts().size();
		if(productCount > 1) {
			productName += " 등 "; 
		}
		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", saleVO.getValid_from());		
		text = text.replace("#{6}", payment.getPlay_sequence() + "(" + payment.getStart_time() + "~" + payment.getEnd_time() + ")");
		text = text.replace("#{7}", payment.getReserverName());
		text = text.replace("#{8}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(saleVO.getWork_Datetime()));
		text = text.replace("#{9}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/mcycamping/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("#{14}", StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
		
		template.setText(text);
		
		// 알림톡 키가 업을 때에		
		if (StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() + "0004" : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
				
		return template;
	}
	
	
	@Override
	public boolean send_noSchedule(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail)
			throws Exception {
		ApiSocialSaleDTO sale = apiResult.getSocialSales().get(0);
		WebPaymentDTO payment = apiResult.getWebPayment();
		
		AlimTalkTemplateDTO template = null;
		
		System.out.println("@@@@@@@ : " + payment.getProduct_group_kind());
		
		if(payment.getProduct_group_kind().equals("1")) {
			template = getNormalReservationMessage_noSchedule(request, sale, payment, pgResult, shopDetail);
		} else if(payment.getProduct_group_kind().equals("2")) {
			template = getScheduleReservationMessage_noSchedule(request, sale, payment, pgResult, shopDetail);
		} else {
			return false;
		}
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
	
	private AlimTalkTemplateDTO getNormalReservationMessage_noSchedule(HttpServletRequest request, ApiSocialSaleDTO sale, WebPaymentDTO payment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception{
		
		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		
		searchTemplate.setShop_code("MCYCAMPING");
		searchTemplate.setType("RESERVE");
		searchTemplate.setProduct_group_kind("1");
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
		String productName = sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME();
		int productCount = sale.getSALE_PRODUCT_LIST().size();
		if(productCount > 1) {
			productName += " 등 "; 
		}
		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", sale.getVALID_FROM());
		text = text.replace("#{6}", sale.getVALID_TO());
		text = text.replace("#{7}", payment.getReserverName());
		text = text.replace("#{8}", sale.getWORK_DATETIME());	
		//text = text.replace("#{9}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/mcycamping/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());
		//text = text.replace("#{10}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/termsOfUse?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("#{11}", StringUtils.hasText(shopDetail.getWeekday_use_time()) ? shopDetail.getWeekday_use_time() : "");	
		text = text.replace("#{12}", StringUtils.hasText(shopDetail.getWeekend_use_time()) ? shopDetail.getWeekend_use_time() : "");	
		//text = text.replace("#{13}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/mcycamping/refundPolicy?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("#{14}", StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");	
		template.setText(text);
		
		// 알림톡 키가 업을 때에		
		if(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
			
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() + "0001" : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
		
		return template;
	}
	
	
	private AlimTalkTemplateDTO getScheduleReservationMessage_noSchedule(HttpServletRequest request, ApiSocialSaleDTO sale, WebPaymentDTO payment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception{

		AlimTalkTemplateDTO searchTemplate = new AlimTalkTemplateDTO(); // default
		searchTemplate.setShop_code("SOGEUMSAN");
		searchTemplate.setType("RESERVE");
		searchTemplate.setProduct_group_kind("2");		
		
		AlimTalkTemplateDTO template = alimTalkMapper.selectAlimTalkTemplate(searchTemplate);
		
		String text = template.getText();
		text = text.replace("#{0}", payment.getShop_name());
		text = text.replace("#{1}", payment.getOrder_no());
		text = text.replace("#{2}", payment.getProduct_group_name());
		String productName = sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME();
		int productCount = sale.getSALE_PRODUCT_LIST().size();
		if(productCount > 1) {
			productName += " 등 "; 
		}
		text = text.replace("#{3}", productName);
		text = text.replace("#{4}", Integer.toString(productCount));
		text = text.replace("#{5}", sale.getVALID_FROM());
		text = text.replace("#{6}", payment.getPlay_sequence() + "(" + payment.getStart_time() + "~" + payment.getEnd_time() + ")");
		text = text.replace("#{7}", payment.getReserverName());
		text = text.replace("#{8}", sale.getWORK_DATETIME());	
		text = text.replace("#{9}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());
//		text = text.replace("#{10}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/termsOfUse?contentMstCd=" + payment.getContent_mst_cd());
		text = text.replace("#{11}", StringUtils.hasText(shopDetail.getWeekday_use_time()) ? shopDetail.getWeekday_use_time() : "");	
		text = text.replace("#{12}", StringUtils.hasText(shopDetail.getWeekend_use_time()) ? shopDetail.getWeekend_use_time() : "");	
//		text = text.replace("#{13}", "https://" + request.getServerName() + request.getContextPath() + "/ticketing/refundPolicy?contentMstCd=" + payment.getContent_mst_cd());
		text = text.replace("#{14}", StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");	
		template.setText(text);
		
		// 알림톡 키가 업을 때에		
		if (StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) && StringUtils.hasText(shopDetail.getAlimtalk_sender_key())) {
			template.setMsg_type("7");
			template.setChangeflag("1");
		} else {
			template.setMsg_type("3");
		}
		
		template.setCallback(StringUtils.hasText(shopDetail.getComp_tel()) ? shopDetail.getComp_tel() : "");
//		template.setTemplate_code(StringUtils.hasText(shopDetail.getAlimtalk_comp_code()) ? shopDetail.getAlimtalk_comp_code() + "0003" : "");
		template.setSender_key(StringUtils.hasText(shopDetail.getAlimtalk_sender_key()) ? shopDetail.getAlimtalk_sender_key() : "");
		
		return template;
	}
}