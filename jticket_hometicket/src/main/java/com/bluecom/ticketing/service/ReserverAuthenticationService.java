package com.bluecom.ticketing.service;

import com.bluecom.ticketing.domain.ReserverInfoDTO;

public interface ReserverAuthenticationService{
	
	int addReserverInfo(ReserverInfoDTO info) throws Exception;
	
	int checkCustReserve(ReserverInfoDTO info) throws Exception;

}
