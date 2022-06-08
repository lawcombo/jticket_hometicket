package com.bluecom.common.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class AlimTalkDTO extends SmsDTO implements Serializable{

	/**
	 * 알림톡/친구톡
	 * R/r:Real, P/p:PUSH,O/o:Pollings
	 */
	String ext_col1;
	/**
	 * 알림톡,친구톡 발신 프로필키
	 */
	String sender_key;
	/**
	 * 알림톡 템플릿코드
	 */
	String template_code;
	/**
	 * MSG_TYPE이 7, 8, 9일때
	 * ‘0’ ­ 알림/친구톡으로만 전송
  	 *	‘1’ ­ 알림/친구톡으로 전송 실패시
	 *	일반 LMS/MMS로 발송(친구톡이미
	 *	지일 경우는 첨부파일을 같이 보내
	 *	야하며 MMS로 전송된다.)
	 *	‘2’ ­ 알림/친구톡으로 전송 실패시
	 *	대체문자로 발송(친구톡 이미지일 경
	 *	우는 첨부파일이 무시됨
	 */
	String changeflag;
}
