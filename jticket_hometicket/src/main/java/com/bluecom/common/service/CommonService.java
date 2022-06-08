package com.bluecom.common.service;

import com.bluecom.common.domain.CompanyWebReservationKeyVO;
import com.bluecom.ticketing.domain.CompanyVO;

public interface CommonService{

	CompanyWebReservationKeyVO selectWebReservationInfoData(String company_code) throws Exception;
	
	CompanyVO selectCompanyByContentMstCd(String content_mst_cd) throws Exception;
	
}
