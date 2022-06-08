package com.bluecom.common.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class AlimTalkTemplateDTO implements Serializable {
	int idx;
	String shop_code;
	String type;
	String product_group_kind;
	String msg_type;
	String callback;
	String subject;
	String text_type;
	String text;
	String ext_col1;
	String sender_key;
	String template_code;
	String changeflag;
	String writer;
	Date write_time;
}
