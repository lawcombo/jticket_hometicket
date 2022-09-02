package com.bluecom.ticketing.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluecom.common.controller.BaseController;
import com.bluecom.common.util.ScriptUtils;
import com.bluecom.ticketing.domain.ReserverInfoDTO;
import com.bluecom.ticketing.domain.VerificationKeyVO;
import com.bluecom.ticketing.service.ReserverAuthenticationService;
import com.bluecom.ticketing.service.TicketingService;

import CheckPlus.kisinfo.SCheckPlus;

@Controller
@RequestMapping("/reserverAuthentication")
public class ReserverAuthenticationController extends BaseController {
	
	@Autowired
	ReserverAuthenticationService reserverAuthenticationService;
	

	@Autowired
	TicketingService ticketingService;
	
//	@GetMapping("/check")	
//	public String check(Model model) throws Exception {
//
////		return "/reserverAuthentication/check_temp";
//		return "/reserverAuthentication/checkplus_main";
//	}
//	
	
	// 소지 인증번호 확인 / 2021-09-15 / 조미근
	@PostMapping("/add")
	@ResponseBody
	public ReserverInfoDTO addReserverInfo(@RequestBody ReserverInfoDTO reserverInfo, Model model) throws Exception {
		
		int result = reserverAuthenticationService.addReserverInfo(reserverInfo);
		if(result <= 0) {
			throw new Exception("본인인증 데이터를 기록할 수 없습니다. 다시 인증을 진행해주시기 바랍니다. 반복시 관리자를 호출해 주세요.");
		}
		
		//인증번호 확인 받은 후 응답값을 addReserverInfo에 저장
		/*String sSiteCode	= "BT744";		// 사이트 코드 (NICE평가정보에서 발급한 사이트코드)
		String sSitePw		= "TCWRWzuAKtiS"; 	// 사이트 패스워드 (NICE평가정보에서 발급한 사이트패스워드)
	
		String sResSeq		= reserverInfo.getResponseSEQ();		// 응답 고유번호 (CPMobileStep1 에서 확인된 cpMobile.getResponseSEQ() 데이타)
		String sAuthNo		= reserverInfo.getAuthNo();		// SMS 인증번호
		String sJumin			= "0000000000000";		// 주민등록번호 체크안함
		
		String sCPRequest	= "";		// 요청 고유번호 (CPMobileStep1 에서 정의된 데이타)
		
		// 객체 생성
    	SCheckPlus cpMobile = new SCheckPlus();
    	
    	// Method 결과값(iRtn)에 따라, 프로세스 진행여부를 파악합니다.
    	int iRtn = cpMobile.fnRequestConfirm(sSiteCode, sSitePw, sResSeq, sAuthNo, sJumin, sCPRequest);
    	
    	if (iRtn == 0)
    	{
    		
				- 응답코드에 따라 귀사의 기획의도에 맞게 진행하시면 됩니다.
				
				- 응답코드 정의 : 첨부해드린 xls 파일을 참고하세요.
			
    		//System.out.println("RETURN_CODE=" + cpMobile.getReturnCode());              // 응답코드
    		//System.out.println("CONFIRM_DATETIME=" + cpMobile.getConfirmDateTime());    // 인증 완료시간
    		//System.out.println("REQ_SEQ=" + cpMobile.getRequestSEQ());                  // 요청 고유번호
    		//System.out.println("RES_SEQ=" + cpMobile.getResponseSEQ());                 // 응답 고유번호
    		
    		if(cpMobile.getReturnCode().equals("0000")) {
    			reserverInfo.setReturnCode(cpMobile.getReturnCode());
    			reserverInfo.setResponseSEQ(cpMobile.getResponseSEQ());

        		int result = reserverAuthenticationService.addReserverInfo(reserverInfo);
        		
        		if(result <= 0) {
        			throw new Exception("본인인증 데이터를 기록할 수 없습니다. 다시 인증을 진행해주시기 바랍니다. 반복시 관리자를 호출해 주세요.");
        		}
    		}else {
    			
    		}
    		
    	}
    	else if (iRtn == -7 || iRtn == -8)
	    {
	    	System.out.println("서버 네트웍크 및 방확벽 관련하여 아래 IP와 Port를 오픈해 주셔야 이용 가능합니다.");
	    	System.out.println("IP : 203.234.219.235 / Port : 3700 ~ 3715");
		}
		else if (iRtn == -9 || iRtn == -10)
		{
			System.out.println("입력값 오류 : fnRequest 함수 처리시, 필요한 6개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.");
		}
		else
		{
			System.out.println("iRtn 값 확인 후, NICE신용평가정보 개발 담당자에게 문의해 주세요.");
		}*/
		
		return reserverInfo;
	}
	
	/*
	 * 소지 인증번호 전송 / 2021-09-15 / 조미근
	 */
	@PostMapping("/certification")
	@ResponseBody
	public ReserverInfoDTO sendCertification(@RequestBody ReserverInfoDTO reserverInfo, Model model) throws Exception {
		String sSiteCode	= "BT744";		// 사이트 코드 (NICE평가정보에서 발급한 사이트코드)
		String sSitePw		= "TCWRWzuAKtiS"; 	// 사이트 패스워드 (NICE평가정보에서 발급한 사이트패스워드)
					
		String sJumin			= "0000000000000";		// 주민등록번호 체크안함
		String sMobileCo	= "1";		// 이통사 구분 없음
		String sMobileNo	= reserverInfo.getPhone();		// 휴대폰 번호
		
		String sCPRequest		= "";
		
		// 객체 생성
		SCheckPlus cpMobile = new SCheckPlus();
		
		// Method 결과값(iRtn)에 따라, 프로세스 진행여부를 파악합니다.
		int iRtn = cpMobile.fnRequestSMSAuth(sSiteCode, sSitePw, sJumin, "", sMobileCo, sMobileNo, sCPRequest);
		
		// Method 결과값에 따른 처리사항
		if (iRtn == 0)
		{
			/*
				- 응답코드에 따라 SMS 인증 여부를 판단합니다.
				
				- 응답코드 정의 : 첨부해드린 xls 파일을 참고하세요.
			*/
			//System.out.println("RETURN_CODE=" + cpMobile.getReturnCode());              // 응답코드
			//System.out.println("REQ_SEQ=" + cpMobile.getRequestSEQ());                  // 요청 고유번호
			//System.out.println("RES_SEQ=" + cpMobile.getResponseSEQ());                 // 응답 고유번호
			
			reserverInfo.setReturnCode(cpMobile.getReturnCode());
			reserverInfo.setResponseSEQ(cpMobile.getResponseSEQ());
			
	    }
	    else if (iRtn == -7 || iRtn == -8)
	    {
	    	System.out.println("서버 네트웍크 및 방확벽 관련하여 아래 IP와 Port를 오픈해 주셔야 이용 가능합니다.");
	    	System.out.println("IP : 203.234.219.235 / Port : 3700 ~ 3715");
		}
		else if (iRtn == -9 || iRtn == -10)
		{
			System.out.println("입력값 오류 : fnRequest 함수 처리시, 필요한 6개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.");
		}
		else
		{
			System.out.println("iRtn 값 확인 후, NICE신용평가정보 개발 담당자에게 문의해 주세요.");
		}
	
		return reserverInfo;
	}
	
	@RequestMapping(value="/success", method = { RequestMethod.GET, RequestMethod.POST } )
	public String success(@ModelAttribute("content_mst_cd") String content_mst_cd, HttpServletResponse response, Model model) throws Exception {
		
		String shopCode = ticketingService.getShopCode(content_mst_cd);
		
		VerificationKeyVO keys = ticketingService.getKeys(shopCode);
		
		if(keys == null 
				|| !StringUtils.hasText(keys.getIdentification_site_code()) 
				|| !StringUtils.hasText(keys.getIdentification_site_password())) {
			ScriptUtils.alertAndClose(response, "인증모듈에 오류가 발생하였습니다.");
		}
		
		model.addAttribute("siteCode", keys.getIdentification_site_code());
		model.addAttribute("sitePassword", keys.getIdentification_site_password());
		
		return "/reserverAuthentication/checkplus_success";
	}
	
	@RequestMapping(value="/fail", method = { RequestMethod.GET, RequestMethod.POST } )	
	public String fail(@ModelAttribute("content_mst_cd") String content_mst_cd, HttpServletResponse response, Model model) throws Exception {
		
		String shopCode = ticketingService.getShopCode(content_mst_cd);
		
		VerificationKeyVO keys = ticketingService.getKeys(shopCode);
		
		if(keys == null 
				|| !StringUtils.hasText(keys.getIdentification_site_code()) 
				|| !StringUtils.hasText(keys.getIdentification_site_password())) {
			ScriptUtils.alertAndClose(response, "인증모듈에 오류가 발생하였습니다.");
		}
		
		model.addAttribute("siteCode", keys.getIdentification_site_code());
		model.addAttribute("sitePassword", keys.getIdentification_site_password());
		
		return "/reserverAuthentication/checkplus_fail";
	}
	
	
	/**
	 * 다이아몬드베이 거래처 예매
	 * @param reserverInfo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value= "/checkCustReserve")
	public ModelAndView checkCustReserve(ReserverInfoDTO reserverInfo, Model model) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = reserverAuthenticationService.checkCustReserve(reserverInfo);
		
		//int result = 0;
    	mv.addObject("result", result);
    	
    	return mv;
	}
	
}
