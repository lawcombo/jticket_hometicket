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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bluecom.common.controller.BaseController;
import com.bluecom.common.service.CommonService;
import com.bluecom.common.service.MailService;
import com.bluecom.common.service.MessageService;
import com.bluecom.common.util.CommUtil;
import com.bluecom.common.util.DateHelper;
import com.bluecom.common.util.ScriptUtils;
import com.bluecom.ticketing.domain.ApiProductRefundDTO;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSocialCancelDTO;
import com.bluecom.ticketing.domain.BookOpenVO;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.CouponVO;
import com.bluecom.ticketing.domain.EssentialDTO;
import com.bluecom.ticketing.domain.FinishTradeVO;
import com.bluecom.ticketing.domain.PaymentInfoDTO;
import com.bluecom.ticketing.domain.ProductDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO;
import com.bluecom.ticketing.domain.RefundHistoryVO;
import com.bluecom.ticketing.domain.RefundVO;
import com.bluecom.ticketing.domain.ReserverDTO;
import com.bluecom.ticketing.domain.SaleDTO;
import com.bluecom.ticketing.domain.SaleProductDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.ScheduleDTO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.ShopPaymentsaleVO;
import com.bluecom.ticketing.domain.VerificationKeyVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;
import com.bluecom.ticketing.domain.WebPaymentStatusDTO;
import com.bluecom.ticketing.domain.WebReservationKeyDTO;
import com.bluecom.ticketing.service.TicketingService;

import egovframework.rte.fdl.property.EgovPropertyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Controller
@RequestMapping("/ticketing")
public class TicketingController extends BaseController {

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
	
	// 날짜 체크 페이지 / 2021-09-23 / 조미근
	@GetMapping("/dateCheck")
	public String dateCheck(@ModelAttribute("essential")EssentialDTO essential,HttpServletResponse response,Model model) throws Exception{
		
		return "/ticketing/dateCheck";
	}
	
	// 예매(예약) 확인 본인인증페이지 / 2021-09-08 / 조미근
	@GetMapping("/checkTicket")
	public String processCheckTicket(@ModelAttribute("essential")EssentialDTO essential, 
			@ModelAttribute("reserver") ReserverDTO reserver, 
			HttpServletResponse response,Model model,RedirectAttributes redirect) throws Exception {

		if(!StringUtils.hasText(essential.getContent_mst_cd())) {
			ScriptUtils.alertAndBackPage(response, "예매를 확인하시고자 하는 시설의 코드가 없습니다.");
		}
		String shopCode = ticketingService.getShopCode(essential.getContent_mst_cd());
		VerificationKeyVO keys = ticketingService.getKeys(shopCode);
		
		if(keys == null 
				|| !StringUtils.hasText(keys.getIdentification_site_code()) 
				|| !StringUtils.hasText(keys.getIdentification_site_password())
				|| !StringUtils.hasText(keys.getPay_merchant_id())
				|| !StringUtils.hasText(keys.getPay_merchant_key())) {
			redirect.addFlashAttribute("msg", "인증를 위한 정보가 없습니다. 관리자에게 연락 바랍니다.");
			if(StringUtils.hasText(essential.getContent_mst_cd())) {
				return "redirect:/ticketing/checkTicket?content_mst_cd=" + essential.getContent_mst_cd();
			} else {
				return "redirect:/error";
			}
		}
		
		model.addAttribute("siteCode", keys.getIdentification_site_code());
		model.addAttribute("sitePassword", keys.getIdentification_site_password());
		
		String returnUrl = "";
		
		if(essential.getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			returnUrl = "/ticketing/checkTicket";
		}
		else if(essential.getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			returnUrl = "/ticketing/diamondbay/checkTicket";
		}
		
		
		//return "/ticketing/checkTicket";
		return returnUrl;
	}
	
	// 결제요청
	@PostMapping("/payRequest")	
	public String payRequest(@ModelAttribute("paymentInfo") @Valid PaymentInfoDTO info, HttpServletResponse response, Errors errors, Model model) throws Exception {
		System.out.println(info.getReserver().getPhone());
		
		log.info("::payRequest CALL");
		
		if(errors.hasErrors()) {
			ScriptUtils.alert(response, errors.getAllErrors().get(0).getDefaultMessage());
			return null;
		}
		
		// 결제 전에 정상적인 회차인지 확인필요 시간이 지난 후에 결제 진행하는 것을 방지하기 위함 / 2022-01-13
		if(StringUtils.hasText(info.getSchedule_code())) {
			ScheduleDTO schedule =  new ScheduleDTO();
			schedule.setShop_code(info.getProductGroup().getShop_code());
			schedule.setSchedule_code(info.getSchedule_code());
			schedule.setPlay_date_from(info.getPlay_date());
			schedule = ticketingService.getScheduleByScheduleCode(schedule);
			
			LocalDateTime now = LocalDateTime.now();
			String[] play = info.getPlay_date().split("-");
			int year = Integer.parseInt(play[0]);
			int month = Integer.parseInt(play[1]);
			int day = Integer.parseInt(play[2]);
			String[] startTime = schedule.getStart_time().split(":");
			int ttime = Integer.parseInt(startTime[0]);
			int mmin = Integer.parseInt(startTime[1]);
			LocalDateTime compare = LocalDateTime.of(year,month,day, ttime, mmin, 00,0000);
			if(now.isAfter(compare)) {
				ScriptUtils.alertAndClose(response, "지나간 회차의 상품입니다. 결제를 진행할 수 없습니다.");
				return null;
			}
		}
		
		// 상품 그룹 확인
		ProductGroupDTO dbProductGroup = ticketingService.getProductGroup(info.getProductGroup());
		if(!checkProductGroup(info.getProductGroup(), dbProductGroup)) {
			
			ScriptUtils.alertAndClose(response, "상품그룹정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
			return null;
		}
		info.setProductGroup(dbProductGroup);
		
		// 해당하는 그룹에 해당하는 상품이 정상적인지 확인
		
		List<ProductDTO> dbProducts = ticketingService.getSelectedProducts(info.getProducts());		
//		if (!checkProducts(info, dbProducts)) {
//			ScriptUtils.alertAndClose(response, "상품정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
//			return null;
//		}
		for(int i=0; i < info.getProducts().size(); i++) {
			ProductDTO product = info.getProducts().get(i);
			for(int j=0; j<dbProducts.size(); j++) {
				ProductDTO dbProduct = dbProducts.get(j);
				if(product.getShop_code().equals(dbProduct.getShop_code()) 
					&& product.getProduct_group_code().equals(dbProduct.getProduct_group_code()) 
					&& product.getProduct_code().equals(dbProduct.getProduct_code())) {
					dbProduct.setCount(product.getCount());
				}
			}
		}
		info.setProducts(dbProducts);
		
		int dbTotalCount = dbProducts.stream().mapToInt(ProductDTO::getCount).sum();
		if(dbTotalCount != info.getTotalCount()) {
			ScriptUtils.alertAndClose(response, "판매매수가 맞지 않습니다. 결제를 진행할 수 없습니다.");
			return null;
		}
		
		BigDecimal dbTotalFee = dbProducts.stream()
			.map(p -> p.getProduct_fee()
			.multiply(BigDecimal.valueOf(p.getCount())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		if(!dbTotalFee.equals(info.getTotalFee())) {
			ScriptUtils.alertAndClose(response, "판매금액정보가 맞지 않습니다. 결제를 진행할 수 없습니다.");
			return null;
		}
		
		if(StringUtils.hasText(info.getSchedule_code())) {
			ScheduleDTO schedule =  new ScheduleDTO();
			schedule.setShop_code(info.getProductGroup().getShop_code());
			schedule.setSchedule_code(info.getSchedule_code());
			schedule.setPlay_date_from(info.getPlay_date());
			schedule = ticketingService.getScheduleByScheduleCode(schedule);
			
			if(schedule == null) {
				ScriptUtils.alertAndClose(response, "해당 상품의 회차정보가 존재하지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}
			info.setSchedule(schedule);
		}
		
		info.setTotalCount(dbTotalCount);
		info.setTotalFee(dbTotalFee);
		
		
		if(info.getProductGroup().getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			//..
		}
		else if(info.getProductGroup().getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			info.getProductGroup().setProduct_group_kind("2");
		}
		else
		{
			//..
		}
		
		
		
		// 판매 금액이 0원이면  결제수단을 0원결제로 변경
		if("0".equals(info.getFee()) ) info.setPayMethod("0000");
		
		WebPaymentDTO webPayment = ticketingService.addWebPaymentInfo(info); //web_payment insert (get order_no)
		
		model.addAttribute("webPayment", webPayment);

		
		//콜백 jsp 화면 분기
		String callBackJsp = "";
		if(info.getProductGroup().getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			callBackJsp = "/ticketing/payRequest";
		}
		else if(info.getProductGroup().getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			callBackJsp = "/ticketing/diamondbay/payRequest";
		}
		else
		{
			callBackJsp = "/ticketing/payRequest";
		}
		
		
		return callBackJsp;
	}
	
	// 0원 결제 결과(미결제)
	@PostMapping("/pay0Result")	
	public String pay0Result(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		//payRequest에서 넘겨받은 값으로 pg 호출 및 api 호출 후 결제 결과를 나타냄
		
		int resultSuccess = 0;
		String resultOrderNum = "";
		String resultMessage = "";
		
		/*
		****************************************************************************************
		* <인증 결과 파라미터>
		****************************************************************************************
		*/
		String authResultCode 	= "0000"; 	// 인증결과 : 0000(성공)
		String authResultMsg 	= "쿠폰 등으로 인한 0원결제건"; 	// 인증결과 메시지
		String nextAppURL 		= ""; 		// 승인 요청 URL - 없음
		String txTid 			= ""; 			// 거래 ID - 없음
		String authToken 		= ""; 		// 인증 TOKEN - 없음
		String payMethod 		= (String)request.getParameter("PayMethod"); 		// 결제수단 - 0원결제일 경우이니 9000 고정
		String mid 				= (String)request.getParameter("MID"); 				// 상점 아이디
		String moid 			= (String)request.getParameter("Moid"); 			// 상점 주문번호
		String amt 				= (String)request.getParameter("Amt"); 				// 결제 금액
		String reqReserved 		= (String)request.getParameter("ReqReserved"); 		// 상점 예약필드 - 파라미터명은 있으나 값은 없음
		String netCancelURL 	= ""; 	// 망취소 요청 URL - 없음
		String coupon			= (String)request.getParameter("coupon"); //쿠폰 금액

//		String authSignature = (String)request.getParameter("Signature");			// Nicepay에서 내려준 응답값의 무결성 검증 Data - 없음
		String authSignature = "0paySignature";

		WebPaymentDTO webPayment = ticketingService.getWebPayment(moid);
		VerificationKeyVO keys = ticketingService.getKeys(webPayment.getShop_code());
		
		/*  
		****************************************************************************************
		* Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한 요소를 방지하기 위해 연동 시 사용하시기 바라며 
		* 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
		****************************************************************************************
		 */
		DataEncrypt sha256Enc 	= new DataEncrypt();
		String merchantKey 		= keys.getPay_merchant_key(); // 상점키
		
		//인증 응답 Signature = hex(sha256(AuthToken + MID + Amt + MerchantKey)
//		String authComparisonSignature = sha256Enc.encrypt(authToken + mid + amt + merchantKey);
		String authComparisonSignature = "0paySignature";
		
		
		/*
		****************************************************************************************
		* <승인 결과 파라미터 정의>
		* 샘플페이지에서는 승인 결과 파라미터 중 일부만 예시되어 있으며, 
		* 추가적으로 사용하실 파라미터는 연동메뉴얼을 참고하세요.
		****************************************************************************************
		*/
		String ResultCode 	= ""; String ResultMsg 	= ""; String PayMethod 	= "";
		String GoodsName 	= ""; String Amt 		= ""; String TID 		= ""; 
		String Signature = ""; String paySignature = ""; String AuthCode = ""; String AuthDate = "";
		
		/*
		****************************************************************************************
		* <인증 결과 성공시 승인 진행>
		****************************************************************************************
		*/
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
		pgResult.setCoupon(coupon);
		
		// 결제 승인 요청 기록 - 0원 결제이기에 따로 승인요청 하지 않아서, 0원결제임을 기록 후 바로 api 호출
		WebPaymentStatusDTO approvalRequestStatus = WebPaymentStatusDTO.builder()
			.status("고객-0원결제-결제인증성공")
			.message(authResultMsg)
			.orderNo(moid)
			.build();
		ticketingService.addWebPaymentStatus(approvalRequestStatus);
		
		// dummy pg_result 저장
		ticketingService.addWebPaymentPgResult(pgResult);
		
		// 0원 결제 이후 api 호출
		ApiResultVO apiResultVO = ticketingService.callTicketApi(pgResult); //pg결제 성공 후 api 호출
		
		// API 호출 성공 
		if(apiResultVO.getSuccess() == 1) {
			
			WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
					.status("고객-0원결제-Api호출-성공")
					.message("ApiCallResult: " + apiResultVO.toString())
					.orderNo(moid)
					.build();
			ticketingService.addWebPaymentStatus(apiCallStatus);
			
			ShopDetailVO shopDetail = ticketingService.getShopDetail(apiResultVO.getWebPayment().getShop_code());
		
			try {
				messageService.send(request, response, apiResultVO, pgResult, shopDetail);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			if(StringUtils.hasText(apiResultVO.getWebPayment().getReserverEmail())) {
			
				try {
					CompanyVO company = ticketingService.getCompany(apiResultVO.getWebPayment().getShop_code());
					mailService.sendReserve(request, apiResultVO, pgResult);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			resultSuccess = 1;
			
			resultMessage = "결제에 성공하였습니다.";
		}else { // API호출 실패
			
			WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
					.status("고객-0원결제-Api호출-실패")
					.message("ApiCallResult: " + apiResultVO.toString())
					.orderNo(moid)
					.build();
			ticketingService.addWebPaymentStatus(apiCallStatus);
			
			SaleVO searchSale = new SaleVO();
			searchSale.setShop_code(webPayment.getShop_code());
			searchSale.setOrder_num(moid);
			SaleVO saleVO = ticketingService.getSaleSsByOrderNum(searchSale);
			
			if(saleVO != null) {
				SaleDTO sale = new SaleDTO();
				sale.setSale_code(saleVO.getSale_code());
				
				List<SaleProductDTO> bookNoList = ticketingService.getBookNoBySaleCode(sale);
				
				callCancelApi(pgResult, bookNoList, apiResultVO.getWebPayment().getContent_mst_cd());
				
				// 0원결제일 경우 API 호출 실패 기록
				WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
					.status("고객-0원결제-취소Api호출")
					.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg)
					.orderNo(moid)
					.build();
				ticketingService.addWebPaymentStatus(cancelErrorStatus);
			}
			
			resultSuccess = 0;
			resultMessage = "결제 API 호출에 실패하였습니다." + apiResultVO.getErrMsg();
		}
		
		resultOrderNum = moid;
		model.addAttribute("success", resultSuccess);
		model.addAttribute("orderNo", resultOrderNum);
		model.addAttribute("message", resultMessage);
		
		return "/ticketing/payResult";
	}
	
	@PostMapping("/payResult")	
	public String payResult(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		//payRequest에서 넘겨받은 값으로 pg 호출 및 api 호출 후 결제 결과를 나타냄
		
		int resultSuccess = 0;
		String resultOrderNum = "";
		String resultMessage = "";
		
		/*
		****************************************************************************************
		* <인증 결과 파라미터>
		****************************************************************************************
		*/
		String authResultCode 	= (String)request.getParameter("AuthResultCode"); 	// 인증결과 : 0000(성공)
		String authResultMsg 	= (String)request.getParameter("AuthResultMsg"); 	// 인증결과 메시지
		String nextAppURL 		= (String)request.getParameter("NextAppURL"); 		// 승인 요청 URL
		String txTid 			= (String)request.getParameter("TxTid"); 			// 거래 ID
		String authToken 		= (String)request.getParameter("AuthToken"); 		// 인증 TOKEN
		String payMethod 		= (String)request.getParameter("PayMethod"); 		// 결제수단
		String mid 				= (String)request.getParameter("MID"); 				// 상점 아이디
		String moid 			= (String)request.getParameter("Moid"); 			// 상점 주문번호
		String amt 				= (String)request.getParameter("Amt"); 				// 결제 금액
		String reqReserved 		= (String)request.getParameter("ReqReserved"); 		// 상점 예약필드
		String netCancelURL 	= (String)request.getParameter("NetCancelURL"); 	// 망취소 요청 URL
		String coupon			= (String)request.getParameter("coupon"); //쿠폰 금액

		String authSignature = (String)request.getParameter("Signature");			// Nicepay에서 내려준 응답값의 무결성 검증 Data

		WebPaymentDTO webPayment = ticketingService.getWebPayment(moid);
		VerificationKeyVO keys = ticketingService.getKeys(webPayment.getShop_code());
		
		/*  
		****************************************************************************************
		* Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한 요소를 방지하기 위해 연동 시 사용하시기 바라며 
		* 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
		****************************************************************************************
		 */
		DataEncrypt sha256Enc 	= new DataEncrypt();
		String merchantKey 		= keys.getPay_merchant_key(); // 상점키
		
		//인증 응답 Signature = hex(sha256(AuthToken + MID + Amt + MerchantKey)
		String authComparisonSignature = sha256Enc.encrypt(authToken + mid + amt + merchantKey);
		
		
		/*
		****************************************************************************************
		* <승인 결과 파라미터 정의>
		* 샘플페이지에서는 승인 결과 파라미터 중 일부만 예시되어 있으며, 
		* 추가적으로 사용하실 파라미터는 연동메뉴얼을 참고하세요.
		****************************************************************************************
		*/
		String ResultCode 	= ""; String ResultMsg 	= ""; String PayMethod 	= "";
		String GoodsName 	= ""; String Amt 		= ""; String TID 		= ""; 
		String Signature = ""; String paySignature = ""; String AuthCode = ""; String AuthDate = "";
		
		/*
		****************************************************************************************
		* <인증 결과 성공시 승인 진행>
		****************************************************************************************
		*/
		String resultJsonStr = "";
		if(authResultCode.equals("0000") && authSignature.equals(authComparisonSignature)){
			
			// 결제 인증 완료 기록
			WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-결제인증성공")
				.message("AuthResultCode: " + authResultCode)
				.orderNo(moid)
				.build();
			ticketingService.addWebPaymentStatus(authenticationFinishedStatus);
			
			/*
			****************************************************************************************
			* <해쉬암호화> (수정하지 마세요)
			* SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다. 
			****************************************************************************************
			*/
			String ediDate			= getyyyyMMddHHmmss();
			String signData 		= sha256Enc.encrypt(authToken + mid + amt + ediDate + merchantKey);

			
			/*
			****************************************************************************************
			* <승인 요청>
			* 승인에 필요한 데이터 생성 후 server to server 통신을 통해 승인 처리 합니다.
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
			WebPaymentStatusDTO approvalRequestStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-결제승인요청")
				.message("")
				.orderNo(moid)
				.build();
			ticketingService.addWebPaymentStatus(approvalRequestStatus);
			
			resultJsonStr = connectToServer(requestData.toString(), nextAppURL, "utf-8");

			
			HashMap resultData = new HashMap();
			boolean paySuccess = false;
			
			if("9999".equals(resultJsonStr)){
				// 결제승인에러 기록
				WebPaymentStatusDTO approvalErrorStatus = WebPaymentStatusDTO.builder()
					.status("고객-예매-결제승인통신에러")
					.message(resultJsonStr)
					.orderNo(moid)
					.build();
				ticketingService.addWebPaymentStatus(approvalErrorStatus);
				
				/*
				*************************************************************************************
				* <망취소 요청>
				* 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
				*************************************************************************************
				*/
				StringBuffer netCancelData = new StringBuffer();
				requestData.append("&").append("NetCancel=").append("1");
				String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");
				
				HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
				ResultCode = (String)cancelResultData.get("ResultCode");
				ResultMsg = (String)cancelResultData.get("ResultMsg");
				Signature = (String)cancelResultData.get("Signature");
				String CancelAmt = (String)cancelResultData.get("CancelAmt");
				String CancelDate = (String)cancelResultData.get("CancelDate");
				String CancelTime = (String)cancelResultData.get("CancelTime");
				paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

				if(ResultCode == null) {
					// 결제 망취소 취소 기록
					WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
						.status("고객-예매-망취소-통신불가")
						.message("")
						.orderNo(moid)
						.build();
					ticketingService.addWebPaymentStatus(cancelErrorStatus);
					
				} else {
					// 결제 망취소 취소 기록
					WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
						.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-망취소-성공" : "고객-예매-망취소-실패")
						.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg)
						.orderNo(moid)
						.build();
					ticketingService.addWebPaymentStatus(cancelErrorStatus);
					
				}
				resultSuccess = 0;
				resultMessage = "결제 승인 통신에 실패하였습니다.";
				
				// 취소 성공 내용 PG_RESULT 에 기록하기
				WebPaymentPgResultDTO result = new WebPaymentPgResultDTO();
				result.setResult_code(ResultCode);
				result.setResult_msg(ResultMsg);
				result.setCancel_amt(amt);
				result.setCancel_time(CancelDate+CancelTime);
				result.setTid(TID);
				
				ticketingService.updateWebPaymentPgResult(result);
				
			}else{
				
				resultData = jsonStringToHashMap(resultJsonStr);
				ResultCode 	= (String)resultData.get("ResultCode");	// 결과코드 (정상 결과코드:3001)
				ResultMsg 	= (String)resultData.get("ResultMsg");	// 결과메시지
				PayMethod 	= (String)resultData.get("PayMethod");	// 결제수단
				GoodsName   = (String)resultData.get("GoodsName");	// 상품명
				Amt       	= (String)resultData.get("Amt");		// 결제 금액
				TID       	= (String)resultData.get("TID");		// 거래번호
				// Signature : Nicepay에서 내려준 응답값의 무결성 검증 Data
				// 가맹점에서 무결성을 검증하는 로직을 구현하여야 합니다.
				Signature = (String)resultData.get("Signature");
				paySignature = sha256Enc.encrypt(TID + mid + Amt + merchantKey);
				
				if(!Signature.equals(paySignature)) {
					// 결제 승인 결과 기록
					WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder()
						.status("고객-예매-결제승인-완료-변조확인")
						.message(PayMethod + ": [Received]" + Signature +"|[ShouldBe]" + paySignature)
						.orderNo(moid)
						.build();
					ticketingService.addWebPaymentStatus(approvalResultStatus);
					
					/*
					*************************************************************************************
					* <무결성 체크 실패 취소 요청>
					* 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
					*************************************************************************************
					*/
					StringBuffer netCancelData = new StringBuffer();
					requestData.append("&").append("NetCancel=").append("1");
					String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");
					
					HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
					ResultCode = (String)cancelResultData.get("ResultCode");
					ResultMsg = (String)cancelResultData.get("ResultMsg");
					Signature = (String)cancelResultData.get("Signature");
					String CancelAmt = (String)cancelResultData.get("CancelAmt");
					paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

					if(ResultCode == null) {
						// 결제 무결성체크실패 취소 통신불가 기록
						WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
							.status("고객-예매-변조취소-통신불가")
							.message("")
							.orderNo(moid)
							.build();
						ticketingService.addWebPaymentStatus(cancelErrorStatus);
						
					} else {
						// 결제 무결성체크실패 취소 기록
						WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
							.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-변조취소-망취소성공" : "고객-예매-변조취소-망취소실패")
							.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg)
							.orderNo(moid)
							.build();
						ticketingService.addWebPaymentStatus(cancelErrorStatus);
					}
					
					resultSuccess = 0;
					resultMessage = "결제 무결성 검증에 실패하였습니다.\n[" + ResultCode + "]"+  ResultMsg;
				} else {
					// 결제 승인 결과 기록
					WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder()
						.status("고객-예매-승인-성공")
						.message(PayMethod + ": " + ResultCode)
						.orderNo(moid)
						.build();
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
					pgResult.setCoupon(coupon);
					
					// 카드일 경우
					if(payMethod.equals("CARD")) {

						//  카드
						String cardNo = (String)resultData.get("CardNo"); 	// 카드번호
						String cardQuota = (String)resultData.get("CardQuota"); 	// 할부개월
						String cardCode = (String)resultData.get("CardCode"); 	// 결제 카드사 코드
						String cardName = (String)resultData.get("CardName"); 	// 결제 카드사명 
						String acquCardCode = (String)resultData.get("AcquCardCode"); 	// 매입 카드사 코드
						String acquCardName = (String)resultData.get("AcquCardName"); 	// 매입 카드사 이름
						String rcptType = (String)resultData.get("RcptType"); 	// 매입 카드사 이름
						
						pgResult.setCardNo(!StringUtils.hasText(cardNo) ? "" : cardNo);
						pgResult.setCardQuota(!StringUtils.hasText(cardQuota) ? "" : cardQuota);
						pgResult.setCardCode(!StringUtils.hasText(cardCode) ? "" : cardCode);
						pgResult.setAcquCardCode(!StringUtils.hasText(acquCardCode) ? "" : acquCardCode);
						pgResult.setAcquCardName(!StringUtils.hasText(acquCardName) ? "" : acquCardName);
						pgResult.setRcptType(!StringUtils.hasText(rcptType) ? "" : rcptType);
					} else if(payMethod.equals("BANK")) {
						String bankCode = (String)resultData.get("BankCode"); 	// 결제은행코드
						String bankName = (String)resultData.get("BankName"); 	// 결제은행명
						String rcptType = (String)resultData.get("RcptType"); 	// 매입 카드사 이름
						pgResult.setCardCode(!StringUtils.hasText(bankCode) ? "" : bankCode);
						pgResult.setCardName(!StringUtils.hasText(bankName) ? "" : bankName);
						pgResult.setRcptType(!StringUtils.hasText(rcptType) ? "" : rcptType);
					}
					AuthCode = (String)resultData.get("AuthCode");; 	// 승인번호
					AuthDate = (String)resultData.get("AuthDate");; 	// 승인날짜
					pgResult.setAuth_code(AuthCode);
					pgResult.setAuth_date(AuthDate);
					
					ticketingService.addWebPaymentPgResult(pgResult);
					
					/*
					*************************************************************************************
					* <결제 성공 여부 확인>
					*************************************************************************************
					*/
					if(PayMethod != null){
						if(PayMethod.equals("CARD")){
							if(ResultCode.equals("3001")) paySuccess = true; // 신용카드(정상 결과코드:3001)       	
						}else if(PayMethod.equals("BANK")){
							if(ResultCode.equals("4000")) paySuccess = true; // 계좌이체(정상 결과코드:4000)	
						}else if(PayMethod.equals("CELLPHONE")){
							if(ResultCode.equals("A000")) paySuccess = true; // 휴대폰(정상 결과코드:A000)	
						}else if(PayMethod.equals("VBANK")){
							if(ResultCode.equals("4100")) paySuccess = true; // 가상계좌(정상 결과코드:4100)
						}else if(PayMethod.equals("SSG_BANK")){
							if(ResultCode.equals("0000")) paySuccess = true; // SSG은행계좌(정상 결과코드:0000)
						}else if(PayMethod.equals("CMS_BANK")){
							if(ResultCode.equals("0000")) paySuccess = true; // 계좌간편결제(정상 결과코드:0000)
						}
					}
					
					if(paySuccess) { // 결제 승인 성공
						
						ApiResultVO apiResultVO = ticketingService.callTicketApi(pgResult); //pg결제 성공 후 api 호출
						
						// API 호출 성공 
						if(apiResultVO.getSuccess() == 1) {
							
							WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
									.status("고객-예매-Api호출-성공")
									.message("ApiCallResult: " + apiResultVO.toString())
									.orderNo(moid)
									.build();
							ticketingService.addWebPaymentStatus(apiCallStatus);
							
							ShopDetailVO shopDetail = ticketingService.getShopDetail(apiResultVO.getWebPayment().getShop_code());
						
							try {
								
								//알림톡 저장
								messageService.send(request, response, apiResultVO, pgResult, shopDetail);
								
							} catch(Exception ex) {
								ex.printStackTrace();
							}
							
							if(StringUtils.hasText(apiResultVO.getWebPayment().getReserverEmail())) {
							
								try {
									CompanyVO company = ticketingService.getCompany(apiResultVO.getWebPayment().getShop_code());
									mailService.sendReserve(request, apiResultVO, pgResult);
								} catch(Exception ex) {
									ex.printStackTrace();
								}
							}
							resultSuccess = 1;
							resultOrderNum = moid;
							resultMessage = "결제에 성공하였습니다.";
						}else { // API호출 실패
							
							WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
									.status("고객-예매-Api호출-실패")
									.message("ApiCallResult: " + apiResultVO.toString())
									.orderNo(moid)
									.build();
							ticketingService.addWebPaymentStatus(apiCallStatus);
							
							SaleVO searchSale = new SaleVO();
							searchSale.setShop_code(webPayment.getShop_code());
							searchSale.setOrder_num(moid);
							SaleVO saleVO = ticketingService.getSaleSsByOrderNum(searchSale);
							
							if(saleVO != null) {
								SaleDTO sale = new SaleDTO();
								sale.setSale_code(saleVO.getSale_code());
								
								List<SaleProductDTO> bookNoList = ticketingService.getBookNoBySaleCode(sale);
								
								callCancelApi(pgResult, bookNoList, apiResultVO.getWebPayment().getContent_mst_cd());
								
								/*
								 *  쿠폰 사용 취소 필요
								 *  1. mb_coupon_num update
								 *  2. bc_web_payment_coupon update
								 */
								// bc_paymentsale (select) 검증해서 쿠폰 있으면 아래 2개 실행, 없으면 skip
								int ret = ticketingService.selectCouponCheck(sale);
								if(ret > 0) {
									ticketingService.updateCouponCancelDate(sale.getCoupon());
									ticketingService.updateCouponUseYn(sale.getOrder_num());
								}
							}
							
							/*
							*************************************************************************************
							* <망취소 요청>
							* API호출 실패시 망취소 처리
							*************************************************************************************
							*/
							StringBuffer netCancelData = new StringBuffer();
							requestData.append("&").append("NetCancel=").append("1");
							String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL, "utf-8");
							
							HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
							ResultCode = (String)cancelResultData.get("ResultCode");
							ResultMsg = (String)cancelResultData.get("ResultMsg");
							Signature = (String)cancelResultData.get("Signature");
							String CancelAmt = (String)cancelResultData.get("CancelAmt");
							paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

							// 결제 망취소 취소 기록
							WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
								.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-예매-Api호출-실패-망취소-성공" : "고객-예매-Api호출-실패-망취소-실패")
								.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg)
								.orderNo(moid)
								.build();
							ticketingService.addWebPaymentStatus(cancelErrorStatus);
							
							resultSuccess = 0;
							resultMessage = "결제 API 호출에 실패하였습니다." + apiResultVO.getErrMsg();
						}
					}else {
						// 결제승인에러 기록
						WebPaymentStatusDTO approvalFailStatus = WebPaymentStatusDTO.builder()
							.status("고객-예매-결제승인-에러")
							.message(resultJsonStr)
							.orderNo(moid)
							.build();
						ticketingService.addWebPaymentStatus(approvalFailStatus);
						
						resultSuccess = 0;
						resultMessage = "결제에 실패 하였습니다.\n" + ResultMsg;
					}
				}
			}
		}else if(authSignature.equals(authComparisonSignature)){
			ResultCode 	= authResultCode; 	
			ResultMsg 	= authResultMsg;
			
			// 결제인증실패 기록
			WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-결제인증실패-변조확인")
				.message("AuthResultCode: " + authResultCode + " | AuthResultMsg" + authResultMsg)
				.orderNo(moid)
				.build();
			ticketingService.addWebPaymentStatus(authenticationFinishedStatus);

			resultSuccess = 0;
			resultMessage = "결제인증에 실패하였습니다.\n[" + ResultCode + "]"+  ResultMsg;
		}else{
			// 결제인증실패 기록
			WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-결제인증실패")
				.message("AuthResultCode: " + authResultCode + " | AuthComapreSignature" + authComparisonSignature)
				.orderNo(moid)
				.build();
			ticketingService.addWebPaymentStatus(authenticationFinishedStatus);
			System.out.println("인증 응답 Signature : " + authSignature);
			System.out.println("인증 생성 Signature : " + authComparisonSignature);

			resultSuccess = 0;
			resultMessage = "결제인증에 실패하였습니다.";
		}
		
		resultOrderNum = removeQuatations(resultOrderNum);
		resultMessage = removeQuatations(resultMessage);
		
		model.addAttribute("success", resultSuccess);
		model.addAttribute("orderNo", resultOrderNum);
		model.addAttribute("message", resultMessage);
		
		return "/ticketing/payResult";
	}


	private String removeQuatations(String text) {
		text = text.replaceAll("\"", "");
		text = text.replaceAll("\'", "");
		return text;
	}

	// 결제 취소 api
	private ApiResultVO callCancelApi(WebPaymentPgResultDTO pgResult, List<SaleProductDTO> bookNoList, String contentMstCd) throws Exception {
		List<ApiProductRefundDTO> productRefund = new ArrayList<>();
		
		for(int i=0; i<bookNoList.size(); i++) {
			productRefund.add(ApiProductRefundDTO.builder()
					.BOOK_NO(bookNoList.get(i).getBook_no())
					.REFUND_FEE(bookNoList.get(i).getRefund_fee().intValue())
					.build());
		}
		
		// api 취소 요청
		ApiSocialCancelDTO apiSocialCancel = ApiSocialCancelDTO.builder()
				.CONTENT_MST_CD(contentMstCd)
				.ONLINE_CHANNEL("1001")
				.ORDER_NUM(pgResult.getMoid())
				.USER_ID("WEBTICKET")
				.TERMINAL_CODE("WEBCANCEL")
				.TERMINAL_DATETIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.CANCEL_TYPE("A") 
				.SALE_PRODUCT_LIST(productRefund)
				.build();
		ApiResultVO apiCancelResult =  ticketingService.callCancelApi(apiSocialCancel);
		
		// 결제승인에러 기록				
		WebPaymentStatusDTO apiCancelStatus = WebPaymentStatusDTO.builder()
			.status(apiCancelResult.getSuccess() == 1 ? "고객-취소Api-성공" : "고객-취소Api-실패")
			.message("Success: " + apiCancelResult.getSuccess() + " | Message: " + apiCancelResult.getErrMsg())
			.orderNo(pgResult.getMoid())
			.build();
		ticketingService.addWebPaymentStatus(apiCancelStatus);
		
		return apiCancelResult;
	}
	
	// 결제 완료
	@GetMapping("/finish")
	public String finish(@ModelAttribute("essential") @Valid EssentialDTO essential, @RequestParam("orderNo") String orderNo, Model model) throws Exception {
		
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderNo);
		params.put("contentMstCd", essential.getContent_mst_cd());
		List<FinishTradeVO> trade = ticketingService.getFinishTrade(params);
		model.addAttribute("trade", trade);

		ShopDetailVO shopDetail = ticketingService.getShopDetailByContentMstCd(essential.getContent_mst_cd());
		model.addAttribute("shopDetail", shopDetail);
		
		return "/ticketing/finish";
	}
	@GetMapping("/diamondbay/finish")
	public String finishForDiamondbay(@ModelAttribute("essential") @Valid EssentialDTO essential, @RequestParam("orderNo") String orderNo, Model model) throws Exception {
		
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderNo);
		params.put("contentMstCd", essential.getContent_mst_cd());
		List<FinishTradeVO> trade = ticketingService.getFinishTrade(params);
		model.addAttribute("trade", trade);
		
		ShopDetailVO shopDetail = ticketingService.getShopDetailByContentMstCd(essential.getContent_mst_cd());
		model.addAttribute("shopDetail", shopDetail);
		
		return "/ticketing/diamondbay/finish";
	}
	
	/**
	 * 예매내역 존재 여부 확인 
	 * @param request
	 * @param saleDTO
	 * @param errors
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/checkTicketAjax")
	@ResponseBody
	public List<SaleDTO> checkTicketAjax(HttpServletRequest request, @Valid SaleDTO saleDTO, Errors errors, HttpServletResponse response, Model model) throws Exception {
		List<SaleDTO> saleDTOList = ticketingService.getCheckTicket(saleDTO);
		return saleDTOList;
	}
	
	/*
	 * 쿠폰 정보 가져오기
	 * 2021-10-12 / 조미근
	 */
	@PostMapping("/checkCoupon")
	@ResponseBody
	public CouponVO checkCoupon(HttpServletRequest request, HttpServletResponse response, Model model, CouponVO coupon) throws Exception{
		CouponVO couponVO = ticketingService.getCouponByCouponNum(coupon);
		
		if(!"".equals(couponVO.getCpn_use_product())) { // mb_coupon_product_group 확인 & product_group_code 비교
			boolean isUsable = false;
			List<CouponVO> group = ticketingService.getCouponGroup(couponVO);
			for(CouponVO vo : group) {
				if(coupon.getProduct_group_code().equals(vo.getProduct_group_code())) {
					isUsable = true;
					break;
				}
			}
			if(!isUsable) {
				couponVO.setMsg("해당 상품에 사용할 수 없는 쿠폰입니다.");
			}
		}
		
		if(!couponVO.getCpm_use_date().equals("")) {
			couponVO.setMsg("이미 사용한 쿠폰입니다.");
		}
		
		Date now = new Date();
		if(now.after(couponVO.getCpm_to_date())) {
			couponVO.setMsg("사용기간이 지난 쿠폰입니다.");
		}
		if(now.before(couponVO.getCpm_from_date())) {
			couponVO.setMsg("사용할 수 있는 기간이 아닙니다. 사용가능한 날짜를 확인해주세요.");
		}
		
		// %쿠폰
		if(couponVO.getCpn_sale_type().equals("1")) {
			int cpnSaleRate = 0;
			try {
				cpnSaleRate = Integer.parseInt(couponVO.getCpn_sale_cost());
			}catch(Exception e) {
				log.info("제주맥주 쿠폰 할인율 이상: " + couponVO);
				e.printStackTrace();
			}
			
			if(cpnSaleRate == 0) {
				couponVO.setMsg("할인율 이상으로 사용 할 수 없는 쿠폰입니다.");
			}
		}
		
		return couponVO;
	}

	// 결제 정보 입력 페이지
	@PostMapping("/insertReserver")
	public String process1InsertReserver(@ModelAttribute("essential")EssentialDTO essential, 
			@ModelAttribute("paymentInfo") PaymentInfoDTO paymentInfo, Errors errors, 
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirect, Model model) throws Exception {
		
		// 상품 그룹 확인
		ProductGroupDTO dbProductGroup = ticketingService.getProductGroups(essential);
		
		if (dbProductGroup == null || !checkProductGroup(paymentInfo.getProductGroup(), dbProductGroup)) {
			ScriptUtils.alertAndBackPage(response, "상품그룹정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
			return null;
		}
		paymentInfo.setProductGroup(dbProductGroup);

		// 상품 가져오기
		// 현재는 상품이 하나뿐이기 때문에 맨 위에꺼 하나만 사용
		List<ProductDTO> dbProducts = ticketingService.getProducts(paymentInfo.getProductGroup());
/*		List<ProductDTO> products = new ArrayList<>();
		ProductDTO product = dbProducts.get(0);
		product.setCount(paymentInfo.getProducts().get(0).getCount()); // 수량만 받은거 사용
		products.add(product);
		paymentInfo.setProducts(products); 
		
		paymentInfo.setTotalCount(products.stream().mapToInt(ProductDTO::getCount).sum());
		paymentInfo.setTotalFee(products.stream()
			.map(p -> p.getProduct_fee()
			.multiply(BigDecimal.valueOf(p.getCount())))
			.reduce(BigDecimal.ZERO, BigDecimal::add));*/
		
		// 0명인 상품리스트 삭제
		paymentInfo.getProducts().removeIf(p -> p.getCount() <= 0);
		
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

		
		List<ProductDTO> productsOrderedByPrice = new ArrayList<>();
		for(ProductDTO product : selectedProducts) {
			productsOrderedByPrice.add(product);
		}
		Collections.sort(productsOrderedByPrice, Collections.reverseOrder()); // 내림차순
		paymentInfo.setProductsOrderedByPrice(productsOrderedByPrice);
			
		// 총액, 총인원수
		paymentInfo.setTotalCount(selectedProducts.stream().mapToInt(ProductDTO::getCount).sum());
		paymentInfo
				.setTotalFee(selectedProducts.stream().map(p -> p.getProduct_fee().multiply(BigDecimal.valueOf(p.getCount())))
						.reduce(BigDecimal.ZERO, BigDecimal::add));
		
		// 회차
		if(StringUtils.hasText(paymentInfo.getSchedule_code())) {
			ScheduleDTO schedule =  new ScheduleDTO();
			schedule.setShop_code(paymentInfo.getProductGroup().getShop_code());
			schedule.setSchedule_code(paymentInfo.getSchedule_code());
			schedule.setPlay_date_from(paymentInfo.getPlay_date());
			schedule = ticketingService.getScheduleByScheduleCode(schedule);
			
			if(schedule == null) {
				ScriptUtils.alertAndClose(response, "해당 상품의 회차정보가 존재하지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}
			paymentInfo.setSchedule(schedule);
		}
		
		// 방문자 정보 가져오기
		ShopDetailVO shopDetail = ticketingService.getShopDetail(paymentInfo.getProductGroup().getShop_code());
		paymentInfo.setVisitorType(shopDetail.getPerson_type());
		// model.addAttribute("shopDetail", shopDetail);
		
		String content_mst_cd = essential.getContent_mst_cd(); 
		String product_group_code = essential.getProduct_group_code();
		
		// 이용약관 가져오기
		WebReservationKeyDTO reserveInfo = ticketingService.selectReserveInfo(dbProductGroup.getShop_code());
		
		if(reserveInfo == null || 
				!StringUtils.hasText(reserveInfo.getInfo_a()) ||
				!StringUtils.hasText(reserveInfo.getInfo_b()) ||
				!StringUtils.hasText(reserveInfo.getInfo_c()) ||
				!StringUtils.hasText(reserveInfo.getInfo_d())) {			
			redirect.addFlashAttribute("msg", "약관정보가 없습니다. 관리자에게 연락 바랍니다.");
			log.error("[ERROR]약관정보가 없습니다. 관리자에게 연락 바랍니다.");
			if(StringUtils.hasText(content_mst_cd) && StringUtils.hasText(product_group_code)) {
				return "redirect:/ticketing/selectSchedule?content_mst_cd=" + content_mst_cd + "&product_group_code=" + product_group_code;
			} else {
				return "redirect:/error";
			}
		}
		
		String content = reserveInfo.getInfo_a();
		reserveInfo.setInfo_a(StringEscapeUtils.unescapeXml(content));
		
		content = reserveInfo.getInfo_b();
		reserveInfo.setInfo_b(StringEscapeUtils.unescapeXml(content));
		
		content = reserveInfo.getInfo_c();
		reserveInfo.setInfo_c(StringEscapeUtils.unescapeXml(content));

		content = reserveInfo.getInfo_d();
		reserveInfo.setInfo_d(StringEscapeUtils.unescapeXml(content));

		model.addAttribute("reserveInfo", reserveInfo);
		
		// 본인인증키 가져오기
		VerificationKeyVO keys = ticketingService.getKeys(paymentInfo.getProductGroup().getShop_code());
		
		if(keys == null 
				|| !StringUtils.hasText(keys.getIdentification_site_code()) 
				|| !StringUtils.hasText(keys.getIdentification_site_password())
				|| !StringUtils.hasText(keys.getPay_merchant_id())
				|| !StringUtils.hasText(keys.getPay_merchant_key())) {
			redirect.addFlashAttribute("msg", "인증를 위한 정보가 없습니다. 관리자에게 연락 바랍니다.");
			
			log.info("[ERROR]인증를 위한 정보가 없습니다. 관리자에게 연락 바랍니다.");
			
			if(StringUtils.hasText(content_mst_cd) && StringUtils.hasText(product_group_code)) {
				return "redirect:/ticketing/selectSchedule?content_mst_cd=" + content_mst_cd + "&product_group_code=" + product_group_code;
			} else {
				return "redirect:/error";
			}
		}
		
		model.addAttribute("siteCode", keys.getIdentification_site_code());
		model.addAttribute("sitePassword", keys.getIdentification_site_password());
		
		return "/ticketing/insertReserverJeju";
	}

	/**
	 * 예매내역 페이지 / 1건인 경우
	 * @param saleDTO
	 * @param model
	 * @return
	 * @throws Exception
	 * 수정 / 2021-09-08 / 조미근
	 */
	@RequestMapping("/showTicketInfo")
	public String ShowTicketInfo(@ModelAttribute("essential")EssentialDTO essential, 
								HttpServletRequest request, SaleDTO saleDTO,
								/*SaleDTO2 changedSaleDTO,*/
								@ModelAttribute("message") String message, Model model) throws Exception {
		
		HttpSession session = request.getSession();
		saleDTO = (SaleDTO) session.getAttribute("saleDTO");
		
		List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductDTOList(saleDTO);
		
		/*if(saleProductDTOList.isEmpty())
			return "redirect:/ticketing/checkTicket?content_mst_cd="+essential.getContent_mst_cd();*/

		// content_mst_cd, product_group_code 기준으로 기본 정보 가져오기
		essential.setProduct_group_code(saleProductDTOList.get(0).getProduct_group_code());
		ProductGroupDTO productGroup = ticketingService.getProductGroups(essential);
		//bc_product 에서 fee & web_yn & schedule_yn 값 가져와서 뿌리기
		List<ProductDTO> products = ticketingService.getProducts(productGroup);
		
		// 방문자 정보 가져오기
		PaymentInfoDTO paymentInfo = new PaymentInfoDTO();
		ShopDetailVO shopDetail = ticketingService.getShopDetail(productGroup.getShop_code());
		paymentInfo.setVisitorType(shopDetail.getPerson_type());
		
		// 0번상품이 금액 상품
		WebPaymentDTO orgWebPayment =  ticketingService.getWebPayment(saleProductDTOList.get(0).getOrder_num());
		paymentInfo.setPayMethod(orgWebPayment.getPay_method());
		
		saleDTO.setShop_code(productGroup.getShop_code());
		
		// web_payment_idx 로 bc_web_payment_coupon 조회 후 금액 가져오기
		List<CouponVO> coupon = ticketingService.getCouponByWebPaymetIdx(saleProductDTOList.get(0).getOrder_num());
		
		//구매한 상품정보 가져오기
		saleDTO.setSale_code(saleProductDTOList.get(0).getSale_code());
		List<SaleProductDTO> purchase = ticketingService.selectPurchaseProduct(saleDTO);
		
		model.addAttribute("products", products);
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("paymentInfo", paymentInfo);
		model.addAttribute("dataList", saleProductDTOList);
		model.addAttribute("buyerInfo", saleDTO);
		model.addAttribute("coupon", coupon);
		model.addAttribute("purchase", purchase);
		
		//model.addAttribute("companyTel", ticketingService.getCompany(saleDTO.getShopCode()).getComp_tel());
		return "/ticketing/ShowTicketInfo";
	}
	
	
	/**
	 * 다이아몬드베이 예매조회 1건
	 * @param essential
	 * @param request
	 * @param saleDTO
	 * @param message
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/diamondbay/showTicketInfo")
	public String ShowTicketInfoOfDiamondBay(@ModelAttribute("essential")EssentialDTO essential, 
								HttpServletRequest request, SaleDTO saleDTO,
								/*SaleDTO2 changedSaleDTO,*/
								@ModelAttribute("message") String message, Model model) throws Exception {
		
		HttpSession session = request.getSession();
		saleDTO = (SaleDTO) session.getAttribute("saleDTO");
		
		List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductDTOList(saleDTO);
		
		/*if(saleProductDTOList.isEmpty())
			return "redirect:/ticketing/checkTicket?content_mst_cd="+essential.getContent_mst_cd();*/

		// content_mst_cd, product_group_code 기준으로 기본 정보 가져오기
		essential.setProduct_group_code(saleProductDTOList.get(0).getProduct_group_code());
		ProductGroupDTO productGroup = ticketingService.getProductGroups(essential);
		//bc_product 에서 fee & web_yn & schedule_yn 값 가져와서 뿌리기
		List<ProductDTO> products = ticketingService.getProducts(productGroup);
		
		// 방문자 정보 가져오기
		PaymentInfoDTO paymentInfo = new PaymentInfoDTO();
		ShopDetailVO shopDetail = ticketingService.getShopDetail(productGroup.getShop_code());
		paymentInfo.setVisitorType(shopDetail.getPerson_type());
		
		// 0번상품이 금액 상품
		WebPaymentDTO orgWebPayment =  ticketingService.getWebPayment(saleProductDTOList.get(0).getOrder_num());
		paymentInfo.setPayMethod(orgWebPayment.getPay_method());
		
		saleDTO.setShop_code(productGroup.getShop_code());
		
		// web_payment_idx 로 bc_web_payment_coupon 조회 후 금액 가져오기
		List<CouponVO> coupon = ticketingService.getCouponByWebPaymetIdx(saleProductDTOList.get(0).getOrder_num());
		
		//구매한 상품정보 가져오기
		saleDTO.setSale_code(saleProductDTOList.get(0).getSale_code());
		List<SaleProductDTO> purchase = ticketingService.selectPurchaseProduct(saleDTO);
		
		model.addAttribute("products", products);
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("paymentInfo", paymentInfo);
		model.addAttribute("dataList", saleProductDTOList);
		model.addAttribute("buyerInfo", saleDTO);
		model.addAttribute("coupon", coupon);
		model.addAttribute("purchase", purchase);
		
		//model.addAttribute("companyTel", ticketingService.getCompany(saleDTO.getShopCode()).getComp_tel());
		return "/ticketing/diamondbay/ShowTicketInfo";
	}
	
	
	
	@RequestMapping("/prevShowTicket")
	public String prevShowTicketList(@ModelAttribute("essential")EssentialDTO essential, SaleDTO saleDTO, 
										HttpServletRequest request, HttpServletResponse response, Model model,
										RedirectAttributes rttr) throws Exception{
		request.getSession().setAttribute("saleDTO", saleDTO);
		
		String redirectPage = null;
		
		if(essential.getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			if(saleDTO.getType().equals("0")) {
				redirectPage = "redirect:/ticketing/showTicketInfo?content_mst_cd=" + essential.getContent_mst_cd();
			}else {
				redirectPage = "redirect:/ticketing/showTicketInfoList?content_mst_cd=" + essential.getContent_mst_cd();
			}
		}
		else if(essential.getContent_mst_cd().toString().contains("DIAMONDBAY"))
		{
			if(saleDTO.getType().equals("0")) {
				redirectPage = "redirect:/ticketing/diamondbay/showTicketInfo?content_mst_cd=" + essential.getContent_mst_cd();
			}else {
				redirectPage = "redirect:/ticketing/diamondbay/showTicketInfoList?content_mst_cd=" + essential.getContent_mst_cd();
			}
		}
		
		
		
		//redirectPage = "redirect:/ticketing/showTicketInfoList?content_mst_cd=" + essential.getContent_mst_cd();
		//rttr.addFlashAttribute("saleDTO", saleDTO);
		//request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
		return redirectPage;
	}
	/*
	 * 예매내역 페이지 / n건인 경우
	 * 추가 / 2021-09-08 / 조미근
	 */
	@RequestMapping("/showTicketInfoList")
	public String ShowTicketInfoList(@ModelAttribute("essential")EssentialDTO essential, 
								SaleDTO saleDTO, HttpServletRequest request,
								@ModelAttribute("message") String message, Model model) throws Exception {
		HttpSession session = request.getSession();
		saleDTO = (SaleDTO) session.getAttribute("saleDTO");
		saleDTO.setType("1");
		List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductDTOList(saleDTO);

		model.addAttribute("dataList", saleProductDTOList);
		model.addAttribute("buyerInfo", saleDTO);
		//model.addAttribute("companyTel", ticketingService.getCompany(saleDTO.getShopCode()).getComp_tel());
		return "/ticketing/ShowTicketInfoList";
	}
	
	/**
	 * 다이아몬드베이 예매내역 조회 여러건
	 * @param essential
	 * @param saleDTO
	 * @param request
	 * @param message
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/diamondbay/showTicketInfoList")
	public String ShowTicketInfoListOfDiamondbay(@ModelAttribute("essential")EssentialDTO essential, 
			SaleDTO saleDTO, HttpServletRequest request,
			@ModelAttribute("message") String message, Model model) throws Exception {
		HttpSession session = request.getSession();
		saleDTO = (SaleDTO) session.getAttribute("saleDTO");
		saleDTO.setType("1");
		List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductDTOList(saleDTO);
		
		model.addAttribute("dataList", saleProductDTOList);
		model.addAttribute("buyerInfo", saleDTO);
		return "/ticketing/diamondbay/ShowTicketInfoList";
	}
	
	/*
	 * 티켓 예매 변경
	 */
	@PostMapping("/modifyTicket")
	public String modifyTicket(@Valid PaymentInfoDTO info, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes rttr, Model model) throws Exception {

		String redirectPage = "redirect:/ticketing/showTicketInfo?content_mst_cd=" + info.getProductGroup().getContent_mst_cd();
				
		//org_order_num 기준으로 webPayment 가져오기
		WebPaymentDTO orgWebPayment =  ticketingService.getWebPayment(info.getOrder_num());
		orgWebPayment.setOrg_order_no(info.getOrder_num());
		orgWebPayment.setOrder_no(null); //넘겨받은 order_no는 지워줌 받은 order_num은 (org_order_num)이기 때문에
		orgWebPayment.setPlay_date(info.getPlay_date());
		
		ScheduleDTO schedule = new ScheduleDTO();
		schedule.setShop_code(info.getProductGroup().getShop_code());
		schedule.setSchedule_code(info.getSchedule_code());
		schedule.setPlay_date_from(info.getPlay_date());
		schedule = ticketingService.getScheduleByScheduleCode(schedule);
		
		orgWebPayment.setStart_time(schedule.getStart_time());
		orgWebPayment.setEnd_time(schedule.getEnd_time());
		orgWebPayment.setSchedule_code(info.getSchedule_code());
		orgWebPayment.setPlay_sequence(schedule.getPlay_sequence());
		
		WebPaymentDTO webPayment = ticketingService.newWebPaymentInfo(orgWebPayment); //새 order_num 생성 (payments & products 저장)
		WebPaymentDTO newWebPayment = ticketingService.getWebPayment(webPayment.getOrder_no()); //새 order_num 기준으로 payment정보 가져오기
		
		//org_order_num으로 기존 pgResult값 가져오기
		WebPaymentPgResultDTO orgPgResult = ticketingService.getWebPaymentPgResult(info.getOrder_num());
		orgPgResult.setOrg_order_num(info.getOrder_num());
		orgPgResult.setWeb_payment_idx(newWebPayment.getIdx());
		orgPgResult.setMoid(newWebPayment.getOrder_no());
		
		//가져온 pgResult값으로 새 order_num으로 insert
		ticketingService.addWebPaymentPgResult(orgPgResult);
		
		//api 호출 기록하기
		WebPaymentStatusDTO status = WebPaymentStatusDTO.builder()
				.web_payment_idx(newWebPayment.getIdx())
				.status("예약변경 api 호출")
				.build();
		ticketingService.addWebPaymentStatus(status);
		
		//api 호출하기
		//새 order_num의 pgResult 가져오기
		WebPaymentPgResultDTO newPgResult = ticketingService.getWebPaymentPgResult(newWebPayment.getOrder_no());
		newPgResult.setOrg_order_num(info.getOrder_num()); //기존 order_num 넣어주기
		newPgResult.setCoupon(webPayment.getCoupon());
		ApiResultVO apiResultVO = ticketingService.callModifyTicketApi(newPgResult);
		
		
		//리턴값에 따른 alert창
		// API 호출 성공 
//		if(apiResultVO.getSuccess() == 0) {
		if(apiResultVO.getSuccess() == 1) {
			//api 호출 기록하기
			WebPaymentStatusDTO success = WebPaymentStatusDTO.builder()
					.web_payment_idx(newWebPayment.getIdx())
					.status("예약변경 성공")
					.build();
			ticketingService.addWebPaymentStatus(success);
			
			ShopDetailVO shopDetail = ticketingService.getShopDetail(webPayment.getShop_code());
			
			SaleVO searchSale = new SaleVO();
			searchSale.setShop_code(info.getProductGroup().getShop_code());
			searchSale.setOrder_num(newWebPayment.getOrder_no());
			SaleVO saleVO = ticketingService.getSaleSsByOrderNum(searchSale);
			
			SaleDTO sale = new SaleDTO();
			sale.setContent_mst_cd(info.getProductGroup().getContent_mst_cd());
			sale.setSale_code(saleVO.getSale_code());
			sale.setMember_name(info.getMember_name());
			sale.setMember_tel(info.getMember_tel());
			sale.setOrder_num(newWebPayment.getOrder_no());
			sale.setProduct_group_code(newWebPayment.getProduct_group());
			sale.setType("0");
			
			request.getSession().removeAttribute("saleDTO");
			request.getSession().setAttribute("saleDTO", sale); //세션에 담아서 넘기기
			
			// messageService.sendChange(request, saleVO, newWebPayment, newPgResult, shopDetail); 
			
			messageService.sendChange(request, response, apiResultVO, newPgResult, shopDetail); //알림톡 메시지 전송
			
			CompanyVO company = ticketingService.getCompany(apiResultVO.getWebPayment().getShop_code());
			mailService.sendChange(request, apiResultVO, newPgResult); // 변경 메일 전송 

			rttr.addFlashAttribute("message", "예매변경이 완료되었습니다.");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			
			return redirectPage;
			
		}else if(apiResultVO.getErrMsg().equals("11")){
			//api 호출 기록하기
			WebPaymentStatusDTO fail = WebPaymentStatusDTO.builder()
					.web_payment_idx(newWebPayment.getIdx())
					.status("예약 선점 후 기존 예매 취소 실패")
					.build();
			ticketingService.addWebPaymentStatus(fail);
			
			//예매 선점은 되었으나 기존 예매가 취소되지 않은 경우 취소 api 재호출
			SaleVO orgSearchSale = new SaleVO();
			orgSearchSale.setShop_code(webPayment.getShop_code());
			orgSearchSale.setOrder_num(info.getOrder_num());
			SaleVO orgSaleVO = ticketingService.getSaleSsByOrderNum(orgSearchSale);
			
			SaleDTO orgSale = new SaleDTO();
			orgSale.setSale_code(orgSaleVO.getSale_code());
			
			List<SaleProductDTO> bookNoList = ticketingService.getBookNoBySaleCode(orgSale);
			
			ApiResultVO apiResult = callCancelApi(orgPgResult, bookNoList, info.getProductGroup().getContent_mst_cd());
			
			// 새 정보 가져오기
			SaleVO searchSale = new SaleVO();
			searchSale.setShop_code(info.getProductGroup().getShop_code());
			searchSale.setOrder_num(newWebPayment.getOrder_no());
			SaleVO saleVO = ticketingService.getSaleSsByOrderNum(searchSale);
			
			SaleDTO sale = new SaleDTO();
			sale.setContent_mst_cd(info.getProductGroup().getContent_mst_cd());
			sale.setSale_code(saleVO.getSale_code());
			sale.setMember_name(info.getMember_name());
			sale.setMember_tel(info.getMember_tel());
			sale.setOrder_num(newWebPayment.getOrder_no());			
			sale.setType("0");
			
			if(apiResult.getSuccess() != 1) {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "예매변경에 실패하였습니다" + apiResult.getErrMsg());
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return redirectPage;
			}else {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "예매변경이 완료되었습니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return redirectPage;
			}
			
		}else { // API호출 실패
			//api 호출 기록하기
			WebPaymentStatusDTO fail = WebPaymentStatusDTO.builder()
					.web_payment_idx(newWebPayment.getIdx())
					.status("예매 변경 실패")
					.build();
			ticketingService.addWebPaymentStatus(fail);
			
			SaleVO searchSale = new SaleVO();
			searchSale.setShop_code(info.getProductGroup().getShop_code());
			searchSale.setOrder_num(info.getOrder_num());
			SaleVO saleVO = ticketingService.getSaleSsByOrderNum(searchSale);
			
			SaleDTO sale = new SaleDTO();
			sale.setSale_code(saleVO.getSale_code());
			sale.setShop_code(info.getProductGroup().getShop_code());
			sale.setMember_name(info.getMember_name());
			sale.setMember_tel(info.getMember_tel());
			sale.setOrder_num(info.getOrder_num());
			sale.setType("0");
			
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "예매변경에 실패하였습니다" + apiResultVO.getErrMsg());
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return redirectPage;
		}
	}
	
	/*
	 * 티켓 환불(취소)
	 */
	@PostMapping("/cancelTicket")
	public String cancelTicket(@ModelAttribute("buyerInfo") SaleDTO sale,  HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes rttr, Model model) throws Exception {
		String redirectPage = "redirect:/ticketing/showTicketInfo";
		
		Date now = new Date();
		Date today = DateHelper.getDateStart(now);
		
		List<RefundVO> refunds = ticketingService.getRefund(sale);
		
		if(refunds == null || refunds.size() == 0) {
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "환불 정보가 존재하지 않습니다.");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return redirectPage;		
		}
		
		for(RefundVO refund : refunds) {
			if(refund.getRefund_yn().equals("1")) {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "이미 환불처리된 티켓입니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return redirectPage;				
			}
			
			if(refund.getUsed_count() > 0) {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "이미 사용된 티켓입니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return redirectPage;
			}
			
			if(now.after(refund.getPlay_datetime())) {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "사용기간이 지난 티켓은 환불이 불가능합니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return redirectPage;
			}
		}

		WebPaymentDTO webPayment =  ticketingService.getWebPayment(sale.getOrder_num());
		WebPaymentPgResultDTO pgResult = ticketingService.getWebPaymentPgResult(sale.getOrder_num());
		if(pgResult == null) {
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "결제 되지 않은 티켓은 고객페이지에서의 취소가 불가능합니다. 관리자에게 연락해 주세요.");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return redirectPage;
		}
		WebPaymentStatusDTO cancelTicketStatus = WebPaymentStatusDTO.builder()
				.status("고객-전체취소-시작")
				.message("OrderNo: " + sale.getOrder_num() + " | Name:" + pgResult.getName() + " | Phone: " + pgResult.getPhone())
				.orderNo(sale.getOrder_num())
				.build();
		ticketingService.addWebPaymentStatus(cancelTicketStatus);

		String partialCancel="0";
		String cancelAmount=pgResult.getAmt();
		// 1. sale_code 기준으로 bc_sale_product 탐색 후 결과 리스트(book_no) 갖고오기
		List<SaleProductDTO> bookNoList = ticketingService.getBookNoBySaleCode(sale);
		if(sale.getType().equals("1")) {
			/*
			 *  부분환불 
			 *  환불 규정에 의해 전체금액의 50%를 환불, 전체인원이 취소되는 경우 (상황4)
			 *  1. 환불하고자 하는 book_no와 refund_fee를 담아 보내기
			 *  2. pgResult.getAmt() 는 환불하고자 하는 금액 (refund_fee)를 보내기
			 */
			
			partialCancel = "1";
			BigDecimal percent = new BigDecimal("0.5");
			for(int i=0; i<bookNoList.size(); i++) {
				BigDecimal productFee = new BigDecimal(String.valueOf(bookNoList.get(i).getProduct_fee()));
				bookNoList.get(i).setRefund_fee(productFee.multiply(percent));
			}
			BigDecimal originalAmount = new BigDecimal(String.valueOf(pgResult.getAmt()));
			int amount = originalAmount.multiply(percent).intValue();
			cancelAmount = String.valueOf(amount);
		}else if(sale.getType().equals("0")) {
			/*
			 *  전체환불
			 *  환불규정에 의해 전체금액 100%를 환불, 전체인원이 취소되는 경우 (상황2)
			 *  1. callCancelApi 호출시 모든 bc_sale_product의 book_no & refund_fee를 list에 담아 보내기
			 *  2. pgResult.getAmt() 는 기존 전체 금액 그대로 보내기 (수정 필요 X)
			 */
			partialCancel = "0";
			for(int i=0; i<bookNoList.size(); i++) {
				bookNoList.get(i).setRefund_fee(bookNoList.get(i).getProduct_fee());
			}
		}

		ApiResultVO apiResult = callCancelApi(pgResult, bookNoList, sale.getContent_mst_cd()); //API 호출
//		ApiResultVO apiResult = new ApiResultVO();
//		apiResult.setSuccess(1);
		
		WebPaymentStatusDTO cancelApiFailStatus = WebPaymentStatusDTO.builder()
				.status(apiResult.getSuccess() == 1 ? "고객-전체취소API호출-성공" : "고객-전체취소API호출-성공\"")
				.message("OrderNo: " + sale.getOrder_num() + " | Name:" + pgResult.getName() + " | Phone: " + pgResult.getPhone() + " | Message: " + apiResult.getErrMsg())
				.orderNo(sale.getOrder_num())
				.build();
		ticketingService.addWebPaymentStatus(cancelApiFailStatus);
		
		if(apiResult.getSuccess() != 1) {
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "예매취소에 실패하였습니다" + apiResult.getErrMsg());
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return redirectPage;
		}

		/*
		 *  쿠폰 사용 취소 필요
		 *  1. mb_coupon_num update
		 *  2. bc_web_payment_coupon update
		 */
		// bc_paymentsale (select) 검증해서 쿠폰 있으면 아래 2개 실행, 없으면 skip
		int ret = ticketingService.selectCouponCheck(sale);
		if(ret > 0) {
			ticketingService.updateCouponCancelDate(sale.getCoupon());
			ticketingService.updateCouponUseYn(sale.getOrder_num());
		}
		
		if("0000".equals(pgResult.getPay_method()) ) {
			// 0원 결제 - PG타지 않고 처리

			// 취소 성공 내용 PG_RESULT 에 기록하기
			WebPaymentPgResultDTO result = new WebPaymentPgResultDTO();
			result.setResult_code("2001");
			result.setResult_msg("취소 성공");
			result.setCancel_amt(cancelAmount);
			Date nowDate = new Date(System.currentTimeMillis());
			SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmss");
			result.setCancel_time(simple.format(nowDate) );
			result.setTid("");
			
			ticketingService.updateWebPaymentPgResult(result);
			
			SaleVO searchSale = new SaleVO();
			searchSale.setShop_code(webPayment.getShop_code());
			searchSale.setOrder_num(webPayment.getOrder_no());
			
			SaleVO saleVO = ticketingService.getSaleSsByOrderNum(searchSale);

			List<SaleProductDTO> saleProducts = ticketingService.getSaleProductDTO(sale);
			saleVO.setSaleProducts(saleProducts);
			
			ShopDetailVO shopDetail = ticketingService.getShopDetail(webPayment.getShop_code());
			
			messageService.sendRefund(request, saleVO, webPayment, pgResult, shopDetail); //알림톡 메시지 전송

			mailService.sendRefund(request, saleVO, webPayment, pgResult); //알림톡 메시지 전송
			sale.setType("1");
			List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductDTOList(sale);
			if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() > 1)
				ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "prevShowTicket?type=1&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code());
			else if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() == 1)
				ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "prevShowTicket?type=0&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code()+"&sale_code="+saleProductDTOList.get(0).getSale_code()+"&order_num="+saleProductDTOList.get(0).getOrder_num());
			else
				ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "checkTicket?content_mst_cd=" + sale.getContent_mst_cd());

			return null;
		}
		/*
		****************************************************************************************
		* <취소요청 파라미터>
		* 취소시 전달하는 파라미터입니다.
		* 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며, 
		* 추가 가능한 옵션 파라미터는 연동메뉴얼을 참고하세요.
		****************************************************************************************
		*/
		String tid 					= pgResult.getTid();	// 거래 ID
		String cancelAmt 			= cancelAmount;	// 취소금액
		String partialCancelCode 	= partialCancel; 	// 부분취소여부
		String mid 					= pgResult.getMid();	// 상점 ID
		String moid					= pgResult.getMoid();	// 주문번호
		String cancelMsg 			= "고객요청";	// 취소사유
		

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
		* <취소 요청>
		* 취소에 필요한 데이터 생성 후 server to server 통신을 통해 취소 처리 합니다.
		* 취소 사유(CancelMsg) 와 같이 한글 텍스트가 필요한 파라미터는 euc-kr encoding 처리가 필요합니다.
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
		String resultJsonStr = connectToServer(requestData.toString(), "https://webapi.nicepay.co.kr/webapi/cancel_process.jsp", "euc-kr");
//		String resultJsonStr = "{\"ResultCode\":\"2001\",\"ResultMsg\":\"정상 처리 되었습니다.\",\"ErrorCD\":\"0000\",\"ErrorMsg\":\"성공\",\"CancelAmt\":\"000000000900\",\"MID\":\"nicepay00m\",\"Moid\":\"001021080900010\",\"PayMethod\":\"CELLPHONE\",\"TID\":\"nicepay00m05012108091827142647\",\"CancelDate\":\"20210809\",\"CancelTime\":\"185659\",\"CancelNum\":\"00000000\",\"RemainAmt\":\"000000000000\",\"Signature\":\"594e9139df3881a8a0ce5155d99ab908430682233f5385a204c722e56c7b4f0d\",\"MallReserved\":\"\"}";
		
		/*
		****************************************************************************************
		* <취소 결과 파라미터 정의>
		* 샘플페이지에서는 취소 결과 파라미터 중 일부만 예시되어 있으며, 
		* 추가적으로 사용하실 파라미터는 연동메뉴얼을 참고하세요.
		****************************************************************************************
		*/
		String ResultCode 	= ""; String ResultMsg 	= ""; String CancelAmt 	= "";
		String CancelDate 	= ""; String CancelTime = ""; String TID 		= ""; 
		
		
		if("9999".equals(resultJsonStr)){
			ResultCode 	= "9999";
			ResultMsg	= "통신실패";

			// 결제 취소 취소 기록
			WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
				.status("고객-전체취소-PG취소-9999통신실패")
				.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg)
				.orderNo(pgResult.getMoid())
				.build();
			ticketingService.addWebPaymentStatus(cancelErrorStatus);
			
			CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());
			
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "예약취소를 위한 통신에 장애가 발생하였습니다.[" +  ResultCode + 
					"-" + ResultMsg + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
					company.getComp_tel() + ")에게 연락 부탁드립니다.");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return redirectPage;
		}else{
			HashMap resultData = jsonStringToHashMap(resultJsonStr);
			ResultCode 	= (String)resultData.get("ResultCode");	// 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)
			ResultMsg 	= (String)resultData.get("ResultMsg");	// 결과메시지
			CancelAmt 	= (String)resultData.get("CancelAmt");	// 취소금액
			CancelDate 	= (String)resultData.get("CancelDate");	// 취소일
			CancelTime 	= (String)resultData.get("CancelTime");	// 취소시간
			TID 		= (String)resultData.get("TID");		// 거래아이디 TID
			
			// 결제 취소 취소 기록
			WebPaymentStatusDTO cancelErrorStatus = WebPaymentStatusDTO.builder()
				.status(ResultCode.equals("2001") || ResultCode.equals("2211") ? "고객-PG취소-성공" : "고객-PG취소-실패")
				.message("ResultCode: " + ResultCode + " | ResultMsg" + ResultMsg)
				.orderNo(pgResult.getMoid())
				.build();
			ticketingService.addWebPaymentStatus(cancelErrorStatus);
						
			/*  
			****************************************************************************************
			* Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한 요소를 방지하기 위해 연동 시 사용하시기 바라며 
			* 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
			****************************************************************************************
			 */
			String Signature = ""; String paySignature = "";
			
			Signature       	= (String)resultData.get("Signature");
			paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);
			
			if(!Signature.equals(paySignature)) {
				// 결제 취소 결과 변조 확인
				WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder()
					.status("고객-전체취소-PG취소-변조확인")
					.message("[Received]" + Signature +"|[ShouldBe]" + paySignature)
					.orderNo(moid)
					.build();
				ticketingService.addWebPaymentStatus(approvalResultStatus);
				
				CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());
				
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "예약취소 데이터의 변조를 확인하였습니다.[관리자(" + company.getComp_tel() + ")에게 연락 부탁드립니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return redirectPage;
			} else {
				if(ResultCode.equals("2001") || ResultCode.equals("2211")) {
					
					// 취소 성공 내용 PG_RESULT 에 기록하기
					WebPaymentPgResultDTO result = new WebPaymentPgResultDTO();
					result.setResult_code(ResultCode);
					result.setResult_msg(ResultMsg);
					result.setCancel_amt(cancelAmount);
					result.setCancel_time(CancelDate+CancelTime);
					result.setTid(TID);
					
					ticketingService.updateWebPaymentPgResult(result);
					
					SaleVO searchSale = new SaleVO();
					searchSale.setShop_code(webPayment.getShop_code());
					searchSale.setOrder_num(webPayment.getOrder_no());
					
					SaleVO saleVO = ticketingService.getSaleSsByOrderNum(searchSale);

					List<SaleProductDTO> saleProducts = ticketingService.getSaleProductDTO(sale);
					saleVO.setSaleProducts(saleProducts);
					

					//bc_paymentsale에 마이너스금액으로 기록
					ShopPaymentsaleVO paymentsale = new ShopPaymentsaleVO();
					paymentsale.setShop_code(sale.getShop_code());
					paymentsale.setSale_code(sale.getSale_code());
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
					historyVO.setShop_code(sale.getShop_code());
					historyVO.setSale_code(sale.getSale_code());
					historyVO.setCount(bookNoList.size());
					historyVO.setFee(refundFee.toString());
					String bookNo="";
					if(bookNoList != null && !bookNoList.isEmpty()) {
						
						for(int i=0; i<bookNoList.size(); i++) {
							if(i !=( bookNoList.size()-1)) {
								bookNo += bookNoList.get(i).getBook_no()+",";
							}else {
								bookNo += bookNoList.get(i).getBook_no();
							}
						}
					}
					historyVO.setBook_no(bookNo);
					historyVO.setWork_id("WEBRESERVE");
					ticketingService.insertRefundHistory(historyVO);
					
					
					ShopDetailVO shopDetail = ticketingService.getShopDetail(webPayment.getShop_code());
					
					//알림톡 메시지 전송
					messageService.sendRefund(request, saleVO, webPayment, pgResult, shopDetail); 

					//알림톡 메시지 전송
					mailService.sendRefund(request, saleVO, webPayment, pgResult); 
					
					sale.setType("1");
					List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductDTOList(sale);
					if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() > 1)
						ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "prevShowTicket?type=1&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code());
					else if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() == 1)
						ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "prevShowTicket?type=0&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code()+"&sale_code="+saleProductDTOList.get(0).getSale_code()+"&order_num="+saleProductDTOList.get(0).getOrder_num());
					else
						ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "checkTicket?content_mst_cd=" + sale.getContent_mst_cd());

					return null;
				}else {

					CompanyVO company = ticketingService.getCompany(webPayment.getShop_code());
					
					sale.setType("1");
					List<SaleProductDTO> saleProductDTOList = ticketingService.getSaleProductDTOList(sale);
					if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() > 1)
						ScriptUtils.alertAndMovePage(response, "예약취소에 장애가 발생하였습니다.[" +  ResultCode + 
								"-" + ResultMsg + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
								company.getComp_tel() + ")에게 연락 부탁드립니다.", "prevShowTicket?type=1&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code());
					else if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() == 1)
						ScriptUtils.alertAndMovePage(response, "예약취소에 장애가 발생하였습니다.[" +  ResultCode + 
								"-" + ResultMsg + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
								company.getComp_tel() + ")에게 연락 부탁드립니다.", "prevShowTicket?type=0&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code()+"&sale_code="+saleProductDTOList.get(0).getSale_code()+"&order_num="+saleProductDTOList.get(0).getOrder_num());
					else
						ScriptUtils.alertAndMovePage(response, "예약취소에 장애가 발생하였습니다.[" +  ResultCode + 
								"-" + ResultMsg + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
								company.getComp_tel() + ")에게 연락 부탁드립니다.", "checkTicket?content_mst_cd=" + sale.getContent_mst_cd());

					return null;
					
					/*rttr.addFlashAttribute("buyerInfo", sale);
					rttr.addFlashAttribute("message", "예약취소에 장애가 발생하였습니다.[" +  ResultCode + 
							"-" + ResultMsg + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
							company.getComp_tel() + ")에게 연락 부탁드립니다.");
					
					request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
					return redirectPage;				*/
				}
			}			
		}
	}
	
	// 상품 안내 페이지
	@GetMapping("/programInfos")
	public String programInfos(@ModelAttribute("essential") @Valid EssentialDTO essential, Errors errors, HttpServletResponse response, Model model) throws Exception {
	
		if(errors.hasErrors()) {
			validationLog(errors);
			
			ScriptUtils.alertAndBackPage(response, "업체 정보가 또는 상품정보가 올바르지 않습니다.");
			return null;

		}
		
		if(essential.getProduct_group_code().equals("101")) {
			return "/ticketing/programInfos101";
		} else if(essential.getProduct_group_code().equals("102")) {
			return "/ticketing/programInfos102";
		} else if(essential.getProduct_group_code().equals("103")) {
			return "/ticketing/programInfos103";
		} else if(essential.getProduct_group_code().equals("104")) {
			return "/ticketing/programInfos104";
		} else if(essential.getProduct_group_code().equals("105")) { // 2022-02-07 체험추가
			return "/ticketing/programInfos105";
		}
		else {			
			ScriptUtils.alertAndBackPage(response, "상품정보가 올바르지 않습니다.");
			return null;
		}
	}
	
	
	
	// 수정 / 2021-09-06 / 조미근
	// 투어 또는 체험 선택 후 티켓 선택 화면으로 바로 이동 내용은 회차 선택만 필요.
	// parameters : content_mst_cd, product_group_code
	@GetMapping("/selectSchedule")
	public String process1SelectTicketGroup(@ModelAttribute("essential") @Valid EssentialDTO essential, Errors errors, HttpServletResponse response, Model model) throws Exception {
	
		log.info("::: Ticketing JEJUBEER SelectSchedule START");
		
		if(errors.hasErrors()) {
			validationLog(errors);
			
			ScriptUtils.alertAndBackPage(response, "상품그룹정보가 올바르지 않습니다. 결제페이지에 진입 할 수 없습니다.");
			return null;

		}		
		// content_mst_cd, product_group_code 기준으로 기본 정보 가져오기
		ProductGroupDTO productGroup = ticketingService.getProductGroups(essential);
		//bc_product 에서 fee & web_yn & schedule_yn 값 가져와서 뿌리기

		model.addAttribute("productGroup", productGroup);

		List<ProductDTO> products = ticketingService.getProducts(productGroup);
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
		
		return "/ticketing/selectTicketJeju";
	}
	
	private boolean checkProductGroup(ProductGroupDTO productGroup, ProductGroupDTO dbProductGroup) {
		
		return productGroup.getShop_code().equals(dbProductGroup.getShop_code())
				&& productGroup.getContent_mst_cd().equals(dbProductGroup.getContent_mst_cd())
				&& productGroup.getProduct_group_code().equals(dbProductGroup.getProduct_group_code());
	}

	private ProductGroupDTO cloneProductGroup(ProductGroupDTO productGroup) {
		ProductGroupDTO newProductGroup = new ProductGroupDTO();
		newProductGroup.setShop_code(productGroup.getShop_code());
		newProductGroup.setProduct_group_code(productGroup.getProduct_group_code());
		newProductGroup.setContent_mst_cd(productGroup.getContent_mst_cd());
		newProductGroup.setProduct_group_kind(newProductGroup.getProduct_group_kind());
		return newProductGroup;
	}
	
	private boolean checkProducts(PaymentInfoDTO paymentInfo, List<ProductDTO> dbProducts) {
		List<ProductDTO> products = paymentInfo.getProducts();
		
		if(products.size() != dbProducts.size()) {
			return false;
		}
//		Collectors.sort(products, Comparator.comparing(ProductDTO::get))
		
		for(int i=0; i < products.size(); i++) {
			ProductDTO product = products.get(i);
			ProductDTO dbProduct = dbProducts.get(i);
		
			if(!product.getShop_code().equals(dbProduct.getShop_code())
				|| !product.getProduct_group_code().equals(dbProduct.getProduct_group_code())
				|| !product.getProduct_code().equals(dbProduct.getProduct_code())
					) {
				return false;
			}
			
			dbProduct.setCount(product.getCount());
		}
		
		return true;
		
	}
	
	/**
	 *  샵정보를 이용해서 입장가능한 날짜 정보를 가져옴 //제주 사용 
	 */		
	@PostMapping("/ajaxMonthData")
	@ResponseBody
	public String ajaxMonthData(HttpServletRequest request, Model model) throws Exception {
		String year = CommUtil.RequestParam(request, "year");
		String month = CommUtil.RequestParam(request, "month");
		String date = CommUtil.RequestParam(request, "date");
		String shop_code = CommUtil.RequestParam(request, "shop_code");
		String contentMstCd = CommUtil.RequestParam(request, "content_mst_cd");
		String product_group_code = request.getParameter("product_group_code");
		//String shopCode = CommUtil.RequestParam(request, "shopCode");
        
		BookOpenVO bookVO = new BookOpenVO();
		bookVO.setShop_code(shop_code);
		bookVO.setContent_mst_cd(contentMstCd);
		bookVO.setDate(date);
		bookVO.setProduct_group_code(product_group_code);
		List list = (List<?>) ticketingService.getBookOpenMonth(bookVO);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		return json;
	}
	
	/**
	 *  샵코드와 그룹코드를 이용해 스케쥴정보 가져옴 //jeju 사용 / 2021-09-06 / 조미근
	 * @return 
	 */	
	@PostMapping("/selectScheduleAjax")
	@ResponseBody
	public Map<String, List<?>> process2SelectScheduleAjax(HttpServletRequest request, ScheduleDTO scheduleDTO, Errors errors, HttpServletResponse response, Model model) throws Exception {
		List<ScheduleDTO> scheduleDTOList = ticketingService.getSchedule(scheduleDTO);
		Map<String, List<?>> map = new HashMap<>();
		map.put("schedule", scheduleDTOList);
		
		ProductGroupDTO productGroup = new ProductGroupDTO();
		productGroup.setContent_mst_cd(scheduleDTO.getContentMstCd());
		productGroup.setProduct_group_code(scheduleDTO.getProduct_group_code());
		List<ProductDTO> products = ticketingService.getProcess2Products(productGroup);
		
		// 사용 기간을 분리
		products = selectValidPeriod(scheduleDTO, products);
		
		map.put("products", products);
		
		return map;
	}
	
	private List<ProductDTO> selectValidPeriod(ScheduleDTO scheduleDTO, List<ProductDTO> products) throws Exception{
		
		// remark 컬럼에서 최대 마감 기한을 가져온다
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Pattern regxDate = Pattern.compile("\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])");
		Matcher m = null;
		String remark;
		String last_sale_date = "";
		
		Date lastDateOfSingleProduct = null;
		for(ProductDTO product : products) {
			remark = product.getRemark();
			
			Date lastUsableDate = null;
			
			try {
				if(StringUtils.hasText(product.getRemark())) {
					if(regxDate.matcher(remark ).find() ) {
						m = regxDate.matcher(remark );
						m.find();
						last_sale_date = m.group(0);
						lastUsableDate = dateFormat.parse(last_sale_date );
						
						last_sale_date = "";
					} else {
						lastUsableDate = null;
					}
				}
			}catch(Exception ex) {
				lastUsableDate = null;
			}
			
			if(lastUsableDate == null) {
				continue;
			}
			
			if(lastDateOfSingleProduct == null) {
				lastDateOfSingleProduct = lastUsableDate;
				continue;
			}
			
			if(lastDateOfSingleProduct.before(lastUsableDate)) {
				lastDateOfSingleProduct = lastUsableDate;
			}
		}

		Date playDate = dateFormat.parse(scheduleDTO.getPlay_date());
		ProductDTO product;
		for(int i=products.size()-1; i>=0; i-- ) {
			product = products.get(i);
			remark = product.getRemark();
			
			Date lastUsableDate;
			
			try {
				lastUsableDate = dateFormat.parse(product.getRemark());
			}catch(Exception ex) {
				lastUsableDate = null;
			}
			// 조회한 날짜가 최대 마감 기한 이전이라면 날짜가 없는것 삭제			
			if(lastDateOfSingleProduct != null && (playDate.before(lastDateOfSingleProduct) || playDate.equals(lastDateOfSingleProduct) ) ) 
			{
				if(lastUsableDate == null) {
					products.remove(i);
				}
			} else { // 조회한 날짜가 최대 마감 기한 이후라면 날짜가 있는것 삭제
				if(lastUsableDate != null) {
					products.remove(i);
				}
			}
		}
		
		return products;
	}

	/**
	 *  샵코드와 그룹코드를 이용해 프로덕트에 권종 정보를 가져옴 
	 */	
/*	@PostMapping("/process2/selectProductAjax")
	@ResponseBody
	public List<ProductDTO> process2SelectProductAjax(HttpServletRequest request, ProductGroupDTO productGroup, Errors errors, HttpServletResponse response, Model model) throws Exception {	
		List<ProductDTO> products = ticketingService.getProcess2Products(productGroup);
		return products;
	}*/
	
	/*********** Process2 : E ***********/
	
	/*********** Common Methods ***********/
	/**
	 * Bean Validation에 실패했을 때, 에러메시지를 내보내기 위한 Exception Handler
	 */
	@ExceptionHandler({BindException.class})
	public ResponseEntity<String> paramViolationError(BindException ex) {
		log.error(ex.getMessage());
		
		String message = "<script>alert('" + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage() + "');window.close();</script>";
		
		HttpHeaders responseHeaders = new HttpHeaders(); responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(message, responseHeaders, HttpStatus.BAD_REQUEST);

		//return ResponseEntity.badRequest().body(message);
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
	
	@GetMapping("/termsOfUse")
	public String termsOfUse(@RequestParam("contentMstCd") String contentMstCd, 
			HttpServletResponse response,Model model) throws Exception {

		if(!StringUtils.hasText(contentMstCd)) {
			ScriptUtils.alertAndBackPage(response, "예매를 확인하시고자 하는 시설의 코드가 없습니다.");
			return null;
		} else {
			CompanyVO company = commonService.selectCompanyByContentMstCd(contentMstCd);
			
			if(company == null) {
				ScriptUtils.alertAndBackPage(response, "예매를 확인하시고자 하는 시설의 코드가 없습니다.");
				return null;
			}
			
			String company_code = company.getCompany_code();
			WebReservationKeyDTO reserveInfo = ticketingService.selectReserveInfoByCompanyCode(company_code);
			
			String content = "";
			
			content = reserveInfo.getInfo_c();
			reserveInfo.setInfo_c(StringEscapeUtils.unescapeXml(content));
			
			model.addAttribute("reserveInfo", reserveInfo);
		}
		
		return "/ticketing/termsOfUse";
	}
	
	@GetMapping("/refundPolicy")
	public String refundPolicy(@RequestParam("contentMstCd") String contentMstCd, 
			HttpServletResponse response,Model model) throws Exception {

		if(!StringUtils.hasText(contentMstCd)) {
			ScriptUtils.alertAndBackPage(response, "예매를 확인하시고자 하는 시설의 코드가 없습니다.");
		} else {
			CompanyVO company = commonService.selectCompanyByContentMstCd(contentMstCd);
			
			if(company == null) {
				ScriptUtils.alertAndBackPage(response, "예매를 확인하시고자 하는 시설의 코드가 없습니다.");
				return null;
			}
			
			String company_code = company.getCompany_code();
			WebReservationKeyDTO reserveInfo = ticketingService.selectReserveInfoByCompanyCode(company_code);
			
			String content = "";
			
			content = reserveInfo.getInfo_d();
			reserveInfo.setInfo_d(StringEscapeUtils.unescapeXml(content));
			
			model.addAttribute("reserveInfo", reserveInfo);
		}
		
		return "/ticketing/refundPolicy";
	}	
	
	
	
	//=============================================================다이아몬드 베이 START======================================================================
	
	/**
	 * 다이아몬드베이 홈티켓
	 * @param essential
	 * @param errors
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/diamondbay")
	public String diamondbay(@ModelAttribute("essential") @Valid EssentialDTO essential, Errors errors, HttpServletResponse response, Model model) throws Exception {
		
		return "/ticketing/diamondbay/selectTicketItem";
	}
	
	
	@GetMapping("/diamondbay/schedule")
	public String diamondbaySchedule(@ModelAttribute("essential") @Valid EssentialDTO essential, Errors errors, HttpServletResponse response, Model model) throws Exception {
		
		log.info("::: Ticketing DIAMOND BAY SelectSchedule START");
		
		if(errors.hasErrors()) {
			validationLog(errors);
			
			ScriptUtils.alertAndBackPage(response, "상품그룹정보가 올바르지 않습니다. 결제페이지에 진입 할 수 없습니다.");
			return null;
			
		}		
		// content_mst_cd, product_group_code 기준으로 기본 정보 가져오기
		ProductGroupDTO productGroup = ticketingService.getProductGroups(essential);
		//bc_product 에서 fee & web_yn & schedule_yn 값 가져와서 뿌리기
		
		model.addAttribute("productGroup", productGroup);
		
		List<ProductDTO> products = ticketingService.getProducts(productGroup);
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
		
		return "/ticketing/diamondbay/selectSchedule";
	}
	
	

	/**
	 * 상품 선택후 예약 번튼 클릭시 - 예약정보입력 페이지로 이동하는 과정
	 * @param essential
	 * @param paymentInfo
	 * @param errors
	 * @param request
	 * @param response
	 * @param redirect
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/diamondbay/insertReserver")
	public String process1InsertReserverOfDiamondBay(@ModelAttribute("essential")EssentialDTO essential, 
			@ModelAttribute("paymentInfo") PaymentInfoDTO paymentInfo, Errors errors, 
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirect, Model model) throws Exception {
		
		// 상품 그룹 확인
		ProductGroupDTO dbProductGroup = ticketingService.getProductGroups(essential);
		
		if (dbProductGroup == null || !checkProductGroup(paymentInfo.getProductGroup(), dbProductGroup)) {
			ScriptUtils.alertAndBackPage(response, "상품그룹정보가 올바르지 않습니다. 결제를 진행할 수 없습니다.");
			return null;
		}
		paymentInfo.setProductGroup(dbProductGroup);

		// 상품 가져오기
		// 현재는 상품이 하나뿐이기 때문에 맨 위에꺼 하나만 사용
		List<ProductDTO> dbProducts = ticketingService.getProducts(paymentInfo.getProductGroup());
/*		List<ProductDTO> products = new ArrayList<>();
		ProductDTO product = dbProducts.get(0);
		product.setCount(paymentInfo.getProducts().get(0).getCount()); // 수량만 받은거 사용
		products.add(product);
		paymentInfo.setProducts(products); 
		
		paymentInfo.setTotalCount(products.stream().mapToInt(ProductDTO::getCount).sum());
		paymentInfo.setTotalFee(products.stream()
			.map(p -> p.getProduct_fee()
			.multiply(BigDecimal.valueOf(p.getCount())))
			.reduce(BigDecimal.ZERO, BigDecimal::add));*/
		
		// 0명인 상품리스트 삭제
		paymentInfo.getProducts().removeIf(p -> p.getCount() <= 0);
		
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

		
		List<ProductDTO> productsOrderedByPrice = new ArrayList<>();
		for(ProductDTO product : selectedProducts) {
			productsOrderedByPrice.add(product);
		}
		Collections.sort(productsOrderedByPrice, Collections.reverseOrder()); // 내림차순
		paymentInfo.setProductsOrderedByPrice(productsOrderedByPrice);
			
		// 총액, 총인원수
		paymentInfo.setTotalCount(selectedProducts.stream().mapToInt(ProductDTO::getCount).sum());
		paymentInfo
				.setTotalFee(selectedProducts.stream().map(p -> p.getProduct_fee().multiply(BigDecimal.valueOf(p.getCount())))
						.reduce(BigDecimal.ZERO, BigDecimal::add));
		
		// 회차
		if(StringUtils.hasText(paymentInfo.getSchedule_code())) {
			ScheduleDTO schedule =  new ScheduleDTO();
			schedule.setShop_code(paymentInfo.getProductGroup().getShop_code());
			schedule.setSchedule_code(paymentInfo.getSchedule_code());
			schedule.setPlay_date_from(paymentInfo.getPlay_date());
			schedule = ticketingService.getScheduleByScheduleCode(schedule);
			
			if(schedule == null) {
				ScriptUtils.alertAndClose(response, "해당 상품의 회차정보가 존재하지 않습니다. 결제를 진행할 수 없습니다.");
				return null;
			}
			paymentInfo.setSchedule(schedule);
		}
		
		// 방문자 정보 가져오기
		ShopDetailVO shopDetail = ticketingService.getShopDetail(paymentInfo.getProductGroup().getShop_code());
		paymentInfo.setVisitorType(shopDetail.getPerson_type());
		// model.addAttribute("shopDetail", shopDetail);
		
		String content_mst_cd = essential.getContent_mst_cd(); 
		String product_group_code = essential.getProduct_group_code();
		
		// 이용약관 가져오기
		WebReservationKeyDTO reserveInfo = ticketingService.selectReserveInfo(dbProductGroup.getShop_code());
		
		if(reserveInfo == null || 
				!StringUtils.hasText(reserveInfo.getInfo_a()) ||
				!StringUtils.hasText(reserveInfo.getInfo_b()) ||
				!StringUtils.hasText(reserveInfo.getInfo_c()) ||
				!StringUtils.hasText(reserveInfo.getInfo_d())) 
		{
			//redirect.addFlashAttribute("msg", "약관정보가 없습니다. 관리자에게 연락 바랍니다.");
			log.error("[ERROR]약관정보가 없습니다.");
			
			if(StringUtils.hasText(content_mst_cd) && StringUtils.hasText(product_group_code)) {
				return "redirect:/ticketing/diamondbay/schedule?content_mst_cd=" + content_mst_cd + "&product_group_code=" + product_group_code;
			} else {
				return "redirect:/error/diamondbay";
			}
		}
		else if(reserveInfo == null 
				|| !StringUtils.hasText(reserveInfo.getIdentification_site_code()) 
				|| !StringUtils.hasText(reserveInfo.getIdentification_site_password())
				|| !StringUtils.hasText(reserveInfo.getPay_merchant_id())
				|| !StringUtils.hasText(reserveInfo.getPay_merchant_key()))
		{
			//redirect.addFlashAttribute("msg", "인증를 위한 정보가 없습니다. 관리자에게 연락 바랍니다.");
			
			log.error("[ERROR]인증를 위한 정보가 없습니다.");
			
			if(StringUtils.hasText(content_mst_cd) && StringUtils.hasText(product_group_code)) {
				return "redirect:/ticketing/diamondbay/schedule?content_mst_cd=" + content_mst_cd + "&product_group_code=" + product_group_code;
			} else {
				return "redirect:/error/diamondbay";
			}
		}
		else
		{
			String content = reserveInfo.getInfo_a();
			reserveInfo.setInfo_a(StringEscapeUtils.unescapeXml(content));
			
			content = reserveInfo.getInfo_b();
			reserveInfo.setInfo_b(StringEscapeUtils.unescapeXml(content));
			
			content = reserveInfo.getInfo_c();
			reserveInfo.setInfo_c(StringEscapeUtils.unescapeXml(content));

			content = reserveInfo.getInfo_d();
			reserveInfo.setInfo_d(StringEscapeUtils.unescapeXml(content));

			model.addAttribute("reserveInfo", reserveInfo);
			
			
			//본인인증 정보
			model.addAttribute("siteCode", reserveInfo.getIdentification_site_code());
			model.addAttribute("sitePassword", reserveInfo.getIdentification_site_password());
		}

		return "/ticketing/diamondbay/insertReserverDiamondbay";
	}
}
