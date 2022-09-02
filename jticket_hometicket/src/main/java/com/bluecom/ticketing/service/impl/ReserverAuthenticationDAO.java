package com.bluecom.ticketing.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bluecom.ticketing.domain.BookOpenVO;
import com.bluecom.ticketing.domain.ProductDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO;
import com.bluecom.ticketing.domain.ReserverInfoDTO;
import com.bluecom.ticketing.domain.ScheduleDTO;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class ReserverAuthenticationDAO extends EgovAbstractMapper {


	public List<ProductGroupDTO> selectProductGroups(String contentMstCd) throws Exception {
		
		return selectList("ticketingMapper.selectProductGroups", contentMstCd);
	}

	public List<ProductDTO> selectProducts(ProductGroupDTO productGroup) throws Exception {
		
		return selectList("ticketingMapper.selectProduct", productGroup);
	}
	
	public List<BookOpenVO> selectBookOpen(BookOpenVO bookOpenVO) throws Exception {
		
		return selectList("ticketingMapper.selectBookOpenList", bookOpenVO);
	}

	public List<ScheduleDTO> selectSchedule(ScheduleDTO scheduleDTO) {
		return selectList("ticketingMapper.selectSchedule", scheduleDTO);
	}

	public String selectPersonType(String shopCode) throws Exception {
		
		return selectOne("ticketingMapper.selectPersonType", shopCode);
	}

	public ProductGroupDTO selectProductGroup(ProductGroupDTO productGroup) {
		
		return selectOne("ticketingMapper.selectProductGroup", productGroup);
	}

	public int insertReserverInfo(ReserverInfoDTO reserverInfo) throws Exception{
		
		return insert("reserverAuthenticationMapper.insertReserverInfo", reserverInfo);
		
	}
	
	
	
	public int checkCustReserve(HashMap<String, String> reserverInfo) throws Exception{
		return selectOne("reserverAuthenticationMapper.checkCustReserve", reserverInfo);
	}

}
