package com.bluecom.common.service.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bluecom.common.domain.CompanyWebReservationKeyVO;
import com.bluecom.ticketing.domain.CompanyVO;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class CommonDAO extends EgovAbstractMapper {

	public CompanyWebReservationKeyVO selectWebReservationInfoData(String company_code) throws Exception {
		return selectOne("commonMapper.selectWebReservationInfoData", company_code);
	}
	
	public CompanyVO selectCompanyByContentMstCd(String content_mst_cd) throws Exception {
		return selectOne("commonMapper.selectCompanyByContentMstCd", content_mst_cd);
		
	}
	
	
}
