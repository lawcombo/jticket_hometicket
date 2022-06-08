package com.bluecom.common.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bluecom.common.service.MessageService;
import com.bluecom.common.util.DateHelper;
import com.bluecom.common.util.FileUtils;
import com.bluecom.common.util.SmsUtils;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSocialSaleDTO;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.SaleDTO;
import com.bluecom.ticketing.domain.SaleProductDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.property.EgovPropertyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("cafe24Lms")
public class Cafe24LmsServiceImpl extends EgovAbstractServiceImpl implements MessageService {

	@Autowired
	private EgovPropertyService propertyService;

	@Override
	public boolean send(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult, WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail)
			throws Exception {
		log.debug("mms 1");
		NumberFormat numberFormat = NumberFormat.getInstance();
		ApiSocialSaleDTO sale = apiResult.getSocialSales().get(0);
		WebPaymentDTO payment = apiResult.getWebPayment();
		String phone = payment.getReserverPhone();
		String phone1;
		String phone2;
		String phone3;
		log.debug("mms 2");
		if(phone.length() == 11) {
			phone1 = phone.substring(0, 3);
			phone2 = phone.substring(3, 7);
			phone3 = phone.substring(7, 11);
			phone = phone1 + "-" + phone2 + "-" + phone3;
		} else if( phone.length() == 10) {
			phone1 = phone.substring(0, 3);
			phone2 = phone.substring(3, 6);
			phone3 = phone.substring(6, 10);
			phone = phone1 + "-" + phone2 + "-" + phone3;
		} else {
			return false;
		}
		log.debug("mms 3");
		String subjectText = payment.getShop_name() + " 예매 안내";
		String destinationText = phone + "|" + payment.getReserverName();
		log.debug("mms 4");
		
		String text = "";
		if(payment.getProduct_group_kind().equals("A")) {
			text = getNormalReservationMessage(request, sale, payment, pgResult, shopDetail);
		} else if(payment.getProduct_group_kind().equals("S")) {
			text = getScheduleReservationMessage(request, sale, payment, pgResult, shopDetail);
		} else {
			return false;
		}
		
		log.debug("mms 5");
		String charsetType = "UTF-8"; //EUC-KR 또는 UTF-8

	    request.setCharacterEncoding(charsetType);
	    response.setCharacterEncoding(charsetType);
	    
	    log.debug("mms 6");
		String sms_url = "https://sslsms.cafe24.com/sms_sender.php"; // SMS 전송요청 URL
        String user_id = SmsUtils.base64Encode("niceticketsms"); // SMS아이디
        String secure = SmsUtils.base64Encode("04cf37062a15bf2a25306d1a171ec101");//인증키
        String msg = SmsUtils.base64Encode(SmsUtils.nullcheck(text, ""));
        String rphone = SmsUtils.base64Encode(SmsUtils.nullcheck(phone, ""));
        String sphone1 = SmsUtils.base64Encode(SmsUtils.nullcheck(propertyService.getString("senderPhone1"), ""));
        String sphone2 = SmsUtils.base64Encode(SmsUtils.nullcheck(propertyService.getString("senderPhone2"), ""));
        String sphone3 = SmsUtils.base64Encode(SmsUtils.nullcheck(propertyService.getString("senderPhone3"), ""));
        String rdate = SmsUtils.base64Encode("");
        String rtime = SmsUtils.base64Encode("");
        String mode = SmsUtils.base64Encode("1");
        String subject = SmsUtils.base64Encode(subjectText);        
        String testflag = SmsUtils.base64Encode(""); //  테스트시: Y
        String destination = SmsUtils.base64Encode(destinationText);
        String repeatFlag = SmsUtils.base64Encode("");
        String repeatNum = SmsUtils.base64Encode("");
        String repeatTime = SmsUtils.base64Encode("");
        String returnurl = "";
        String nointeractive = "1"; // 
        String smsType = SmsUtils.base64Encode("L");

        String[] host_info = sms_url.split("/");
        String host = host_info[2];
        String path = "/" + host_info[3];
        int port = 80;

        // 데이터 맵핑 변수 정의
        String arrKey[]
            = new String[] {"user_id","secure","msg", "rphone","sphone1","sphone2","sphone3","rdate","rtime"
                        ,"mode","testflag","destination","repeatFlag","repeatNum", "repeatTime", "smsType", "subject"};
        String valKey[]= new String[arrKey.length] ;
        valKey[0] = user_id;
        valKey[1] = secure;
        valKey[2] = msg;
        valKey[3] = rphone;
        valKey[4] = sphone1;
        valKey[5] = sphone2;
        valKey[6] = sphone3;
        valKey[7] = rdate;
        valKey[8] = rtime;
        valKey[9] = mode;
        valKey[10] = testflag;
        valKey[11] = destination;
        valKey[12] = repeatFlag;
        valKey[13] = repeatNum;
        valKey[14] = repeatTime;
        valKey[15] = smsType;
        valKey[16] = subject;

        String boundary = "";
        Random rnd = new Random();
        String rndKey = Integer.toString(rnd.nextInt(32000));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytData = rndKey.getBytes();
        md.update(bytData);
        byte[] digest = md.digest();
        for(int i =0;i<digest.length;i++)
        {
            boundary = boundary + Integer.toHexString(digest[i] & 0xFF);
        }
        boundary = "---------------------"+boundary.substring(0,11);
        log.debug("mms 7");
        // 본문 생성
        String data = "";
        String index = "";
        String value = "";
        for (int i=0;i<arrKey.length; i++)
        {
            index =  arrKey[i];
            value = valKey[i];
            data +="--"+boundary+"\r\n";
            data += "Content-Disposition: form-data; name=\""+index+"\"\r\n";
            data += "\r\n"+value+"\r\n";
            data +="--"+boundary+"\r\n";
        }

        //out.println(data);
        log.debug("mms 8:" + data);
        InetAddress addr = InetAddress.getByName(host);
        Socket socket = new Socket(host, port);
       
        // 헤더 전송
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charsetType));
        wr.write("POST "+path+" HTTP/1.0\r\n");
        wr.write("Content-Length: "+data.length()+"\r\n");
        wr.write("Content-type: multipart/form-data, boundary="+boundary+"\r\n");
        wr.write("\r\n");

        // 데이터 전송
        wr.write(data);
        wr.flush();
        log.debug("mms 9");
        // 결과값 얻기
        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(),charsetType));
        String line;
        String alert = "";
        ArrayList tmpArr = new ArrayList();
        while ((line = rd.readLine()) != null) {
            tmpArr.add(line);
        }
        wr.close();
        rd.close();
        log.debug("mms 10");
        String tmpMsg = (String)tmpArr.get(tmpArr.size()-1);
        String[] rMsg = tmpMsg.split(",");
        String Result= rMsg[0]; //발송결과
        String Count= ""; //잔여건수
        if(rMsg.length>1) {Count= rMsg[1]; }

                        //발송결과 알림
        if(Result.equals("success")) {
            alert = "성공적으로 발송하였습니다.";
            alert += " 잔여건수는 "+ Count+"건 입니다.";
        }
        else if(Result.equals("reserved")) {
            alert = "성공적으로 예약되었습니다";
            alert += " 잔여건수는 "+ Count+"건 입니다.";
        }
        else if(Result.equals("3205")) {
            alert = "잘못된 번호형식입니다.";
        }
        else {
            alert = "[Error]"+Result;
        }
        log.debug("mms 11:" + Result);
		return false;
	}

	private String getNormalReservationMessage(HttpServletRequest request, ApiSocialSaleDTO sale, WebPaymentDTO payment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception{
		String expireDate = DateHelper.getExpireDate(sale.getSALE_PRODUCT_LIST().get(0).getPLAY_DATE(), payment.getValid_period());
		String templateFile = request.getSession().getServletContext().getRealPath("/") + "resources" + File.separator + "mms_normal.txt";

		String text = FileUtils.readText(templateFile);	
		text = text.replace("{0}", payment.getShop_name());
		text = text.replace("{1}", payment.getOrder_no());
		text = text.replace("{2}", payment.getProduct_group_name());
		text = text.replace("{3}", sale.getSALE_DATE());
		text = text.replace("{4}", expireDate);
		text = text.replace("{7}", payment.getReserverName());
		text = text.replace("{8}", sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME() + (payment.getTotal_count() == 1 ? "" : (" 외 " + (payment.getTotal_count() - 1))));
		text = text.replace("{9}", sale.getSALE_DATE());
		text = text.replace("{10}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?contentMstCd=" + payment.getContent_mst_cd() + "&type=1");
		text = text.replace("{11}", shopDetail.getShop_tel());
		return text;
	}
	

	private String getScheduleReservationMessage(HttpServletRequest request, ApiSocialSaleDTO sale, WebPaymentDTO payment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception{
		String templateFile = request.getSession().getServletContext().getRealPath("/") + "resources" + File.separator + "mms_schedule.txt";

		String text = FileUtils.readText(templateFile);	
		text = text.replace("{0}", payment.getShop_name());
		text = text.replace("{1}", payment.getOrder_no());
		text = text.replace("{2}", payment.getProduct_group_name());
		text = text.replace("{3}", payment.getPlay_date());
		text = text.replace("{4}", payment.getPlay_sequence() + "(" + payment.getStart_time() + "~" + payment.getEnd_time() + ")");
		text = text.replace("{7}", payment.getReserverName());
		text = text.replace("{8}", sale.getSALE_PRODUCT_LIST().get(0).getPRODUCT_NAME() + (payment.getTotal_count() == 1 ? "" : (" 외 " + (payment.getTotal_count() - 1))));
		text = text.replace("{9}", sale.getSALE_DATE());
		text = text.replace("{10}", "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/checkTicket?contentMstCd=" + payment.getContent_mst_cd() + "&type=2");
		text = text.replace("{11}", shopDetail.getShop_tel());
		
		return text;
	}
	
	@Override
	public boolean sendRefund(HttpServletRequest request, SaleVO saleVO, WebPaymentDTO webPayment,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}	

	@Override
	public boolean sendChange(HttpServletRequest request, HttpServletResponse response, ApiResultVO apiResult,
			WebPaymentPgResultDTO pgResult, ShopDetailVO shopDetail) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
