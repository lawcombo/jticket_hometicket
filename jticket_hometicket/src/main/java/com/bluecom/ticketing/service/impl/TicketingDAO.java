package com.bluecom.ticketing.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bluecom.common.domain.MemberSalesCriteria;
import com.bluecom.ticketing.domain.BookOpenVO;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.CouponVO;
import com.bluecom.ticketing.domain.EssentialDTO;
import com.bluecom.ticketing.domain.FinishTradeVO;
import com.bluecom.ticketing.domain.MemberSalesVO;
import com.bluecom.ticketing.domain.ProductDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO;
import com.bluecom.ticketing.domain.RefundHistoryVO;
import com.bluecom.ticketing.domain.RefundVO;
import com.bluecom.ticketing.domain.ReserverInfoDTO;
import com.bluecom.ticketing.domain.SaleDTO;
import com.bluecom.ticketing.domain.SaleProductDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.ScheduleDTO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.ShopPaymentsaleVO;
import com.bluecom.ticketing.domain.VerificationKeyVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;
import com.bluecom.ticketing.domain.WebPaymentProductDTO;
import com.bluecom.ticketing.domain.WebPaymentStatusDTO;
import com.bluecom.ticketing.domain.WebPaymentVisitorDTO;
import com.bluecom.ticketing.domain.WebReservationKeyDTO;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class TicketingDAO extends EgovAbstractMapper {

	// 쿠폰 존재유무 확인 / 2021-10-13 / 조미근
	public int selectCouponCheck(SaleDTO sale) throws Exception{
		return selectOne("ticketingMapper.selectCouponCheck", sale);
	}
	
	// 취소시 web_payment_coupon update / 2021-10-13 / 조미근
	public int updateCouponUseYn(String order_no) throws Exception{
		return update("ticketingMapper.updateCouponUseYn", order_no);
	}
	
	// 취소시 쿠폰 정보 update / 2021-10-13 / 조미근
	public int updateCouponCancelDate(List<CouponVO> list) throws Exception{
		return update("ticketingMapper.updateCouponCancelDate", list);
	}
	
	// 사용한 쿠폰 가져오기 / 2021-10-13 / 조미근
	public List<CouponVO> selectCouponByWebPaymetIdx(String order_no) throws Exception{
		return selectList("ticketingMapper.selectCouponByWebPaymetIdx", order_no);
	}
	
	// 쿠폰 사용 정보 update / 2021-10-13 / 조미근
	public int updateCouponUseDate(List<CouponVO> list) throws Exception{
		return insert("ticketingMapper.updateCouponUseDate", list);
	}
	
	// 쿠폰 사용 insert / 2021-10-13 / 조미근
	public int insertWebPaymentCoupon(List<CouponVO> list) throws Exception{
		return insert("ticketingMapper.insertWebPaymentCoupon", list);
	}
	
	// 쿠폰그룹 조회하기 / 2021-10-12 / 조미근
	public List<CouponVO> selectCouponGroup(CouponVO vo) throws Exception{
		return selectList("ticketingMapper.selectCouponGroup", vo);
	}
	
	// 쿠폰번호 조회하기 / 2021-10-12 / 조미근
	public CouponVO selectCouponByCouponNum(CouponVO vo) throws Exception{
		return selectOne("ticketingMapper.selectCouponByCouponNum", vo);
	}
	
	// 구매한 상품정보 가져오기
	public List<SaleProductDTO> selectPurchaseProduct(SaleDTO saleDTO) throws Exception{
		return selectList("ticketingMapper.selectPurchaseProduct", saleDTO);
	}
	
	// book_no 가져오기 / 2021-09-27 / 조미근
	public List<SaleProductDTO> selectBookNoBySaleCode(SaleDTO saleDTO) throws Exception {
		return selectList("ticketingMapper.selectBookNoBySaleCode", saleDTO);
	}
	
	// 수정 / 2021-09-07 / 조미근
	public ProductGroupDTO selectProductGroups(EssentialDTO essential) throws Exception {

		return selectOne("ticketingMapper.selectProductGroups", essential);
	}

	public List<ProductDTO> selectProducts(ProductGroupDTO productGroup) throws Exception {

		return selectList("ticketingMapper.selectProducts", productGroup);
	}

	public List<BookOpenVO> selectBookOpen(BookOpenVO bookOpenVO) throws Exception {

		return selectList("ticketingMapper.selectBookOpenList", bookOpenVO);
	}

	public List<ScheduleDTO> selectSchedule(ScheduleDTO scheduleDTO) throws Exception {
		return selectList("ticketingMapper.selectSchedule", scheduleDTO);
	}

	public String selectVisitorType(String shopCode) throws Exception {

		return selectOne("ticketingMapper.selectVisitorType", shopCode);
	}

	public ProductGroupDTO selectProductGroup(ProductGroupDTO productGroup) throws Exception {

		return selectOne("ticketingMapper.selectProductGroup", productGroup);
	}

	public void insertWebPaymentProducts(List<WebPaymentProductDTO> webPaymentProducts) throws Exception {
		
		insert("ticketingMapper.insertWebPaymentProducts", webPaymentProducts);
	}
	public int insertWebPayment(WebPaymentDTO webPaymentDTO)  throws Exception{
		insert("ticketingMapper.insertWebPayment", webPaymentDTO);
		return webPaymentDTO.getIdx();
	}

	public void insertWebPaymentVisitors(List<WebPaymentVisitorDTO> webPaymentVisitors) throws Exception {

		insert("ticketingMapper.insertWebPaymentVisitors", webPaymentVisitors);
	}

	public void insertWebPaymentStatusWithWebPaymentIdx(WebPaymentStatusDTO status) throws Exception {

		insert("ticketingMapper.insertWebPaymentStatusWithWebPaymentIdx", status);
	}
	
	public void insertWebPaymentStatus(WebPaymentStatusDTO status) throws Exception {

		insert("ticketingMapper.insertWebPaymentStatus", status);
	}

	public List<ProductDTO> selectSelectedProducts(List<ProductDTO> products) throws Exception {

		return selectList("ticketingMapper.selectSelectedProducts", products);
	}

	public String selectWebPaymentOrderNo(int idx) throws Exception{
		
		return selectOne("ticketingMapper.selectWebPaymentOrderNo", idx);
	}

	public List<ProductDTO> selectProcess2Products(ProductGroupDTO productGroup) throws Exception {
		return selectList("ticketingMapper.selectProcess2Product", productGroup);
	}

	public List<SaleDTO> selectCheckTicket(SaleDTO saleDTO) throws Exception {
		return selectList("ticketingMapper.selectCheckTicket", saleDTO);
	}

	public List<SaleProductDTO> selectSaleProduct(SaleDTO saleDTO) throws Exception {
		return selectList("ticketingMapper.selectSaleProduct", saleDTO);
	}
	
	// 예매내역이 N건인 경우 리스트 (추가) / 2021-09-10 / 조미근
	public List<SaleProductDTO> selectSaleProductList(SaleDTO saleDTO) throws Exception {
		return selectList("ticketingMapper.selectSaleProductList", saleDTO);
	}

	public void updateWebPaymentPayId(WebPaymentStatusDTO status) throws Exception {
		
		update("ticketingMapper.updateWebPaymentPayId", status);
	}

	public void insertWebPaymentPgResult(WebPaymentPgResultDTO pgResult) throws Exception {
		
		insert("ticketingMapper.insertWebPaymentPgResult", pgResult);
	}
	
	//pg_result 취소승인 기록하기 / 2021-09-28 / 조미근
	public void updateWebPaymentPgResult(WebPaymentPgResultDTO pgResult) throws Exception{
		update("ticketingMapper.updateWebPaymentPgResult", pgResult);
	}

	public WebPaymentDTO selectWebPaymentByOrderNo(String idx) throws Exception{

		return selectOne("ticketingMapper.selectWebPaymentByOrderNo", idx);
	}

	public void updateReserverEmail(ReserverInfoDTO reserver) throws Exception {

		update("ticketingMapper.updateReserverEmail", reserver);
		
	}

	public List<WebPaymentProductDTO> selectWebPaymentProductsByOrderNo(String orderNo) throws Exception {

		return selectList("ticketingMapper.selectWebPaymentProductsByOrderNo", orderNo);
	}

	public String selectWebSaleSequence() throws Exception{
		return selectOne("ticketingMapper.selectWebSaleSequence");
	}
	
	public String selectWebSaleSequences(int count) throws Exception{
		return selectOne("ticketingMapper.selectWebSaleSequences", count);
	}

	public List<FinishTradeVO> selectFinishTrade(Map<String, Object> params) throws Exception{
	
		return selectList("ticketingMapper.selectFinishTrade", params);
	}

	public WebPaymentPgResultDTO selectWebPaymentPgResult(String orderNo) {

		return selectOne("ticketingMapper.selectWebPaymentPgResult", orderNo);
	}

	public ScheduleDTO selectScheduleByScheduleCode(ScheduleDTO schedule) {
		
		return selectOne("ticketingMapper.selectScheduleByScheduleCode", schedule);
	}

	public List<RefundVO> selectRefund(SaleDTO sale) throws Exception{

		return selectList("ticketingMapper.selectRefund", sale);
	}

	public CompanyVO selectCompany(String shopCode) throws Exception{
		
		return selectOne("ticketingMapper.selectCompany", shopCode);
	}

	public ShopDetailVO selectShopDetail(String shopCode) throws Exception{
		
		return selectOne("ticketingMapper.selectShopDetail", shopCode);
	}

	public ShopDetailVO selectShopDetailByContentMstCd(String contentMstCd) throws Exception{

		return selectOne("ticketingMapper.selectShopDetailByContentMstCd", contentMstCd);
	}

	public SaleVO selectSaleSsByOrderNum(SaleVO searchSale) throws Exception{

		return selectOne("ticketingMapper.selectSaleSsByOrderNum", searchSale);
	}

	public List<MemberSalesVO> selectMemberSales(MemberSalesCriteria criteria) throws Exception{

		return selectList("ticketingMapper.selectMemberSales", criteria);
	}


	public WebReservationKeyDTO selectReserveInfo(String shop_code) throws Exception {
		return selectOne("ticketingMapper.selectReserveInfo", shop_code);
	}
	
	public WebReservationKeyDTO selectReserveInfoByCompanyCode(String company_code) throws Exception {
		return selectOne("ticketingMapper.selectReserveInfoByCompanyCode", company_code);
	}

	public int selectMemberSalesCount(MemberSalesCriteria criteria) throws Exception{

		return selectOne("ticketingMapper.selectMemberSalesCount", criteria);
	}

	public SaleDTO selectSaleBySaleCode(SaleDTO sale) throws Exception{

		return selectOne("ticketingMapper.selectSaleBySaleCode", sale);

	}

	public VerificationKeyVO selectKeys(String shopCode) throws Exception{
		
		return selectOne("ticketingMapper.selectKeys", shopCode);
	}

	public String selectShopCode(String contentMstCd) throws Exception{

		return selectOne("ticketingMapper.selectShopCode", contentMstCd);
	}

	public int selectRealFee(HashMap<String, String> param) throws Exception {
	
		return selectOne("ticketingMapper.selectRealFee", param);
	}
	
	// 환불 history 기록 / 2021-10-26 / 조미근
	public int insertRefundHistory(RefundHistoryVO vo) throws Exception{
		return insert("ticketingMapper.insertRefundHistory", vo);
	}
	
	// 부분환불시 paymentsale 가져오기 / 2021-10-26 / 조미근
	public List<ShopPaymentsaleVO> selectPaymentSale(ShopPaymentsaleVO vo) throws Exception{
		return selectList("ticketingMapper.selectPaymentSale", vo);
	}
	
	// 부분환불시 paymentsale 기록 / 2021-10-26 / 조미근
	public int insertPaymentSale(ShopPaymentsaleVO vo) throws Exception{
		return insert("ticketingMapper.insertPaymentSale", vo);
	}
	
	
	
	
	public VerificationKeyVO selectSitekeyInfo(String ordNo) throws Exception{
		return selectOne("ticketingMapper.selectSitekeyInfo", ordNo);
	}
}
