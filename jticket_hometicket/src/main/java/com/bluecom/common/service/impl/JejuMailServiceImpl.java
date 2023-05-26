package com.bluecom.common.service.impl;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bluecom.common.domain.MailVO;
import com.bluecom.common.service.MailService;
import com.bluecom.common.util.FileUtils;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSaleProductDTO;
import com.bluecom.ticketing.domain.ApiSocialSaleDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.property.EgovPropertyService;
import jdk.nashorn.internal.runtime.Debug;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("jejuMail")
public class JejuMailServiceImpl extends EgovAbstractServiceImpl implements MailService {

	@Autowired
	private EgovPropertyService propertyService;

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public boolean sendReserve(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult) throws Exception{

		boolean resultBoolean = false;
		
		String contentMstCd = apiResult.getWebPayment().getContent_mst_cd().toString();
		
		if(contentMstCd.contains("JEJUBEER"))
		{
			resultBoolean = sendNew(request, apiResult, pgResult, "완료", "정상처리", "제주맥주 양조장 예약이 완료되었습니다.");
		}
		else if(contentMstCd.contains("DIAMONDBAY"))
		{
			resultBoolean = sendNewOfDiamondbay(request, apiResult, pgResult, "완료", "정상처리", "다이아몬드베이 예약이 완료되었습니다.");
		}
		else
		{
			resultBoolean = sendNew(request, apiResult, pgResult, "완료", "정상처리", "소금산그랜드밸리 예매 완료되었습니다.");
		}
		
		return resultBoolean;
	}
	
	public boolean sendNew(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, String fifth, String sixth, String subject) throws Exception{
		ApiSocialSaleDTO sale = apiResult.getSocialSales().get(0);
		WebPaymentDTO payment = apiResult.getWebPayment();
//		String expireDate = "";
//		if(payment.getProduct_group_kind().equals("A")) {
//			expireDate = DateHelper.getExpireDate(sale.getSALE_DATE(), payment.getValid_period());
//		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		MailVO mailVO = new MailVO();
		mailVO.setTo(payment.getReserverEmail());
		
		
		mailVO.setFrom(propertyService.getString("senderEmail"));
		subject = "소금산그랜드밸리 예매 완료되었습니다.";
		
		mailVO.setSubject(subject);
		
		String templateFile = request.getSession().getServletContext().getRealPath("/") + "resources" + File.separator + "jeju_email.html";
		
//		0	reserverName
//		1	14 휴대폰번호
//		2	8 totalCount
//		3	workdate
//		4	useDate
//		5	완료		
//		6	정상처리
//		7	이미지 https://jticket.nicetcm.co.kr/resources/images/jeju/email_logo.png		
//		8	예약확인링크
	
		String text = FileUtils.readText(templateFile);
		text = text.replace("{0}", payment.getReserverName());
		text = text.replace("{1}", payment.getReserverPhone());
		text = text.replace("{2}", Integer.toString(payment.getTotal_count()));
//		text = text.replace("{3}", sale.getWORK_DATETIME());
		Date workDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.getWORK_DATETIME());
		text = text.replace("{3}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(workDatetime));
		//String useDate = payment.getPlay_sequence() + "회차 " + sale.getVALID_FROM() + " (" + payment.getStart_time() + " ~ " + payment.getEnd_time() + ")";
		String useDate = sale.getVALID_FROM() + " / " + payment.getStart_time();
		text = text.replace("{4}", useDate);		
		text = text.replace("{5}", fifth);
		text = text.replace("{6}", sixth);
		text = text.replace("{7}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resources/images/company_logo.png");
		text = text.replace("{8}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("{9}", payment.getProduct_group_name());
		
		mailVO.setText(text);
		
		System.out.println(mailVO.getSubject());
		
	    try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
            // true는 멀티파트 메세지를 사용하겠다는 의미
            
            /*
             * 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용 가능 
             * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
             */
            
            mailHelper.setFrom(mailVO.getFrom());
            // 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용 따라서 보내는이(setFrom())반드시 필요
            // 보내는이와 메일주소를 수신하는이가 볼때 모두 표기 되게 원하신다면 아래의 코드를 사용하시면 됩니다.
            //mailHelper.setFrom("보내는이 이름 <보내는이 아이디@도메인주소>");
            mailHelper.setTo(mailVO.getTo());
            mailHelper.setSubject(mailVO.getSubject());
            mailHelper.setText(mailVO.getText(), true);
            // true는 html을 사용하겠다는 의미입니다.
            
            /*
             * 단순한 텍스트만 사용하신다면 다음의 코드를 사용하셔도 됩니다. mailHelper.setText(content);
             */
            mailSender.send(mail);
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Mail 전송 실패" + e.getMessage());
            return false;
        }	
		
		
		return true;
	}
	
	@Override
	public boolean sendChange(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult) throws Exception {

		log.debug("예약변경 메일 전송", apiResult.getWebPayment().getOrder_no());
		return sendNew(request, apiResult, pgResult, "변경", "변경", "제주맥주 양조장 투어 예약이 변경되었습니다.");
	}
	
	@Override
	public boolean sendRefund(HttpServletRequest request, SaleVO sale, WebPaymentDTO payment, WebPaymentPgResultDTO pgResult) throws Exception {
	
//		String expireDate = "";
//		if(payment.getProduct_group_kind().equals("A")) {
//			expireDate = DateHelper.getExpireDate(sale.getSALE_DATE(), payment.getValid_period());
//		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		MailVO mailVO = new MailVO();
		mailVO.setTo(payment.getReserverEmail());
		mailVO.setFrom(propertyService.getString("senderEmail"));
		mailVO.setSubject("소금산그랜드밸리 예매 취소되었습니다.");
		String templateFile = request.getSession().getServletContext().getRealPath("/") + "resources" + File.separator + "jeju_email.html";
		
//		0	reserverName
//		1	14 휴대폰번호
//		2	8 totalCount
//		3	workdate
//		4	useDate
//		5	완료		
//		6	정상처리
//		7	이미지 https://jticket.nicetcm.co.kr/resources/images/jeju/email_logo.png		
//		8	예약확인링크
	
		String text = FileUtils.readText(templateFile);
		text = text.replace("{0}", payment.getReserverName());
		text = text.replace("{1}", payment.getReserverPhone());
		text = text.replace("{2}", Integer.toString(payment.getTotal_count()));
		text = text.replace("{3}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(sale.getReg_Date()));
		//String useDate = payment.getPlay_sequence() + "회차 " + sale.getValid_from() + " (" + payment.getStart_time() + " ~ " + payment.getEnd_time() + ")";
		String useDate = sale.getValid_from() + " / " + payment.getStart_time();
		text = text.replace("{4}", useDate);		
		text = text.replace("{5}", "취소");
		text = text.replace("{6}", "취소");
		//text = text.replace("{7}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resources/images/jeju/email_logo.jpg");
		text = text.replace("{7}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resources/images/company_logo.png");
		text = text.replace("{8}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("{9}", payment.getProduct_group_name());
		
		mailVO.setText(text);
		
		System.out.println(mailVO.getSubject());
		
	    try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
            // true는 멀티파트 메세지를 사용하겠다는 의미
            
            /*
             * 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용 가능 
             * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
             */
            
            mailHelper.setFrom(mailVO.getFrom());
            // 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용 따라서 보내는이(setFrom())반드시 필요
            // 보내는이와 메일주소를 수신하는이가 볼때 모두 표기 되게 원하신다면 아래의 코드를 사용하시면 됩니다.
            //mailHelper.setFrom("보내는이 이름 <보내는이 아이디@도메인주소>");
            mailHelper.setTo(mailVO.getTo());
            mailHelper.setSubject(mailVO.getSubject());
            mailHelper.setText(mailVO.getText(), true);
            // true는 html을 사용하겠다는 의미입니다.
            
            /*
             * 단순한 텍스트만 사용하신다면 다음의 코드를 사용하셔도 됩니다. mailHelper.setText(content);
             */
            mailSender.send(mail);
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Mail 전송 실패" + e.getMessage());
            return false;
        }	
		
		
		return true;
	}
	
	private String getPayMethodName(String payMethod) {
		String payMethodName = "";
		if(payMethod.equals("CARD")){
			payMethodName = "신용카드"; // 신용카드(정상 결과코드:3001)       	
		}else if(payMethod.equals("BANK")){
			payMethodName = "계좌이체"; // 계좌이체(정상 결과코드:4000)	
		}else if(payMethod.equals("CELLPHONE")){
			payMethodName = "휴대폰"; // 휴대폰(정상 결과코드:A000)	
		}else if(payMethod.equals("VBANK")){
			payMethodName = "가상계좌"; // 가상계좌(정상 결과코드:4100)
		}else if(payMethod.equals("SSG_BANK")){
			payMethodName = "SSG은행계좌"; // SSG은행계좌(정상 결과코드:0000)
		}else if(payMethod.equals("CMS_BANK")){
			payMethodName = "계좌간편결제"; // 계좌간편결제(정상 결과코드:0000)
		}
		return payMethodName;
	}
	
	public String sendMail(MailVO mailVO) {
		
        return "succ";
	}
	
	/**
	 * 다이아몬드베이 예매 메일
	 * @param request
	 * @param apiResult
	 * @param pgResult
	 * @param fifth
	 * @param sixth
	 * @param subject
	 * @return
	 * @throws Exception
	 */
	public boolean sendNewOfDiamondbay(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, String fifth, String sixth, String subject) throws Exception{
		
		
		ApiSocialSaleDTO sale 		= apiResult.getSocialSales().get(0);
		WebPaymentDTO payment 		= apiResult.getWebPayment();
		NumberFormat numberFormat 	= NumberFormat.getInstance();
		
		MailVO mailVO = new MailVO();
		mailVO.setTo(payment.getReserverEmail());
		
		
		mailVO.setFrom(propertyService.getString("senderEmailOfDiamondbay"));
		subject = "다이아몬드베이 예매가 완료되었습니다.";
		
		
		mailVO.setSubject(subject);
		
		String templateFile = request.getSession().getServletContext().getRealPath("/") + "resources" + File.separator + "html" + File.separator + "diamondbay_email.html";
		
//		0	reserverName
//		1	14 휴대폰번호
//		2	8 totalCount
//		3	workdate
//		4	useDate
//		5	완료		
//		6	정상처리
//		7	이미지 https://jticket.nicetcm.co.kr/resources/images/jeju/email_logo.png		
//		8	예약확인링크
	
		String text = FileUtils.readText(templateFile);
		text = text.replace("{0}", payment.getReserverName());
		text = text.replace("{1}", payment.getReserverPhone());
		text = text.replace("{2}", Integer.toString(payment.getTotal_count()));
		Date workDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.getWORK_DATETIME());
		text = text.replace("{3}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(workDatetime));
		String useDate = sale.getVALID_FROM() + " / " + payment.getStart_time();
		text = text.replace("{4}", useDate);		
		text = text.replace("{5}", fifth);
		text = text.replace("{6}", sixth);
		text = text.replace("{8}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("{9}", payment.getProduct_group_name());
		
		mailVO.setText(text);
		
		System.out.println(mailVO.getSubject());
		
	    try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
            // true는 멀티파트 메세지를 사용하겠다는 의미
            
            /*
             * 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용 가능 
             * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
             */
            
            mailHelper.setFrom(mailVO.getFrom());
            // 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용 따라서 보내는이(setFrom())반드시 필요
            // 보내는이와 메일주소를 수신하는이가 볼때 모두 표기 되게 원하신다면 아래의 코드를 사용하시면 됩니다.
            //mailHelper.setFrom("보내는이 이름 <보내는이 아이디@도메인주소>");
            mailHelper.setTo(mailVO.getTo());
            mailHelper.setSubject(mailVO.getSubject());
            mailHelper.setText(mailVO.getText(), true);
            // true는 html을 사용하겠다는 의미입니다.
            
            /*
             * 단순한 텍스트만 사용하신다면 다음의 코드를 사용하셔도 됩니다. mailHelper.setText(content);
             */
            mailSender.send(mail);
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Mail 전송 실패" + e.getMessage());
            return false;
        }	
		
		
		return true;
	}
	
	
	/**
	 * 다이아몬드베이 예매취소 메일
	 */
	@Override
	public boolean sendRefundOfDiamondbay(HttpServletRequest request, SaleVO sale, WebPaymentDTO payment, WebPaymentPgResultDTO pgResult) throws Exception {
	
		
		NumberFormat numberFormat 	= NumberFormat.getInstance();
		MailVO mailVO 				= new MailVO();
		
		mailVO.setTo(payment.getReserverEmail());
		mailVO.setFrom(propertyService.getString("senderEmailOfDiamondbay"));
		
		mailVO.setSubject("다이아몬드베이 예매 취소되었습니다.");
		
		String templateFile = request.getSession().getServletContext().getRealPath("/") + "resources" + File.separator + "html" + File.separator + "diamondbay_email.html";
		
//		0	reserverName
//		1	14 휴대폰번호
//		2	8 totalCount
//		3	workdate
//		4	useDate
//		5	완료		
//		6	정상처리
//		7	이미지 https://jticket.nicetcm.co.kr/resources/images/jeju/email_logo.png		
//		8	예약확인링크
	
		String text = FileUtils.readText(templateFile);
		text = text.replace("{0}", payment.getReserverName());
		text = text.replace("{1}", payment.getReserverPhone());
		text = text.replace("{2}", Integer.toString(payment.getTotal_count()));
		text = text.replace("{3}", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(sale.getReg_Date()));
		String useDate = sale.getValid_from() + " / " + payment.getStart_time();
		text = text.replace("{4}", useDate);		
		text = text.replace("{5}", "취소");
		text = text.replace("{6}", "취소");
		text = text.replace("{8}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?content_mst_cd=" + payment.getContent_mst_cd());
		text = text.replace("{9}", payment.getProduct_group_name());
		
		mailVO.setText(text);
		
		System.out.println(mailVO.getSubject());
		
	    try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
            // true는 멀티파트 메세지를 사용하겠다는 의미
            
            /*
             * 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용 가능 
             * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
             */
            
            mailHelper.setFrom(mailVO.getFrom());
            // 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용 따라서 보내는이(setFrom())반드시 필요
            // 보내는이와 메일주소를 수신하는이가 볼때 모두 표기 되게 원하신다면 아래의 코드를 사용하시면 됩니다.
            //mailHelper.setFrom("보내는이 이름 <보내는이 아이디@도메인주소>");
            mailHelper.setTo(mailVO.getTo());
            mailHelper.setSubject(mailVO.getSubject());
            mailHelper.setText(mailVO.getText(), true);
            // true는 html을 사용하겠다는 의미입니다.
            
            /*
             * 단순한 텍스트만 사용하신다면 다음의 코드를 사용하셔도 됩니다. mailHelper.setText(content);
             */
            mailSender.send(mail);
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Mail 전송 실패" + e.getMessage());
            return false;
        }	
		
		
		return true;
	}
	
	
	
	@Override
	public boolean sendReserve(HttpServletRequest request, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception{

		ApiSocialSaleDTO sale = apiResult.getSocialSales().get(0);
		WebPaymentDTO payment = apiResult.getWebPayment();
//		String expireDate = "";
//		if(payment.getProduct_group_kind().equals("1")) {
//			expireDate = DateHelper.getExpireDate(sale.getSALE_DATE(), payment.getValid_period());
//		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		MailVO mailVO = new MailVO();
		mailVO.setTo(payment.getReserverEmail());
		mailVO.setFrom(propertyService.getString("senderEmail_noSchedule"));
		mailVO.setSubject(payment.getShop_name() + " 예약안내 메일");
		String templateFile = request.getSession().getServletContext().getRealPath("/") + "resources" + File.separator + "reservation_mail.html";
//		0	reserverName
//		1	shopName
//		2	productGroupName
//		3	reservationDate
//		4	expireDateOrScheduleName
//		5	expireDateOrSchedule
//		6	reserverPhone
//		7	ticketGroupInfo
//		8	totalCount
//		9	totalFee
//		10	payMethod
//		11	payAmount
//		12	transactionId
//		13	tradeDate
//		14 휴대폰번호
//		15 대표자
//		16 사업자번호
//		17 안내번호
//		18 소재지
//		19 마이페이지
//		20 예매페이지
//		21 메일
//		22 회사명
		String text = FileUtils.readText(templateFile);
		text = text.replace("{0}", payment.getReserverName());
		text = text.replace("{1}", payment.getShop_name());
		text = text.replace("{2}", payment.getProduct_group_name());
		text = text.replace("{3}", sale.getSALE_DATE());
		text = text.replace("{4}", payment.getProduct_group_kind().equals("1") ? "사용기간" : "사용일");
		if(payment.getProduct_group_kind().equals("1")) {
			text = text.replace("{5}", sale.getVALID_FROM() + " ~ " + sale.getVALID_TO());
		} else {
			String useDate = payment.getPlay_sequence() + "회차 " + sale.getVALID_FROM() + 
					" (" + payment.getStart_time() + " ~ " + payment.getEnd_time() + ")";
			text = text.replace("{5}", useDate);
		}
		
		text = text.replace("{6}", payment.getReserverPhone());
		StringBuilder groupBuilder = new StringBuilder();
		for(ApiSaleProductDTO product : sale.getSALE_PRODUCT_LIST()) {
			groupBuilder.append("<tr>");
			groupBuilder.append("<td style=\"background:#ffffff;padding:10px;border:1px solid #CCCCCC;font-size:13px;font-family:돋움 !important\" >성인 (Adult)</td>");		
			groupBuilder.append("<td  style=\"background:#ffffff;padding:10px;border:1px solid #CCCCCC;font-size:13px;font-family:돋움 !important;text-align:right\">");
			groupBuilder.append(numberFormat.format(product.getUNIT_PRICE()) + " 원");
			groupBuilder.append("</td>");
			groupBuilder.append("<td  style=\"background:#ffffff;padding:10px;border:1px solid #CCCCCC;font-size:13px;font-family:돋움 !important;text-align:center\">");
			groupBuilder.append(product.getQUANTITY() + " 매");
			groupBuilder.append("</td>");
			groupBuilder.append("<td  style=\"background:#ffffff;padding:10px;border:1px solid #CCCCCC;font-size:13px;font-family:돋움 !important;text-align:right\">");
			groupBuilder.append(numberFormat.format(product.getUNIT_PRICE() * product.getQUANTITY()) + " 원");
			groupBuilder.append("</td>");
			groupBuilder.append("</tr>");
			
		}
		text = text.replace("{7}", groupBuilder.toString());
		text = text.replace("{8}", Integer.toString(payment.getTotal_count()));
		text = text.replace("{9}", numberFormat.format(payment.getTotal_fee()));
		text = text.replace("{10}", getPayMethodName(pgResult.getPay_method()));
		text = text.replace("{11}", numberFormat.format(Integer.parseInt(pgResult.getAmt())));
		text = text.replace("{12}", pgResult.getTid());
		text = text.replace("{13}", pgResult.getAuth_date() ==  null ? "" : pgResult.getAuth_date());
		text = text.replace("{14}", payment.getReserverPhone());
		text = text.replace("{15}", shopDetail.getMaster_name());
		text = text.replace("{16}", shopDetail.getCorporate_number());
		text = text.replace("{17}", shopDetail.getShop_tel());
		text = text.replace("{18}", shopDetail.getAddress1() + " " + shopDetail.getAddress2());
		if(payment.getProduct_group_kind().equals("1")) {
			text = text.replace("{19}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());
			text = text.replace("{20}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());
		} else if(payment.getProduct_group_kind().equals("2")) {
			text = text.replace("{19}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());
			text = text.replace("{20}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/selectTicket?content_mst_cd=" + payment.getContent_mst_cd());			
		} 
		text = text.replace("{21}", payment.getOrder_no());
		text = text.replace("{22}", shopDetail.getShop_name());
		mailVO.setText(text);
		
		System.out.println(mailVO.getSubject());
		
	    try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
            // true는 멀티파트 메세지를 사용하겠다는 의미
            
            /*
             * 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용 가능 
             * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
             */
            
            mailHelper.setFrom(mailVO.getFrom());
            // 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용 따라서 보내는이(setFrom())반드시 필요
            // 보내는이와 메일주소를 수신하는이가 볼때 모두 표기 되게 원하신다면 아래의 코드를 사용하시면 됩니다.
            //mailHelper.setFrom("보내는이 이름 <보내는이 아이디@도메인주소>");
            mailHelper.setTo(mailVO.getTo());
            mailHelper.setSubject(mailVO.getSubject());
            mailHelper.setText(mailVO.getText(), true);
            // true는 html을 사용하겠다는 의미입니다.
            
            /*
             * 단순한 텍스트만 사용하신다면 다음의 코드를 사용하셔도 됩니다. mailHelper.setText(content);
             */
            mailSender.send(mail);
        } catch(Exception e) {
            e.printStackTrace();
            log.error("Mail 전송 실패" + e.getMessage());
            return false;
        }	
		
		
		return true;
	}
	
}
