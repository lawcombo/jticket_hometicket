package com.bluecom.ticketing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bluecom.common.domain.MemberSalesCriteria;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSocialCancelDTO;
import com.bluecom.ticketing.domain.BookOpenVO;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.CouponVO;
import com.bluecom.ticketing.domain.EssentialDTO;
import com.bluecom.ticketing.domain.FinishTradeVO;
import com.bluecom.ticketing.domain.MemberSalesVO;
import com.bluecom.ticketing.domain.PaymentInfoDTO;
import com.bluecom.ticketing.domain.ProductDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO;
import com.bluecom.ticketing.domain.RefundHistoryVO;
import com.bluecom.ticketing.domain.RefundVO;
import com.bluecom.ticketing.domain.SaleDTO;
import com.bluecom.ticketing.domain.SaleProductDTO;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.ScheduleDTO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.ShopPaymentsaleVO;
import com.bluecom.ticketing.domain.VerificationKeyVO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;
import com.bluecom.ticketing.domain.WebPaymentStatusDTO;
import com.bluecom.ticketing.domain.WebReservationKeyDTO;

public interface TicketingService{
	// 쿠폰 존재유무 확인 / 2021-10-13 / 조미근
	public int selectCouponCheck(SaleDTO sale) throws Exception;
	// 취소시 web_payment_coupon update / 2021-10-13 / 조미근
	public int updateCouponUseYn(String order_no) throws Exception;
	// 취소시 쿠폰 정보 update / 2021-10-13 / 조미근
	public int updateCouponCancelDate(List<CouponVO> list) throws Exception;
	// 사용한 쿠폰 가져오기 / 2021-10-13 / 조미근
	public List<CouponVO> getCouponByWebPaymetIdx(String order_no) throws Exception;
	// 쿠폰그룹 조회하기 / 2021-10-12 / 조미근
	List<CouponVO> getCouponGroup(CouponVO vo) throws Exception;
	// 쿠폰번호 조회하기 / 2021-10-12 / 조미근
	CouponVO getCouponByCouponNum(CouponVO vo) throws Exception;
	// 구매한 상품정보 가져오기
	public List<SaleProductDTO> selectPurchaseProduct(SaleDTO saleDTO) throws Exception;
	// book_no 가져오기 / 2021-09-27 / 조미근
	List<SaleProductDTO> getBookNoBySaleCode(SaleDTO saleDTO) throws Exception;
	// 수정 / 2021-09-07 / 조미근
	ProductGroupDTO getProductGroups(EssentialDTO essential) throws Exception;
	
	List<ProductDTO> getProducts(ProductGroupDTO productGroup) throws Exception;
	
	List<BookOpenVO> getBookOpenMonth(BookOpenVO bookOpenVO) throws Exception;
	
	List<ScheduleDTO> getSchedule(ScheduleDTO scheduleDTO) throws Exception;
	
	String getVisitorType(String shopCode) throws Exception;

	ProductGroupDTO getProductGroup(ProductGroupDTO productGroup) throws Exception;

	WebPaymentDTO addWebPaymentInfo(PaymentInfoDTO paymentInfo) throws Exception;
	WebPaymentDTO newWebPaymentInfo(WebPaymentDTO paymentInfo) throws Exception; //변경예약건 / 2021-09-16 / 조미근

	List<ProductDTO> getSelectedProducts(List<ProductDTO> products) throws Exception;

	void addWebPaymentStatus(WebPaymentStatusDTO authenticationFinishedStatus) throws Exception;
		
	List<ProductDTO> getProcess2Products(ProductGroupDTO productGroup) throws Exception;
	
	List<SaleDTO> getCheckTicket(SaleDTO saleDTO) throws Exception;
	
	List<SaleProductDTO> getSaleProductDTO(SaleDTO saleDTO) throws Exception;
	List<SaleProductDTO> getSaleProductDTOList(SaleDTO saleDTO) throws Exception; // 예매내역이 N건인 경우 리스트 (추가) / 2021-09-10 / 조미근

	void addWebPaymentPgResult(WebPaymentPgResultDTO pgResult) throws Exception;

	ApiResultVO callTicketApi(WebPaymentPgResultDTO pgResult) throws Exception;
	ApiResultVO callModifyTicketApi(WebPaymentPgResultDTO pgResult) throws Exception;

	ApiResultVO callCancelApi(ApiSocialCancelDTO apiSocialCancel) throws Exception;

	List<FinishTradeVO> getFinishTrade(Map<String, Object> params) throws Exception;

	WebPaymentPgResultDTO getWebPaymentPgResult(String orderNo) throws Exception;
	
	//pg_result 취소승인 기록하기 / 2021-09-28 / 조미근
	void updateWebPaymentPgResult(WebPaymentPgResultDTO pgResult) throws Exception;

	WebPaymentDTO getWebPayment(String moid) throws Exception;

	ScheduleDTO getScheduleByScheduleCode(ScheduleDTO schedule) throws Exception;

	List<RefundVO> getRefund(SaleDTO sale) throws Exception;

	CompanyVO getCompany(String shopCode) throws Exception;

	ShopDetailVO getShopDetail(String shop_code) throws Exception;

	ShopDetailVO getShopDetailByContentMstCd(String contentMstCd) throws Exception;

	SaleVO getSaleSsByOrderNum(SaleVO searchSale) throws Exception;

	List<MemberSalesVO> getMemberSales(MemberSalesCriteria criteria) throws Exception;

	WebReservationKeyDTO selectReserveInfo(String shop_code) throws Exception;
	
	WebReservationKeyDTO selectReserveInfoByCompanyCode(String company_code) throws Exception;

	int getMemberSalesCount(MemberSalesCriteria criteria) throws Exception;

	SaleDTO getSaleBySaleCode(SaleDTO sale) throws Exception;

	VerificationKeyVO getKeys(String shop_code) throws Exception;

	String getShopCode(String contentMstCd) throws Exception;


	// 환불 history 기록 / 2021-10-26 / 조미근
	public int insertRefundHistory(RefundHistoryVO vo) throws Exception;
	
	// 부분환불시 paymentsale 가져오기 / 2021-10-26 / 조미근
	public List<ShopPaymentsaleVO> selectPaymentSale(ShopPaymentsaleVO vo) throws Exception;
	
	// 부분환불시 paymentsale 기록 / 2021-10-26 / 조미근
	public int insertPaymentSale(ShopPaymentsaleVO vo) throws Exception;
	
	
	public Model kisPgPayReult(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception;
	public HashMap<String, String> custPayReult(HttpServletRequest request, HttpServletResponse response, Model model, PaymentInfoDTO paymentInfo) throws Exception;
	public String kisPgPayCancelReult(SaleDTO sale,  HttpServletRequest request, HttpServletResponse response, RedirectAttributes rttr, String redirectPage) throws Exception;
	
	//다이아몬드베이 특정날짜, 특정 회차에 공지 있는지 select
	public HashMap<String, String> selectNoticeInfo(SaleProductDTO vo) throws Exception;
	
}
