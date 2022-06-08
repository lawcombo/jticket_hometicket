package com.bluecom.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluecom.common.domain.CompanyWebReservationKeyVO;
import com.bluecom.common.service.CommonService;
import com.bluecom.ticketing.domain.CompanyVO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("commonService")
public class CommonServiceImpl extends EgovAbstractServiceImpl implements CommonService {
	
	@Autowired
	private CommonDAO commonMapper;
	

	@Override
	public CompanyWebReservationKeyVO selectWebReservationInfoData(String company_code) throws Exception {
		return commonMapper.selectWebReservationInfoData(company_code);
	}	
	
	
	@Override
	public CompanyVO selectCompanyByContentMstCd(String content_mst_cd) throws Exception {		
		return commonMapper.selectCompanyByContentMstCd(content_mst_cd);		
	}
	
}
