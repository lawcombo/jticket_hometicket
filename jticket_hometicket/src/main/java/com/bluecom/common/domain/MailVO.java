package com.bluecom.common.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class MailVO implements Serializable{
	
	String from = "";
	String to = "";
	String subject = "";
	String text;

	String reserverName;
	String shopName;
	String reservationDate;
	
	String product_group_name;
	String expireDateOrScheduleName;
	String expireDateOrSchedule;
	String productGroupName;
	String reserverPhone;
	String ticketGroupInfo;
	String totalCount;
	String totalFee;
	
	String payMethod;
	String payAmount;
	String tradeId;
	String tradeDate;
	
//	String username;
//	String password;
//	String hostname;
//	String port;
}
