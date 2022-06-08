package com.bluecom.common.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bluecom.common.util.SmsUtils;

@Controller
public class SmsController {

	
	
	
	@GetMapping("/sms/send")
	public String sms_send(HttpSession session) throws Exception {
				
		return "/sms/send";
	}
	
	@GetMapping("/sms/calljson")
	public String sms_calljson(HttpSession session) throws Exception {
				
		
		return "/sms/calljson";
	}
	
	
	@GetMapping("/sms/finish")
	public String sms_finish(HttpSession session) throws Exception {
				
		System.out.println("SMS FINISH");
		return "/sms/finish";
	}
	
	@PostMapping("/sms/send_action")
	public String send_action(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String charsetType = "UTF-8"; //EUC-KR 또는 UTF-8

	    request.setCharacterEncoding(charsetType);
	    response.setCharacterEncoding(charsetType);
	    
		String sms_url = "";
        sms_url = "https://sslsms.cafe24.com/sms_sender.php"; // SMS 전송요청 URL
        String user_id = SmsUtils.base64Encode("niceticketsms"); // SMS아이디
        String secure = SmsUtils.base64Encode("04cf37062a15bf2a25306d1a171ec101");//인증키
        String msg = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("msg"), ""));
        String rphone = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("rphone"), ""));
        String sphone1 = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("sphone1"), ""));
        String sphone2 = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("sphone2"), ""));
        String sphone3 = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("sphone3"), ""));
        String rdate = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("rdate"), ""));
        String rtime = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("rtime"), ""));
        String mode = SmsUtils.base64Encode("1");
        String subject = "";
        if(SmsUtils.nullcheck(request.getParameter("smsType"), "").equals("L")) {
            subject = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("subject"), ""));
        }
        String testflag = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("testflag"), ""));
        String destination = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("destination"), ""));
        String repeatFlag = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("repeatFlag"), ""));
        String repeatNum = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("repeatNum"), ""));
        String repeatTime = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("repeatTime"), ""));
        String returnurl = SmsUtils.nullcheck(request.getParameter("returnurl"), "");
        String nointeractive = SmsUtils.nullcheck(request.getParameter("nointeractive"), "");
        String smsType = SmsUtils.base64Encode(SmsUtils.nullcheck(request.getParameter("smsType"), ""));

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

        /*out.println(nointeractive);

        if(nointeractive.equals("1") && !(Result.equals("Test Success!")) && !(Result.equals("success")) && !(Result.equals("reserved")) ) {
            out.println("<script>alert('" + alert + "')</script>");
        }
        else if(!(nointeractive.equals("1"))) {
            out.println("<script>alert('" + alert + "')</script>");
        }


        out.println("<script>location.href='"+returnurl+"';</script>");
		*/
        return "redirect:/sms/finish";
        
	}
	
	   
	
}
