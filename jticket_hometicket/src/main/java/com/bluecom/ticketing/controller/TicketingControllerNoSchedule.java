package com.bluecom.ticketing.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bluecom.api.utills.ApiConnectionToolkit;
import com.bluecom.common.controller.BaseController;
import com.bluecom.common.service.CommonService;
import com.bluecom.common.service.MailService;
import com.bluecom.common.service.MessageService;
import com.bluecom.common.util.CommUtil;
import com.bluecom.common.util.DateHelper;
import com.bluecom.common.util.ScriptUtils;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSocialCancelDTO;
import com.bluecom.ticketing.domain.BaseDTO_noSchedule;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.ErrorPathCode;
import com.bluecom.ticketing.domain.ErrorSiteCode;
import com.bluecom.ticketing.domain.ErrorTypeCode;
import com.bluecom.ticketing.domain.EssentialDTO;
import com.bluecom.ticketing.domain.FinishTradeVO_noSchedule;
import com.bluecom.ticketing.domain.PaymentInfoDTO_noSchedule;
import com.bluecom.ticketing.domain.ProductDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO_noSchedule;
import com.bluecom.ticketing.domain.RefundHistoryVO;
import com.bluecom.ticketing.domain.RefundVO;
import com.bluecom.ticketing.domain.SaleDTO_noSchedule;
import com.bluecom.ticketing.domain.SaleProductDTO_noSchedule;
import com.bluecom.ticketing.domain.SaleVO_noSchedule;
import com.bluecom.ticketing.domain.ScheduleDTO;
import com.bluecom.ticketing.domain.SelfAuthenticationDTO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.ShopInfo_noScheduleDTO;
import com.bluecom.ticketing.domain.ShopPaymentsaleVO;
import com.bluecom.ticketing.domain.SogeumsanLinkVO;
import com.bluecom.ticketing.domain.TicketValidVO;
import com.bluecom.ticketing.domain.VerificationKeyVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;
import com.bluecom.ticketing.domain.WebPaymentStatusDTO;
import com.bluecom.ticketing.domain.WebReservationKeyDTO;
import com.bluecom.ticketing.service.ReserverAuthenticationService;
import com.bluecom.ticketing.service.TicketingService;
import com.fasterxml.jackson.databind.MapperFeature;

import egovframework.rte.fdl.property.EgovPropertyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Controller
@RequestMapping("/ticketing")
public class TicketingControllerNoSchedule extends BaseController {

	@Autowired
	TicketingService ticketingService;
	
	@Autowired
	EgovPropertyService propertyService;
	
	@Autowired
	@Qualifier("jejuMail")
	MailService mailService;

	@Autowired
	@Qualifier("bgfAlimTalk")
	MessageService messageService;
	
	@Autowired
	CommonService commonService;
	
	
	@Autowired
	ReserverAuthenticationService reserverAuthenticationService;



	private String removeQuatations(String text) {
		text = text.replaceAll("\"", "");
		text = text.replaceAll("\'", "");
		return text;
	}

	
	
	private boolean checkProductGroup_noSchedule(ProductGroupDTO_noSchedule productGroup, ProductGroupDTO_noSchedule dbProductGroup) {
		
		return productGroup.getShop_code().equals(dbProductGroup.getShop_code())
				&& productGroup.getContent_mst_cd().equals(dbProductGroup.getContent_mst_cd())
				&& productGroup.getProduct_group_code().equals(dbProductGroup.getProduct_group_code());
	}

	
	public final synchronized String getyyyyMMddHHmmss(){
		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
		return yyyyMMddHHmmss.format(new Date());
	}

	// SHA-256 형식으로 암호화
	public class DataEncrypt{
		MessageDigest md;
		String strSRCData = "";
		String strENCData = "";
		String strOUTData = "";
		
		public DataEncrypt(){ }
		public String encrypt(String strData){
			String passACL = null;
			MessageDigest md = null;
			try{
				md = MessageDigest.getInstance("SHA-256");
				md.reset();
				md.update(strData.getBytes());
				byte[] raw = md.digest();
				passACL = encodeHex(raw);
			}catch(Exception e){
				System.out.print("암호화 에러" + e.toString());
			}
			return passACL;
		}
		
		public String encodeHex(byte [] b){
			char [] c = Hex.encodeHex(b);
			return new String(c);
		}
	}

	//server to server 통신
	public String connectToServer(String data, String reqUrl, String encoding) throws Exception{
		HttpURLConnection conn 		= null;
		BufferedReader resultReader = null;
		PrintWriter pw 				= null;
		URL url 					= null;
		
		int statusCode = 0;
		StringBuffer recvBuffer = new StringBuffer();
		try{
			url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(25000);
			conn.setDoOutput(true);
			
			pw = new PrintWriter(conn.getOutputStream());
			pw.write(data);
			pw.flush();
			
			statusCode = conn.getResponseCode();
			resultReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			for(String temp; (temp = resultReader.readLine()) != null;){
				recvBuffer.append(temp).append("\n");
			}
			
			if(!(statusCode == HttpURLConnection.HTTP_OK)){
				throw new Exception();
			}
			
			return recvBuffer.toString().trim();
		}catch (Exception e){
			e.printStackTrace();
			return "9999";
		}finally{
			recvBuffer.setLength(0);
			
			try{
				if(resultReader != null){
					resultReader.close();
				}
			}catch(Exception ex){
				resultReader = null;
			}
			
			try{
				if(pw != null) {
					pw.close();
				}
			}catch(Exception ex){
				pw = null;
			}
			
			try{
				if(conn != null) {
					conn.disconnect();
				}
			}catch(Exception ex){
				conn = null;
			}
		}
	}

	//JSON String -> HashMap 변환
	private static HashMap jsonStringToHashMap(String str) throws Exception{
		HashMap dataMap = new HashMap();
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(str);
			JSONObject jsonObject = (JSONObject)obj;

			Iterator<String> keyStr = jsonObject.keySet().iterator();
			while(keyStr.hasNext()){
				String key = keyStr.next();
				Object value = jsonObject.get(key);
				
				dataMap.put(key, value);
			}
		}catch(Exception e){
			
		}
		return dataMap;
	}
	
	
	//=========================================MCY 캠핑파크 =================================================
		@GetMapping("/mcycamping/selectTicket")
		public String process1SelectTicketOfMcyCamping(@ModelAttribute("shopInfo") @Valid ShopInfo_noScheduleDTO shopInfo,
				Errors errors, HttpServletResponse response, RedirectAttributes redirectAttribute, Model model, HttpServletRequest request)
				throws Exception {
			
			if (errors.hasErrors()) {
				validationLog(errors);

				ScriptUtils.alertAndBackPage(response, "시설정보가 존재하지 않습니다");
				return null;
			}

			String shopCode = ticketingService.getShopCode(shopInfo.getContent_mst_cd());
			if(!StringUtils.hasText(shopCode)) {
				ScriptUtils.alertAndBackPage(response, "시설정보가 존재하지 않습니다");
				return null;
			}
			shopInfo.setShop_code(shopCode);
			
			// 본인인증 정보 가져오기
			SelfAuthenticationDTO selfAuthentication = new SelfAuthenticationDTO();
			selfAuthentication.setShop_code(shopCode);
			selfAuthentication.setContent_mst_cd(shopInfo.getContent_mst_cd());
			selfAuthentication.setSuccess_url("/checkReservationSuccess");
			selfAuthentication.setFail_url("/checkReservationFail");
			selfAuthentication = reserverAuthenticationService.getSelfAuthenticationEncodedData(request, selfAuthentication);		
			if(StringUtils.hasText(selfAuthentication.getMessage())) {
				ScriptUtils.alertAndBackPage(response, "본인인증 모듈 에러: " + selfAuthentication.getMessage());
				return null;
			}
			model.addAttribute("selfAuthentication", selfAuthentication);
			
			// 상품그룹 가져오기
			List<ProductGroupDTO> productGroups = ticketingService.getProductGroups_noSchedule(shopInfo.getContent_mst_cd());
			model.addAttribute("productGroups", productGroups);
			
			//=======로그인 정보 ( 소금산 밸리용 )======================
			model.addAttribute("loginUserId", shopInfo.getUserId());
			model.addAttribute("loginUserNm", shopInfo.getUserName());
			
			return "/ticketing/mcycamping/selectTicket";
		}
		
		
		@GetMapping("/mcycamping/insertReserver")
		public String process1InsertReserverOfMcyCamping(@ModelAttribute("paymentInfo") PaymentInfoDTO_noSchedule paymentInfo, Errors errors,
				HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirect, Model model)
				throws Exception {

			// content_mst_cd
			// product_group_code
			// products.product_code, products.count

			paymentInfo.setType(propertyService.getInt("type"));
			// shopCode 조회
			String shopCode = ticketingService.getShopCode(paymentInfo.getContent_mst_cd());
			paymentInfo.getProductGroup().setShop_code(shopCode);
			
			// 상품 그룹 확인
			//ProductGroupDTO dbProductGroup = ticketingService.getProductGroup(paymentInfo.getProductGroup());
			ProductGroupDTO_noSchedule dbProductGroup = ticketingService.getProductGroup_noSchedule(paymentInfo.getProductGroup());
			
			
			
			dbProductGroup.setType(propertyService.getInt("type"));
			paymentInfo.setProductGroup(dbProductGroup);

			// 해당하는 그룹에 해당하는 상품이 정상적인지 확인
			for(ProductDTO product : paymentInfo.getProducts()) {
				product.setShop_code(shopCode);
				product.setProduct_group_code(paymentInfo.getProductGroup().getProduct_group_code());
			}
			
			// 0명인 상품리스트 삭제
			paymentInfo.getProducts().removeIf(p -> p.getCount() <= 0);
			
			List<ProductDTO> dbProducts = ticketingService.getProducts_NoSchedule(paymentInfo.getProductGroup());
			
			
			List<ProductDTO> selectedProducts = new ArrayList<>();		
			// 상품코드로 상품 정보 다 불러오기, 인원수만 불러온 상품에 넣기
			for (int i = 0; i < paymentInfo.getProducts().size(); i++) {
				ProductDTO product = paymentInfo.getProducts().get(i);
				for (int j = 0; j < dbProducts.size(); j++) {
					ProductDTO dbProduct = dbProducts.get(j);
					if (product.getShop_code().equals(dbProduct.getShop_code())
							&& product.getProduct_group_code().equals(dbProduct.getProduct_group_code())
							&& product.getProduct_code().equals(dbProduct.getProduct_code())) {
						dbProduct.setCount(product.getCount());
						selectedProducts.add(dbProduct);
					}
				}
			}
			paymentInfo.setProducts(selectedProducts);

			// 총액, 총인원수
			paymentInfo.setTotalCount(selectedProducts.stream().mapToInt(ProductDTO::getCount).sum());
			paymentInfo
					.setTotalFee(selectedProducts.stream().map(p -> p.getProduct_fee().multiply(BigDecimal.valueOf(p.getCount())))
							.reduce(BigDecimal.ZERO, BigDecimal::add));

			// 방문자 타입(P이면 입장자 정보를 받는다. 안쓰지만 일단은 넣어둠
			String visitorType = ticketingService.getVisitorType(paymentInfo.getProductGroup().getShop_code());
			paymentInfo.setVisitorType(visitorType);

			// 유효기간 가져오기
			TicketValidVO ticketValid = new TicketValidVO();
			ticketValid.setShop_code(shopCode);
			ticketValid = ticketingService.selectTicketValid(ticketValid);
			
			
			
			if(ticketValid == null || !StringUtils.hasText(ticketValid.getValid_period())) { 
				ScriptUtils.alertAndBackPage(response, "티켓의 유효기간이 정의되지 않았습니다.");
				return null;
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			Date date2 = format.parse("2022-02-28");
			
			// 유효기간 설정 / 2022-02-25 / 조미근
			Calendar calTo = Calendar.getInstance(); 
			calTo.getActualMaximum(Calendar.DAY_OF_MONTH);
			String temp = String.valueOf(calTo.getActualMaximum(Calendar.DAY_OF_MONTH)-LocalDateTime.now().getDayOfMonth());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(now);
			if(now.compareTo(date2) > 0) { //2월 말 기준 이후
				paymentInfo.setValid_period(ticketValid.getValid_period());
			}else { //2월 말 기준 이전
				paymentInfo.setValid_period(temp);
			}
			
//				// 회차
//				if (StringUtils.hasText(paymentInfo.getSchedule_code())) {
//					ScheduleDTO schedule = new ScheduleDTO();
//					schedule.setShop_code(paymentInfo.getProductGroup().getShop_code());
//					schedule.setSchedule_code(paymentInfo.getSchedule_code());
//					schedule = ticketingService.getScheduleByScheduleCode(schedule);
	//
//					if (schedule == null) {
//						ScriptUtils.alertAndClose(response, "해당 상품의 회차정보가 존재하지 않습니다. 결제를 진행할 수 없습니다.");
//						return null;
//					}
//					paymentInfo.setSchedule(schedule);
//				}

			
			// 본인인증 정보 가져오기
			SelfAuthenticationDTO selfAuthentication = new SelfAuthenticationDTO();
			selfAuthentication.setShop_code(shopCode);
			selfAuthentication.setContent_mst_cd(paymentInfo.getContent_mst_cd());
			selfAuthentication.setSuccess_url("/checkReservationSuccess");
			selfAuthentication.setFail_url("/checkReservationFail");
			selfAuthentication = reserverAuthenticationService.getSelfAuthenticationEncodedData(request, selfAuthentication);		
			if(StringUtils.hasText(selfAuthentication.getMessage())) {
				ScriptUtils.alertAndBackPage(response, "본인인증 모듈 에러: " + selfAuthentication.getMessage());
				return null;
			}
			model.addAttribute("selfAuthentication", selfAuthentication);
			
			
			ShopDetailVO shopDetail = ticketingService.getShopDetail(paymentInfo.getProductGroup().getShop_code());
			model.addAttribute("shopDetail", shopDetail);

			
			
			
			// 이용약관 가져오기
			WebReservationKeyDTO reserveInfo = ticketingService.selectReserveInfo(dbProductGroup.getShop_code());
			
			if(reserveInfo == null || 
					!StringUtils.hasText(reserveInfo.getInfo_a()) ||
					!StringUtils.hasText(reserveInfo.getInfo_b()) ||
					!StringUtils.hasText(reserveInfo.getInfo_c()) ||
					!StringUtils.hasText(reserveInfo.getInfo_d()) 
					//!StringUtils.hasText(reserveInfo.getInfo_e())
					) {			
				redirect.addFlashAttribute("msg", "약관정보가 없습니다. 관리자에게 연락 바랍니다.");
				
				ScriptUtils.alertAndClose(response, "약관정보가 없습니다. 관리자에게 연락 바랍니다.");
				
				log.error("[ERROR]약관정보가 없습니다. 관리자에게 연락 바랍니다.");
				if(StringUtils.hasText(paymentInfo.getContent_mst_cd()) && StringUtils.hasText(paymentInfo.getProductGroup().getProduct_group_code())) {
					return "redirect:/ticketing/rimopass/hjcruise/schedule?content_mst_cd=" + paymentInfo.getContent_mst_cd() + "&product_group_code=" + paymentInfo.getProductGroup().getProduct_group_code();
				} else {
					return "redirect:/error";
				}
			}
			
			
			
			//개인정보 취급 방침
			String content = reserveInfo.getInfo_a();
			reserveInfo.setInfo_a(StringEscapeUtils.unescapeXml(content));
			
			//이용약관에 대한 동의
			content = reserveInfo.getInfo_b();
			reserveInfo.setInfo_b(StringEscapeUtils.unescapeXml(content));
			
			//이용약관
			//content = reserveInfo.getInfo_c();
			//reserveInfo.setInfo_c(StringEscapeUtils.unescapeXml(content));

			//취소,환불규정에 대한 동의
			content = reserveInfo.getInfo_d();
			reserveInfo.setInfo_d(StringEscapeUtils.unescapeXml(content));
			
			model.addAttribute("reserveInfo", reserveInfo);
			
			
			
			
			
			
			// 키가 있는지 확인
			VerificationKeyVO keys = ticketingService.getKeys(paymentInfo.getProductGroup().getShop_code());

			if (keys == null || !StringUtils.hasText(keys.getIdentification_site_code())
					|| !StringUtils.hasText(keys.getIdentification_site_password())
					|| !StringUtils.hasText(keys.getPay_merchant_id())
					|| !StringUtils.hasText(keys.getPay_merchant_key())) {
				redirect.addFlashAttribute("msg", "인증를 위한 정보가 없습니다. 관리자에게 연락 바랍니다.");
				if (paymentInfo.getType() == 1) {
					return "redirect:/ticketing/process1/selectWork?contentMstCd="
							+ paymentInfo.getProductGroup().getContent_mst_cd();
				} else {
					return "redirect:/ticketing/process2/selectWork?contentMstCd="
							+ paymentInfo.getProductGroup().getContent_mst_cd();
				}
			}

			model.addAttribute("siteCode", keys.getIdentification_site_code());
			model.addAttribute("sitePassword", keys.getIdentification_site_password());

			
			return "/ticketing/mcycamping/insertReserver";
		}
		
		
		@GetMapping("/mcycamping/finish")
		public String process1FinishOfMcyCamping(@ModelAttribute("shopInfo") @Valid ShopInfo_noScheduleDTO shopInfo,
				@RequestParam("orderNo") String orderNo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

			
			// ============================== 소금산벨리 ( 리버스아이티 ) 에 예매 내역 전송 ============================= 
			//sogeumsanResApiCall(shopInfo.getContent_mst_cd(), orderNo, shopInfo.getUserId(), shopInfo.getUserName(), "slae");
			//=================================================================================================
			
			
			
			Map<String, Object> params = new HashMap<>();
			params.put("orderNo", orderNo);
			params.put("contentMstCd", shopInfo.getContent_mst_cd());
			FinishTradeVO_noSchedule trade = ticketingService.getFinishTrade_noSchedule(params);
			model.addAttribute("trade", trade);
			
			List<FinishTradeVO_noSchedule> products = ticketingService.selectProductsForGA(params);
			model.addAttribute("products", products);


			String shopCode = ticketingService.getShopCode(shopInfo.getContent_mst_cd());
			// 본인인증 정보 가져오기
			SelfAuthenticationDTO selfAuthentication = new SelfAuthenticationDTO();
			selfAuthentication.setShop_code(shopCode);
			selfAuthentication.setContent_mst_cd(shopInfo.getContent_mst_cd());
			selfAuthentication.setSuccess_url("/checkReservationSuccess");
			selfAuthentication.setFail_url("/checkReservationFail");
			selfAuthentication = reserverAuthenticationService.getSelfAuthenticationEncodedData(request, selfAuthentication);		
			if(StringUtils.hasText(selfAuthentication.getMessage())) {
				ScriptUtils.alertAndBackPage(response, "본인인증 모듈 에러: " + selfAuthentication.getMessage());
				return null;
			}
			model.addAttribute("selfAuthentication", selfAuthentication);
			
			ShopDetailVO shopDetail = ticketingService.getShopDetailByContentMstCd(shopInfo.getContent_mst_cd());
			model.addAttribute("shopDetail", shopDetail);

			return "/ticketing/mcycamping/finish";
		}
		
		
		@PostMapping("/mcycamping/checkTicketAjax")
		@ResponseBody
		public List<SaleDTO_noSchedule> checkTicketAjaxOfMcyCamping(@RequestBody SaleDTO_noSchedule sale, Model model) throws Exception {
			
			//List<SaleDTO> saleDTOList = ticketingService.getCheckTicket(sale);
			List<SaleDTO_noSchedule> saleDTOList = ticketingService.getCheckTicket_noSchedule(sale);
			
			return saleDTOList;
		}
		
		
		@GetMapping("/mcycamping/prevShowTicket")
		public String prevShowTicketListOfMcyCamping(SaleDTO_noSchedule sale, HttpServletRequest request, HttpServletResponse response, 
				Model model, RedirectAttributes rttr, @ModelAttribute("refundResult") String refundResult, @ModelAttribute("message") String message) throws Exception{
			//request.getSession().setAttribute("saleDTO", saleDTO);
			
			//List<SaleDTO> saleDTOList = ticketingService.getCheckTicket(sale);
			List<SaleDTO_noSchedule> saleDTOList = ticketingService.getCheckTicket_noSchedule(sale);
			
			
			if(saleDTOList.size() > 1) {
				if(StringUtils.hasText(refundResult)) {
					rttr.addAttribute("message", refundResult);
				} else {
					rttr.addAttribute("message", message);
				}
				return "redirect:/ticketing/mcycamping/showTicketInfoList?content_mst_cd=" + sale.getContent_mst_cd() + "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") + "&member_tel=" + sale.getMember_tel() + "&today=" + sale.getToday();
			} else if(saleDTOList.size() == 1) {
				if(StringUtils.hasText(refundResult)) {
					rttr.addAttribute("message", refundResult);
				} else {
					rttr.addAttribute("message", message);
				}
				return "redirect:/ticketing/mcycamping/showTicketInfo?content_mst_cd=" + sale.getContent_mst_cd() + "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") + "&member_tel=" + sale.getMember_tel() + "&today=" + sale.getToday() 
				+ "&sale_code=" + saleDTOList.get(0).getSale_code();
			} else {
				if(StringUtils.hasText(refundResult)) {
					message = "환불이 정상 처리 되었습니다. 더이상 예매 내역이 없습니다.";
				}
				
				ScriptUtils.alertAndMovePage(response, message, "/ticketing/mcycamping/selectTicket?content_mst_cd=" + sale.getContent_mst_cd());
				return null;
			}
		}
		
		
		
		@PostMapping("/mcycamping/cancelTicket")
		public String cancelTicketOfMcyCamping(@ModelAttribute("sale") SaleDTO_noSchedule sale,
				@RequestParam(name = "isMyPage", required = false) String isMyPage, HttpServletRequest request,
				HttpServletResponse response, RedirectAttributes rttr, Model model) throws Exception {
			String failRedirectPage = "redirect:/ticketing/sogeumsan/showTicketInfo?content_mst_cd=" + sale.getContent_mst_cd() + "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") 
				+ "&member_tel=" + sale.getMember_tel() + "&today=" + sale.getToday() + "&sale_code=" + sale.getSale_code();
			String successRedirectPage = "redirect:/ticketing/sogeumsan/prevShowTicket?content_mst_cd=" + sale.getContent_mst_cd() + "&today=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) 
				+ "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") + "&member_tel=" + sale.getMember_tel();;
			Date now = new Date();
			Date today = DateHelper.getDateStart(now);

			List<RefundVO> refunds = ticketingService.getRefund_noSchedule(sale);

			if (refunds == null || refunds.size() == 0) {
//					rttr.addFlashAttribute("sale", sale);
				rttr.addFlashAttribute("message", "환불 정보가 존재하지 않습니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return failRedirectPage;
			}

			for (RefundVO refund : refunds) {
				if (refund.getRefund_yn().equals("1")) {
//						rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message", "이미 환불처리된 티켓입니다.");
					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
					return failRedirectPage;
				}

				if (refund.getUsed_count() > 0) {
//						rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message", "이미 사용된 티켓입니다.");
					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
					return failRedirectPage;
				}

				if (refund.getProduct_group_kind().equals("1")) {
					if (today.after(refund.getExpire_date())) {
//							rttr.addFlashAttribute("sale", sale);
						rttr.addFlashAttribute("message", "사용기간이 지난 티켓은 환불이 불가능합니다.");
						request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
						return failRedirectPage;
					}
				} else if (refund.getProduct_group_kind().equals("2")) {
					if (now.after(refund.getPlay_datetime())) {
//							rttr.addFlashAttribute("sale", sale);
						rttr.addFlashAttribute("message", "사용기간이 지난 티켓은 환불이 불가능합니다.");
						request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
						return failRedirectPage;
					}

				} else {
//						rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message", "존재하지 않는 종류의 티켓타입입니다.");
					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
					return failRedirectPage;
				}
			}

			WebPaymentDTO webPayment = ticketingService.getWebPayment(sale.getOrder_num());
			WebPaymentPgResultDTO pgResult = ticketingService.getWebPaymentPgResult(sale.getOrder_num());

			WebPaymentStatusDTO cancelTicketStatus = WebPaymentStatusDTO.builder().status("고객-전체취소-시작").message("OrderNo: "
					+ sale.getOrder_num() + " | Name:" + pgResult.getName() + " | Phone: " + pgResult.getPhone())
					.orderNo(sale.getOrder_num()).build();
			ticketingService.addWebPaymentStatus(cancelTicketStatus);

			
			
			//ApiResultVO apiResult = callCancelApi(pgResult, sale.getContent_mst_cd(), "A");
			ApiResultVO apiResult = callCancelApi_noSchedule(pgResult, sale.getContent_mst_cd(), "A");
			
//				ApiResultVO apiResult = new ApiResultVO();
//				apiResult.setSuccess(1);

			if (apiResult.getSuccess() != 1) {
//					rttr.addFlashAttribute("sale", sale);
				rttr.addFlashAttribute("message", "예매취소에 실패하였습니다" + apiResult.getErrMsg());
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return failRedirectPage;
			}
			/*
			 ****************************************************************************************
			 * <취소요청 파라미터> 취소시 전달하는 파라미터입니다. 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며, 추가 가능한 옵션 파라미터는
			 * 연동메뉴얼을 참고하세요.
			 ****************************************************************************************
			 */
			String tid = pgResult.getTid(); // 거래 ID
			String cancelAmt = pgResult.getAmt(); // 취소금액
			String partialCancelCode = "0"; // 부분취소여부
			String mid = pgResult.getMid(); // 상점 ID
			String moid = pgResult.getMoid(); // 주문번호
			String cancelMsg = "고객요청"; // 취소사유

			VerificationKeyVO keys = ticketingService.getKeys(webPayment.getShop_code());

			/*
			 ****************************************************************************************
			 * <해쉬암호화> (수정하지 마세요) SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
			 ****************************************************************************************
			 */
			DataEncrypt sha256Enc = new DataEncrypt();
			String merchantKey = keys.getPay_merchant_key(); // 상점키
			String ediDate = getyyyyMMddHHmmss();
			String signData = sha256Enc.encrypt(mid + cancelAmt + ediDate + merchantKey);

			/*
			 ****************************************************************************************
			 * <취소 요청> 취소에 필요한 데이터 생성 후 server to server 통신을 통해 취소 처리 합니다. 취소 사유(CancelMsg)
			 * 와 같이 한글 텍스트가 필요한 파라미터는 euc-kr encoding 처리가 필요합니다.
			 ****************************************************************************************
			 */
			StringBuffer requestData = new StringBuffer();
			requestData.append("TID=").append(tid).append("&");
			requestData.append("MID=").append(mid).append("&");
			requestData.append("Moid=").append(moid).append("&");
			requestData.append("CancelAmt=").append(cancelAmt).append("&");
			requestData.append("CancelMsg=").append(URLEncoder.encode(cancelMsg, "euc-kr")).append("&");
			requestData.append("PartialCancelCode=").append(partialCancelCode).append("&");
			requestData.append("EdiDate=").append(ediDate).append("&");
			requestData.append("SignData=").append(signData);
			String resultJsonStr = connectToServer(requestData.toString(),"https://webapi.nicepay.co.kr/webapi/cancel_process.jsp", "euc-kr");
//				String resultJsonStr = "{\"ResultCode\":\"2001\",\"ResultMsg\":\"정상 처리 되었습니다.\",\"ErrorCD\":\"0000\",\"ErrorMsg\":\"성공\",\"CancelAmt\":\"000000000900\",\"MID\":\"nicepay00m\",\"Moid\":\"001021080900010\",\"PayMethod\":\"CELLPHONE\",\"TID\":\"nicepay00m05012108091827142647\",\"CancelDate\":\"20210809\",\"CancelTime\":\"185659\",\"CancelNum\":\"00000000\",\"RemainAmt\":\"000000000000\",\"Signature\":\"594e9139df3881a8a0ce5155d99ab908430682233f5385a204c722e56c7b4f0d\",\"MallReserved\":\"\"}";
			/*
			 ****************************************************************************************
			 * <취소 결과 파라미터 정의> 샘플페이지에서는 취소 결과 파라미터 중 일부만 예시되어 있으며, 추가적으로 사용하실 파라미터는 연동메뉴얼을
			 * 참고하세요.
			 ****************************************************************************************
			 */
			String ResultCode = "";
			String ResultMsg = "";
			String CancelAmt = "";
			String CancelDate = "";
			String CancelTime = "";
			String TID = "";

			if ("9999".equals(resultJsonStr)) {
				ResultCode = "9999";
				ResultMsg = "통신실패";

				// 결제 취소 취소 기록
				WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder().status("고객-전체취소-PG취소-9999통신실패")
						.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg + " | Request Data: " + requestData.toString())
						.orderNo(pgResult.getMoid())
						.build();
				ticketingService.addWebPaymentStatus(cancelErrorStatus);

				CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());

//					rttr.addFlashAttribute("sale", sale);
				rttr.addFlashAttribute("message", "예약취소를 위한 통신에 장애가 발생하였습니다.[" + ResultCode + "-" + ResultMsg
						+ "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + company.getComp_tel() + ")에게 연락 부탁드립니다.");
//					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return failRedirectPage;
			} else {



				HashMap resultData = jsonStringToHashMap(resultJsonStr);
				ResultCode = (String) resultData.get("ResultCode"); // 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)
				ResultMsg = (String) resultData.get("ResultMsg"); // 결과메시지
				CancelAmt = (String) resultData.get("CancelAmt"); // 취소금액
				CancelDate = (String) resultData.get("CancelDate"); // 취소일
				CancelTime = (String) resultData.get("CancelTime"); // 취소시간
				TID = (String) resultData.get("TID"); // 거래아이디 TID
				
				// 결제 취소 취소 기록
				WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
						.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-PG취소-성공" : "고객-PG취소-실패")
						.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg + " | Request Data: " + requestData.toString())
						.orderNo(pgResult.getMoid())
						.build();
				ticketingService.addWebPaymentStatus(cancelErrorStatus);
				/*
				 ****************************************************************************************
				 * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한
				 * 요소를 방지하기 위해 연동 시 사용하시기 바라며 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
				 ****************************************************************************************
				 */
				String Signature = "";
				String paySignature = "";

				Signature = (String) resultData.get("Signature");
				paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

				if (!Signature.equals(paySignature)) {
					// 결제 취소 결과 변조 확인
					WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder().status("고객-전체취소-PG취소-변조확인")
							.message("[Received]" + Signature + "|[ShouldBe]" + paySignature)
							.orderNo(moid).build();
					ticketingService.addWebPaymentStatus(approvalResultStatus);

					CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());

//						rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message",
							"예약취소 데이터의 변조를 확인하였습니다. 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + company.getComp_tel() + ")에게 연락 부탁드립니다.");
					//request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
									
					return successRedirectPage;
					
				} else {
					if (ResultCode.equals("2001") || ResultCode.equals("2211")) {

						SaleVO_noSchedule searchSale = new SaleVO_noSchedule();
						searchSale.setShop_code(webPayment.getShop_code());
						searchSale.setOrder_num(webPayment.getOrder_no());

						//SaleVO_noSchedule saleVO = ticketingService.getSaleSsByOrderNum(searchSale);
						SaleVO_noSchedule saleVO = ticketingService.getSaleSsByOrderNum_noSchedule(searchSale);
						
						SaleDTO_noSchedule paymentSaleSearchSale = new SaleDTO_noSchedule();
						paymentSaleSearchSale.setSaleProductSearchType("SS");
						paymentSaleSearchSale.setSale_code(saleVO.getSale_code());
						paymentSaleSearchSale.setContent_mst_cd(webPayment.getContent_mst_cd());
						paymentSaleSearchSale.setMember_name(saleVO.getMember_name());
						paymentSaleSearchSale.setMember_tel(saleVO.getMember_tel());
						List<SaleProductDTO_noSchedule> saleProducts = ticketingService.getSaleProduct(paymentSaleSearchSale);
						saleVO.setSaleProducts(saleProducts);
						
						//bc_paymentsale에 마이너스금액으로 기록
						ShopPaymentsaleVO paymentsale = new ShopPaymentsaleVO();
						paymentsale.setShop_code(saleVO.getShop_code());
						paymentsale.setSale_code(saleVO.getSale_code());
						paymentsale.setPayment_no(saleProducts.get(0).getSale_sequence());
						List<ShopPaymentsaleVO> list = ticketingService.selectPaymentSale(paymentsale);
						for(int i=0; i<list.size(); i++) {
							if(list.get(i).getPayment_idx().equals("1")) {
								paymentsale = list.get(i);
								int idx = Integer.parseInt(list.get((list.size()-1)).getPayment_idx())+1;
								paymentsale.setPayment_idx(String.valueOf(idx));
							}
						}
						BigDecimal refundFee =  new BigDecimal(paymentsale.getPayment_fee().intValue());
						BigDecimal minus = new BigDecimal(-1);
						paymentsale.setPayment_fee(refundFee.multiply(minus));
						ticketingService.insertPaymentSale(paymentsale);
						
						//bc_refund_history 기록
						RefundHistoryVO historyVO = new RefundHistoryVO();
						historyVO.setShop_code(saleVO.getShop_code());
						historyVO.setSale_code(saleVO.getSale_code());
						historyVO.setCount(saleProducts.size());
						historyVO.setFee(refundFee.toString());
						String book_nos = saleProducts.stream().map(SaleProductDTO_noSchedule::getBook_no).collect(Collectors.joining(","));
						historyVO.setBook_no(book_nos);
						historyVO.setWork_id("WEBRESERVE");
						ticketingService.insertRefundHistory(historyVO);
						
						ShopDetailVO shopDetail = ticketingService.getShopDetail(webPayment.getShop_code());

						//messageService.sendRefund(request, saleVO, webPayment, pgResult, shopDetail);
						messageService.sendRefund_noSchedule(request, saleVO, webPayment, pgResult, shopDetail);
						
						rttr.addFlashAttribute("refundResult", "예매가 정상 취소 되었습니다.");
						
						return successRedirectPage;
					} else {

						CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());

//							rttr.addFlashAttribute("sale", sale);
						rttr.addFlashAttribute("message", "예약취소에 장애가 발생하였습니다.[" + ResultCode + "-" + ResultMsg
								+ "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + company.getComp_tel() + ")에게 연락 부탁드립니다.");

//							request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
						return successRedirectPage;
					}
				}
			}
		}
		
		
		@PostMapping("/mcycamping/payRequest")
		public String payRequestNoScheduleOfMcyCamping(@ModelAttribute("paymentInfo") @Valid PaymentInfoDTO_noSchedule info, HttpServletResponse response,
				Errors errors, Model model) throws Exception {
			
			if (errors.hasErrors()) {
				ScriptUtils.alert(response, errors.getAllErrors().get(0).getDefaultMessage());
				return null;
			}

			// 상품 그룹 확인
			//ProductGroupDTO dbProductGroup = ticketingService.getProductGroup(info.getProductGroup());
			ProductGroupDTO_noSchedule dbProductGroup = ticketingService.getProductGroup_noSchedule(info.getProductGroup());
			
			
			if (!checkProductGroup_noSchedule(info.getProductGroup(), dbProductGroup)) {

				logProductGroup(info.getProductGroup(), dbProductGroup);
				
				ScriptUtils.alertAndClose(response, "상품그룹정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}
			dbProductGroup.setType(info.getProductGroup().getType());
			info.setProductGroup(dbProductGroup);

			// 해당하는 그룹에 해당하는 상품이 정상적인지 확인

			List<ProductDTO> dbProducts = ticketingService.getSelectedProducts(info.getProducts());
//				if (!checkProducts(info, dbProducts)) {
//					ScriptUtils.alertAndClose(response, "상품정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
//					return null;
//				}
			for (int i = 0; i < info.getProducts().size(); i++) {
				ProductDTO product = info.getProducts().get(i);
				for (int j = 0; j < dbProducts.size(); j++) {
					ProductDTO dbProduct = dbProducts.get(j);
					if (product.getShop_code().equals(dbProduct.getShop_code())
							&& product.getProduct_group_code().equals(dbProduct.getProduct_group_code())
							&& product.getProduct_code().equals(dbProduct.getProduct_code())) {
						dbProduct.setCount(product.getCount());
					}
				}
			}
			info.setProducts(dbProducts);

			int dbTotalCount = dbProducts.stream().mapToInt(ProductDTO::getCount).sum();
			if (dbTotalCount != info.getTotalCount()) {
				ScriptUtils.alertAndClose(response, "판매매수가 맞지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}

			BigDecimal dbTotalFee = dbProducts.stream()
					.map(p -> p.getProduct_fee().multiply(BigDecimal.valueOf(p.getCount())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			if (!dbTotalFee.equals(info.getTotalFee())) {
				ScriptUtils.alertAndClose(response, "판매금액정보가 맞지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}

			if (StringUtils.hasText(info.getSchedule_code())) {
				ScheduleDTO schedule = new ScheduleDTO();
				schedule.setShop_code(info.getProductGroup().getShop_code());
				schedule.setSchedule_code(info.getSchedule_code());
				schedule = ticketingService.getScheduleByScheduleCode(schedule);

				if (schedule == null) {
					ScriptUtils.alertAndClose(response, "해당 상품의 회차정보가 존재하지 않습니다. 결제를 진행할 수 없습니다.");
					return null;
				}
				info.setSchedule(schedule);
			}

			info.setTotalCount(dbTotalCount);
			info.setTotalFee(dbTotalFee);

			// 판매 금액이 0원이면  결제수단을 0원결제로 변경
			if("0".equals(info.getTotalFee()) ) info.setPayMethod("0000");
			
			// 유효기간 가져오기
			TicketValidVO ticketValid = new TicketValidVO();
			ticketValid.setShop_code(info.getProductGroup().getShop_code());
			ticketValid = ticketingService.selectTicketValid(ticketValid);
			if(ticketValid == null || !StringUtils.hasText(ticketValid.getValid_period())) { 
				ScriptUtils.alertAndBackPage(response, "티켓의 유효기간이 정의되지 않았습니다.");
				return null;
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			Date date2 = format.parse("2022-02-28");
			
			// 유효기간 설정 / 2022-02-25 / 조미근
			Calendar calTo = Calendar.getInstance(); 
			calTo.getActualMaximum(Calendar.DAY_OF_MONTH);
			String temp = String.valueOf(calTo.getActualMaximum(Calendar.DAY_OF_MONTH)-LocalDateTime.now().getDayOfMonth());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(now);
			if(now.compareTo(date2) > 0) { //2월 말 기준 이후
				info.setValid_period(ticketValid.getValid_period());
			}else { //2월 말 기준 이전
				info.setValid_period(temp);
			}
			
			//WebPaymentDTO webPayment = ticketingService.addWebPaymentInfo(info);
			WebPaymentDTO webPayment = ticketingService.addWebPaymentInfo_noSchedule(info);

			//System.out.println(info);
			model.addAttribute("webPayment", webPayment);

			// 이용약관 가져오기
			WebReservationKeyDTO reserveInfo = ticketingService.selectReserveInfo(dbProductGroup.getShop_code());
			String content = reserveInfo.getInfo_d();
			reserveInfo.setInfo_d(StringEscapeUtils.unescapeXml(content));
			model.addAttribute("reserveInfo", reserveInfo);
			
			
			
			return "/ticketing/mcycamping/payRequest";
		}
		
		
		
		@PostMapping("/mcycamping/payResult")
		public String payResult_noScheduleOfMcyCamping(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("shopInfo") @Valid ShopInfo_noScheduleDTO loginUserInfo) throws Exception {

			int resultSuccess = 0;
			String resultOrderNum = "";
			String resultMessage = "";

			
			/*
			 ****************************************************************************************
			 * <인증 결과 파라미터>
			 ****************************************************************************************
			 */
			String authResultCode = (String) request.getParameter("AuthResultCode"); // 인증결과 : 0000(성공)
			String authResultMsg = (String) request.getParameter("AuthResultMsg"); // 인증결과 메시지
			String nextAppURL = (String) request.getParameter("NextAppURL"); // 승인 요청 URL
			String txTid = (String) request.getParameter("TxTid"); // 거래 ID
			String authToken = (String) request.getParameter("AuthToken"); // 인증 TOKEN
			String payMethod = (String) request.getParameter("PayMethod"); // 결제수단
			String mid = (String) request.getParameter("MID"); // 상점 아이디
			String moid = (String) request.getParameter("Moid"); // 상점 주문번호
			String amt = (String) request.getParameter("Amt"); // 결제 금액
			String reqReserved = (String) request.getParameter("ReqReserved"); // 상점 예약필드
			String netCancelURL = (String) request.getParameter("NetCancelURL"); // 망취소 요청 URL

			String authSignature = (String) request.getParameter("Signature"); // Nicepay에서 내려준 응답값의 무결성 검증 Data

			WebPaymentDTO webPayment = ticketingService.getWebPayment(moid);
			VerificationKeyVO keys = ticketingService.getKeys(webPayment.getShop_code());

			/*
			 ****************************************************************************************
			 * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한
			 * 요소를 방지하기 위해 연동 시 사용하시기 바라며 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
			 ****************************************************************************************
			 */
			DataEncrypt sha256Enc = new DataEncrypt();
			String merchantKey = keys.getPay_merchant_key(); // 상점키

			// 인증 응답 Signature = hex(sha256(AuthToken + MID + Amt + MerchantKey)
			String authComparisonSignature = sha256Enc.encrypt(authToken + mid + amt + merchantKey);

			/*
			 ****************************************************************************************
			 * <승인 결과 파라미터 정의> 샘플페이지에서는 승인 결과 파라미터 중 일부만 예시되어 있으며, 추가적으로 사용하실 파라미터는 연동메뉴얼을
			 * 참고하세요.
			 ****************************************************************************************
			 */
			String ResultCode = "";
			String ResultMsg = "";
			String PayMethod = "";
			String GoodsName = "";
			String Amt = "";
			String TID = "";
			String Signature = "";
			String paySignature = "";
			String AuthCode = "";
			String AuthDate = "";

			/*
			 ****************************************************************************************
			 * <인증 결과 성공시 승인 진행>
			 ****************************************************************************************
			 */
			String resultJsonStr = "";
			if (authResultCode.equals("0000") && authSignature.equals(authComparisonSignature)) {

				// 결제 인증 완료 기록
				WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제인증성공")
						.message("AuthResultCode: " + authResultCode).orderNo(moid).build();
				ticketingService.addWebPaymentStatus(authenticationFinishedStatus);

				/*
				 ****************************************************************************************
				 * <해쉬암호화> (수정하지 마세요) SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
				 ****************************************************************************************
				 */
				String ediDate = getyyyyMMddHHmmss();
				String signData = sha256Enc.encrypt(authToken + mid + amt + ediDate + merchantKey);

				/*
				 ****************************************************************************************
				 * <승인 요청> 승인에 필요한 데이터 생성 후 server to server 통신을 통해 승인 처리 합니다.
				 ****************************************************************************************
				 */
				StringBuffer requestData = new StringBuffer();
				requestData.append("TID=").append(txTid).append("&");
				requestData.append("AuthToken=").append(authToken).append("&");
				requestData.append("MID=").append(mid).append("&");
				requestData.append("Amt=").append(amt).append("&");
				requestData.append("EdiDate=").append(ediDate).append("&");
				requestData.append("CharSet=").append("utf-8").append("&");
				requestData.append("SignData=").append(signData);

				// 결제 승인 요청 기록
				WebPaymentStatusDTO approvalRequestStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인요청").message("")
						.orderNo(moid).build();
				ticketingService.addWebPaymentStatus(approvalRequestStatus);

				resultJsonStr = connectToServer(requestData.toString(), nextAppURL, "utf-8");

				HashMap resultData = new HashMap();
				boolean paySuccess = false;
				if ("9999".equals(resultJsonStr)) {
					// 결제승인에러 기록
					WebPaymentStatusDTO approvalErrorStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인통신에러")
							.message(resultJsonStr).orderNo(moid).build();
					ticketingService.addWebPaymentStatus(approvalErrorStatus);

					/*
					 *************************************************************************************
					 * <망취소 요청> 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
					 *************************************************************************************
					 */
					StringBuffer netCancelData = new StringBuffer();
					requestData.append("&").append("NetCancel=").append("1");
					String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");

					HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
					ResultCode = (String) cancelResultData.get("ResultCode");
					ResultMsg = (String) cancelResultData.get("ResultMsg");
					Signature = (String) cancelResultData.get("Signature");
					String CancelAmt = (String) cancelResultData.get("CancelAmt");
					paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

					if (ResultCode == null) {
						// 결제 망취소 취소 기록
						WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder().status("고객-예매-망취소-통신불가")
								.message("").orderNo(moid).build();
						ticketingService.addWebPaymentStatus(cancelErrorStatus);

					} else {
						// 결제 망취소 취소 기록
						WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
								.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-망취소-성공"
										: "고객-예매-망취소-실패")
								.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg).orderNo(moid).build();
						ticketingService.addWebPaymentStatus(cancelErrorStatus);

					}
					resultSuccess = 0;
					resultMessage = "결제 승인 통신에 실패하였습니다.";

				} else {

					resultData = jsonStringToHashMap(resultJsonStr);
					ResultCode = (String) resultData.get("ResultCode"); // 결과코드 (정상 결과코드:3001)
					ResultMsg = (String) resultData.get("ResultMsg"); // 결과메시지
					PayMethod = (String) resultData.get("PayMethod"); // 결제수단
					GoodsName = (String) resultData.get("GoodsName"); // 상품명
					Amt = (String) resultData.get("Amt"); // 결제 금액
					TID = (String) resultData.get("TID"); // 거래번호
					// Signature : Nicepay에서 내려준 응답값의 무결성 검증 Data
					// 가맹점에서 무결성을 검증하는 로직을 구현하여야 합니다.
					Signature = (String) resultData.get("Signature");
					paySignature = sha256Enc.encrypt(TID + mid + Amt + merchantKey);

					if (!Signature.equals(paySignature)) {
						// 결제 승인 결과 기록
						WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인-완료-변조확인")
								.message(PayMethod + ": [Received]" + Signature + "|[ShouldBe]" + paySignature)
								.orderNo(moid).build();
						ticketingService.addWebPaymentStatus(approvalResultStatus);

						/*
						 *************************************************************************************
						 * <무결성 체크 실패 취소 요청> 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
						 *************************************************************************************
						 */
						StringBuffer netCancelData = new StringBuffer();
						requestData.append("&").append("NetCancel=").append("1");
						String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");

						HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
						ResultCode = (String) cancelResultData.get("ResultCode");
						ResultMsg = (String) cancelResultData.get("ResultMsg");
						Signature = (String) cancelResultData.get("Signature");
						String CancelAmt = (String) cancelResultData.get("CancelAmt");
						paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

						if (ResultCode == null) {
							// 결제 무결성체크실패 취소 통신불가 기록
							WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
									.status("고객-예매-변조취소-통신불가").message("").orderNo(moid).build();
							ticketingService.addWebPaymentStatus(cancelErrorStatus);

						} else {
							// 결제 무결성체크실패 취소 기록
							WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
									.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-변조취소-망취소성공"
											: "고객-예매-변조취소-망취소실패")
									.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg).orderNo(moid)
									.build();
							ticketingService.addWebPaymentStatus(cancelErrorStatus);
						}

						resultSuccess = 0;
						resultMessage = "결제 무결성 검증에 실패하였습니다.\n[" + ResultCode + "]" + ResultMsg;
					} else {
						// 결제 승인 결과 기록
						WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder().status("고객-예매-승인-성공")
								.message(PayMethod + ": " + ResultCode).orderNo(moid).build();
						ticketingService.addWebPaymentStatus(approvalResultStatus);

						// API 호출

						// PG사 결제 결과 정보 저장
						WebPaymentPgResultDTO pgResult = new WebPaymentPgResultDTO();

						pgResult.setAuth_result_code(authResultCode);
						pgResult.setAuth_result_msg(authResultMsg);
						pgResult.setNext_ap_url(nextAppURL);
						pgResult.setTransaction_id(txTid);
						pgResult.setAuth_token(authToken);
						pgResult.setPay_method(payMethod);
						pgResult.setMid(mid);
						pgResult.setMoid(moid);
						pgResult.setAmt(amt);
						pgResult.setNet_cancel_url(netCancelURL);
						pgResult.setAuth_signature_comparison(authSignature.equals(authComparisonSignature) ? "1" : "0");
						pgResult.setApproval_result_code(ResultCode);
						pgResult.setApproval_result_msg(ResultMsg);
						pgResult.setTid(TID);

						// 카드일 경우
						if (payMethod.equals("CARD")) {

							// 카드
							String cardNo = (String) resultData.get("CardNo"); // 카드번호
							String cardQuota = (String) resultData.get("CardQuota"); // 할부개월
							String cardCode = (String) resultData.get("CardCode"); // 결제 카드사 코드
							String cardName = (String) resultData.get("CardName"); // 결제 카드사명
							String acquCardCode = (String) resultData.get("AcquCardCode"); // 매입 카드사 코드
							String acquCardName = (String) resultData.get("AcquCardName"); // 매입 카드사 이름
							String rcptType = (String) resultData.get("RcptType"); // 매입 카드사 이름

							pgResult.setCardNo(!StringUtils.hasText(cardNo) ? "" : cardNo);
							pgResult.setCardQuota(!StringUtils.hasText(cardQuota) ? "" : cardQuota);
							pgResult.setCardCode(!StringUtils.hasText(cardCode) ? "" : cardCode);
							pgResult.setAcquCardCode(!StringUtils.hasText(acquCardCode) ? "" : acquCardCode);
							pgResult.setAcquCardName(!StringUtils.hasText(acquCardName) ? "" : acquCardName);
							pgResult.setRcptType(!StringUtils.hasText(rcptType) ? "" : rcptType);
						} else if (payMethod.equals("BANK")) {
							String bankCode = (String) resultData.get("BankCode"); // 결제은행코드
							String bankName = (String) resultData.get("BankName"); // 결제은행명
							String rcptType = (String) resultData.get("RcptType"); // 매입 카드사 이름
							pgResult.setCardCode(!StringUtils.hasText(bankCode) ? "" : bankCode);
							pgResult.setCardName(!StringUtils.hasText(bankName) ? "" : bankName);
							pgResult.setRcptType(!StringUtils.hasText(rcptType) ? "" : rcptType);
						}
						AuthCode = (String) resultData.get("AuthCode");
						; // 승인번호
						AuthDate = (String) resultData.get("AuthDate");
						; // 승인날짜
						pgResult.setAuth_code(AuthCode);
						pgResult.setAuth_date(AuthDate);

						ticketingService.addWebPaymentPgResult(pgResult);

						/*
						 *************************************************************************************
						 * <결제 성공 여부 확인>
						 *************************************************************************************
						 */
						if (PayMethod != null) {
							if (PayMethod.equals("CARD")) {
								if (ResultCode.equals("3001"))
									paySuccess = true; // 신용카드(정상 결과코드:3001)
							} else if (PayMethod.equals("BANK")) {
								if (ResultCode.equals("4000"))
									paySuccess = true; // 계좌이체(정상 결과코드:4000)
							} else if (PayMethod.equals("CELLPHONE")) {
								if (ResultCode.equals("A000"))
									paySuccess = true; // 휴대폰(정상 결과코드:A000)
							} else if (PayMethod.equals("VBANK")) {
								if (ResultCode.equals("4100"))
									paySuccess = true; // 가상계좌(정상 결과코드:4100)
							} else if (PayMethod.equals("SSG_BANK")) {
								if (ResultCode.equals("0000"))
									paySuccess = true; // SSG은행계좌(정상 결과코드:0000)
							} else if (PayMethod.equals("CMS_BANK")) {
								if (ResultCode.equals("0000"))
									paySuccess = true; // 계좌간편결제(정상 결과코드:0000)
							}
						}

						if (paySuccess) { // 결제 승인 성공

							//ApiResultVO apiResultVO = ticketingService.callTicketApi(pgResult);
							ApiResultVO apiResultVO = ticketingService.callTicketApi_noSchedule(pgResult);

							// API 호출 성공
							if (apiResultVO.getSuccess() == 1) {

								WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder().status("고객-예매-Api호출-성공")
										.message("ApiCallResult: " + apiResultVO.toString()).orderNo(moid).build();
								ticketingService.addWebPaymentStatus(apiCallStatus);

								ShopDetailVO shopDetail = ticketingService
										.getShopDetail(apiResultVO.getWebPayment().getShop_code());

								try {
									messageService.send_noSchedule(request, response, apiResultVO, pgResult, shopDetail);
									
									
								} catch (Exception ex) {
									ex.printStackTrace();
								}

								if (StringUtils.hasText(apiResultVO.getWebPayment().getReserverEmail())) {

									try {
										mailService.sendReserve(request, apiResultVO, pgResult, shopDetail);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
								resultSuccess = 1;
								resultOrderNum = moid;
								resultMessage = "결제에 성공하였습니다.";
							} else { // API호출 실패

								WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder().status("고객-예매-Api호출-실패")
										.message("ApiCallResult: " + apiResultVO.toString()).orderNo(moid).build();
								ticketingService.addWebPaymentStatus(apiCallStatus);

								// TODO: 현재는 전체취소로 되어 있음. 추후 일자로 부분취소 추가시("E")인지 계산
								callCancelApi_noSchedule(pgResult, apiResultVO.getWebPayment().getContent_mst_cd(), "A");

								/*
								 *************************************************************************************
								 * <망취소 요청> API호출 실패시 망취소 처리
								 *************************************************************************************
								 */
								StringBuffer netCancelData = new StringBuffer();
								requestData.append("&").append("NetCancel=").append("1");
								String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");

								HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
								ResultCode = (String) cancelResultData.get("ResultCode");
								ResultMsg = (String) cancelResultData.get("ResultMsg");
								Signature = (String) cancelResultData.get("Signature");
								String CancelAmt = (String) cancelResultData.get("CancelAmt");
								paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

								// 결제 망취소 취소 기록
								WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
										.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-Api호출-실패-망취소-성공"
												: "고객-예매-Api호출-실패-망취소-실패")
										.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg).orderNo(moid)
										.build();
								ticketingService.addWebPaymentStatus(cancelErrorStatus);

								resultSuccess = 0;
								resultMessage = "결제 API 호출에 실패하였습니다." + apiResultVO.getErrMsg();
							}
						} else {
							// 결제승인에러 기록
							WebPaymentStatusDTO approvalFailStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인-에러")
									.message("ResultCode: " + ResultCode + " | Message:" + resultJsonStr).orderNo(moid).build();
							ticketingService.addWebPaymentStatus(approvalFailStatus);

							resultSuccess = 0;
							resultMessage = "결제에 실패 하였습니다.\n" + ResultMsg;
						}
					}
				}
			} else if (authSignature.equals(authComparisonSignature)) {
				ResultCode = authResultCode;
				ResultMsg = authResultMsg;

				// 결제인증실패 기록
				WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제인증실패-변조확인")
						.message("AuthResultCode: " + authResultCode + " | AuthResultMsg" + authResultMsg).orderNo(moid)
						.build();
				ticketingService.addWebPaymentStatus(authenticationFinishedStatus);

				resultSuccess = 0;
				resultMessage = "결제인증에 실패하였습니다.\n[" + ResultCode + "]" + ResultMsg;
			} else {
				// 결제인증실패 기록
				WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제인증실패")
						.message("AuthResultCode: " + authResultCode + " | AuthComapreSignature" + authComparisonSignature)
						.orderNo(moid).build();
				ticketingService.addWebPaymentStatus(authenticationFinishedStatus);

				resultSuccess = 0;
				resultMessage = "결제인증에 실패하였습니다.";
			}

			resultOrderNum = removeQuatations(resultOrderNum);
			resultMessage = removeQuatations(resultMessage);

			model.addAttribute("success", resultSuccess);
			model.addAttribute("orderNo", resultOrderNum);
			model.addAttribute("message", resultMessage);

			return "/ticketing/mcycamping/payResult";
		}
		
		
		
		//=============================================== 소금산 벨리 회차 없는 예매 ==================================================================
		
		
		@GetMapping("/sogeumsan/schedule")
		public String sogeumSanSchedule(@ModelAttribute("essential") @Valid EssentialDTO essential, Errors errors, HttpServletResponse response, Model model) throws Exception {
			
			log.info("::: Ticketing SogeumSan Velly SelectSchedule START");
			
			if(errors.hasErrors()) {
				validationLog(errors);
				
				ScriptUtils.alertAndBackPage(response, "상품그룹정보가 올바르지 않습니다. 결제페이지에 진입 할 수 없습니다.");
				return null;
				
			}
			
			//==============================소금산 벨리 예매버튼 클릭시 원주 소금산 벨리 로그인 접속자 정보 받기 ==================================
			System.out.println("로그인 USER ID 	==> " + essential.getUserId());
			System.out.println("로그인 USER NAME 	==> " + essential.getUserName());
			//=====================================================================================================================
			
			
			// content_mst_cd, product_group_code 기준으로 기본 정보 가져오기
			ProductGroupDTO productGroup = ticketingService.getProductGroups(essential);
			//bc_product 에서 fee & web_yn & schedule_yn 값 가져와서 뿌리기
			
			model.addAttribute("productGroup", productGroup);
			
			//List<ProductDTO> products = ticketingService.getProducts(productGroup);
			//List<ProductDTO> products = ticketingService.selectProductsForGanghwa(productGroup);
			List<ProductDTO> products = ticketingService.selectProductsForSogeumsan(productGroup);
			model.addAttribute("products", products);
			
			ScheduleDTO scheduleDTO = new ScheduleDTO();
			scheduleDTO.setContentMstCd(essential.getContent_mst_cd());
			scheduleDTO.setProduct_group_code(essential.getProduct_group_code());
			scheduleDTO.setShop_code(productGroup.getShop_code());
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			scheduleDTO.setPlay_date(dateFormat.format(cal.getTime()));
			
			List<ScheduleDTO> scheduleDTOList = ticketingService.getSchedule(scheduleDTO);
			model.addAttribute("scheduleDTOList", scheduleDTOList);
			
			return "/ticketing/sogeumsan/selectSchedule";
			
		}
		
		
		@GetMapping("/sogeumsan/selectTicket")
		public String process1SelectTicket(@ModelAttribute("shopInfo") @Valid ShopInfo_noScheduleDTO shopInfo,
				Errors errors, HttpServletResponse response, RedirectAttributes redirectAttribute, Model model, HttpServletRequest request)
				throws Exception {

			//==============================소금산 벨리 예매버튼 클릭시 원주 소금산 벨리 로그인 접속자 정보 받기 ==================================
			System.out.println("로그인 USER ID 	==> " + shopInfo.getUserId());
			System.out.println("로그인 USER NAME 	==> " + shopInfo.getUserName());
			//=====================================================================================================================
			
			if (errors.hasErrors()) {
				validationLog(errors);

				ScriptUtils.alertAndBackPage(response, "시설정보가 존재하지 않습니다");
				return null;
			}

			String shopCode = ticketingService.getShopCode(shopInfo.getContent_mst_cd());
			if(!StringUtils.hasText(shopCode)) {
				ScriptUtils.alertAndBackPage(response, "시설정보가 존재하지 않습니다");
				return null;
			}
			shopInfo.setShop_code(shopCode);
			
			// 본인인증 정보 가져오기
			SelfAuthenticationDTO selfAuthentication = new SelfAuthenticationDTO();
			selfAuthentication.setShop_code(shopCode);
			selfAuthentication.setContent_mst_cd(shopInfo.getContent_mst_cd());
			selfAuthentication.setSuccess_url("/checkReservationSuccess");
			selfAuthentication.setFail_url("/checkReservationFail");
			selfAuthentication = reserverAuthenticationService.getSelfAuthenticationEncodedData(request, selfAuthentication);		
			if(StringUtils.hasText(selfAuthentication.getMessage())) {
				ScriptUtils.alertAndBackPage(response, "본인인증 모듈 에러: " + selfAuthentication.getMessage());
				return null;
			}
			model.addAttribute("selfAuthentication", selfAuthentication);
			
			// 상품그룹 가져오기
			List<ProductGroupDTO> productGroups = ticketingService.getProductGroups_noSchedule(shopInfo.getContent_mst_cd());
			model.addAttribute("productGroups", productGroups);
			
			//=======로그인 정보 ( 소금산 밸리용 )======================
			model.addAttribute("loginUserId", shopInfo.getUserId());
			model.addAttribute("loginUserNm", shopInfo.getUserName());
			
			
			// =======소금산 그랜드밸리 온라인 예매 수량 600개 하드코딩, 향후 삭제 소스=======
			List<ProductGroupDTO> resStatusCount = ticketingService.selectResStatusCount(shopInfo.getContent_mst_cd());
			
			if(resStatusCount != null)
			{
				if(resStatusCount.size() > 0 )
				{
					int saledQuantity 		= resStatusCount.get(0).getSALE_QUANTITY();
					int availableQuantity 	= resStatusCount.get(0).getAVAILABLE_QUANTITY();
					
					model.addAttribute("saledQuantity", saledQuantity);
					model.addAttribute("availableQuantity", availableQuantity);
				}
				else
				{
					model.addAttribute("saledQuantity", 0);
					model.addAttribute("availableQuantity", 600);
				}
			}
			else
			{
				model.addAttribute("saledQuantity", 0);
				model.addAttribute("availableQuantity", 600);
			}
			
			/*
			if(resStatusCount.size() > 0 )
			{
				int saledQuantity 		= resStatusCount.get(0).getSALE_QUANTITY();
				int availableQuantity 	= resStatusCount.get(0).getAVAILABLE_QUANTITY();
				
				model.addAttribute("saledQuantity", saledQuantity);
				model.addAttribute("availableQuantity", availableQuantity);
			}
			else
			{
				model.addAttribute("saledQuantity", 0);
				model.addAttribute("availableQuantity", 600);
			}
			*/
			// ====================================================
			
			
			return "/ticketing/sogeumsan/selectTicket";
		}
		
		
		@PostMapping("/sogeumsan/getProducts")
		@ResponseBody
		public Object getProducts(@RequestBody @Valid ProductGroupDTO_noSchedule productGroup, Errors errors) throws Exception {
			
			if (errors.hasErrors()) {
				validationLog(errors);
				
				String errorCode = CommUtil.getErrorCode(ErrorSiteCode.HOMETICKET.getValue(), ErrorPathCode.SELECTTICKET.getValue(), ErrorTypeCode.VALIDATION.getValue() );
				
				return new BaseDTO_noSchedule(errorCode, errors.getAllErrors().get(0).getDefaultMessage());
			}
			
			ProductGroupDTO_noSchedule dbProductGroup = ticketingService.getProductGroup_noSchedule(productGroup);
			if (dbProductGroup == null || !checkProductGroup_noSchedule(productGroup, dbProductGroup)) {

				String errorCode = CommUtil.getErrorCode(ErrorSiteCode.HOMETICKET.getValue(), ErrorPathCode.SELECTTICKET.getValue(), ErrorTypeCode.PRODUCTGROUPMISMATCH.getValue() );
				
				return new BaseDTO_noSchedule(errorCode, "상품그룹이 존재하지 않습니다.");
			}

			List<ProductDTO> products = ticketingService.getProducts_NoSchedule(productGroup);
					
			return products;
		}
		
		
		
		@GetMapping("/sogeumsan/insertReserver")
		public String process1InsertReserver(@ModelAttribute("paymentInfo") PaymentInfoDTO_noSchedule paymentInfo, Errors errors,
				HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirect, Model model)
				throws Exception {

			// content_mst_cd
			// product_group_code
			// products.product_code, products.count

			paymentInfo.setType(propertyService.getInt("type"));
			// shopCode 조회
			String shopCode = ticketingService.getShopCode(paymentInfo.getContent_mst_cd());
			paymentInfo.getProductGroup().setShop_code(shopCode);
			
			// 상품 그룹 확인
			//ProductGroupDTO dbProductGroup = ticketingService.getProductGroup(paymentInfo.getProductGroup());
			ProductGroupDTO_noSchedule dbProductGroup = ticketingService.getProductGroup_noSchedule(paymentInfo.getProductGroup());
			
			
			
			dbProductGroup.setType(propertyService.getInt("type"));
			paymentInfo.setProductGroup(dbProductGroup);

			// 해당하는 그룹에 해당하는 상품이 정상적인지 확인
			for(ProductDTO product : paymentInfo.getProducts()) {
				product.setShop_code(shopCode);
				product.setProduct_group_code(paymentInfo.getProductGroup().getProduct_group_code());
			}
			
			// 0명인 상품리스트 삭제
			paymentInfo.getProducts().removeIf(p -> p.getCount() <= 0);
			
			List<ProductDTO> dbProducts = ticketingService.getProducts_NoSchedule(paymentInfo.getProductGroup());
			
			
			List<ProductDTO> selectedProducts = new ArrayList<>();		
			// 상품코드로 상품 정보 다 불러오기, 인원수만 불러온 상품에 넣기
			for (int i = 0; i < paymentInfo.getProducts().size(); i++) {
				ProductDTO product = paymentInfo.getProducts().get(i);
				for (int j = 0; j < dbProducts.size(); j++) {
					ProductDTO dbProduct = dbProducts.get(j);
					if (product.getShop_code().equals(dbProduct.getShop_code())
							&& product.getProduct_group_code().equals(dbProduct.getProduct_group_code())
							&& product.getProduct_code().equals(dbProduct.getProduct_code())) {
						dbProduct.setCount(product.getCount());
						selectedProducts.add(dbProduct);
					}
				}
			}
			paymentInfo.setProducts(selectedProducts);

			// 총액, 총인원수
			paymentInfo.setTotalCount(selectedProducts.stream().mapToInt(ProductDTO::getCount).sum());
			paymentInfo
					.setTotalFee(selectedProducts.stream().map(p -> p.getProduct_fee().multiply(BigDecimal.valueOf(p.getCount())))
							.reduce(BigDecimal.ZERO, BigDecimal::add));

			// 방문자 타입(P이면 입장자 정보를 받는다. 안쓰지만 일단은 넣어둠
			String visitorType = ticketingService.getVisitorType(paymentInfo.getProductGroup().getShop_code());
			paymentInfo.setVisitorType(visitorType);

			// 유효기간 가져오기
			TicketValidVO ticketValid = new TicketValidVO();
			ticketValid.setShop_code(shopCode);
			ticketValid = ticketingService.selectTicketValid(ticketValid);
			
			
			
			if(ticketValid == null || !StringUtils.hasText(ticketValid.getValid_period())) { 
				ScriptUtils.alertAndBackPage(response, "티켓의 유효기간이 정의되지 않았습니다.");
				return null;
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			Date date2 = format.parse("2022-02-28");
			
			// 유효기간 설정 / 2022-02-25 / 조미근
			Calendar calTo = Calendar.getInstance(); 
			calTo.getActualMaximum(Calendar.DAY_OF_MONTH);
			String temp = String.valueOf(calTo.getActualMaximum(Calendar.DAY_OF_MONTH)-LocalDateTime.now().getDayOfMonth());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(now);
			if(now.compareTo(date2) > 0) { //2월 말 기준 이후
				paymentInfo.setValid_period(ticketValid.getValid_period());
			}else { //2월 말 기준 이전
				paymentInfo.setValid_period(temp);
			}
			
//			// 회차
//			if (StringUtils.hasText(paymentInfo.getSchedule_code())) {
//				ScheduleDTO schedule = new ScheduleDTO();
//				schedule.setShop_code(paymentInfo.getProductGroup().getShop_code());
//				schedule.setSchedule_code(paymentInfo.getSchedule_code());
//				schedule = ticketingService.getScheduleByScheduleCode(schedule);
	//
//				if (schedule == null) {
//					ScriptUtils.alertAndClose(response, "해당 상품의 회차정보가 존재하지 않습니다. 결제를 진행할 수 없습니다.");
//					return null;
//				}
//				paymentInfo.setSchedule(schedule);
//			}

			
			// 본인인증 정보 가져오기
			SelfAuthenticationDTO selfAuthentication = new SelfAuthenticationDTO();
			selfAuthentication.setShop_code(shopCode);
			selfAuthentication.setContent_mst_cd(paymentInfo.getContent_mst_cd());
			selfAuthentication.setSuccess_url("/checkReservationSuccess");
			selfAuthentication.setFail_url("/checkReservationFail");
			selfAuthentication = reserverAuthenticationService.getSelfAuthenticationEncodedData(request, selfAuthentication);		
			if(StringUtils.hasText(selfAuthentication.getMessage())) {
				ScriptUtils.alertAndBackPage(response, "본인인증 모듈 에러: " + selfAuthentication.getMessage());
				return null;
			}
			model.addAttribute("selfAuthentication", selfAuthentication);
			
			
			ShopDetailVO shopDetail = ticketingService.getShopDetail(paymentInfo.getProductGroup().getShop_code());
			model.addAttribute("shopDetail", shopDetail);

			// 키가 있는지 확인
			VerificationKeyVO keys = ticketingService.getKeys(paymentInfo.getProductGroup().getShop_code());

			if (keys == null || !StringUtils.hasText(keys.getIdentification_site_code())
					|| !StringUtils.hasText(keys.getIdentification_site_password())
					|| !StringUtils.hasText(keys.getPay_merchant_id())
					|| !StringUtils.hasText(keys.getPay_merchant_key())) {
				redirect.addFlashAttribute("msg", "인증를 위한 정보가 없습니다. 관리자에게 연락 바랍니다.");
				if (paymentInfo.getType() == 1) {
					return "redirect:/ticketing/process1/selectWork?contentMstCd="
							+ paymentInfo.getProductGroup().getContent_mst_cd();
				} else {
					return "redirect:/ticketing/process2/selectWork?contentMstCd="
							+ paymentInfo.getProductGroup().getContent_mst_cd();
				}
			}

			model.addAttribute("siteCode", keys.getIdentification_site_code());
			model.addAttribute("sitePassword", keys.getIdentification_site_password());

			
			model.addAttribute("loginUserId", paymentInfo.getLoginUserId());
			model.addAttribute("loginUserNm", paymentInfo.getLoginUserNm());
			
			System.out.println("[/sogeumsan/insertReserver] : " + paymentInfo.getLoginUserId());
			System.out.println("[/sogeumsan/insertReserver] : " + paymentInfo.getLoginUserNm());
			
			
			return "/ticketing/sogeumsan/insertReserver";
		}

		
		
		private void logProductGroup(ProductGroupDTO_noSchedule info, ProductGroupDTO_noSchedule db) {
			try {
				log.info("========= product_group_is_not_match Start =========");
				log.info("paymentInfo: " + info);
				log.info("db: " + db);
				log.info("========= product_group_is_not_match End =========");
			} catch(Exception ex) {
				log.info("========= product_group_is_not_match Exception =========");
				ex.printStackTrace();
				log.info("========= product_group_is_not_match Exception =========");
			}
			
		}
		
		
		@PostMapping("/sogeumsan/payRequest")
		public String payRequestNoSchedule(@ModelAttribute("paymentInfo") @Valid PaymentInfoDTO_noSchedule info, HttpServletResponse response,
				Errors errors, Model model) throws Exception {
			
			if (errors.hasErrors()) {
				ScriptUtils.alert(response, errors.getAllErrors().get(0).getDefaultMessage());
				return null;
			}

			// 상품 그룹 확인
			//ProductGroupDTO dbProductGroup = ticketingService.getProductGroup(info.getProductGroup());
			ProductGroupDTO_noSchedule dbProductGroup = ticketingService.getProductGroup_noSchedule(info.getProductGroup());
			
			
			if (!checkProductGroup_noSchedule(info.getProductGroup(), dbProductGroup)) {

				logProductGroup(info.getProductGroup(), dbProductGroup);
				
				ScriptUtils.alertAndClose(response, "상품그룹정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}
			dbProductGroup.setType(info.getProductGroup().getType());
			info.setProductGroup(dbProductGroup);

			// 해당하는 그룹에 해당하는 상품이 정상적인지 확인

			List<ProductDTO> dbProducts = ticketingService.getSelectedProducts(info.getProducts());
//			if (!checkProducts(info, dbProducts)) {
//				ScriptUtils.alertAndClose(response, "상품정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
//				return null;
//			}
			for (int i = 0; i < info.getProducts().size(); i++) {
				ProductDTO product = info.getProducts().get(i);
				for (int j = 0; j < dbProducts.size(); j++) {
					ProductDTO dbProduct = dbProducts.get(j);
					if (product.getShop_code().equals(dbProduct.getShop_code())
							&& product.getProduct_group_code().equals(dbProduct.getProduct_group_code())
							&& product.getProduct_code().equals(dbProduct.getProduct_code())) {
						dbProduct.setCount(product.getCount());
					}
				}
			}
			info.setProducts(dbProducts);

			int dbTotalCount = dbProducts.stream().mapToInt(ProductDTO::getCount).sum();
			if (dbTotalCount != info.getTotalCount()) {
				ScriptUtils.alertAndClose(response, "판매매수가 맞지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}

			BigDecimal dbTotalFee = dbProducts.stream()
					.map(p -> p.getProduct_fee().multiply(BigDecimal.valueOf(p.getCount())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			if (!dbTotalFee.equals(info.getTotalFee())) {
				ScriptUtils.alertAndClose(response, "판매금액정보가 맞지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}

			if (StringUtils.hasText(info.getSchedule_code())) {
				ScheduleDTO schedule = new ScheduleDTO();
				schedule.setShop_code(info.getProductGroup().getShop_code());
				schedule.setSchedule_code(info.getSchedule_code());
				schedule = ticketingService.getScheduleByScheduleCode(schedule);

				if (schedule == null) {
					ScriptUtils.alertAndClose(response, "해당 상품의 회차정보가 존재하지 않습니다. 결제를 진행할 수 없습니다.");
					return null;
				}
				info.setSchedule(schedule);
			}

			info.setTotalCount(dbTotalCount);
			info.setTotalFee(dbTotalFee);

			// 판매 금액이 0원이면  결제수단을 0원결제로 변경
			if("0".equals(info.getTotalFee()) ) info.setPayMethod("0000");
			
			// 유효기간 가져오기
			TicketValidVO ticketValid = new TicketValidVO();
			ticketValid.setShop_code(info.getProductGroup().getShop_code());
			ticketValid = ticketingService.selectTicketValid(ticketValid);
			if(ticketValid == null || !StringUtils.hasText(ticketValid.getValid_period())) { 
				ScriptUtils.alertAndBackPage(response, "티켓의 유효기간이 정의되지 않았습니다.");
				return null;
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			Date date2 = format.parse("2022-02-28");
			
			// 유효기간 설정 / 2022-02-25 / 조미근
			Calendar calTo = Calendar.getInstance(); 
			calTo.getActualMaximum(Calendar.DAY_OF_MONTH);
			String temp = String.valueOf(calTo.getActualMaximum(Calendar.DAY_OF_MONTH)-LocalDateTime.now().getDayOfMonth());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(now);
			if(now.compareTo(date2) > 0) { //2월 말 기준 이후
				info.setValid_period(ticketValid.getValid_period());
			}else { //2월 말 기준 이전
				info.setValid_period(temp);
			}
			
			//WebPaymentDTO webPayment = ticketingService.addWebPaymentInfo(info);
			WebPaymentDTO webPayment = ticketingService.addWebPaymentInfo_noSchedule(info);

			//System.out.println(info);
			model.addAttribute("webPayment", webPayment);

			// 이용약관 가져오기
			WebReservationKeyDTO reserveInfo = ticketingService.selectReserveInfo(dbProductGroup.getShop_code());
			String content = reserveInfo.getInfo_d();
			reserveInfo.setInfo_d(StringEscapeUtils.unescapeXml(content));
			model.addAttribute("reserveInfo", reserveInfo);
			
			
			model.addAttribute("loginUserId", info.getLoginUserId());
			model.addAttribute("loginUserNm", info.getLoginUserNm());
			
			System.out.println("[sogeumsan/payRequest]: " + info.getLoginUserId());
			System.out.println("[sogeumsan/payRequest]: " + info.getLoginUserNm());
			
			
			return "/ticketing/sogeumsan/payRequest";
		}
		
		
		
		@PostMapping("/sogeumsan/payResult")
		public String payResult_noSchedule(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("shopInfo") @Valid ShopInfo_noScheduleDTO loginUserInfo) throws Exception {

			int resultSuccess = 0;
			String resultOrderNum = "";
			String resultMessage = "";

			
			
			System.out.println("[/sogeumsan/payResult]: " + loginUserInfo.getUserId());
			System.out.println("[/sogeumsan/payResult]: " + loginUserInfo.getUserName());
			
			
			
			/*
			 ****************************************************************************************
			 * <인증 결과 파라미터>
			 ****************************************************************************************
			 */
			String authResultCode = (String) request.getParameter("AuthResultCode"); // 인증결과 : 0000(성공)
			String authResultMsg = (String) request.getParameter("AuthResultMsg"); // 인증결과 메시지
			String nextAppURL = (String) request.getParameter("NextAppURL"); // 승인 요청 URL
			String txTid = (String) request.getParameter("TxTid"); // 거래 ID
			String authToken = (String) request.getParameter("AuthToken"); // 인증 TOKEN
			String payMethod = (String) request.getParameter("PayMethod"); // 결제수단
			String mid = (String) request.getParameter("MID"); // 상점 아이디
			String moid = (String) request.getParameter("Moid"); // 상점 주문번호
			String amt = (String) request.getParameter("Amt"); // 결제 금액
			String reqReserved = (String) request.getParameter("ReqReserved"); // 상점 예약필드
			String netCancelURL = (String) request.getParameter("NetCancelURL"); // 망취소 요청 URL

			String authSignature = (String) request.getParameter("Signature"); // Nicepay에서 내려준 응답값의 무결성 검증 Data

			WebPaymentDTO webPayment = ticketingService.getWebPayment(moid);
			VerificationKeyVO keys = ticketingService.getKeys(webPayment.getShop_code());

			/*
			 ****************************************************************************************
			 * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한
			 * 요소를 방지하기 위해 연동 시 사용하시기 바라며 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
			 ****************************************************************************************
			 */
			DataEncrypt sha256Enc = new DataEncrypt();
			String merchantKey = keys.getPay_merchant_key(); // 상점키

			// 인증 응답 Signature = hex(sha256(AuthToken + MID + Amt + MerchantKey)
			String authComparisonSignature = sha256Enc.encrypt(authToken + mid + amt + merchantKey);

			/*
			 ****************************************************************************************
			 * <승인 결과 파라미터 정의> 샘플페이지에서는 승인 결과 파라미터 중 일부만 예시되어 있으며, 추가적으로 사용하실 파라미터는 연동메뉴얼을
			 * 참고하세요.
			 ****************************************************************************************
			 */
			String ResultCode = "";
			String ResultMsg = "";
			String PayMethod = "";
			String GoodsName = "";
			String Amt = "";
			String TID = "";
			String Signature = "";
			String paySignature = "";
			String AuthCode = "";
			String AuthDate = "";

			/*
			 ****************************************************************************************
			 * <인증 결과 성공시 승인 진행>
			 ****************************************************************************************
			 */
			String resultJsonStr = "";
			if (authResultCode.equals("0000") && authSignature.equals(authComparisonSignature)) {

				// 결제 인증 완료 기록
				WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제인증성공")
						.message("AuthResultCode: " + authResultCode).orderNo(moid).build();
				ticketingService.addWebPaymentStatus(authenticationFinishedStatus);

				/*
				 ****************************************************************************************
				 * <해쉬암호화> (수정하지 마세요) SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
				 ****************************************************************************************
				 */
				String ediDate = getyyyyMMddHHmmss();
				String signData = sha256Enc.encrypt(authToken + mid + amt + ediDate + merchantKey);

				/*
				 ****************************************************************************************
				 * <승인 요청> 승인에 필요한 데이터 생성 후 server to server 통신을 통해 승인 처리 합니다.
				 ****************************************************************************************
				 */
				StringBuffer requestData = new StringBuffer();
				requestData.append("TID=").append(txTid).append("&");
				requestData.append("AuthToken=").append(authToken).append("&");
				requestData.append("MID=").append(mid).append("&");
				requestData.append("Amt=").append(amt).append("&");
				requestData.append("EdiDate=").append(ediDate).append("&");
				requestData.append("CharSet=").append("utf-8").append("&");
				requestData.append("SignData=").append(signData);

				// 결제 승인 요청 기록
				WebPaymentStatusDTO approvalRequestStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인요청").message("")
						.orderNo(moid).build();
				ticketingService.addWebPaymentStatus(approvalRequestStatus);

				resultJsonStr = connectToServer(requestData.toString(), nextAppURL, "utf-8");

				HashMap resultData = new HashMap();
				boolean paySuccess = false;
				if ("9999".equals(resultJsonStr)) {
					// 결제승인에러 기록
					WebPaymentStatusDTO approvalErrorStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인통신에러")
							.message(resultJsonStr).orderNo(moid).build();
					ticketingService.addWebPaymentStatus(approvalErrorStatus);

					/*
					 *************************************************************************************
					 * <망취소 요청> 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
					 *************************************************************************************
					 */
					StringBuffer netCancelData = new StringBuffer();
					requestData.append("&").append("NetCancel=").append("1");
					String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");

					HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
					ResultCode = (String) cancelResultData.get("ResultCode");
					ResultMsg = (String) cancelResultData.get("ResultMsg");
					Signature = (String) cancelResultData.get("Signature");
					String CancelAmt = (String) cancelResultData.get("CancelAmt");
					paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

					if (ResultCode == null) {
						// 결제 망취소 취소 기록
						WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder().status("고객-예매-망취소-통신불가")
								.message("").orderNo(moid).build();
						ticketingService.addWebPaymentStatus(cancelErrorStatus);

					} else {
						// 결제 망취소 취소 기록
						WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
								.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-망취소-성공"
										: "고객-예매-망취소-실패")
								.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg).orderNo(moid).build();
						ticketingService.addWebPaymentStatus(cancelErrorStatus);

					}
					resultSuccess = 0;
					resultMessage = "결제 승인 통신에 실패하였습니다.";

				} else {

					resultData = jsonStringToHashMap(resultJsonStr);
					ResultCode = (String) resultData.get("ResultCode"); // 결과코드 (정상 결과코드:3001)
					ResultMsg = (String) resultData.get("ResultMsg"); // 결과메시지
					PayMethod = (String) resultData.get("PayMethod"); // 결제수단
					GoodsName = (String) resultData.get("GoodsName"); // 상품명
					Amt = (String) resultData.get("Amt"); // 결제 금액
					TID = (String) resultData.get("TID"); // 거래번호
					// Signature : Nicepay에서 내려준 응답값의 무결성 검증 Data
					// 가맹점에서 무결성을 검증하는 로직을 구현하여야 합니다.
					Signature = (String) resultData.get("Signature");
					paySignature = sha256Enc.encrypt(TID + mid + Amt + merchantKey);

					if (!Signature.equals(paySignature)) {
						// 결제 승인 결과 기록
						WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인-완료-변조확인")
								.message(PayMethod + ": [Received]" + Signature + "|[ShouldBe]" + paySignature)
								.orderNo(moid).build();
						ticketingService.addWebPaymentStatus(approvalResultStatus);

						/*
						 *************************************************************************************
						 * <무결성 체크 실패 취소 요청> 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
						 *************************************************************************************
						 */
						StringBuffer netCancelData = new StringBuffer();
						requestData.append("&").append("NetCancel=").append("1");
						String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");

						HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
						ResultCode = (String) cancelResultData.get("ResultCode");
						ResultMsg = (String) cancelResultData.get("ResultMsg");
						Signature = (String) cancelResultData.get("Signature");
						String CancelAmt = (String) cancelResultData.get("CancelAmt");
						paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

						if (ResultCode == null) {
							// 결제 무결성체크실패 취소 통신불가 기록
							WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
									.status("고객-예매-변조취소-통신불가").message("").orderNo(moid).build();
							ticketingService.addWebPaymentStatus(cancelErrorStatus);

						} else {
							// 결제 무결성체크실패 취소 기록
							WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
									.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-변조취소-망취소성공"
											: "고객-예매-변조취소-망취소실패")
									.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg).orderNo(moid)
									.build();
							ticketingService.addWebPaymentStatus(cancelErrorStatus);
						}

						resultSuccess = 0;
						resultMessage = "결제 무결성 검증에 실패하였습니다.\n[" + ResultCode + "]" + ResultMsg;
					} else {
						// 결제 승인 결과 기록
						WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder().status("고객-예매-승인-성공")
								.message(PayMethod + ": " + ResultCode).orderNo(moid).build();
						ticketingService.addWebPaymentStatus(approvalResultStatus);

						// API 호출

						// PG사 결제 결과 정보 저장
						WebPaymentPgResultDTO pgResult = new WebPaymentPgResultDTO();

						pgResult.setAuth_result_code(authResultCode);
						pgResult.setAuth_result_msg(authResultMsg);
						pgResult.setNext_ap_url(nextAppURL);
						pgResult.setTransaction_id(txTid);
						pgResult.setAuth_token(authToken);
						pgResult.setPay_method(payMethod);
						pgResult.setMid(mid);
						pgResult.setMoid(moid);
						pgResult.setAmt(amt);
						pgResult.setNet_cancel_url(netCancelURL);
						pgResult.setAuth_signature_comparison(authSignature.equals(authComparisonSignature) ? "1" : "0");
						pgResult.setApproval_result_code(ResultCode);
						pgResult.setApproval_result_msg(ResultMsg);
						pgResult.setTid(TID);

						// 카드일 경우
						if (payMethod.equals("CARD")) {

							// 카드
							String cardNo = (String) resultData.get("CardNo"); // 카드번호
							String cardQuota = (String) resultData.get("CardQuota"); // 할부개월
							String cardCode = (String) resultData.get("CardCode"); // 결제 카드사 코드
							String cardName = (String) resultData.get("CardName"); // 결제 카드사명
							String acquCardCode = (String) resultData.get("AcquCardCode"); // 매입 카드사 코드
							String acquCardName = (String) resultData.get("AcquCardName"); // 매입 카드사 이름
							String rcptType = (String) resultData.get("RcptType"); // 매입 카드사 이름

							pgResult.setCardNo(!StringUtils.hasText(cardNo) ? "" : cardNo);
							pgResult.setCardQuota(!StringUtils.hasText(cardQuota) ? "" : cardQuota);
							pgResult.setCardCode(!StringUtils.hasText(cardCode) ? "" : cardCode);
							pgResult.setAcquCardCode(!StringUtils.hasText(acquCardCode) ? "" : acquCardCode);
							pgResult.setAcquCardName(!StringUtils.hasText(acquCardName) ? "" : acquCardName);
							pgResult.setRcptType(!StringUtils.hasText(rcptType) ? "" : rcptType);
						} else if (payMethod.equals("BANK")) {
							String bankCode = (String) resultData.get("BankCode"); // 결제은행코드
							String bankName = (String) resultData.get("BankName"); // 결제은행명
							String rcptType = (String) resultData.get("RcptType"); // 매입 카드사 이름
							pgResult.setCardCode(!StringUtils.hasText(bankCode) ? "" : bankCode);
							pgResult.setCardName(!StringUtils.hasText(bankName) ? "" : bankName);
							pgResult.setRcptType(!StringUtils.hasText(rcptType) ? "" : rcptType);
						}
						AuthCode = (String) resultData.get("AuthCode");
						; // 승인번호
						AuthDate = (String) resultData.get("AuthDate");
						; // 승인날짜
						pgResult.setAuth_code(AuthCode);
						pgResult.setAuth_date(AuthDate);

						ticketingService.addWebPaymentPgResult(pgResult);

						/*
						 *************************************************************************************
						 * <결제 성공 여부 확인>
						 *************************************************************************************
						 */
						if (PayMethod != null) {
							if (PayMethod.equals("CARD")) {
								if (ResultCode.equals("3001"))
									paySuccess = true; // 신용카드(정상 결과코드:3001)
							} else if (PayMethod.equals("BANK")) {
								if (ResultCode.equals("4000"))
									paySuccess = true; // 계좌이체(정상 결과코드:4000)
							} else if (PayMethod.equals("CELLPHONE")) {
								if (ResultCode.equals("A000"))
									paySuccess = true; // 휴대폰(정상 결과코드:A000)
							} else if (PayMethod.equals("VBANK")) {
								if (ResultCode.equals("4100"))
									paySuccess = true; // 가상계좌(정상 결과코드:4100)
							} else if (PayMethod.equals("SSG_BANK")) {
								if (ResultCode.equals("0000"))
									paySuccess = true; // SSG은행계좌(정상 결과코드:0000)
							} else if (PayMethod.equals("CMS_BANK")) {
								if (ResultCode.equals("0000"))
									paySuccess = true; // 계좌간편결제(정상 결과코드:0000)
							}
						}

						if (paySuccess) { // 결제 승인 성공

							
							log.info("[TicketApi Call]");
							//ApiResultVO apiResultVO = ticketingService.callTicketApi(pgResult);
							ApiResultVO apiResultVO = ticketingService.callTicketApi_noSchedule(pgResult);

							// API 호출 성공
							if (apiResultVO.getSuccess() == 1) {

								WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder().status("고객-예매-Api호출-성공")
										.message("ApiCallResult: " + apiResultVO.toString()).orderNo(moid).build();
								ticketingService.addWebPaymentStatus(apiCallStatus);

								ShopDetailVO shopDetail = ticketingService
										.getShopDetail(apiResultVO.getWebPayment().getShop_code());

								try {
									messageService.send_noSchedule(request, response, apiResultVO, pgResult, shopDetail);
									
									
								} catch (Exception ex) {
									ex.printStackTrace();
								}

								if (StringUtils.hasText(apiResultVO.getWebPayment().getReserverEmail())) {

									try {
										mailService.sendReserve(request, apiResultVO, pgResult, shopDetail);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
								resultSuccess = 1;
								resultOrderNum = moid;
								resultMessage = "결제에 성공하였습니다.";
							} else { // API호출 실패

								WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder().status("고객-예매-Api호출-실패")
										.message("ApiCallResult: " + apiResultVO.toString()).orderNo(moid).build();
								ticketingService.addWebPaymentStatus(apiCallStatus);

								// TODO: 현재는 전체취소로 되어 있음. 추후 일자로 부분취소 추가시("E")인지 계산
								callCancelApi_noSchedule(pgResult, apiResultVO.getWebPayment().getContent_mst_cd(), "A");

								/*
								 *************************************************************************************
								 * <망취소 요청> API호출 실패시 망취소 처리
								 *************************************************************************************
								 */
								StringBuffer netCancelData = new StringBuffer();
								requestData.append("&").append("NetCancel=").append("1");
								String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");

								HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
								ResultCode = (String) cancelResultData.get("ResultCode");
								ResultMsg = (String) cancelResultData.get("ResultMsg");
								Signature = (String) cancelResultData.get("Signature");
								String CancelAmt = (String) cancelResultData.get("CancelAmt");
								paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

								// 결제 망취소 취소 기록
								WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
										.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-Api호출-실패-망취소-성공"
												: "고객-예매-Api호출-실패-망취소-실패")
										.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg).orderNo(moid)
										.build();
								ticketingService.addWebPaymentStatus(cancelErrorStatus);

								resultSuccess = 0;
								resultMessage = "결제 API 호출에 실패하였습니다." + apiResultVO.getErrMsg();
							}
						} else {
							// 결제승인에러 기록
							WebPaymentStatusDTO approvalFailStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제승인-에러")
									.message("ResultCode: " + ResultCode + " | Message:" + resultJsonStr).orderNo(moid).build();
							ticketingService.addWebPaymentStatus(approvalFailStatus);

							resultSuccess = 0;
							resultMessage = "결제에 실패 하였습니다.\n" + ResultMsg;
						}
					}
				}
			} else if (authSignature.equals(authComparisonSignature)) {
				ResultCode = authResultCode;
				ResultMsg = authResultMsg;

				// 결제인증실패 기록
				WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제인증실패-변조확인")
						.message("AuthResultCode: " + authResultCode + " | AuthResultMsg" + authResultMsg).orderNo(moid)
						.build();
				ticketingService.addWebPaymentStatus(authenticationFinishedStatus);

				resultSuccess = 0;
				resultMessage = "결제인증에 실패하였습니다.\n[" + ResultCode + "]" + ResultMsg;
			} else {
				// 결제인증실패 기록
				WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder().status("고객-예매-결제인증실패")
						.message("AuthResultCode: " + authResultCode + " | AuthComapreSignature" + authComparisonSignature)
						.orderNo(moid).build();
				ticketingService.addWebPaymentStatus(authenticationFinishedStatus);

				resultSuccess = 0;
				resultMessage = "결제인증에 실패하였습니다.";
			}

			resultOrderNum = removeQuatations(resultOrderNum);
			resultMessage = removeQuatations(resultMessage);

			model.addAttribute("success", resultSuccess);
			model.addAttribute("orderNo", resultOrderNum);
			model.addAttribute("message", resultMessage);

			return "/ticketing/sogeumsan/payResult";
		}
		
		
		/**
		 * api취소 호출
		 * 
		 * @param pgResult
		 * @param contentMstCd
		 * @param cancelType   A: 전체취소, 부분취소
		 * @return
		 * @throws Exception
		 */
		private ApiResultVO callCancelApi_noSchedule(WebPaymentPgResultDTO pgResult, String contentMstCd, String cancelType)
				throws Exception {
			// api 취소 요청
			ApiSocialCancelDTO apiSocialCancel = ApiSocialCancelDTO.builder().CONTENT_MST_CD(contentMstCd)
					.ONLINE_CHANNEL(propertyService.getString("online_channel")).ORDER_NUM(pgResult.getMoid()).USER_ID("WEBRESERVE").TERMINAL_CODE("WEBCANCEL")
					.TERMINAL_DATETIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
					.CANCEL_TYPE(cancelType).build();
			ApiResultVO apiCancelResult = ticketingService.callCancelApi(apiSocialCancel);

			// 결제승인에러 기록
			WebPaymentStatusDTO apiCancelStatus = WebPaymentStatusDTO.builder()
					.status(apiCancelResult.getSuccess() == 1 ? "고객-취소Api-성공" : "고객-취소Api-실패")
					.message("Success: " + apiCancelResult.getSuccess() + " | Message: " + apiCancelResult.getErrMsg())
					.orderNo(pgResult.getMoid()).build();
			ticketingService.addWebPaymentStatus(apiCancelStatus);

			return apiCancelResult;
		}
		
		
		
		
		
		@PostMapping("/sogeumsan/checkTicketAjax")
		@ResponseBody
		public List<SaleDTO_noSchedule> checkTicketAjax(@RequestBody SaleDTO_noSchedule sale, Model model) throws Exception {
			
			//List<SaleDTO> saleDTOList = ticketingService.getCheckTicket(sale);
			List<SaleDTO_noSchedule> saleDTOList = ticketingService.getCheckTicket_noSchedule(sale);
			
			return saleDTOList;
		}
		
		
		
		
		@GetMapping("/sogeumsan/prevShowTicket")
		public String prevShowTicketList(SaleDTO_noSchedule sale, HttpServletRequest request, HttpServletResponse response, 
				Model model, RedirectAttributes rttr, @ModelAttribute("refundResult") String refundResult, @ModelAttribute("message") String message) throws Exception{
			//request.getSession().setAttribute("saleDTO", saleDTO);
			
			//List<SaleDTO> saleDTOList = ticketingService.getCheckTicket(sale);
			List<SaleDTO_noSchedule> saleDTOList = ticketingService.getCheckTicket_noSchedule(sale);
			
			
			if(saleDTOList.size() > 1) {
				if(StringUtils.hasText(refundResult)) {
					rttr.addAttribute("message", refundResult);
				} else {
					rttr.addAttribute("message", message);
				}
				return "redirect:/ticketing/sogeumsan/showTicketInfoList?content_mst_cd=" + sale.getContent_mst_cd() + "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") + "&member_tel=" + sale.getMember_tel() + "&today=" + sale.getToday();
			} else if(saleDTOList.size() == 1) {
				if(StringUtils.hasText(refundResult)) {
					rttr.addAttribute("message", refundResult);
				} else {
					rttr.addAttribute("message", message);
				}
				return "redirect:/ticketing/sogeumsan/showTicketInfo?content_mst_cd=" + sale.getContent_mst_cd() + "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") + "&member_tel=" + sale.getMember_tel() + "&today=" + sale.getToday() 
				+ "&sale_code=" + saleDTOList.get(0).getSale_code();
			} else {
				if(StringUtils.hasText(refundResult)) {
					message = "환불이 정상 처리 되었습니다. 더이상 예매 내역이 없습니다.";
				}
				
				ScriptUtils.alertAndMovePage(response, message, "/ticketing/sogeumsan/selectTicket?content_mst_cd=" + sale.getContent_mst_cd());
				return null;
			}
		}
		
		
		@GetMapping("/sogeumsan/showTicketInfoList")
		public String ShowTicketInfoList_noSchedule(SaleDTO_noSchedule saleDTO, HttpServletRequest request,
				@ModelAttribute("message") String message, Model model) throws Exception {
//			List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductList(saleDTO);
	//
//			model.addAttribute("saleProducts", saleProductDTOList);
			
			String shopCode = ticketingService.getShopCode(saleDTO.getContent_mst_cd());
			saleDTO.setShop_code(shopCode);
			
			model.addAttribute("sale", saleDTO);
			List<SaleVO_noSchedule> sales = ticketingService.getSalesByMemberInfo(saleDTO);
			
			
			model.addAttribute("sales", sales);
			//model.addAttribute("companyTel", ticketingService.getCompany(saleDTO.getShopCode()).getComp_tel());
			
			
			return "/ticketing/sogeumsan/showTicketInfoList";
		}
		
		
		@GetMapping("/sogeumsan/showTicketInfo")
		public String ShowTicketInfo_noSchedule(HttpServletRequest request, SaleDTO_noSchedule saleDTO, @ModelAttribute("message") String message, Model model) throws Exception {
			
			BigDecimal totalFee = BigDecimal.ZERO;
			
			String orderNum = ticketingService.getOrderNumBySaleCode(saleDTO);
			String shopCode = ticketingService.getShopCode(saleDTO.getContent_mst_cd());
			
			
			//SaleVO saleVO = new SaleVO();
			SaleVO_noSchedule saleVO = new SaleVO_noSchedule();
			
			saleVO.setContent_mst_cd(saleDTO.getContent_mst_cd());
			saleVO.setSale_code(saleDTO.getSale_code());
			saleVO.setOrder_num(orderNum);
			saleVO.setShop_code(shopCode);
			saleVO.setContent_mst_cd(saleDTO.getContent_mst_cd());
			
			saleVO = ticketingService.getSaleByOrderNum(saleVO);
			
			List<SaleProductDTO_noSchedule> saleProducts = ticketingService.getSaleProduct(saleDTO);

			int refundQuantity= 0;
			/*for(SaleProductDTO saleProduct : saleProducts) {
				if(saleProduct.getRefund_yn().equals("1")) {
					refundQuantity++;
				} else {
					SaleProductDTO tsSaleProduct = ticketingService.getTsSaleProductByBookNo(saleProduct);
					if(tsSaleProduct != null) {
						saleProduct.setPlay_date(tsSaleProduct.getPlay_date());
					}
				}
			}*/
			
			// 상품코드별 상품판매정보
			Map<String, List<SaleProductDTO_noSchedule>> saleProductMap = new HashMap<>();
			
			for(int i=0; i< saleProducts.size(); i++) {
				
				SaleProductDTO_noSchedule saleProduct = saleProducts.get(i);
				
				if(saleProductMap.containsKey(saleProduct.getProduct_code())) {
					saleProductMap.get(saleProduct.getProduct_code()).add(saleProduct);
				} else {
					List<SaleProductDTO_noSchedule> persons = new ArrayList<>();
					persons.add(saleProduct);
					saleProductMap.put(saleProduct.getProduct_code(), persons);
				}
			}
			model.addAttribute("saleProductMap", saleProductMap);
			
			int targetIdx = 0;
			
			
			for(int i=0; i<saleProducts.size(); i++) {
				
				// 전체 구매 금액 계산
				totalFee = totalFee.add(saleProducts.get(i).getProduct_fee());
				
				if(saleProducts.get(i).getRefund_yn().equals("1")) {
					refundQuantity++;
				}else {
					SaleProductDTO_noSchedule tsSaleProduct = ticketingService.getTsSaleProductByBookNo(saleProducts.get(i));
					if(tsSaleProduct != null) {
						saleProducts.get(i).setPlay_date(tsSaleProduct.getPlay_date());
					}
					if(i > 0) {
						if(saleProducts.get(i).getProduct_name().equals(saleProducts.get(targetIdx).getProduct_name())) {
							int currentCount = Integer.parseInt(saleProducts.get(i).getQuantity());
							if(currentCount <= 0)
								continue;
							int count = Integer.parseInt(saleProducts.get(targetIdx).getQuantity()) + currentCount;
							saleProducts.get(targetIdx).setQuantity(String.valueOf(count));						
							
							BigDecimal currentProductFee = saleProducts.get(i).getProduct_fee().multiply(BigDecimal.valueOf(currentCount));
							BigDecimal productFee = saleProducts.get(targetIdx).getProduct_fee().add(currentProductFee);

							saleProducts.get(targetIdx).setProduct_fee(productFee);

						} else {
							targetIdx = i;
						}
					}
					
				}
			}
			
			saleVO.setRefund_quantity(refundQuantity);
			model.addAttribute("saleProducts", saleProducts);
			model.addAttribute("sale", saleVO);
			model.addAttribute("totalFee", totalFee);
			
//			ShopDetailVO shopDetail = ticketingService.getShopDetail(productGroup.getShop_code());
			return "/ticketing/sogeumsan/showTicketInfo";
		}
		
		
		
		@GetMapping("/sogeumsan/finish")
		public String process1Finish(@ModelAttribute("shopInfo") @Valid ShopInfo_noScheduleDTO shopInfo,
				@RequestParam("orderNo") String orderNo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

			System.out.println("[/sogeumsan/finish]: "+ shopInfo.getUserId());
			System.out.println("[/sogeumsan/finish]: "+ shopInfo.getUserName());
			
			// ============================== 소금산벨리 ( 리버스아이티 ) 에 예매 내역 전송 ============================= 
			sogeumsanResApiCall(shopInfo.getContent_mst_cd(), orderNo, shopInfo.getUserId(), shopInfo.getUserName(), "slae");
			//=================================================================================================
			
			
			
			Map<String, Object> params = new HashMap<>();
			params.put("orderNo", orderNo);
			params.put("contentMstCd", shopInfo.getContent_mst_cd());
			FinishTradeVO_noSchedule trade = ticketingService.getFinishTrade_noSchedule(params);
			model.addAttribute("trade", trade);
			
			List<FinishTradeVO_noSchedule> products = ticketingService.selectProductsForGA(params);
			model.addAttribute("products", products);


			String shopCode = ticketingService.getShopCode(shopInfo.getContent_mst_cd());
			// 본인인증 정보 가져오기
			SelfAuthenticationDTO selfAuthentication = new SelfAuthenticationDTO();
			selfAuthentication.setShop_code(shopCode);
			selfAuthentication.setContent_mst_cd(shopInfo.getContent_mst_cd());
			selfAuthentication.setSuccess_url("/checkReservationSuccess");
			selfAuthentication.setFail_url("/checkReservationFail");
			selfAuthentication = reserverAuthenticationService.getSelfAuthenticationEncodedData(request, selfAuthentication);		
			if(StringUtils.hasText(selfAuthentication.getMessage())) {
				ScriptUtils.alertAndBackPage(response, "본인인증 모듈 에러: " + selfAuthentication.getMessage());
				return null;
			}
			model.addAttribute("selfAuthentication", selfAuthentication);
			
			ShopDetailVO shopDetail = ticketingService.getShopDetailByContentMstCd(shopInfo.getContent_mst_cd());
			model.addAttribute("shopDetail", shopDetail);

			return "/ticketing/sogeumsan/finish";
		}
		
		
		/**
		 * 소금산밸리 예매 API 연동
		 * @param contentMstCd
		 * @param orderNum
		 */
		public void sogeumsanResApiCall(String contentMstCd, String orderNum, String userId, String userNm, String statusFlag) {
			
			try {
				Map<String, Object> params = new HashMap<>();
				params.put("orderNo", orderNum);
				params.put("contentMstCd", contentMstCd);
				
				//예매 내역 조회
				List<FinishTradeVO_noSchedule> resSaleInfoList = ticketingService.selectResSaleInfoList(params);
				
				com.fasterxml.jackson.databind.ObjectMapper voToJsonTmp = new com.fasterxml.jackson.databind.ObjectMapper();
				
				//ObjectMapper voToJsonTmp 											= new ObjectMapper();
				
				SogeumsanLinkVO.ReqNappleToNiceOfRes reqParamVo 					= new SogeumsanLinkVO.ReqNappleToNiceOfRes();
				
				SogeumsanLinkVO.socialSaleHeader defaultParam 						= new SogeumsanLinkVO.socialSaleHeader();								//공통영역
				List<SogeumsanLinkVO.socialSaleProductList> productList 			= new ArrayList<SogeumsanLinkVO.socialSaleProductList>();				//상품정보
				
				String jsonStr = "";
				
				for(int i=0; i<resSaleInfoList.size(); i++)
				{
					if(i == 0)
					{
						makeCommonParam(resSaleInfoList.get(i), defaultParam, userId, userNm, statusFlag);
					}
					
					// 헤더 셋팅 후 PRODUCT_LIST 가공
					SogeumsanLinkVO.socialSaleProductList productVo = new SogeumsanLinkVO.socialSaleProductList();
					productVo = makeProductList(resSaleInfoList.get(i), productVo);
					
					//PRODUCT_LIST 배열에 객체 담기.
					productList.add(productVo);
				}
				
				defaultParam.setSALE_PRODUCT_LIST(productList);
				reqParamVo.setParam(defaultParam);
				
				//VO key값 대문자 ( 사용자 지정형태로 )
				voToJsonTmp.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
				
				//vo -> json string 변환
				jsonStr = voToJsonTmp.writeValueAsString(reqParamVo);
				
				log.info("======================================");
				log.info("|	*Start SogeumSan API CALL!");
				log.info("|	[ORDER_NUM] 		=> "+ orderNum );
				log.info("|	[JSON STRING]		=> "+ jsonStr );
				
				//API 호출 ( 리버스아이티 )
				String apliCallResult = ApiConnectionToolkit.callByPost(statusFlag, jsonStr);
				
				
				
				log.info("|	*API sending result => [ " + apliCallResult +" ]");
				log.info("|	*END SogeumSan API CALL!");
				log.info("======================================");
				
			}catch (Exception e) {
				log.info("[sogeumsanResApiCall FAIL]");
				e.printStackTrace();
			}
		}
		
		
		/**
		 * 공통영역
		 * @param map
		 * @param defaultParam
		 * @param userId
		 * @param userNm
		 * @param statusFlag
		 * @return
		 * @throws Exception
		 */
		public static SogeumsanLinkVO.socialSaleHeader makeCommonParam(FinishTradeVO_noSchedule map, SogeumsanLinkVO.socialSaleHeader defaultParam, String userId, String userNm, String statusFlag) throws Exception{
			
			//공통영역 셋팅
			defaultParam.setCONTENT_MST_CD("SOGEUMSAN_0_1");
			defaultParam.setSALE_DATE(map.getSaleSaleDate());
			defaultParam.setTERMINAL_DATETIME(map.getSaleWorkDateTime());
			defaultParam.setUSER_ID(userId);
			defaultParam.setUSER_NM(userNm);
			defaultParam.setUSER_TEL("");
			defaultParam.setORDER_NUM(map.getSaleOrderNum());
			defaultParam.setVALID_FROM(map.getSaleValidFrom());
			defaultParam.setVALID_TO(map.getSaleValidTo());
			defaultParam.setSTATUS_FLAG(statusFlag);
			
			return defaultParam;
		}
		
		/**
		 * 상품영역
		 * @param map
		 * @param productVo
		 * @return
		 * @throws Exception
		 */
		public static SogeumsanLinkVO.socialSaleProductList makeProductList(FinishTradeVO_noSchedule map, SogeumsanLinkVO.socialSaleProductList productVo) throws Exception{
			
			//================================ NICE에 보낼 PRODUCT_LIST 데이터 가공 START ==============================
			productVo.setPRODUCT_CODE(map.getSaleProductCode());				//상품코드
			productVo.setPRODUCT_NAME(map.getSaleProductName());				//상품이름
			productVo.setBOOK_NO(map.getSaleBookNo());							//개별예매번호 ( 개별 바코드 번호 )
			
			productVo.setUNIT_PRICE(Integer.parseInt(map.getSaleUniPrice()));	//단가
			productVo.setQUANTITY(Integer.parseInt(map.getSaleQuantity()));		//수량
			
			productVo.setPLAY_DATE("");
			
			int totalFee = Integer.parseInt(map.getSaleUniPrice()) * Integer.parseInt(map.getSaleQuantity());
			productVo.setPRODUCT_FEE(totalFee);									//총금액
			productVo.setSEAT_CODE("");
			productVo.setSCHEDULE_CODE("");
			//================================ NICE에 보낼 PRODUCT_LIST 데이터 가공 END ===================================
			
			return productVo;
		}
		
		
		
		//=============================================== 예매 사용자 취소 =========================================
		
		@PostMapping("/sogeumsan/cancelTicket")
		public String cancelTicket(@ModelAttribute("sale") SaleDTO_noSchedule sale,
				@RequestParam(name = "isMyPage", required = false) String isMyPage, HttpServletRequest request,
				HttpServletResponse response, RedirectAttributes rttr, Model model) throws Exception {
			String failRedirectPage = "redirect:/ticketing/sogeumsan/showTicketInfo?content_mst_cd=" + sale.getContent_mst_cd() + "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") 
				+ "&member_tel=" + sale.getMember_tel() + "&today=" + sale.getToday() + "&sale_code=" + sale.getSale_code();
			String successRedirectPage = "redirect:/ticketing/sogeumsan/prevShowTicket?content_mst_cd=" + sale.getContent_mst_cd() + "&today=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) 
				+ "&member_name=" + URLEncoder.encode(sale.getMember_name(), "UTF-8") + "&member_tel=" + sale.getMember_tel();;
			Date now = new Date();
			Date today = DateHelper.getDateStart(now);

			List<RefundVO> refunds = ticketingService.getRefund_noSchedule(sale);

			if (refunds == null || refunds.size() == 0) {
//				rttr.addFlashAttribute("sale", sale);
				rttr.addFlashAttribute("message", "환불 정보가 존재하지 않습니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return failRedirectPage;
			}

			for (RefundVO refund : refunds) {
				if (refund.getRefund_yn().equals("1")) {
//					rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message", "이미 환불처리된 티켓입니다.");
					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
					return failRedirectPage;
				}

				if (refund.getUsed_count() > 0) {
//					rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message", "이미 사용된 티켓입니다.");
					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
					return failRedirectPage;
				}

				if (refund.getProduct_group_kind().equals("1")) {
					if (today.after(refund.getExpire_date())) {
//						rttr.addFlashAttribute("sale", sale);
						rttr.addFlashAttribute("message", "사용기간이 지난 티켓은 환불이 불가능합니다.");
						request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
						return failRedirectPage;
					}
				} else if (refund.getProduct_group_kind().equals("2")) {
					if (now.after(refund.getPlay_datetime())) {
//						rttr.addFlashAttribute("sale", sale);
						rttr.addFlashAttribute("message", "사용기간이 지난 티켓은 환불이 불가능합니다.");
						request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
						return failRedirectPage;
					}

				} else {
//					rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message", "존재하지 않는 종류의 티켓타입입니다.");
					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
					return failRedirectPage;
				}
			}

			WebPaymentDTO webPayment = ticketingService.getWebPayment(sale.getOrder_num());
			WebPaymentPgResultDTO pgResult = ticketingService.getWebPaymentPgResult(sale.getOrder_num());

			WebPaymentStatusDTO cancelTicketStatus = WebPaymentStatusDTO.builder().status("고객-전체취소-시작").message("OrderNo: "
					+ sale.getOrder_num() + " | Name:" + pgResult.getName() + " | Phone: " + pgResult.getPhone())
					.orderNo(sale.getOrder_num()).build();
			ticketingService.addWebPaymentStatus(cancelTicketStatus);

			
			
			//ApiResultVO apiResult = callCancelApi(pgResult, sale.getContent_mst_cd(), "A");
			ApiResultVO apiResult = callCancelApi_noSchedule(pgResult, sale.getContent_mst_cd(), "A");
			
//			ApiResultVO apiResult = new ApiResultVO();
//			apiResult.setSuccess(1);

			if (apiResult.getSuccess() != 1) {
//				rttr.addFlashAttribute("sale", sale);
				rttr.addFlashAttribute("message", "예매취소에 실패하였습니다" + apiResult.getErrMsg());
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return failRedirectPage;
			}
			/*
			 ****************************************************************************************
			 * <취소요청 파라미터> 취소시 전달하는 파라미터입니다. 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며, 추가 가능한 옵션 파라미터는
			 * 연동메뉴얼을 참고하세요.
			 ****************************************************************************************
			 */
			String tid = pgResult.getTid(); // 거래 ID
			String cancelAmt = pgResult.getAmt(); // 취소금액
			String partialCancelCode = "0"; // 부분취소여부
			String mid = pgResult.getMid(); // 상점 ID
			String moid = pgResult.getMoid(); // 주문번호
			String cancelMsg = "고객요청"; // 취소사유

			VerificationKeyVO keys = ticketingService.getKeys(webPayment.getShop_code());

			/*
			 ****************************************************************************************
			 * <해쉬암호화> (수정하지 마세요) SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
			 ****************************************************************************************
			 */
			DataEncrypt sha256Enc = new DataEncrypt();
			String merchantKey = keys.getPay_merchant_key(); // 상점키
			String ediDate = getyyyyMMddHHmmss();
			String signData = sha256Enc.encrypt(mid + cancelAmt + ediDate + merchantKey);

			/*
			 ****************************************************************************************
			 * <취소 요청> 취소에 필요한 데이터 생성 후 server to server 통신을 통해 취소 처리 합니다. 취소 사유(CancelMsg)
			 * 와 같이 한글 텍스트가 필요한 파라미터는 euc-kr encoding 처리가 필요합니다.
			 ****************************************************************************************
			 */
			StringBuffer requestData = new StringBuffer();
			requestData.append("TID=").append(tid).append("&");
			requestData.append("MID=").append(mid).append("&");
			requestData.append("Moid=").append(moid).append("&");
			requestData.append("CancelAmt=").append(cancelAmt).append("&");
			requestData.append("CancelMsg=").append(URLEncoder.encode(cancelMsg, "euc-kr")).append("&");
			requestData.append("PartialCancelCode=").append(partialCancelCode).append("&");
			requestData.append("EdiDate=").append(ediDate).append("&");
			requestData.append("SignData=").append(signData);
			String resultJsonStr = connectToServer(requestData.toString(),"https://webapi.nicepay.co.kr/webapi/cancel_process.jsp", "euc-kr");
//			String resultJsonStr = "{\"ResultCode\":\"2001\",\"ResultMsg\":\"정상 처리 되었습니다.\",\"ErrorCD\":\"0000\",\"ErrorMsg\":\"성공\",\"CancelAmt\":\"000000000900\",\"MID\":\"nicepay00m\",\"Moid\":\"001021080900010\",\"PayMethod\":\"CELLPHONE\",\"TID\":\"nicepay00m05012108091827142647\",\"CancelDate\":\"20210809\",\"CancelTime\":\"185659\",\"CancelNum\":\"00000000\",\"RemainAmt\":\"000000000000\",\"Signature\":\"594e9139df3881a8a0ce5155d99ab908430682233f5385a204c722e56c7b4f0d\",\"MallReserved\":\"\"}";
			/*
			 ****************************************************************************************
			 * <취소 결과 파라미터 정의> 샘플페이지에서는 취소 결과 파라미터 중 일부만 예시되어 있으며, 추가적으로 사용하실 파라미터는 연동메뉴얼을
			 * 참고하세요.
			 ****************************************************************************************
			 */
			String ResultCode = "";
			String ResultMsg = "";
			String CancelAmt = "";
			String CancelDate = "";
			String CancelTime = "";
			String TID = "";

			if ("9999".equals(resultJsonStr)) {
				ResultCode = "9999";
				ResultMsg = "통신실패";

				// 결제 취소 취소 기록
				WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder().status("고객-전체취소-PG취소-9999통신실패")
						.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg + " | Request Data: " + requestData.toString())
						.orderNo(pgResult.getMoid())
						.build();
				ticketingService.addWebPaymentStatus(cancelErrorStatus);

				CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());

//				rttr.addFlashAttribute("sale", sale);
				rttr.addFlashAttribute("message", "예약취소를 위한 통신에 장애가 발생하였습니다.[" + ResultCode + "-" + ResultMsg
						+ "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + company.getComp_tel() + ")에게 연락 부탁드립니다.");
//				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return failRedirectPage;
			} else {



				HashMap resultData = jsonStringToHashMap(resultJsonStr);
				ResultCode = (String) resultData.get("ResultCode"); // 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)
				ResultMsg = (String) resultData.get("ResultMsg"); // 결과메시지
				CancelAmt = (String) resultData.get("CancelAmt"); // 취소금액
				CancelDate = (String) resultData.get("CancelDate"); // 취소일
				CancelTime = (String) resultData.get("CancelTime"); // 취소시간
				TID = (String) resultData.get("TID"); // 거래아이디 TID
				
				// 결제 취소 취소 기록
				WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
						.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-PG취소-성공" : "고객-PG취소-실패")
						.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg + " | Request Data: " + requestData.toString())
						.orderNo(pgResult.getMoid())
						.build();
				ticketingService.addWebPaymentStatus(cancelErrorStatus);
				/*
				 ****************************************************************************************
				 * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한
				 * 요소를 방지하기 위해 연동 시 사용하시기 바라며 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
				 ****************************************************************************************
				 */
				String Signature = "";
				String paySignature = "";

				Signature = (String) resultData.get("Signature");
				paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

				if (!Signature.equals(paySignature)) {
					// 결제 취소 결과 변조 확인
					WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder().status("고객-전체취소-PG취소-변조확인")
							.message("[Received]" + Signature + "|[ShouldBe]" + paySignature)
							.orderNo(moid).build();
					ticketingService.addWebPaymentStatus(approvalResultStatus);

					CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());

//					rttr.addFlashAttribute("sale", sale);
					rttr.addFlashAttribute("message",
							"예약취소 데이터의 변조를 확인하였습니다. 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + company.getComp_tel() + ")에게 연락 부탁드립니다.");
					//request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
									
					return successRedirectPage;
					
				} else {
					if (ResultCode.equals("2001") || ResultCode.equals("2211")) {

						SaleVO_noSchedule searchSale = new SaleVO_noSchedule();
						searchSale.setShop_code(webPayment.getShop_code());
						searchSale.setOrder_num(webPayment.getOrder_no());

						//SaleVO_noSchedule saleVO = ticketingService.getSaleSsByOrderNum(searchSale);
						SaleVO_noSchedule saleVO = ticketingService.getSaleSsByOrderNum_noSchedule(searchSale);
						
						SaleDTO_noSchedule paymentSaleSearchSale = new SaleDTO_noSchedule();
						paymentSaleSearchSale.setSaleProductSearchType("SS");
						paymentSaleSearchSale.setSale_code(saleVO.getSale_code());
						paymentSaleSearchSale.setContent_mst_cd(webPayment.getContent_mst_cd());
						paymentSaleSearchSale.setMember_name(saleVO.getMember_name());
						paymentSaleSearchSale.setMember_tel(saleVO.getMember_tel());
						List<SaleProductDTO_noSchedule> saleProducts = ticketingService.getSaleProduct(paymentSaleSearchSale);
						saleVO.setSaleProducts(saleProducts);
						
						//bc_paymentsale에 마이너스금액으로 기록
						ShopPaymentsaleVO paymentsale = new ShopPaymentsaleVO();
						paymentsale.setShop_code(saleVO.getShop_code());
						paymentsale.setSale_code(saleVO.getSale_code());
						paymentsale.setPayment_no(saleProducts.get(0).getSale_sequence());
						List<ShopPaymentsaleVO> list = ticketingService.selectPaymentSale(paymentsale);
						for(int i=0; i<list.size(); i++) {
							if(list.get(i).getPayment_idx().equals("1")) {
								paymentsale = list.get(i);
								int idx = Integer.parseInt(list.get((list.size()-1)).getPayment_idx())+1;
								paymentsale.setPayment_idx(String.valueOf(idx));
							}
						}
						BigDecimal refundFee =  new BigDecimal(paymentsale.getPayment_fee().intValue());
						BigDecimal minus = new BigDecimal(-1);
						paymentsale.setPayment_fee(refundFee.multiply(minus));
						ticketingService.insertPaymentSale(paymentsale);
						
						//bc_refund_history 기록
						RefundHistoryVO historyVO = new RefundHistoryVO();
						historyVO.setShop_code(saleVO.getShop_code());
						historyVO.setSale_code(saleVO.getSale_code());
						historyVO.setCount(saleProducts.size());
						historyVO.setFee(refundFee.toString());
						String book_nos = saleProducts.stream().map(SaleProductDTO_noSchedule::getBook_no).collect(Collectors.joining(","));
						historyVO.setBook_no(book_nos);
						historyVO.setWork_id("WEBRESERVE");
						ticketingService.insertRefundHistory(historyVO);
						
						ShopDetailVO shopDetail = ticketingService.getShopDetail(webPayment.getShop_code());

						
						//=============================알림톡=====================================
						//일단 주석. 템플릿 없음.
						//messageService.sendRefund_noSchedule(request, saleVO, webPayment, pgResult, shopDetail);
						
						rttr.addFlashAttribute("refundResult", "예매가 정상 취소 되었습니다.");
						
						return successRedirectPage;
					} else {

						CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());

//						rttr.addFlashAttribute("sale", sale);
						rttr.addFlashAttribute("message", "예약취소에 장애가 발생하였습니다.[" + ResultCode + "-" + ResultMsg
								+ "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + company.getComp_tel() + ")에게 연락 부탁드립니다.");

//						request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
						return successRedirectPage;
					}
				}
			}
		}
		
		
		
		
		
		
}

