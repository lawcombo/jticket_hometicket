package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class CompanyVO implements Serializable{
	String company_code;
	String company_name;
	String status;
	String site_mst_cd;
	String corporate_number;
	String master_name;
	String master_tel;
	String master_mail;
	String postal_code;
	String address1;
	String address2;
	String comp_tel;
	String comp_fax;
	String comp_mail;
	String corporate_save;
	String corporate_origin;
	String charge_name;
	String charge_tel;
	String charge_mail;
	String open_Date;
	String upDate_id;
	Date upDate_Datetime;
	String work_id;
	Date work_Datetime;
	String package_yn;

}
