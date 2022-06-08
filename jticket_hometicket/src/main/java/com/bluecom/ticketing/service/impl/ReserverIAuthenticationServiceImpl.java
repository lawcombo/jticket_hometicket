package com.bluecom.ticketing.service.impl;

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
	
}
