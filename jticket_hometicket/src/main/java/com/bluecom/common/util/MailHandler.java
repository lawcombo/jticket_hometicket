package com.bluecom.common.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bluecom.common.domain.MailVO;

public class MailHandler {
	
	@Autowired
	private JavaMailSender mailSender;
	
	private String from;
	private String to;
	private String subject;
	private String text;
		
	public void setVO(MailVO mailVO) {
		from = mailVO.getFrom();
		to = mailVO.getTo();
		subject = mailVO.getSubject();
		text = mailVO.getText();
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean send() {
		try {
	        MimeMessage mail = mailSender.createMimeMessage();
	        MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
	        // true는 멀티파트 메세지를 사용하겠다는 의미
	        
	        /*
	         * 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용 가능 
	         * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
	         */
	        
	        //mailHelper.setFrom(from);
	        // 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용 따라서 보내는이(setFrom())반드시 필요
	        // 보내는이와 메일주소를 수신하는이가 볼때 모두 표기 되게 원하신다면 아래의 코드를 사용하시면 됩니다.
	        //mailHelper.setFrom("보내는이 이름 <보내는이 아이디@도메인주소>");

	        mailHelper.setFrom(from);
			mailHelper.setTo(to);
            mailHelper.setSubject(subject);
            mailHelper.setText(text, true);
	        // true는 html을 사용하겠다는 의미입니다.
	        
	        /*
	         * 단순한 텍스트만 사용하신다면 다음의 코드를 사용하셔도 됩니다. mailHelper.setText(content);
	         */
	        
	        mailSender.send(mail);
	        
	    } catch(Exception e) {
	        e.printStackTrace();
	        return false;
	    }	
		return true;	
	}
}