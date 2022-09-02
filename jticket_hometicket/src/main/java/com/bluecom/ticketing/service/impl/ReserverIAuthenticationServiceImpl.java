package com.bluecom.ticketing.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluecom.ticketing.domain.ReserverInfoDTO;
import com.bluecom.ticketing.service.ReserverAuthenticationService;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class ReserverIAuthenticationServiceImpl extends EgovAbstractServiceImpl implements ReserverAuthenticationService {
	
	@Autowired
	private ReserverAuthenticationDAO reserverAuthenticationMapper;
	

	@Override
	public int addReserverInfo(ReserverInfoDTO info) throws Exception {
		
		return reserverAuthenticationMapper.insertReserverInfo(info);
	}
	
	@Override
	public int checkCustReserve(ReserverInfoDTO info) throws Exception {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		String contentMstCd = info.getContentMstCd();
		contentMstCd = contentMstCd.substring(0, contentMstCd.indexOf("_"));
		
		map.put("contentMstCd", contentMstCd);
		map.put("custCode", info.getCustCode());
		map.put("custRegNo", info.getCustRegNo());
		
		return reserverAuthenticationMapper.checkCustReserve(map);
	}
	
}
