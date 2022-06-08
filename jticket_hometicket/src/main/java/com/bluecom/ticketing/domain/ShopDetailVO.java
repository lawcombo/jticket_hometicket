package com.bluecom.ticketing.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ShopDetailVO implements Serializable{
	String company_code = "";
	String shop_code = "";
	String shop_name = "";
	String shop_type = "";
	String content_mst_cd = "";
	String operate_type = "";
	String corporate_number = "";
	String master_name = "";
	String master_tel = "";
	String master_mail = "";
	String postal_code = "";
	String address1 = "";
	String address2 = "";
	String shop_tel = "";
	String shop_fax = "";
	String shop_mail = "";
	String corporate_save = "";
	String corporate_origin = "";
	String charge_name = "";
	String charge_tel = "";
	String charge_mail = "";
	String open_Date = "";
	String upDate_id = "";
	Date upDate_Datetime = null;
	String work_id = "";
	Date work_Datetime = null;
	String status = "";
	String pt_save_yn = "";
	String belong_shop = "";
	String belong_type = "";
	String shop_class = "";
	String delete_yn = "";
	String param_enc_key = "";
	String company_mst_cd = "";
	String person_type = "";
	String sale_type = "";
	String piece_ticket_yn = "";
	String weekday_use_time;
	String weekend_use_time;
	
	// bc_company
	String alimtalk_comp_code;
	String alimtalk_sender_key;
	String comp_tel;

}
