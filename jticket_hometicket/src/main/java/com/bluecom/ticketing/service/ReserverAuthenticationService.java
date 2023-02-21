package com.bluecom.ticketing.service;

import javax.servlet.http.HttpServletRequest;

import com.bluecom.ticketing.domain.ReserverInfoDTO;
import com.bluecom.ticketing.domain.SelfAuthenticationDTO;

public interface ReserverAuthenticationService{
	
	int addReserverInfo(ReserverInfoDTO info) throws Exception;
	
	int checkCustReserve(ReserverInfoDTO info) throws Exception;

	
	SelfAuthenticationDTO getSelfAuthenticationEncodedData(HttpServletRequest request, SelfAuthenticationDTO selfAuthentication) throws Exception;
}
