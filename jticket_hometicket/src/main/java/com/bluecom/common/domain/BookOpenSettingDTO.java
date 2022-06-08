package com.bluecom.common.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class BookOpenSettingDTO extends BaseDTO implements Serializable{
	
	String shopCode;
	String year;
	String month;
	String searchStartDate;
	String searchEndDate;
	
	String playDate;
	String playStartDate;
	String playEndDate;
	int playStartHour;
	int playStartMinute;
	Date startDate;
	int beforeDays;
//	String start_time;
	String bookYn;
	int daysDiff;
}
