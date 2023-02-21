package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class TicketValidVO implements Serializable{

	String shop_code;
	String valid_period;
	Date update_datetime;
	String update_id;

}
