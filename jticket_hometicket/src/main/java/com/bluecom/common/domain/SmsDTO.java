package com.bluecom.common.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class SmsDTO implements Serializable {

//	protected int mseq;
	/**
	 * 메시지 구분값
	 * 1:SMS, 3:LMS/MMS,5:SMS 알림톡,7:LMS 알림톡
	 */
	protected String msg_type;
	/**
	 * 수신번호
	 */
	protected String dstaddr;
	/**
	 * 발신번호
	 */
	protected String callback;
	protected String text_type;
	protected String subject;
	protected String text;
	protected String request_time;
	
}
