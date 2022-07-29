package com.bluecom.ticketing.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.bluecom.common.domain.MemberSalesCriteria;
import com.bluecom.ticketing.domain.ApiCardInfoDTO;
import com.bluecom.ticketing.domain.ApiParamDTO;
import com.bluecom.ticketing.domain.ApiPaymentsInfoDTO;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSaleProductDTO;
import com.bluecom.ticketing.domain.ApiSocialCancelDTO;
import com.bluecom.ticketing.domain.ApiSocialSaleDTO;
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
import com.bluecom.ticketing.domain.WebPaymentProductDTO;
import com.bluecom.ticketing.domain.WebPaymentStatusDTO;
import com.bluecom.ticketing.domain.WebReservationKeyDTO;
import com.bluecom.ticketing.service.TicketingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.property.EgovPropertyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketingServiceImpl extends EgovAbstractServiceImpl implements TicketingService {
	
	@Autowired
	private TicketingDAO ticketingMapper;
	
	@Autowired
	EgovPropertyService propertyService;
	
	/*
	 *  쿠폰 존재유무 확인 / 2021-10-13 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#selectCouponCheck(com.bluecom.ticketing.domain.SaleDTO)
	 */
	@Override
	public int selectCouponCheck(SaleDTO sale) throws Exception{
		return ticketingMapper.selectCouponCheck(sale);
	}
	/*
	 *  취소시 web_payment_coupon update / 2021-10-13 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#updateCouponUseYn(java.lang.String)
	 */
	@Override
	public int updateCouponUseYn(String order_no) throws Exception{
		return ticketingMapper.updateCouponUseYn(order_no);
	}
	/*
	 *  취소시 쿠폰 정보 update / 2021-10-13 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#updateCouponCancelDate(java.util.List)
	 */
	@Override
	public int updateCouponCancelDate(List<CouponVO> list) throws Exception{
		return ticketingMapper.updateCouponCancelDate(list);
	}
	/*
	 *  사용한 쿠폰 가져오기 / 2021-10-13 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#selectCouponByWebPaymetIdx(int)
	 */
	@Override
	public List<CouponVO> getCouponByWebPaymetIdx(String order_no) throws Exception{
		List<CouponVO> list = ticketingMapper.selectCouponByWebPaymetIdx(order_no);
		
		int couponFee=0;
		if(!list.isEmpty()) {
			for(CouponVO vo : list) {
				couponFee += Integer.parseInt(vo.getCpn_sale_cost());
			}
			list.get(0).setCouponFee(String.valueOf(couponFee));
		}
		return list;
	}
	/*
	 * 쿠폰그룹 조회하기 / 2021-10-12 / 조미근(non-Javadoc)
	 * @see com.bluecom.ticketing.service.TicketingService#selectCouponGroup(com.bluecom.ticketing.domain.CouponVO)
	 */
	@Override
	public List<CouponVO> getCouponGroup(CouponVO vo) throws Exception{
		return ticketingMapper.selectCouponGroup(vo);
	}
	/*
	 *  쿠폰번호 조회하기 / 2021-10-12 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#selectCoupon(com.bluecom.ticketing.domain.CouponVO)
	 */
	@Override
	public CouponVO getCouponByCouponNum(CouponVO vo) throws Exception{
		return ticketingMapper.selectCouponByCouponNum(vo);
	}
	/*
	 *  구매한 상품정보 가져오기
	 * @see com.bluecom.ticketing.service.TicketingService#selectPurchaseProduct(com.bluecom.ticketing.domain.SaleDTO)
	 */
	@Override
	public List<SaleProductDTO> selectPurchaseProduct(SaleDTO saleDTO) throws Exception{
		List<SaleProductDTO> list = ticketingMapper.selectPurchaseProduct(saleDTO);
		Pattern regxDate = Pattern.compile("\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])");
		String last_sale_date = "";
		Matcher m = null;
		
		log.debug("======================================================================");
		for(SaleProductDTO vo : list) {
			if(org.springframework.util.StringUtils.hasText(vo.getRemark()) ) {
				if(regxDate.matcher(vo.getRemark() ).find() ) {
					m = regxDate.matcher(vo.getRemark() );
					m.find();
					last_sale_date = m.group(0);
					log.debug(m.group(0) );
					log.debug(m.group() );
					vo.setLast_sale_date(last_sale_date);
				}
			}
		}
		log.debug("======================================================================");
		return list;
	}
	/*
	 * book_no 가져오기 / 2021-09-27 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#getSaleProductDTOList(com.bluecom.ticketing.domain.SaleDTO)
	 */
	@Override
	public List<SaleProductDTO> getBookNoBySaleCode(SaleDTO saleDTO) throws Exception {
		return ticketingMapper.selectBookNoBySaleCode(saleDTO);
	}
	/*
	 * 수정 / 2021-09-07 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#getProductGroups(com.bluecom.ticketing.domain.EssentialDTO)
	 */
	@Override
	public ProductGroupDTO getProductGroups(EssentialDTO essential) throws Exception {
	
		return ticketingMapper.selectProductGroups(essential);
	}
	
	@Override
	public List<BookOpenVO> getBookOpenMonth(BookOpenVO bookOpenVO) throws Exception {
		return ticketingMapper.selectBookOpen(bookOpenVO);
	}
	
	@Override
	public ProductGroupDTO getProductGroup(ProductGroupDTO productGroup) throws Exception {
		
		return ticketingMapper.selectProductGroup(productGroup);
	}
		
	@Override
	public List<ProductDTO> getProducts(ProductGroupDTO productGroup) throws Exception {

		return ticketingMapper.selectProducts(productGroup);
	}
		
	@Override
	public String getVisitorType(String shopCode) throws Exception {

		return ticketingMapper.selectVisitorType(shopCode);
	}

	@Override
	public List<ScheduleDTO> getSchedule(ScheduleDTO scheduleDTO) throws Exception {
		return ticketingMapper.selectSchedule(scheduleDTO);
	}
	
	@Override
	public List<ProductDTO> getSelectedProducts(List<ProductDTO> products) throws Exception {
		
		return ticketingMapper.selectSelectedProducts(products);
	}

	@Transactional
	@Override
	public WebPaymentDTO addWebPaymentInfo(PaymentInfoDTO paymentInfo) throws Exception {
		
		// 예매자 이메일 기록
		ticketingMapper.updateReserverEmail(paymentInfo.getReserver());
		
		
		// 웹 예매 정보
		WebPaymentDTO webPayment = new WebPaymentDTO();
		webPayment.setReserver_authentication_idx(paymentInfo.getReserver().getIdx());
		webPayment.setProduct_group(paymentInfo.getProductGroup().getProduct_group_code());
		webPayment.setTotal_count(paymentInfo.getTotalCount());
		webPayment.setTotal_fee(paymentInfo.getTotalFee());
		webPayment.setPay_method(paymentInfo.getPayMethod());
		webPayment.setVisitor_type(paymentInfo.getVisitorType());
		webPayment.setContent_mst_cd(paymentInfo.getProductGroup().getContent_mst_cd());
		webPayment.setPiece_ticket_yn(paymentInfo.getProductGroup().getPiece_ticket_yn());
		webPayment.setShop_code(paymentInfo.getProductGroup().getShop_code());
		webPayment.setProduct_group_kind(paymentInfo.getProductGroup().getProduct_group_kind());
		webPayment.setSchedule_code(paymentInfo.getSchedule() == null ? "" : paymentInfo.getSchedule().getSchedule_code());
		webPayment.setPlay_sequence(paymentInfo.getSchedule() == null ? "1" :paymentInfo.getSchedule().getPlay_sequence());
		webPayment.setPlay_date(paymentInfo.getSchedule() == null ? "" :paymentInfo.getPlay_date());
		webPayment.setProduct_group_name(paymentInfo.getProductGroup().getProduct_group_name());
		webPayment.setStart_time(paymentInfo.getSchedule() == null ? "" : paymentInfo.getSchedule().getStart_time());
		webPayment.setEnd_time(paymentInfo.getSchedule() == null ? "" : paymentInfo.getSchedule().getEnd_time());
		webPayment.setAgree_1(paymentInfo.isAgree_1() ? "1" : "0");
		webPayment.setAgree_2(paymentInfo.isAgree_2() ? "1" : "0");
		
		
		//일단 제주맥주일경우, 마케팅 정보동의 체크
		if(paymentInfo.getProductGroup().getContent_mst_cd().toString().contains("JEJUBEER"))
		{
			//sms
			if(paymentInfo.getAgree_4() != null && "0".equals(paymentInfo.getAgree_4()))
			{//동의:0, 비동의:null,  bc_web_payment Table에는 동의:1, 비동의:0
				webPayment.setAgree_4("1");
			}
			else
			{
				webPayment.setAgree_4("0");
			}
			
			if(paymentInfo.getAgree_5() != null && "0".equals(paymentInfo.getAgree_5()))
			{//동의:0, 비동의:null
				webPayment.setAgree_5("1");
			}
			else
			{
				webPayment.setAgree_5("0");
			}
		}
		
		
		
		int enteredCount = ticketingMapper.insertWebPayment(webPayment);
		if(enteredCount <= 0) {
			throw new Exception("예매 정보를 생성 할 수 없습니다.");
		}
		paymentInfo.setIdx(webPayment.getIdx());
		
		// 상품 기록
		List<WebPaymentProductDTO> webPaymentProducts = new ArrayList<>();
		for(ProductDTO product : paymentInfo.getProducts()) {
			WebPaymentProductDTO webPaymentProduct = new WebPaymentProductDTO();
			webPaymentProduct.setWeb_payment_idx(paymentInfo.getIdx());
			webPaymentProduct.setProduct_code(product.getProduct_code());
			webPaymentProduct.setProduct_name(product.getProduct_name());
			webPaymentProduct.setProduct_fee(product.getProduct_fee());
			webPaymentProduct.setCount(product.getCount());
			
			webPaymentProducts.add(webPaymentProduct);
		}
		ticketingMapper.insertWebPaymentProducts(webPaymentProducts);
		
		if(!paymentInfo.getCouponFee().equals("0")) {
			for(CouponVO vo : paymentInfo.getCoupon()) {
				vo.setWeb_payment_idx(webPayment.getIdx());
			}
			ticketingMapper.insertWebPaymentCoupon(paymentInfo.getCoupon()); // bc_web_payment_coupon insert
//			ticketingMapper.updateCouponUseDate(paymentInfo.getCoupon()); // mb_coupon_num update
		}
	
		// 웹 예매 상태 기록
		WebPaymentStatusDTO status = WebPaymentStatusDTO.builder()
				.web_payment_idx(paymentInfo.getIdx())
				.status("결제시도")
				.build();
		ticketingMapper.insertWebPaymentStatusWithWebPaymentIdx(status);
		
		VerificationKeyVO keys = getKeys(webPayment.getShop_code());
		
		webPayment.setMerchantKey(keys.getPay_merchant_key());
		webPayment.setMerchantID(keys.getPay_merchant_id());
//		webPayment.setProduct_group_name(paymentInfo.getProductGroup().getProduct_group_name());
		webPayment.setReserverName(paymentInfo.getReserver().getName());
		webPayment.setReserverPhone(paymentInfo.getReserver().getPhone());
		webPayment.setReserverEmail(paymentInfo.getReserver().getEmail());
		
		// 예매번호 가져오기
		String orderNo = ticketingMapper.selectWebPaymentOrderNo(webPayment.getIdx());
		webPayment.setOrder_no(orderNo);
		
		webPayment.setCouponFee(paymentInfo.getCouponFee());
		int realFee = (paymentInfo.getTotalFee().intValue() - Integer.parseInt(paymentInfo.getCouponFee()) );
		webPayment.setFee(Integer.toString(realFee));
		
		return webPayment;
		
	}
	
	/*
	 * 변경 예약건
	 * @see com.bluecom.ticketing.service.TicketingService#newWebPaymentInfo(com.bluecom.ticketing.domain.WebPaymentDTO)
	 */
	@Transactional
	@Override
	public WebPaymentDTO newWebPaymentInfo(WebPaymentDTO paymentInfo) throws Exception {
		
		// 예매자 이메일 기록
		//ticketingMapper.updateReserverEmail(paymentInfo.getReserver());
		
		int enteredCount = ticketingMapper.insertWebPayment(paymentInfo); //기존 내용으로 새 order_no 생성
		if(enteredCount <= 0) {
			throw new Exception("예매 정보를 생성 할 수 없습니다.");
		}
		paymentInfo.setIdx(enteredCount);
		
		// 상품 기록 - 기존 order_num기준으로 가지고 옴
		List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(paymentInfo.getOrg_order_no());
		for(int i=0; i< products.size(); i++) {
			products.get(i).setWeb_payment_idx(enteredCount);
		}
		ticketingMapper.insertWebPaymentProducts(products); //상품 내용 새 order_num으로 기록
		
		// 기존 bc_web_payment_coupon 가져오기
		List<CouponVO> coupons = ticketingMapper.selectCouponByWebPaymetIdx(paymentInfo.getOrg_order_no());
		if(!coupons.isEmpty()) {
			for(CouponVO vo : coupons) {
				vo.setWeb_payment_idx(enteredCount);
			}
			ticketingMapper.insertWebPaymentCoupon(coupons); // bc_web_payment_coupon insert
			ticketingMapper.updateCouponUseYn(paymentInfo.getOrg_order_no()); // 기존 쿠폰은 use_yn=0 로 변경
			paymentInfo.setCoupon("1");
		}
		
		// 예매번호 가져오기
		String orderNo = ticketingMapper.selectWebPaymentOrderNo(enteredCount);
		paymentInfo.setOrder_no(orderNo);
		
		return paymentInfo;
		
	}
	
	@Override
	public void addWebPaymentStatus(WebPaymentStatusDTO status) throws Exception {
		
		ticketingMapper.insertWebPaymentStatus(status);
		//ticketingMapper.updateWebPaymentPayId(status);
		
	}
	
	@Override
	public List<ProductDTO> getProcess2Products(ProductGroupDTO productGroup) throws Exception {
		return ticketingMapper.selectProcess2Products(productGroup);
	}

	@Override
	public List<SaleDTO> getCheckTicket(SaleDTO saleDTO) throws Exception {
		List<SaleDTO> saleDtoList = ticketingMapper.selectCheckTicket(saleDTO);
		if(saleDtoList.size() == 0) {
			SaleDTO newSaleDTO = new SaleDTO();
			newSaleDTO.setResult_code("0");
			newSaleDTO.setResult_message("검색 결과가 없습니다.");
			saleDtoList.add(newSaleDTO);
		} else {
			saleDtoList.get(0).setResult_code("1");
		}
		return saleDtoList;
	}

	@Override
	public List<SaleProductDTO> getSaleProductDTO(SaleDTO saleDTO) throws Exception {
		return ticketingMapper.selectSaleProduct(saleDTO);
	}
	
	/*
	 * 예매내역이 N건인 경우 리스트 (추가) / 2021-09-10 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#getSaleProductDTO(com.bluecom.ticketing.domain.SaleDTO)
	 */
	@Override
	public List<SaleProductDTO> getSaleProductDTOList(SaleDTO saleDTO) throws Exception {
		
		log.debug("[CONTENT_MST_CD] ===============> " + saleDTO.getContentMstCd());
		
		List<SaleProductDTO> saleProducts = ticketingMapper.selectSaleProductList(saleDTO);
		
		log.debug("[saleProducts] =================> " + saleProducts);
		
		for(SaleProductDTO saleProduct: saleProducts) {
			
			HashMap<String, String> param = new HashMap<>();
			param.put("shop_code", saleProduct.getSale_shop_code());
			param.put("ss_sale_code", saleProduct.getSale_code());
			// 실제 결제 금액 체크(SS는 복합결제 없음)
			int realFee = ticketingMapper.selectRealFee(param);
			saleProduct.setReal_fee(realFee);
		}
		
		return saleProducts;
	}
	
	@Override
	public void addWebPaymentPgResult(WebPaymentPgResultDTO pgResult) throws Exception {
		
		ticketingMapper.insertWebPaymentPgResult(pgResult);		
	}
	
	/*
	 * pg_result 취소승인 기록하기 / 2021-09-28 / 조미근
	 * @see com.bluecom.ticketing.service.TicketingService#updateWebPaymentPgResult(com.bluecom.ticketing.domain.WebPaymentPgResultDTO)
	 */
	@Override
	public void updateWebPaymentPgResult(WebPaymentPgResultDTO pgResult) throws Exception{
		ticketingMapper.updateWebPaymentPgResult(pgResult);
	}
	
	@Transactional
	@Override
	public ApiResultVO callTicketApi(WebPaymentPgResultDTO pgResult) throws Exception {

		List<CouponVO> coupons = null;
		if(pgResult.getCoupon() != null && !pgResult.getCoupon().equals("0")) {
			coupons = ticketingMapper.selectCouponByWebPaymetIdx(pgResult.getMoid()); // 사용된 쿠폰 가져오기 - 0원결제에서 타면 쿠폰이 0보다 크다
		}
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
			List<ApiSocialSaleDTO> socialSales = new ArrayList<>();
			WebPaymentDTO webPayment = ticketingMapper.selectWebPaymentByOrderNo(pgResult.getMoid()); // 주문번호만 있으면 됌
			
			// 쿠폰 사용전 체크
			if(coupons != null && coupons.size() > 0) {
				ApiResultVO alreadyUseCoupon = new ApiResultVO();
				for(CouponVO coupon : coupons) {
					CouponVO usedCP = ticketingMapper.selectCouponByCouponNum(coupon);
					if(usedCP == null) {
						alreadyUseCoupon.setSuccess(0);
						alreadyUseCoupon.setErrMsg("쿠폰정보가 없습니다.");
						return alreadyUseCoupon;
					}
					if(!"".equals(usedCP.getCpm_use_date())) {
						alreadyUseCoupon.setSuccess(0);
						alreadyUseCoupon.setErrMsg("이미 만료된 쿠폰입니다.");
						return alreadyUseCoupon;
					}
				}
			}
			// 쿠폰 사용
			updateCouponForUse(pgResult.getMoid() );
			
			Date now = new Date();
			String ticketingDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
			String ticketingDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			
			String paymentCode = getPaymentCode(pgResult.getPay_method());
			if(webPayment.getVisitor_type().equals("A")) { // 일반입장
				if(webPayment.getPiece_ticket_yn().equals("0")) { // 묶음 티켓
					ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
					
					List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
					List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(pgResult.getMoid());
					String saleSequence = ticketingMapper.selectWebSaleSequences(products.size());
					String saleSequenceLeft = saleSequence.substring(0, saleSequence.length() - 8);
					int saleSequenceRight = Integer.parseInt(saleSequence.substring(saleSequence.length() - 8, saleSequence.length()));
					
					// 카드 1에 쿠폰최대 4매까지, bc_sale_product에서 sale_sequence는, bc_paymentsale에서 payment_no와 동일
					// 카드(첫결제수단)는 bc_paymentsale에서 payment_idx = 1, 쿠폰은 payment_idx가 점차 증가함
					String firstSaleSequence = saleSequenceLeft + (StringUtils.leftPad(Integer.toString(saleSequenceRight++), 8, '0'));
					
					for(int i=0; i<products.size(); i++) {
						WebPaymentProductDTO product = products.get(i);
						
						String ticketControlNo = pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i), 3, '0');  // length: 18
						saleProducts.add(ApiSaleProductDTO.builder()
								.SALE_SEQUENCE(firstSaleSequence)
								.TICKET_CONTROL_NO(ticketControlNo)
								.PRODUCT_CONTENT_MST_CD(webPayment.getContent_mst_cd())
								.PRODUCT_CODE(product.getProduct_code())
								.PRODUCT_NAME(product.getProduct_name())
								.PACKAGE_YN("0")
								.BOOK_YN("1")
								.BOOK_NO(pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i), 5, '0'))
								.UNIT_PRICE(product.getProduct_fee().intValue())
								.QUANTITY(product.getCount())
								.PLAY_DATE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_date() : "")
								.PLAY_SEQUENCE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_sequence() : "1")
								.PRODUCT_FEE(product.getProduct_fee().intValue() * product.getCount())
								.REFUND_FEE(0)
								.SEASON_CODE("0")
								.MEMBER_DISCOUNT_YN("0")
								.SEAT_CODE("")
								.SCHEDULE_CODE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getSchedule_code() : "")
								.PERSON_NAME(product.getName())
								.PERSON_MOBILE_NO(product.getPhone())
								.PERSON_JUMIN(product.getJumin())
								.PERSON_ADDR(product.getAddr())
								.build());
						
						
					}
					List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
					List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1, coupons);			
					
					socialSale.setSALE_PRODUCT_LIST(saleProducts);
					socialSale.setCARD_INFO(cardInfos);
					socialSale.setPAYMENTS_INFO(paymentInfos);
					socialSales.add(socialSale);
				} else { // 낱장티켓
					ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
					
					List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(pgResult.getMoid());
					List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
					
					String saleSequence = ticketingMapper.selectWebSaleSequences(webPayment.getTotal_count());
					
					String saleSequenceLeft = saleSequence.substring(0, saleSequence.length() - 8);
					int saleSequenceRight = Integer.parseInt(saleSequence.substring(saleSequence.length() - 8, saleSequence.length()));
					
					// 카드 1에 쿠폰최대 4매까지, bc_sale_product에서 sale_sequence는, bc_paymentsale에서 payment_no와 동일
					// 카드(첫결제수단)는 bc_paymentsale에서 payment_idx = 1, 쿠폰은 payment_idx가 점차 증가함
					String firstSaleSequence = saleSequenceLeft + (StringUtils.leftPad(Integer.toString(saleSequenceRight++), 8, '0'));
					
					int idx=0;
					for(int i=0; i<products.size(); i++) {
						WebPaymentProductDTO product = products.get(i);
						for(int j=0; j<product.getCount(); j++) {
							
							String ticketControlNo = pgResult.getMoid() + StringUtils.leftPad(Integer.toString(idx+1), 3, '0');  // length: 18
							
							saleProducts.add(ApiSaleProductDTO.builder()
									.SALE_SEQUENCE(firstSaleSequence)
									.TICKET_CONTROL_NO(ticketControlNo)
									.PRODUCT_CONTENT_MST_CD(webPayment.getContent_mst_cd())
									.PRODUCT_CODE(product.getProduct_code())
									.PRODUCT_NAME(product.getProduct_name())
									.PACKAGE_YN("0")
									.BOOK_YN("1")
									.BOOK_NO(pgResult.getMoid() + StringUtils.leftPad(Integer.toString(idx+1), 5, '0'))
									.UNIT_PRICE(product.getProduct_fee().intValue())
									.QUANTITY(1)
									.PLAY_DATE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_date() : "")
									.PLAY_SEQUENCE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_sequence() : "1")
									.PRODUCT_FEE(product.getProduct_fee().intValue())
									.REFUND_FEE(0)
									.SEASON_CODE("0")
									.MEMBER_DISCOUNT_YN("0")
									.SEAT_CODE("")
									.SCHEDULE_CODE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getSchedule_code() : "")
									.PERSON_NAME(product.getName())
									.PERSON_MOBILE_NO(product.getPhone())
									.PERSON_JUMIN(product.getJumin())
									.PERSON_ADDR(product.getAddr())
									.build());	
							
							idx += 1;
						}	
					}
					
					List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
					List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1, coupons);
					
					socialSale.setSALE_PRODUCT_LIST(saleProducts);
					socialSale.setPAYMENTS_INFO(paymentInfos);
					socialSale.setCARD_INFO(cardInfos);
					
					socialSales.add(socialSale);
				}
				
			} else if(webPayment.getVisitor_type().equals("P")) { // 개인정보수집입장
				
				ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
				
				List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(pgResult.getMoid());
				List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
				//List<ApiPaymentsInfoDTO> paymentInfos = new ArrayList<>();
				//List<ApiCardInfoDTO> cardInfos = new ArrayList<>(); 
				
				String saleSequence = ticketingMapper.selectWebSaleSequences(webPayment.getTotal_count());
				
				String saleSequenceLeft = saleSequence.substring(0, saleSequence.length() - 8);
				int saleSequenceRight = Integer.parseInt(saleSequence.substring(saleSequence.length() - 8, saleSequence.length()));
				
				// 카드 1에 쿠폰최대 4매까지, bc_sale_product에서 sale_sequence는, bc_paymentsale에서 payment_no와 동일
				// 카드(첫결제수단)는 bc_paymentsale에서 payment_idx = 1, 쿠폰은 payment_idx가 점차 증가함
				String firstSaleSequence = saleSequenceLeft + (StringUtils.leftPad(Integer.toString(saleSequenceRight++), 8, '0'));
				
				for(int i=0; i<products.size(); i++) {
					WebPaymentProductDTO product = products.get(i);
					String ticketControlNo = pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i+1), 3, '0');  // length: 18
					
					saleProducts.add(ApiSaleProductDTO.builder()
							.SALE_SEQUENCE(firstSaleSequence)
							.TICKET_CONTROL_NO(ticketControlNo)
							.PRODUCT_CONTENT_MST_CD(webPayment.getContent_mst_cd())
							.PRODUCT_CODE(product.getProduct_code())
							.PRODUCT_NAME(product.getProduct_name())
							.PACKAGE_YN("0")
							.BOOK_YN("1")
							.BOOK_NO(pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i+1), 5, '0'))
							.UNIT_PRICE(product.getProduct_fee().intValue())
							.QUANTITY(1)
							.PLAY_DATE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_date() : "")
							.PLAY_SEQUENCE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_sequence() : "1")
							.PRODUCT_FEE(product.getProduct_fee().intValue())
							.REFUND_FEE(0)
							.SEASON_CODE("0")
							.MEMBER_DISCOUNT_YN("0")
							.SEAT_CODE("")
							.SCHEDULE_CODE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getSchedule_code() : "")
							.PERSON_NAME(product.getName())
							.PERSON_MOBILE_NO(product.getPhone())
							.PERSON_JUMIN(product.getJumin())
							.PERSON_ADDR(product.getAddr())
							.build());
					
				}
				List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
				List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1, coupons);
				
				socialSale.setSALE_PRODUCT_LIST(saleProducts);
				socialSale.setPAYMENTS_INFO(paymentInfos);
				socialSale.setCARD_INFO(cardInfos);
				
				socialSales.add(socialSale);
			}
			
			ApiResultVO result = null;
			for(ApiSocialSaleDTO socialSale : socialSales) {
				// api 호출
				ApiParamDTO param = ApiParamDTO.builder()
						.Param(socialSale)
						.build();
				String socialSalesJson = objectMapper.writeValueAsString(param);
				log.debug(socialSalesJson);
				result = callApi(socialSalesJson, "/item/socialsale", HttpMethod.POST);
				if(result.getSuccess() != 1) {
					break;
				}
			}
			result.setWebPayment(webPayment);
			result.setSocialSales(socialSales);
			
			if(result.getSuccess() != 1) {
				if(coupons != null) {
					updateCouponUseYn(pgResult.getMoid() );
					updateCouponCancelDate(coupons);
				}
			}
			
			return result;
			
		} catch (Exception e) {
			// TODO: handle exception
			
			if(coupons != null) {
				updateCouponUseYn(pgResult.getMoid() );
				updateCouponCancelDate(coupons);
			}
			ApiResultVO exceptionResult = new ApiResultVO();
			exceptionResult.setSuccess(0);
			exceptionResult.setErrMsg("알 수 없는 오류로 인해 api호출에 실패하였습니다.");

			return exceptionResult;
		}
	}

	@Transactional
	@Override
	public ApiResultVO callModifyTicketApi(WebPaymentPgResultDTO pgResult) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		List<ApiSocialSaleDTO> socialSales = new ArrayList<>();
		WebPaymentDTO webPayment = ticketingMapper.selectWebPaymentByOrderNo(pgResult.getMoid());
		Date now = new Date();
		String ticketingDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String ticketingDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
		
		List<CouponVO> coupons = null;
		if(pgResult.getCoupon() != null && !pgResult.getCoupon().equals("0")) {
			coupons = ticketingMapper.selectCouponByWebPaymetIdx(pgResult.getMoid()); // 사용된 쿠폰 가져오기
		}
		
		String paymentCode = getPaymentCode(pgResult.getPay_method());
		if(webPayment.getVisitor_type().equals("A")) { // 일반입장
			if(webPayment.getPiece_ticket_yn().equals("0")) { // 묶음 티켓
				ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
				
				List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
				//List<ApiPaymentsInfoDTO> paymentInfos = new ArrayList<>();
				//List<ApiCardInfoDTO> cardInfos = new ArrayList<>(); 
				List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(pgResult.getMoid());
				String saleSequence = ticketingMapper.selectWebSaleSequences(products.size());
				String saleSequenceLeft = saleSequence.substring(0, saleSequence.length() - 8);
				int saleSequenceRight = Integer.parseInt(saleSequence.substring(saleSequence.length() - 8, saleSequence.length()));
				
				String firstSaleSequence = saleSequenceLeft + (StringUtils.leftPad(Integer.toString(saleSequenceRight++), 8, '0'));
				for(int i=0; i<products.size(); i++) {
					WebPaymentProductDTO product = products.get(i);
					
					String ticketControlNo = pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i), 3, '0');  // length: 18
					saleProducts.add(ApiSaleProductDTO.builder()
							.SALE_SEQUENCE(firstSaleSequence)
							.TICKET_CONTROL_NO(ticketControlNo)
							.PRODUCT_CONTENT_MST_CD(webPayment.getContent_mst_cd())
							.PRODUCT_CODE(product.getProduct_code())
							.PRODUCT_NAME(product.getProduct_name())
							.PACKAGE_YN("0")
							.BOOK_YN("1")
							.BOOK_NO(pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i), 5, '0'))
							.UNIT_PRICE(product.getProduct_fee().intValue())
							.QUANTITY(product.getCount())
							.PLAY_DATE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_date() : "")
							.PLAY_SEQUENCE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_sequence() : "1")
							.PRODUCT_FEE(product.getProduct_fee().intValue() * product.getCount())
							.REFUND_FEE(0)
							.SEASON_CODE("0")
							.MEMBER_DISCOUNT_YN("0")
							.SEAT_CODE("")
							.SCHEDULE_CODE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getSchedule_code() : "")
							.PERSON_NAME(product.getName())
							.PERSON_MOBILE_NO(product.getPhone())
							.PERSON_JUMIN(product.getJumin())
							.PERSON_ADDR(product.getAddr())
							.build());
					
					
				}
				List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
				//ApiPaymentsInfoDTO paymentInfo = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
				//paymentInfos.add(paymentInfo);
				
				List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1, coupons);
				/*ApiCardInfoDTO cardInfo = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1);
				cardInfos.add(cardInfo);*/
				
				socialSale.setSALE_PRODUCT_LIST(saleProducts);
				socialSale.setCARD_INFO(cardInfos);
				socialSale.setPAYMENTS_INFO(paymentInfos);
				socialSales.add(socialSale);
			} else { // 낱장티켓
				ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
				
				List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(pgResult.getMoid());
				List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
				//List<ApiPaymentsInfoDTO> paymentInfos = new ArrayList<>();
				//List<ApiCardInfoDTO> cardInfos = new ArrayList<>(); 
				
				String saleSequence = ticketingMapper.selectWebSaleSequences(webPayment.getTotal_count());

				String saleSequenceLeft = saleSequence.substring(0, saleSequence.length() - 8);
				int saleSequenceRight = Integer.parseInt(saleSequence.substring(saleSequence.length() - 8, saleSequence.length()));

				// 카드 1에 쿠폰최대 4매까지, bc_sale_product에서 sale_sequence는, bc_paymentsale에서 payment_no와 동일
				// 카드(첫결제수단)는 bc_paymentsale에서 payment_idx = 1, 쿠폰은 payment_idx가 점차 증가함
				String firstSaleSequence = saleSequenceLeft + (StringUtils.leftPad(Integer.toString(saleSequenceRight++), 8, '0'));
				
				int idx=0;
				for(int i=0; i<products.size(); i++) {
					WebPaymentProductDTO product = products.get(i);
					for(int j=0; j<product.getCount(); j++) {
						
						String ticketControlNo = pgResult.getMoid() + StringUtils.leftPad(Integer.toString(idx+1), 3, '0');  // length: 18
						
						saleProducts.add(ApiSaleProductDTO.builder()
								.SALE_SEQUENCE(firstSaleSequence)
								.TICKET_CONTROL_NO(ticketControlNo)
								.PRODUCT_CONTENT_MST_CD(webPayment.getContent_mst_cd())
								.PRODUCT_CODE(product.getProduct_code())
								.PRODUCT_NAME(product.getProduct_name())
								.PACKAGE_YN("0")
								.BOOK_YN("1")
								.BOOK_NO(pgResult.getMoid() + StringUtils.leftPad(Integer.toString(idx+1), 5, '0'))
								.UNIT_PRICE(product.getProduct_fee().intValue())
								.QUANTITY(1)
								.PLAY_DATE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_date() : "")
								.PLAY_SEQUENCE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_sequence() : "1")
								.PRODUCT_FEE(product.getProduct_fee().intValue())
								.REFUND_FEE(0)
								.SEASON_CODE("0")
								.MEMBER_DISCOUNT_YN("0")
								.SEAT_CODE("")
								.SCHEDULE_CODE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getSchedule_code() : "")
								.PERSON_NAME(product.getName())
								.PERSON_MOBILE_NO(product.getPhone())
								.PERSON_JUMIN(product.getJumin())
								.PERSON_ADDR(product.getAddr())
								.build());	
						
						idx += 1;
					}	
				}
				
				List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
				//ApiPaymentsInfoDTO paymentInfo = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
				//paymentInfos.add(paymentInfo);
				
				List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1, coupons);
				/*ApiCardInfoDTO cardInfo = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1);			
				cardInfos.add(cardInfo);*/
				
				socialSale.setSALE_PRODUCT_LIST(saleProducts);
				socialSale.setPAYMENTS_INFO(paymentInfos);
				socialSale.setCARD_INFO(cardInfos);
				
				socialSales.add(socialSale);
			}
			
		} else if(webPayment.getVisitor_type().equals("P")) { // 개인정보수집입장
			
			ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
			
			List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(pgResult.getMoid());
			List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
			//List<ApiPaymentsInfoDTO> paymentInfos = new ArrayList<>();
			//List<ApiCardInfoDTO> cardInfos = new ArrayList<>(); 
			
			String saleSequence = ticketingMapper.selectWebSaleSequences(webPayment.getTotal_count());

			String saleSequenceLeft = saleSequence.substring(0, saleSequence.length() - 8);
			int saleSequenceRight = Integer.parseInt(saleSequence.substring(saleSequence.length() - 8, saleSequence.length()));

			// 카드 1에 쿠폰최대 4매까지, bc_sale_product에서 sale_sequence는, bc_paymentsale에서 payment_no와 동일
			// 카드(첫결제수단)는 bc_paymentsale에서 payment_idx = 1, 쿠폰은 payment_idx가 점차 증가함
			String firstSaleSequence = saleSequenceLeft + (StringUtils.leftPad(Integer.toString(saleSequenceRight++), 8, '0'));
				
			for(int i=0; i<products.size(); i++) {
				WebPaymentProductDTO product = products.get(i);
				String ticketControlNo = pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i+1), 3, '0');  // length: 18
				
				saleProducts.add(ApiSaleProductDTO.builder()
						.SALE_SEQUENCE(firstSaleSequence)
						.TICKET_CONTROL_NO(ticketControlNo)
						.PRODUCT_CONTENT_MST_CD(webPayment.getContent_mst_cd())
						.PRODUCT_CODE(product.getProduct_code())
						.PRODUCT_NAME(product.getProduct_name())
						.PACKAGE_YN("0")
						.BOOK_YN("1")
						.BOOK_NO(pgResult.getMoid() + StringUtils.leftPad(Integer.toString(i+1), 5, '0'))
						.UNIT_PRICE(product.getProduct_fee().intValue())
						.QUANTITY(1)
						.PLAY_DATE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_date() : "")
						.PLAY_SEQUENCE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getPlay_sequence() : "1")
						.PRODUCT_FEE(product.getProduct_fee().intValue())
						.REFUND_FEE(0)
						.SEASON_CODE("0")
						.MEMBER_DISCOUNT_YN("0")
						.SEAT_CODE("")
						.SCHEDULE_CODE(org.springframework.util.StringUtils.hasText(webPayment.getSchedule_code()) ? webPayment.getSchedule_code() : "")
						.PERSON_NAME(product.getName())
						.PERSON_MOBILE_NO(product.getPhone())
						.PERSON_JUMIN(product.getJumin())
						.PERSON_ADDR(product.getAddr())
						.build());				
			}
			List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
			//ApiPaymentsInfoDTO paymentInfo = getApiPaymentInfoWithQuantity(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1, coupons);
			//paymentInfos.add(paymentInfo);
			
			List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1, coupons);
			/*ApiCardInfoDTO cardInfo = getApiCardInfoWithQuantity(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1);			
			cardInfos.add(cardInfo);*/
			
			socialSale.setSALE_PRODUCT_LIST(saleProducts);
			socialSale.setPAYMENTS_INFO(paymentInfos);
			socialSale.setCARD_INFO(cardInfos);
			
			socialSales.add(socialSale);
		}
				
		ApiResultVO result = null;
		for(ApiSocialSaleDTO socialSale : socialSales) {
			// api 호출
			ApiParamDTO param = ApiParamDTO.builder()
					.Param(socialSale)
					.build();
			String socialSalesJson = objectMapper.writeValueAsString(param);
			log.debug(socialSalesJson);
			result = callApi(socialSalesJson, "/item/modsocialsale", HttpMethod.POST);
			if(result.getSuccess() != 1) {
				break;
			}
		}
		result.setWebPayment(webPayment);
		result.setSocialSales(socialSales);
		
		return result;
	}

	private ApiSocialSaleDTO getApiSocialSale(WebPaymentPgResultDTO pgResult, WebPaymentDTO webPayment, Date date) throws Exception {
		Date validTo = new Date(date.getTime());
		if(webPayment.getProduct_group_kind().equals("A")) {
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(date); // 10분 더하기 cal.add(Calendar.MINUTE, 10);
			cal.add(Calendar.DATE, webPayment.getValid_period());
			validTo = cal.getTime();
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mbId = authentication.getName();
		Map<String, Object> params = new HashMap<>();
		params.put("content_mst_cd", webPayment.getContent_mst_cd());
		params.put("mb_id", mbId);
		
		
		return ApiSocialSaleDTO.builder()
				.CONTENT_MST_CD(webPayment.getContent_mst_cd())
				.TERMINAL_CODE("WEBTICKET")
				.SALE_DATE(new SimpleDateFormat("yyyy-MM-dd").format(date))
				.TERMINAL_DATETIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
//					.USER_ID(null)				
				.MEMBER_YN("0")
				.MEMBER_NO(null)
//					.MEMBER_NO(null)
				.SALE_KIND_CODE("0030")
				.ONLINE_CHANNEL("1001")
				.WORK_DATETIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))
				.ORDER_NUM(pgResult.getMoid())
				.MEMBER_NAME(webPayment.getReserverName())
				.MEMBER_TEL(webPayment.getReserverPhone())
				.MEMBER_EMAIL(webPayment.getReserverEmail())
				.VALID_FROM(webPayment.getPlay_date())
				.VALID_TO(webPayment.getPlay_date())
				.AGREE_1(webPayment.getAgree_1())
				.AGREE_2(webPayment.getAgree_2())
				.ORG_ORDER_NUM(pgResult.getOrg_order_num())
				.S_CHECK_YN("1") // 고객은 예약 정원 체크함
				.build();
				//.VALID_FROM(new SimpleDateFormat("yyyy-MM-dd").format(date).toString())
				//.VALID_TO(new SimpleDateFormat("yyyy-MM-dd").format(date).toString())
				//.build();
	}

//	private ApiPaymentsInfoDTO getApiPaymentInfo(String ticketingDate, String ticketingDateTime,
//			String paymentCode, WebPaymentProductDTO product, String saleSequence, String reserverName,
//			WebPaymentPgResultDTO pgResult, int paymentIdx) {
//		
//		ApiPaymentsInfoDTO paymentInfo = ApiPaymentsInfoDTO.builder()
//				.PAYMENT_CODE(paymentCode)
//				.PAYMENT_NO(saleSequence)
//				.PAYMENT_IDX(1)
//				.PAYMENT_FEE(Integer.parseInt(pgResult.getAmt()))
//				.PENALTY_FEE(0)
//				.PAYMENT_USER_ID(org.springframework.util.StringUtils.hasText(reserverName) 
//						? reserverName.length() > 16 ? reserverName.substring(0, 16) : reserverName
//						: "-")
//				.PAYMENT_DATE(ticketingDate)
//				.DECIDE_DATE(ticketingDate)
//				.DECIDE_ID(org.springframework.util.StringUtils.hasText(reserverName) 
//						? reserverName.length() > 16 ? reserverName.substring(0, 16) : reserverName
//						: "-")
//				.BILL_YN("1")
//				.BILL_DATETIME(ticketingDateTime)
//				.build();
//		
//		return paymentInfo;
//	}
	
	private List<ApiPaymentsInfoDTO> getApiPaymentInfoWithQuantity(String ticketingDate, String ticketingDateTime,
			String paymentCode, String saleSequence, String reserverName, 
			WebPaymentPgResultDTO pgResult, int paymentIdx, List<CouponVO> coupons) {
				
		List<ApiPaymentsInfoDTO> list = new ArrayList<ApiPaymentsInfoDTO>();
		ApiPaymentsInfoDTO paymentInfo = ApiPaymentsInfoDTO.builder()
				.PAYMENT_CODE(paymentCode)
				.PAYMENT_NO(saleSequence)
				.PAYMENT_IDX(paymentIdx)
				.PAYMENT_FEE(Integer.parseInt(pgResult.getAmt()))
				.PENALTY_FEE(0)
				.PAYMENT_USER_ID(org.springframework.util.StringUtils.hasText(reserverName) 
						? reserverName.length() > 16 ? reserverName.substring(0, 16) : reserverName
						: "-")
				.PAYMENT_DATE(ticketingDate)
				.DECIDE_DATE(ticketingDate)
				.DECIDE_ID(org.springframework.util.StringUtils.hasText(reserverName) 
						? reserverName.length() > 16 ? reserverName.substring(0, 16) : reserverName
						: "-")
				.BILL_YN("1")
				.BILL_DATETIME(ticketingDateTime)
				.build();
		list.add(paymentInfo);
		
		if(coupons != null) {
			for(CouponVO vo : coupons) {
				ApiPaymentsInfoDTO paymentInfoCoupon = ApiPaymentsInfoDTO.builder()
						.PAYMENT_CODE("0010")
						.PAYMENT_NO(saleSequence)
						.PAYMENT_IDX(++paymentIdx)
						.PAYMENT_FEE(Integer.parseInt(vo.getCpn_sale_cost()))
						.PENALTY_FEE(0)
						.PAYMENT_USER_ID(org.springframework.util.StringUtils.hasText(reserverName) 
								? reserverName.length() > 16 ? reserverName.substring(0, 16) : reserverName
								: "-")
						.PAYMENT_DATE(ticketingDate)
						.DECIDE_DATE(ticketingDate)
						.DECIDE_ID(org.springframework.util.StringUtils.hasText(reserverName) 
								? reserverName.length() > 16 ? reserverName.substring(0, 16) : reserverName
								: "-")
						.BILL_YN("1")
						.BILL_DATETIME(ticketingDateTime)
						.build();
				list.add(paymentInfoCoupon);
			}
		}
		
		return list;
	}

//	private ApiCardInfoDTO getApiCardInfo(WebPaymentPgResultDTO pgResult, String ticketingDateTime,
//			String paymentCode, String saleSequence, int paymentIdx) {
//		
//		String approvalNo = pgResult.getAuth_code();
//		if(org.springframework.util.StringUtils.hasText(approvalNo)) {
//			if(approvalNo.length() > 20) { // TODO: 추후 원래대로 돌림
//				approvalNo.substring(0, 19);
//			}
//		} else {
//			approvalNo = "-";
//		}
//		
//		ApiCardInfoDTO cardInfo = ApiCardInfoDTO.builder()
//				.PAYMENT_CODE(paymentCode)
//				.PAYMENT_NO(saleSequence)
//				.PAYMENT_IDX(paymentIdx)
//				.TRANSACTION_DATETIME(ticketingDateTime)
//				.CARD_NO("")
//				.INSTALL_PERIOD("")
//				.APPROVAL_NO(approvalNo) 
//				.APPROVAL_AMOUNT(Integer.parseInt(pgResult.getAmt()))
//				.DISCOUNT_AMOUNT(0)
//				.SERVICE_AMOUNT(0)
//				.TAX_AMOUNT(0)
//				.FEE_AMOUNT(0)
//				.ORIGINAL_TRANSACTION_DATETIME(ticketingDateTime)
//				.CARD_CODE("")
//				.CARD_NAME("")
//				.PURCHASE_CODE("")
//				.PURCHASE_NAME("")
//				.STORE_ID("")
//				.PART_CANCEL_YN("0")
//				.CASH_TYPE("")
//				.build();
//		if(pgResult.getPay_method().equals("CARD")) {
//			cardInfo.setCARD_NO(pgResult.getCardNo());
//			cardInfo.setINSTALL_PERIOD(pgResult.getCardQuota());
//			cardInfo.setCARD_CODE(pgResult.getCardCode());
//			cardInfo.setCARD_NAME(pgResult.getCardName());
//			cardInfo.setPURCHASE_CODE(pgResult.getAcquCardCode());
//			cardInfo.setPURCHASE_NAME(pgResult.getAcquCardName());
//			cardInfo.setCASH_TYPE(pgResult.getRcptType());
//		} else if(pgResult.getPay_method().equals("BANK")) {
//			cardInfo.setCARD_CODE(pgResult.getCardCode());
//			cardInfo.setCARD_NAME(pgResult.getCardName());
//			cardInfo.setCASH_TYPE(pgResult.getRcptType());
//		}
//		
//		return cardInfo;
//	}
//	
	private List<ApiCardInfoDTO> getApiCardInfoWithQuantity(WebPaymentPgResultDTO pgResult, String ticketingDateTime,
			String paymentCode, String saleSequence, int paymentIdx, List<CouponVO> coupons) {
		
		List<ApiCardInfoDTO> list = new ArrayList<>();
		
		String approvalNo = pgResult.getAuth_code();
		if(org.springframework.util.StringUtils.hasText(approvalNo)) {
			if(approvalNo.length() > 20) { // TODO: 추후 원래대로 돌림
				approvalNo.substring(0, 19);
			}
		} else {
			approvalNo = "-";
		}
		
		ApiCardInfoDTO cardInfo = ApiCardInfoDTO.builder()
				.PAYMENT_CODE(paymentCode)
				.PAYMENT_NO(saleSequence)
				.PAYMENT_IDX(paymentIdx)
				.TRANSACTION_DATETIME(ticketingDateTime)
				.CARD_NO("")
				.INSTALL_PERIOD("")
				.APPROVAL_NO(approvalNo) 
				.APPROVAL_AMOUNT(Integer.parseInt(pgResult.getAmt()))
				.DISCOUNT_AMOUNT(0)
				.SERVICE_AMOUNT(0)
				.TAX_AMOUNT(0)
				.FEE_AMOUNT(0)
				.ORIGINAL_TRANSACTION_DATETIME(ticketingDateTime)
				.CARD_CODE("")
				.CARD_NAME("")
				.PURCHASE_CODE("")
				.PURCHASE_NAME("")
				.STORE_ID("")
				.PART_CANCEL_YN("0")
				.CASH_TYPE("")
				.build();
		if(pgResult.getPay_method().equals("CARD")) {
			cardInfo.setCARD_NO(pgResult.getCardNo());
			cardInfo.setINSTALL_PERIOD(pgResult.getCardQuota());
			cardInfo.setCARD_CODE(pgResult.getCardCode());
			cardInfo.setCARD_NAME(pgResult.getCardName());
			cardInfo.setPURCHASE_CODE(pgResult.getAcquCardCode());
			cardInfo.setPURCHASE_NAME(pgResult.getAcquCardName());
			cardInfo.setCASH_TYPE(pgResult.getRcptType());
		} else if(pgResult.getPay_method().equals("BANK")) {
			cardInfo.setCARD_CODE(pgResult.getCardCode());
			cardInfo.setCARD_NAME(pgResult.getCardName());
			cardInfo.setCASH_TYPE(pgResult.getRcptType());
		}
		
		list.add(cardInfo);
		
		if(coupons != null) {
			for(CouponVO vo : coupons) {
				ApiCardInfoDTO cardInfoCoupon = ApiCardInfoDTO.builder()
						.PAYMENT_CODE("0010")
						.PAYMENT_NO(saleSequence)
						.PAYMENT_IDX(++paymentIdx)
						.TRANSACTION_DATETIME(ticketingDateTime)
						.CARD_NO("")
						.INSTALL_PERIOD("")
						.APPROVAL_NO(vo.getCpm_num()) 
						.APPROVAL_AMOUNT(Integer.parseInt(vo.getCpn_sale_cost()))
						.DISCOUNT_AMOUNT(0)
						.SERVICE_AMOUNT(0)
						.TAX_AMOUNT(0)
						.FEE_AMOUNT(0)
						.ORIGINAL_TRANSACTION_DATETIME(ticketingDateTime)
						.CARD_CODE("")
						.CARD_NAME("")
						.PURCHASE_CODE("")
						.PURCHASE_NAME("")
						.STORE_ID("")
						.PART_CANCEL_YN("0")
						.CASH_TYPE("")
						.build();
				list.add(cardInfoCoupon);
			}
		}
		
		return list;
	}
	
	private ApiResultVO callApi(String socialSalesJson, String target, HttpMethod httpMethod) throws Exception{
		
		try {
			int timeout = 40000;
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectionRequestTimeout(timeout);
			factory.setConnectTimeout(timeout);
			factory.setReadTimeout(timeout);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			
			RestTemplate restTemplate = new RestTemplate(factory);
			
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON_UTF8);
			String apiKey = propertyService.getString("apiKey");
			String apiValue = propertyService.getString("apiValue");
			header.add(apiKey, apiValue);
			
			HttpEntity<?> entity = new HttpEntity<>(socialSalesJson, header);
			
			String url = propertyService.getString("apiUrl") + target;
			UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
			ResponseEntity<Map> resultMap = null;
			try {
				
				resultMap = restTemplate.exchange(uri.toString(), httpMethod, entity, Map.class);
				result.put("statusCode", resultMap.getStatusCodeValue());
				result.put("header", resultMap.getHeaders());
				result.put("body",  resultMap.getBody());
			}catch(Exception ex) {
				
				log.error("ApiCallError[Disconnectec]: " + ex.getMessage());
				return new ApiResultVO(0, "API서버 통신와 통신을 할 수 없습니다.");
			}
			
			// status코드가 정상이 아닐 때
			if(!result.get("statusCode").toString().equals("200")) {
				log.error("ApiCallError[Status]: " + result.get("statusCode"));
				log.error("ApiCallError[body]: " + socialSalesJson);
				return new ApiResultVO(0, "API서버 통신와 통신을 할 수 없습니다.");
			}
			
			ApiResultVO apiResult = getResult(resultMap);
			
			// 결과가 정상이 아닐때
			if(apiResult.getSuccess() == 0) {
				
				String errorMsg = StringEscapeUtils.unescapeXml(apiResult.getErrMsg()).replaceAll("[\"\'\n\r]", " ");
				log.error("ApiCallError[Status]: " + result.get("statusCode"));
				log.error("ApiCallError[result]: " + apiResult.getSuccess() + "|" + errorMsg);
				log.error("ApiCallError[body]: " + socialSalesJson);
				
			}
			return apiResult;
			
		} catch (Exception e) {
			// TODO: handle exception
			ApiResultVO exceptionResult = new ApiResultVO();
			exceptionResult.setSuccess(0);
			exceptionResult.setErrMsg("알 수 없는 오류로 인해 api호출에 실패하였습니다.");
			
			return exceptionResult;
		}
	}
 
	private ApiResultVO getResult(ResponseEntity<Map> resultMap)
			throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String resultJsonString = mapper.writeValueAsString(resultMap.getBody());
        JsonNode root = mapper.readTree(resultJsonString);
        int success = root.at("/Result/success").asInt(0);
        String errMsg = root.at("/Result/errMsg").toString();
        return new ApiResultVO(success, errMsg);
	}

	private String getPaymentCode(String pay_method) {
		
		switch(pay_method) {
			case "0000": return "0000";
			case "CARD": return "0002";
			case "BANK": return "0004";
			case "CELLPHONE": return "0006";
			default: return "9000";
		}
	}

	@Override
	public ApiResultVO callCancelApi(ApiSocialCancelDTO apiSocialCancel) throws Exception {
		
		WebPaymentDTO webPayment = ticketingMapper.selectWebPaymentByOrderNo(apiSocialCancel.getORDER_NUM());
		apiSocialCancel.setCONTENT_MST_CD(webPayment.getContent_mst_cd());

		ApiParamDTO param = ApiParamDTO.builder()
				.Param(apiSocialCancel)
				.build();
		ObjectMapper objectMapper = new ObjectMapper();
		String cancelJson = objectMapper.writeValueAsString(param);

		ApiResultVO result = callApi(cancelJson, "/item/socialcancel", HttpMethod.PUT);
		return result;
	}
	
	@Override
	public List<FinishTradeVO> getFinishTrade(Map<String, Object> params) throws Exception {

		return ticketingMapper.selectFinishTrade(params);
	}

	@Override
	public WebPaymentPgResultDTO getWebPaymentPgResult(String orderNo) throws Exception {

		return ticketingMapper.selectWebPaymentPgResult(orderNo);
	}

	@Override
	public WebPaymentDTO getWebPayment(String orderNo) throws Exception {

		return ticketingMapper.selectWebPaymentByOrderNo(orderNo);
	}
	@Override
	public ScheduleDTO getScheduleByScheduleCode(ScheduleDTO schedule) throws Exception{

		return ticketingMapper.selectScheduleByScheduleCode(schedule);
	}

	@Override
	public List<RefundVO> getRefund(SaleDTO sale) throws Exception {
		
		return ticketingMapper.selectRefund(sale);
	}
	
	@Override
	public CompanyVO getCompany(String shopCode) throws Exception {
		
		return ticketingMapper.selectCompany(shopCode);
	}
	@Override
	public ShopDetailVO getShopDetail(String shopCode) throws Exception {
		
		return ticketingMapper.selectShopDetail(shopCode);
	}
	@Override
	public ShopDetailVO getShopDetailByContentMstCd(String contentMstCd) throws Exception {
	
		return ticketingMapper.selectShopDetailByContentMstCd(contentMstCd);
	}
	@Override
	public SaleVO getSaleSsByOrderNum(SaleVO searchSale) throws Exception {

		return ticketingMapper.selectSaleSsByOrderNum(searchSale);
	}
	
	@Override
	public List<MemberSalesVO> getMemberSales(MemberSalesCriteria criteria) throws Exception {

		return ticketingMapper.selectMemberSales(criteria);
	}

	@Override
	public WebReservationKeyDTO selectReserveInfo(String shop_code) throws Exception {
		// TODO Auto-generated method stub
		return ticketingMapper.selectReserveInfo(shop_code);
	}

	@Override
	public WebReservationKeyDTO selectReserveInfoByCompanyCode(String company_code) throws Exception {
		// TODO Auto-generated method stub
		return ticketingMapper.selectReserveInfoByCompanyCode(company_code);
	}
	
	@Override
	public int getMemberSalesCount(MemberSalesCriteria criteria) throws Exception {
		
		return ticketingMapper.selectMemberSalesCount(criteria);
	}

	@Override
	public SaleDTO getSaleBySaleCode(SaleDTO sale) throws Exception {

		return ticketingMapper.selectSaleBySaleCode(sale);
	}
	
	@Override
	public VerificationKeyVO getKeys(String shopCode) throws Exception {
		
		return ticketingMapper.selectKeys(shopCode);
	}
	
	@Override
	public String getShopCode(String contentMstCd) throws Exception {

		return ticketingMapper.selectShopCode(contentMstCd);
	}
	
	private void updateCouponForUse(String order_num) throws Exception {
		List<CouponVO> coupon = ticketingMapper.selectCouponByWebPaymetIdx(order_num);
		if(coupon != null && coupon.size() > 0) {
			ticketingMapper.updateCouponUseDate(coupon);
		}
	}
	

	/*
	 * 환불 history 기록 / 2021-10-26 / 조미근
	 * 
	 * @see
	 * com.bluecom.shop.service.ShopWebTicketService#insertRefundHistory(com.bluecom
	 * .shop.domain.RefundHistoryVO)
	 */
	@Override
	public int insertRefundHistory(RefundHistoryVO vo) throws Exception {
		return ticketingMapper.insertRefundHistory(vo);
	}

	/*
	 * 부분환불시 paymentsale 가져오기 / 2021-10-26 / 조미근
	 * 
	 * @see
	 * com.bluecom.shop.service.ShopWebTicketService#selectPaymentSale(com.bluecom.
	 * shop.domain.ShopPaymentsaleVO)
	 */
	@Override
	public List<ShopPaymentsaleVO> selectPaymentSale(ShopPaymentsaleVO vo) throws Exception {
		return ticketingMapper.selectPaymentSale(vo);
	}

	/*
	 * 부분환불시 paymentsale 기록 / 2021-10-26 / 조미근
	 * 
	 * @see
	 * com.bluecom.shop.service.ShopWebTicketService#insertPaymentSale(com.bluecom.
	 * shop.domain.ShopPaymentsaleVO)
	 */
	@Override
	public int insertPaymentSale(ShopPaymentsaleVO vo) throws Exception {
		return ticketingMapper.insertPaymentSale(vo);
	}
}

