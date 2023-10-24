package com.bluecom.ticketing.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.bluecom.common.domain.MemberSalesCriteria;
import com.bluecom.common.service.MailService;
import com.bluecom.common.service.MessageService;
import com.bluecom.common.util.CommUtil;
import com.bluecom.common.util.DateHelper;
import com.bluecom.common.util.ScriptUtils;
import com.bluecom.ticketing.domain.ApiCardInfoDTO;
import com.bluecom.ticketing.domain.ApiParamDTO;
import com.bluecom.ticketing.domain.ApiPaymentsInfoDTO;
import com.bluecom.ticketing.domain.ApiProductRefundDTO;
import com.bluecom.ticketing.domain.ApiResultVO;
import com.bluecom.ticketing.domain.ApiSaleProductDTO;
import com.bluecom.ticketing.domain.ApiSocialCancelDTO;
import com.bluecom.ticketing.domain.ApiSocialSaleDTO;
import com.bluecom.ticketing.domain.BookOpenVO;
import com.bluecom.ticketing.domain.CompanyVO;
import com.bluecom.ticketing.domain.CouponVO;
import com.bluecom.ticketing.domain.EssentialDTO;
import com.bluecom.ticketing.domain.FinishTradeVO;
import com.bluecom.ticketing.domain.FinishTradeVO_noSchedule;
import com.bluecom.ticketing.domain.MemberSalesVO;
import com.bluecom.ticketing.domain.PaymentInfoDTO;
import com.bluecom.ticketing.domain.PaymentInfoDTO_noSchedule;
import com.bluecom.ticketing.domain.ProductDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO;
import com.bluecom.ticketing.domain.ProductGroupDTO_noSchedule;
import com.bluecom.ticketing.domain.RefundHistoryVO;
import com.bluecom.ticketing.domain.RefundVO;
import com.bluecom.ticketing.domain.SaleDTO;
import com.bluecom.ticketing.domain.SaleDTO_noSchedule;
import com.bluecom.ticketing.domain.SaleProductDTO;
import com.bluecom.ticketing.domain.SaleProductDTO_noSchedule;
import com.bluecom.ticketing.domain.SaleVO;
import com.bluecom.ticketing.domain.SaleVO_noSchedule;
import com.bluecom.ticketing.domain.ScheduleDTO;
import com.bluecom.ticketing.domain.ShopDetailVO;
import com.bluecom.ticketing.domain.ShopPaymentsaleVO;
import com.bluecom.ticketing.domain.TicketValidVO;
import com.bluecom.ticketing.domain.VerificationKeyVO;
import com.bluecom.ticketing.domain.VisitorDTO;
import com.bluecom.ticketing.domain.WebPaymentDTO;
import com.bluecom.ticketing.domain.WebPaymentPgResultDTO;
import com.bluecom.ticketing.domain.WebPaymentProductDTO;
import com.bluecom.ticketing.domain.WebPaymentStatusDTO;
import com.bluecom.ticketing.domain.WebPaymentVisitorDTO;
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
	
	@Autowired
	@Qualifier("jejuMail")
	MailService mailService;

	@Autowired
	@Qualifier("bgfAlimTalk")
	MessageService messageService;
	
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
	public List<ProductGroupDTO> getProductGroups_noSchedule(String contentMstCd) throws Exception {
	
		return ticketingMapper.getProductGroups_noSchedule(contentMstCd);
	}
	
	@Override
	public List<ProductGroupDTO> selectResStatusCount(String contentMstCd) throws Exception {
		
		return ticketingMapper.selectResStatusCount(contentMstCd);
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
	public ProductGroupDTO_noSchedule getProductGroup_noSchedule(ProductGroupDTO_noSchedule productGroup) throws Exception {
		
		return ticketingMapper.getProductGroup_noSchedule(productGroup);
	}
	
	@Override
	public List<ProductDTO> getProducts(ProductGroupDTO productGroup) throws Exception {

		return ticketingMapper.selectProducts(productGroup);
	}
		
	
	
	public List<ProductDTO> getProducts_NoSchedule(ProductGroupDTO_noSchedule productGroup) throws Exception {
		
		return ticketingMapper.selectProducts_NoSchedule(productGroup);
	}
	
	
	@Override
	public List<ProductDTO> selectProductsForGanghwa(ProductGroupDTO productGroup) throws Exception {
		
		return ticketingMapper.selectProductsForGanghwa(productGroup);
	}
	
	
	@Override
	public List<ProductDTO> selectProductsForSogeumsan(ProductGroupDTO productGroup) throws Exception {
		
		return ticketingMapper.selectProductsForSogeumsan(productGroup);
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
	
	
	@Transactional
	@Override
	public WebPaymentDTO addWebPaymentInfo_noSchedule(PaymentInfoDTO_noSchedule paymentInfo) throws Exception {
		
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
		webPayment.setProduct_group_kind(Integer.toString(paymentInfo.getType()));
		webPayment.setSchedule_code(paymentInfo.getSchedule() == null ? "" : paymentInfo.getSchedule().getSchedule_code());
		webPayment.setPlay_sequence(paymentInfo.getSchedule() == null ? "1" :paymentInfo.getSchedule().getPlay_sequence());
		webPayment.setPlay_date(paymentInfo.getSchedule() == null ? "" :paymentInfo.getPlay_date());
		webPayment.setProduct_group_name(paymentInfo.getProductGroup().getProduct_group_name());
		webPayment.setStart_time(paymentInfo.getSchedule() == null ? "" : paymentInfo.getSchedule().getStart_time());
		webPayment.setEnd_time(paymentInfo.getSchedule() == null ? "" : paymentInfo.getSchedule().getEnd_time());
		webPayment.setAgree_1(paymentInfo.isAgree_1() ? "1" : "0");
		webPayment.setAgree_2(paymentInfo.isAgree_2() ? "1" : "0");
		webPayment.setValid_period(Integer.parseInt(paymentInfo.getValid_period()));
		
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
		
		if(paymentInfo.getVisitorType().equals("P")) {

			List<WebPaymentVisitorDTO> webPaymentVisitors = new ArrayList<>();
			for(VisitorDTO visitor: paymentInfo.getVisitors()) {
				WebPaymentVisitorDTO webPaymentVisitor = new WebPaymentVisitorDTO();
				webPaymentVisitor.setWeb_payment_idx(paymentInfo.getIdx());
				webPaymentVisitor.setName(visitor.getName());
				webPaymentVisitor.setPhone(visitor.getPhone());
				webPaymentVisitor.setJumin(visitor.getJumin());
				webPaymentVisitor.setProduct_code(visitor.getProduct_code());
				webPaymentVisitor.setAddr(visitor.getAddr());
				
				webPaymentVisitors.add(webPaymentVisitor);
			}
			// 방문자 정보 임시 기록
			ticketingMapper.insertWebPaymentVisitors(webPaymentVisitors);
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
		
		return webPayment;
		
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
	public List<ProductDTO> getProcess2ProductsForGanghwa(ProductGroupDTO productGroup) throws Exception {
		return ticketingMapper.selectProcess2ProductForGanghwa(productGroup);
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
	public List<SaleDTO_noSchedule> getCheckTicket_noSchedule(SaleDTO_noSchedule saleDTO) throws Exception {
		return ticketingMapper.selectCheckTicket_noSchedule(saleDTO);
	}
	
	
	@Override
	public List<SaleVO_noSchedule> getSalesByMemberInfo(SaleDTO_noSchedule saleDTO) throws Exception {

		return ticketingMapper.selectSalesByMemberInfo(saleDTO);
	}
	
	
	
	@Override
	public String getOrderNumBySaleCode(SaleDTO_noSchedule saleDTO) throws Exception {

		return ticketingMapper.selectOrderNumBySaleCode(saleDTO);
	}

	
	
	
	@Override
	public SaleVO_noSchedule getSaleByOrderNum(SaleVO_noSchedule saleVO) throws Exception {
		
		SaleVO_noSchedule sale = getSaleSsByOrderNum_noSchedule(saleVO);
		sale.setContent_mst_cd(saleVO.getContent_mst_cd());
		List<SaleProductDTO_noSchedule> tsSaleProdcuts = ticketingMapper.selectTsSaleProduct(sale);
		
		sale.setIssued_quantity(tsSaleProdcuts.size());
		
		return sale;
	}
	
	
	@Override
	public SaleVO_noSchedule getSaleSsByOrderNum_noSchedule(SaleVO_noSchedule searchSale) throws Exception {

		return ticketingMapper.selectSaleSsByOrderNum_noSchedule(searchSale);
	}
	
	
	
	@Override
	public List<SaleProductDTO_noSchedule> getSaleProduct(SaleDTO_noSchedule saleDTO) throws Exception {
		return ticketingMapper.selectSaleProduct_noSchedule(saleDTO);
	}
	
	
	
	
	@Override
	public SaleProductDTO_noSchedule getTsSaleProductByBookNo(SaleProductDTO_noSchedule saleProduct) throws Exception {

		return ticketingMapper.selectTsSaleProductByBookNo(saleProduct);
	}
	
	
	@Override
	public FinishTradeVO_noSchedule getFinishTrade_noSchedule(Map<String, Object> params) throws Exception {

		return ticketingMapper.selectFinishTrade_noSchedule(params);
	}
	
	
	@Override
	public List<FinishTradeVO_noSchedule> selectResSaleInfoList(Map<String, Object> params) throws Exception {
		
		return ticketingMapper.selectResSaleInfoList(params);
	}
	
	@Override
	public List<FinishTradeVO_noSchedule> selectProductsForGA(Map<String, Object> params) throws Exception{
		return ticketingMapper.selectProductsForGA(params);
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

		log.info("::: CALL TIcketApi");
		
		List<CouponVO> coupons = null;
		/*
		if(pgResult.getCoupon() != null && !pgResult.getCoupon().equals("0")) {
			coupons = ticketingMapper.selectCouponByWebPaymetIdx(pgResult.getMoid()); // 사용된 쿠폰 가져오기 - 0원결제에서 타면 쿠폰이 0보다 크다
		}
		*/
		
		log.info("::: Reserve Data Make Start");
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
			List<ApiSocialSaleDTO> socialSales = new ArrayList<>();
			WebPaymentDTO webPayment = ticketingMapper.selectWebPaymentByOrderNo(pgResult.getMoid()); // 주문번호만 있으면 됌
			
			// 쿠폰 사용전 체크
			/*
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
			*/
			
			// 쿠폰 사용
			//updateCouponForUse(pgResult.getMoid() );
			
			Date now = new Date();
			String ticketingDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
			String ticketingDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			
			String paymentCode = getPaymentCode(pgResult.getPay_method());
			if(webPayment.getVisitor_type().equals("A")) 
			{ // 일반입장
				if(webPayment.getPiece_ticket_yn().equals("0")) 
				{ // 묶음 티켓
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
				/*
				if(coupons != null) {
					updateCouponUseYn(pgResult.getMoid() );
					updateCouponCancelDate(coupons);
				}
				*/
			}
			
			return result;
			
		} catch (Exception e) {
			// TODO: handle exception
			
			/*
			if(coupons != null) {
				updateCouponUseYn(pgResult.getMoid() );
				updateCouponCancelDate(coupons);
			}
			*/
			
			ApiResultVO exceptionResult = new ApiResultVO();
			exceptionResult.setSuccess(0);
			exceptionResult.setErrMsg("알 수 없는 오류로 인해 api호출에 실패하였습니다.");

			return exceptionResult;
		}
	}

	
	
	
	//회차없는 예매
	@Transactional
	@Override
	public ApiResultVO callTicketApi_noSchedule(WebPaymentPgResultDTO pgResult) throws Exception {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
			List<ApiSocialSaleDTO> socialSales = new ArrayList<>();
			WebPaymentDTO webPayment = ticketingMapper.selectWebPaymentByOrderNo(pgResult.getMoid());
			Date now = new Date();
			String ticketingDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
			String ticketingDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
			
			
			
			log.info("VISITOR_TYPE ==> " + webPayment.getVisitor_type());
			
			
			String paymentCode = getPaymentCode(pgResult.getPay_method());
			if(webPayment.getVisitor_type().equals("A")) { // 일반입장
				if(webPayment.getPiece_ticket_yn().equals("0")) { // 묶음 티켓
					//ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
					ApiSocialSaleDTO socialSale = getApiSocialSale_noSchedule(pgResult, webPayment, now);
					
					List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
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
					List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity_noSchedule(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1);
					List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity_noSchedule(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1);
					
					socialSale.setSALE_PRODUCT_LIST(saleProducts);
					socialSale.setCARD_INFO(cardInfos);
					socialSale.setPAYMENTS_INFO(paymentInfos);
					socialSales.add(socialSale);
				} else { // 낱장티켓
					//ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
					ApiSocialSaleDTO socialSale = getApiSocialSale_noSchedule(pgResult, webPayment, now);
					
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

					List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity_noSchedule(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1);
					List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity_noSchedule(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1);
					
					socialSale.setSALE_PRODUCT_LIST(saleProducts);
					socialSale.setPAYMENTS_INFO(paymentInfos);
					socialSale.setCARD_INFO(cardInfos);
					
					socialSales.add(socialSale);
				}
				
			} else if(webPayment.getVisitor_type().equals("P")) { // 개인정보수집입장
				
				//ApiSocialSaleDTO socialSale = getApiSocialSale(pgResult, webPayment, now);
				ApiSocialSaleDTO socialSale = getApiSocialSale_noSchedule(pgResult, webPayment, now);
				
				List<WebPaymentProductDTO> products = ticketingMapper.selectWebPaymentProductsByOrderNo(pgResult.getMoid());
				List<ApiSaleProductDTO> saleProducts = new ArrayList<>();
				
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
				List<ApiPaymentsInfoDTO> paymentInfos = getApiPaymentInfoWithQuantity_noSchedule(ticketingDate, ticketingDateTime, paymentCode, firstSaleSequence, webPayment.getReserverName(), pgResult, 1);
				List<ApiCardInfoDTO> cardInfos = getApiCardInfoWithQuantity_noSchedule(pgResult, ticketingDateTime, paymentCode, firstSaleSequence, 1);
				
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
			
			return result;
		}catch(Exception ex) {

			ApiResultVO exceptionResult = new ApiResultVO();
			exceptionResult.setSuccess(0);
			//exceptionResult.setErroMsg("알 수 없는 오류로 인해 api호출에 실패하였습니다.");
			exceptionResult.setErrMsg("알 수 없는 오류로 인해 api호출에 실패하였습니다.");
			
			return exceptionResult;
		}
		
	}
	
	
	
	private List<ApiPaymentsInfoDTO> getApiPaymentInfoWithQuantity_noSchedule(String ticketingDate, String ticketingDateTime,
			String paymentCode, String saleSequence, String reserverName, 
			WebPaymentPgResultDTO pgResult, int paymentIdx) {
				
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
		
		return list;
	}
	
	
	
	private List<ApiCardInfoDTO> getApiCardInfoWithQuantity_noSchedule(WebPaymentPgResultDTO pgResult, String ticketingDateTime,
			String paymentCode, String saleSequence, int paymentIdx) {
		
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
		
		return list;
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
		
		//거래처에서 예매한 건이면 terminalCode = "WEBTICKETCUST" 셋팅
		String terminalCode = "WEBTICKET";
		if(webPayment.getPay_method().toString().equals("CUST"))
		{
			terminalCode = "WEBTICKETCUST";
		}
		
		return ApiSocialSaleDTO.builder()
				.CONTENT_MST_CD(webPayment.getContent_mst_cd())
				.TERMINAL_CODE(terminalCode)
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
	
	
	private ApiSocialSaleDTO getApiSocialSale_noSchedule(WebPaymentPgResultDTO pgResult, WebPaymentDTO webPayment, Date saleDate) throws Exception {
		
		Date validFrom = new Date(saleDate.getTime());
		Date validTo = new Date(saleDate.getTime());
		if(webPayment.getProduct_group_kind().equals("1")) {
			Calendar calFrom = Calendar.getInstance(); 
			calFrom.setTime(saleDate);
//			calFrom.add(Calendar.DATE, 1); // 익일부터 사용일경우 주석 풀면 됨
			validFrom = calFrom.getTime();
			
			Calendar calTo = Calendar.getInstance(); 
			calTo.setTime(validFrom); // 10분 더하기 cal.add(Calendar.MINUTE, 10);
			calTo.add(Calendar.DATE, webPayment.getValid_period());
			validTo = calTo.getTime();
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mbId = authentication.getName();
		Map<String, Object> params = new HashMap<>();
		params.put("content_mst_cd", webPayment.getContent_mst_cd());
		params.put("mb_id", mbId);
		
		return ApiSocialSaleDTO.builder()
				.CONTENT_MST_CD(webPayment.getContent_mst_cd())
				.TERMINAL_CODE("WEBRESERVE")
				.SALE_DATE(new SimpleDateFormat("yyyy-MM-dd").format(saleDate))
				.TERMINAL_DATETIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(saleDate))
//					.USER_ID(null)				
				.MEMBER_YN("0")
				.MEMBER_NO(null)
//					.MEMBER_NO(null)
				.SALE_KIND_CODE("0030")
				.ONLINE_CHANNEL(propertyService.getString("online_channel"))
				.WORK_DATETIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(saleDate))
				.ORDER_NUM(pgResult.getMoid())
				.MEMBER_NAME(webPayment.getReserverName())
				.MEMBER_TEL(webPayment.getReserverPhone())
				.MEMBER_EMAIL(webPayment.getReserverEmail())
				.VALID_FROM(new SimpleDateFormat("yyyy-MM-dd").format(validFrom))
				.VALID_TO(new SimpleDateFormat("yyyy-MM-dd").format(validTo))
				.AGREE_1(webPayment.getAgree_1())
				.AGREE_2(webPayment.getAgree_2())
				.build();
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
		} else if(pgResult.getPay_method().equals("CUST") || pgResult.getPay_method().equals("cust")) {
			cardInfo.setPURCHASE_CODE(pgResult.getCustCode());
			cardInfo.setPURCHASE_NAME("거래처예매");
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
			//int timeout = 40000; // 40초
			int timeout = 90000; // 1분30초로 변경
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectionRequestTimeout(timeout);
			factory.setConnectTimeout(timeout);
			factory.setReadTimeout(timeout);
			
			HashMap<String, Object> result = new HashMap<String, Object>();
			
			RestTemplate restTemplate = new RestTemplate(factory);
			
			HttpHeaders header 	= new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON_UTF8);
			String apiKey 		= propertyService.getString("apiKey");
			String apiValue 	= propertyService.getString("apiValue");
			header.add(apiKey, apiValue);
			
			HttpEntity<?> entity = new HttpEntity<>(socialSalesJson, header);
			
			String url = propertyService.getString("apiUrl") + target;
			
			
			log.info("[API CALL] Target URL ==> " + url);
			
			UriComponents uri 				= UriComponentsBuilder.fromHttpUrl(url).build();
			ResponseEntity<Map> resultMap 	= null;
			
			try {
				
				resultMap = restTemplate.exchange(uri.toString(), httpMethod, entity, Map.class);
				result.put("statusCode", resultMap.getStatusCodeValue());
				result.put("header", resultMap.getHeaders());
				result.put("body",  resultMap.getBody());
			}catch(Exception ex) {
				
				log.error("ApiCallError[Disconnectec]: " + ex.getMessage());
				
				ex.printStackTrace();
				
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
	
	
	//==========================================================================KSI PG 결제모듈=====================================================================
	/**
	 * kis pg 결제 모듈
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public Model kisPgPayReult(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
		//String apprReqUrl  = "https://testapi.kispg.co.kr/v2/payment";  	//개발
		//String apprReqUrl  = "https://api.kispg.co.kr/v2/payment";      	//운영
		String apprReqUrl 		= propertyService.getString("kisPgPayModule"); 
		String apprCancelReqUrl = propertyService.getString("kisPgPayCanelModule"); 
		
		int resultSuccess 		= 0;
		String resultOrderNum 	= "";
		String resultMessage 	= "";
		
		/*------------ 인증 데이터 로깅 --------------*/
		request.setCharacterEncoding("utf-8");
		
		Enumeration params = request.getParameterNames();
		  
		/*
		while(params.hasMoreElements()) {
			String name = (String) params.nextElement();
			System.out.print(name + " : " + request.getParameter(name) + "\r\n"); 
		}  
		*/
		
		String charset     = "utf-8";   							// 가맹점 charset
		

		String resultCd    = request.getParameter("resultCd");		//  인증 결과코드 (정상 : 0000)
		String resultMsg   = request.getParameter("resultMsg");		//  인증 결과메시지
		String payMethod   = request.getParameter("payMethod");		//  인증 결제수단 (ex. card)
		String tid         = request.getParameter("tid");			//  KISPG TID
		String goodsAmt    = request.getParameter("amt");			//  결제금액
		String mbsReserved = request.getParameter("mbsReserved");	//  가맹점 에코필드  
		String ediDate     = getyyyyMMddHHmmss();					//  결제요청시간 (yyyyMMddHHmmss)
		
		
		String ordNo 		= request.getParameter("ordNo");		//  상품 주문번호
		VerificationKeyVO key = ticketingMapper.selectSitekeyInfo(ordNo);
		
		String mid         	= key.getPay_merchant_id();				// 상점아이디
		String merchantKey 	= key.getPay_merchant_key();			// 상점키
		
		String encData     	= encrypt(mid + ediDate + goodsAmt + merchantKey);	// 가맹점 검증 해쉬값
		

		//결제인증 정상
		if(null != resultCd && resultCd.equals("0000"))
		{
			
			// 결제 인증 완료 기록 저장
			WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-결제인증성공")
				.message("AuthResultCode: " + resultCd)
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(authenticationFinishedStatus);
			
			//결제 승인 요청 및 상태 저장
			HashMap<String, String> returnMap = callKisPgModulePay(charset, tid, goodsAmt, ediDate, mid, encData, ordNo, apprReqUrl);
			
			
			if(returnMap.get("payResultFlag").equals("success"))
			{//결제 승인 요청 성공 시
				
				//PG사 결제승인 결과 정보 저장 
				WebPaymentPgResultDTO pgResult = new WebPaymentPgResultDTO();
				
				try 
				{
					pgResult = insertPgResultTable(resultCd, resultMsg, apprReqUrl, mid, returnMap );
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					
					//결제 승인 취소 요청 및 상태 저장
					String cancelMsg = "niceFail";
					HashMap<String, String> returnCancelMap = callKisPgModulePayCancel(charset, tid, goodsAmt, ediDate, mid, encData, ordNo, apprCancelReqUrl, payMethod, cancelMsg);
					
					if(returnCancelMap.get("payResultFlag").equals("success"))
					{
						resultSuccess = 0;
						resultMessage = "PG-REULST-INSERT-실패.\n결제취소 되었습니다." + e.getMessage();
					}
					else
					{
						resultSuccess = 0;
						resultMessage = "PG-REULST-INSERT-실패.\n결제취소실패!\n관리자에게 문의하세요." + e.getMessage();
					}
					
					WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
							.status("PG-REULST-INSERT-실패")
							.message("ERROR MSG: " + e.getMessage())
							.orderNo(ordNo)
							.build();
					addWebPaymentStatus(apiCallStatus);
				}
				
				
				//결제수단별 승인 응답코드, 정상인지 check
				if(getPaymentResultStatus(returnMap))
				{
					//결제 승인 응답코드 정상이면 발권솔루션(ISMS) 예매API 호출
					ApiResultVO apiResultVO = callTicketApi(pgResult); //pg결제 성공 후 api 호출
					
					
					if(apiResultVO.getSuccess() == 1) 
					{// API 호출 성공 
						
						WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
								.status("고객-예매-Api호출-성공")
								.message("ApiCallResult: " + apiResultVO.toString())
								.orderNo(ordNo)
								.build();
						addWebPaymentStatus(apiCallStatus);
						
						ShopDetailVO shopDetail = getShopDetail(apiResultVO.getWebPayment().getShop_code());
						
						try {
							//알림톡 저장
							messageService.send(request, response, apiResultVO, pgResult, shopDetail);
							
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						
						if(null != apiResultVO.getWebPayment().getReserverEmail() && !"".equals(apiResultVO.getWebPayment().getReserverEmail()))
						
						//메일
						//if(StringUtils.hasText(apiResultVO.getWebPayment().getReserverEmail()))
						if(null != apiResultVO.getWebPayment().getReserverEmail() && !"".equals(apiResultVO.getWebPayment().getReserverEmail()))
						{
							try {
								
								CompanyVO company = getCompany(apiResultVO.getWebPayment().getShop_code());
								mailService.sendReserve(request, apiResultVO, pgResult);
								
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						}
						
						resultSuccess 	= 1;
						resultOrderNum 	= ordNo;
						resultMessage 	= "결제에 성공하였습니다.";
					}
					else
					{// API호출 실패
						
						WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
								.status("고객-예매-Api호출-실패")
								.message("ApiCallResult: " + apiResultVO.toString())
								.orderNo(ordNo)
								.build();
						addWebPaymentStatus(apiCallStatus);
						
						WebPaymentDTO webPayment = getWebPayment(ordNo);
						SaleVO searchSale = new SaleVO();
						searchSale.setShop_code(webPayment.getShop_code());
						searchSale.setOrder_num(ordNo);
						
						SaleVO saleVO = getSaleSsByOrderNum(searchSale);
						
						if(saleVO != null) {
							SaleDTO sale = new SaleDTO();
							sale.setSale_code(saleVO.getSale_code());
							
							List<SaleProductDTO> bookNoList = getBookNoBySaleCode(sale);
							
							//발권솔루션 API 호출 실패시, 통합권시스템 예매내역 삭제 api 호출.
							callReserveCancelApi(pgResult, bookNoList, apiResultVO.getWebPayment().getContent_mst_cd());
							
						}
						
						
						//결제 승인 취소 요청 및 상태 저장
						String cancelMsg = "niceFail";
						HashMap<String, String> returnCancelMap = callKisPgModulePayCancel(charset, tid, goodsAmt, ediDate, mid, encData, ordNo, apprCancelReqUrl, payMethod, cancelMsg);
						
						if(returnCancelMap.get("payResultFlag").equals("success"))
						{
							resultSuccess = 0;
							resultMessage = "결제 API 호출에 실패하였습니다.\n결제취소 되었습니다." + apiResultVO.getErrMsg();
						}
						else
						{
							resultSuccess = 0;
							resultMessage = "결제 API 호출에 실패하였습니다.\n결제취소실패!\n관리자에게 문의하세요." + apiResultVO.getErrMsg();
						}
					}
				}
				else
				{// 성공 응답코드 아닐 경우 
					
					// 결제승인에러 기록
					WebPaymentStatusDTO approvalFailStatus = WebPaymentStatusDTO.builder()
						.status("고객-예매-결제승인-에러")
						.message(CommUtil.convertMapToJsonString(returnMap))
						.orderNo(ordNo)
						.build();
					addWebPaymentStatus(approvalFailStatus);
					
					resultSuccess = 0;
					resultMessage = "결제에 실패 하였습니다.\n" + returnMap.get("r_resultMsg");;
				}
			}
			else
			{
				//결제승인 요청 실패, 통신에러 ( return : 9999 )
				
				resultSuccess = 0;
				resultMessage = "결제 승인 통신에 실패하였습니다.\n" + returnMap.get("r_resultMsg");;
			}
		}
		else
		{// 결제 인증이 실패시
			
			// 결제인증실패 기록
			WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder()
					.status("고객-예매-결제인증실패")
					.message("AuthResultCode: " + resultCd + " | AuthResultMsg" + resultMsg)
					.orderNo(ordNo)
					.build();
			
			addWebPaymentStatus(authenticationFinishedStatus);
			
			resultSuccess = 0;
			resultMessage = "결제인증에 실패하였습니다.";
		}
		
		resultOrderNum = CommUtil.rmQuatations(resultOrderNum);
		resultMessage = CommUtil.rmQuatations(resultMessage);
		
		model.addAttribute("success", resultSuccess);
		model.addAttribute("orderNo", resultOrderNum);
		model.addAttribute("message", resultMessage);
		
		return model;
	}
	
	/**
	 * 거래처 예매
	 */
	@Override
	public HashMap<String, String> custPayReult(HttpServletRequest request, HttpServletResponse response, Model model, PaymentInfoDTO paymentInfo) throws Exception {

		//String apprReqUrl  = "https://testapi.kispg.co.kr/v2/payment";  	//개발
		//String apprReqUrl  = "https://api.kispg.co.kr/v2/payment";      	//운영
		
		String apprReqUrl 		= "https://iticket.nicetcm.co.kr"; 
		String apprCancelReqUrl = propertyService.getString("kisPgPayCanelModule"); 
		
		
		int resultSuccess 		= 0;
		String resultOrderNum 	= "";
		String resultMessage 	= "";
		
		/*------------ 인증 데이터 로깅 --------------*/
		request.setCharacterEncoding("utf-8");
		
		Enumeration params = request.getParameterNames();
		
		
		String charset     	= "utf-8";   							//	가맹점 charset

		String resultCd    	= "0000";								//	인증 결과코드 (정상 : 0000)
		String resultMsg   	= "거래처예매성공";							//	인증 결과메시지
		String payMethod   	= paymentInfo.getPayMethodStr();		//	인증 결제수단 (ex. card)
		String tid         	= paymentInfo.getOrdNo();				//	KISPG TID => 거래처 예매라 예매번호로 tid 셋팅
		String goodsAmt    	= paymentInfo.getGoodsAmt(); 			//	결제금액
		String mbsReserved 	= "";									//	가맹점 에코필드  
		String ediDate     	= getyyyyMMddHHmmss();					//	결제요청시간 (yyyyMMddHHmmss)
		
		String goodsNm 		= paymentInfo.getGoodsNm();				//	상품명
		String ordNo 		= paymentInfo.getOrdNo();				//	상품 주문번호
		String ordNm		= paymentInfo.getOrdNm();				//	주문자명
		String ordTel		= paymentInfo.getOrdTel();				//	주문자 핸드폰번화
		
		String custCode		= paymentInfo.getCustCode();			//거래처코드
		
		//아래는 필요 없을듯.
		//VerificationKeyVO key = ticketingMapper.selectSitekeyInfo(ordNo);
		String mid         	= "custpay00M";							// 상점아이디
		String merchantKey 	= "";									// 상점키
		String encData     	= encrypt(mid + ediDate + goodsAmt + merchantKey);	// 가맹점 검증 해쉬값
		
		//결제인증 정상
		if(null != resultCd && resultCd.equals("0000"))
		{
			// 결제 인증 완료 기록 저장
			WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder()
				.status("거래처-예매-결제인증성공")
				.message("AuthResultCode: " + resultCd)
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(authenticationFinishedStatus);
			
			
			// 결제 승인 요청 기록
			WebPaymentStatusDTO approvalRequestStatus = WebPaymentStatusDTO.builder()
				.status("거래처-예매-결제승인요청")
				.message("")
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(approvalRequestStatus);
			
			
			//결제 승인 요청 및 상태 저장
			//HashMap<String, String> returnMap = callKisPgModulePay(charset, tid, goodsAmt, ediDate, mid, encData, ordNo, apprReqUrl);
			
			//거래처 예매라 KIS PG 호출 하지 않고, returnMap에 값 셋팅.
			HashMap<String, String> returnMap = new HashMap<String, String>();
			
			returnMap.put("payResultFlag"	, "success");
		    returnMap.put("r_resultCode"	, "0000");		//승인 결과코드	
		    returnMap.put("r_resultMsg"		, "success");	//승인 결과메시지
		    returnMap.put("r_payMethod"		, payMethod);	//승인 결제수단
		    returnMap.put("r_tid"			, tid);			//거래번호
		    returnMap.put("r_appDtm"		, ediDate);		//거래일시
		    returnMap.put("r_appNo"			, ordNo);		//승인번호
		    returnMap.put("r_ordNo"			, ordNo);		//주문번호
		    returnMap.put("r_goodsName"		, goodsNm);		//결제 상품명
		    returnMap.put("r_amt"			, goodsAmt);	//결제 금액
		    returnMap.put("r_ordNm"			, ordNm);		//주문자명 (결제자이름)
		    returnMap.put("r_fnNm"			, payMethod);	//카드사명 => 거래처 예매라 "cust"
		    returnMap.put("r_appCardCd"		, "000");		//발급사 코드
		    returnMap.put("r_acqCardCd"		, "000");		//매입사 코드
		    returnMap.put("r_quota"			, "");			//승인 할부개월 (카드할부기간)
		    returnMap.put("r_usePointAmt"	, "");			//사용 포인트 양
		    returnMap.put("r_cardType"		, "9");			//카드타입 (0 : 신용, 1 : 체크 )
		    returnMap.put("r_authType"		, "09");		//인증타입 (01 : Keyin, 02 : ISP, 03 : VISA)
		    returnMap.put("r_cashCrctFlg"	, "0");			//현금영수증 (0 : 사용안함, 1 : 사용 )
		    returnMap.put("r_vacntNo"		, "");			//가상계좌번호
		    returnMap.put("r_lmtDay"		, "");			//입금기한
		    returnMap.put("r_socHpNo"		, ordTel);		//휴대폰번호
		    returnMap.put("r_cardNo"		, "");			//카드번호 (마스킹 카드번호)
		    returnMap.put("r_mbsReserved"	, "");			//가맹점 예약 필드
		    returnMap.put("r_crctType"		, "0");			//현금영수증타입 (0:미발행 1:소득공제 2:지출증빙)
		    returnMap.put("r_crctNo"		, "");			//현금영수증번호 (휴대폰번호 | 사업자번호)
			
			
		    // 결제 승인 결과 기록
			WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder()
				.status("거래처-예매-승인-성공")
				.message(payMethod + ":" + resultCd )
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(approvalResultStatus);
			
			
			if(resultCd.equals("0000"))
			{//결제 승인 요청 성공 시
				
				//PG사 결제승인 결과 정보 저장 
				WebPaymentPgResultDTO pgResult = new WebPaymentPgResultDTO();
				
				try 
				{
					pgResult = insertPgResultTable(resultCd, resultMsg, apprReqUrl, mid, returnMap );
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					
					// 결제 승인 요청 기록
					WebPaymentStatusDTO approvalCancelRequestStatus = WebPaymentStatusDTO.builder()
						.status("거래처-예매-결제승인취소요청")
						.message("")
						.orderNo(ordNo)
						.build();
					addWebPaymentStatus(approvalCancelRequestStatus);
					
					
					HashMap<String, String> returnCancelMap = new HashMap<String, String>();
					
					returnCancelMap.put("payResultFlag"	, "success");
			    	
				    returnCancelMap.put("r_resultCode"	, "0000");		//승인취소 결과코드	
				    returnCancelMap.put("r_resultMsg"	, "거래처취소");	//승인취소 결과메시지
				    returnCancelMap.put("r_payMethod"	, payMethod);	//승인취소 결제수단
				    returnCancelMap.put("r_tid"			, tid);			//거래번호
				    returnCancelMap.put("r_appDtm"		, ediDate);		//거래일시
				    returnCancelMap.put("r_appNo"		, ordNo);		//승인번호
				    returnCancelMap.put("r_ordNo"		, ordNo);		//주문번호
				    returnCancelMap.put("r_amt"			, goodsAmt);	//결제 금액
				    returnCancelMap.put("r_cancelYN"	, "Y");			//취소여부
				    
				    
				    // 결제 승인취소 결과 기록
					WebPaymentStatusDTO approvalCancelResultStatus = WebPaymentStatusDTO.builder()
						.status("거래처-예매-승인취소-성공")
						.message(returnCancelMap.get("r_payMethod") + ": " + returnCancelMap.get("r_resultCode"))
						.orderNo(ordNo)
						.build();
					addWebPaymentStatus(approvalCancelResultStatus);
					
					
					
					
					//결제 승인 취소 요청 및 상태 저장
					//String cancelMsg = "niceFail";
					//HashMap<String, String> returnCancelMap = callKisPgModulePayCancel(charset, tid, goodsAmt, ediDate, mid, encData, ordNo, apprCancelReqUrl, payMethod, cancelMsg);
					
					if(returnCancelMap.get("payResultFlag").equals("success"))
					{
						resultSuccess = 0;
						resultMessage = "PG-REULST-INSERT-실패.\n결제취소 되었습니다." + e.getMessage();
					}
					else
					{
						resultSuccess = 0;
						resultMessage = "PG-REULST-INSERT-실패.\n결제취소실패!\n관리자에게 문의하세요." + e.getMessage();
					}
					
					WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
							.status("PG-REULST-INSERT-실패")
							.message("ERROR MSG: " + e.getMessage())
							.orderNo(ordNo)
							.build();
					addWebPaymentStatus(apiCallStatus);
				}
				
				//결제수단별 승인 응답코드, 정상인지 check
				if(getPaymentResultStatus(returnMap))
				{
					//결제 승인 응답코드 정상이면 발권솔루션(ISMS) 예매API 호출
					
					pgResult.setCustCode(custCode);
					ApiResultVO apiResultVO = callTicketApi(pgResult); //pg결제 성공 후 api 호출
					
					
					if(apiResultVO.getSuccess() == 1) 
					{// API 호출 성공 
						
						WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
								.status("고객-예매-Api호출-성공")
								.message("ApiCallResult: " + apiResultVO.toString())
								.orderNo(ordNo)
								.build();
						addWebPaymentStatus(apiCallStatus);
						
						ShopDetailVO shopDetail = getShopDetail(apiResultVO.getWebPayment().getShop_code());
						
						try {
							//알림톡 저장
							messageService.send(request, response, apiResultVO, pgResult, shopDetail);
							
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						
						if(null != apiResultVO.getWebPayment().getReserverEmail() && !"".equals(apiResultVO.getWebPayment().getReserverEmail()))
						
						//메일
						//if(StringUtils.hasText(apiResultVO.getWebPayment().getReserverEmail()))
						if(null != apiResultVO.getWebPayment().getReserverEmail() && !"".equals(apiResultVO.getWebPayment().getReserverEmail()))
						{
							try {
								
								CompanyVO company = getCompany(apiResultVO.getWebPayment().getShop_code());
								mailService.sendReserve(request, apiResultVO, pgResult);
								
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						}
						
						resultSuccess 	= 1;
						resultOrderNum 	= ordNo;
						resultMessage 	= "결제에 성공하였습니다.";
					}
					else
					{// API호출 실패
						
						WebPaymentStatusDTO apiCallStatus = WebPaymentStatusDTO.builder()
								.status("고객-예매-Api호출-실패")
								.message("ApiCallResult: " + apiResultVO.toString())
								.orderNo(ordNo)
								.build();
						addWebPaymentStatus(apiCallStatus);
						
						WebPaymentDTO webPayment = getWebPayment(ordNo);
						SaleVO searchSale = new SaleVO();
						searchSale.setShop_code(webPayment.getShop_code());
						searchSale.setOrder_num(ordNo);
						
						SaleVO saleVO = getSaleSsByOrderNum(searchSale);
						
						if(saleVO != null) {
							SaleDTO sale = new SaleDTO();
							sale.setSale_code(saleVO.getSale_code());
							
							List<SaleProductDTO> bookNoList = getBookNoBySaleCode(sale);
							
							//발권솔루션 API 호출 실패시, 통합권시스템 예매내역 삭제 api 호출.
							callReserveCancelApi(pgResult, bookNoList, apiResultVO.getWebPayment().getContent_mst_cd());
							
						}
						
						
						//결제 승인 취소 요청 및 상태 저장
						String cancelMsg = "niceFail";
						HashMap<String, String> returnCancelMap = callKisPgModulePayCancel(charset, tid, goodsAmt, ediDate, mid, encData, ordNo, apprCancelReqUrl, payMethod, cancelMsg);
						
						if(returnCancelMap.get("payResultFlag").equals("success"))
						{
							resultSuccess = 0;
							resultMessage = "결제 API 호출에 실패하였습니다.\n결제취소 되었습니다." + apiResultVO.getErrMsg();
						}
						else
						{
							resultSuccess = 0;
							resultMessage = "결제 API 호출에 실패하였습니다.\n결제취소실패!\n관리자에게 문의하세요." + apiResultVO.getErrMsg();
						}
					}
				}
				else
				{// 성공 응답코드 아닐 경우 
					
					// 결제승인에러 기록
					WebPaymentStatusDTO approvalFailStatus = WebPaymentStatusDTO.builder()
						.status("고객-예매-결제승인-에러")
						.message(CommUtil.convertMapToJsonString(returnMap))
						.orderNo(ordNo)
						.build();
					addWebPaymentStatus(approvalFailStatus);
					
					resultSuccess = 0;
					resultMessage = "결제에 실패 하였습니다.\n" + returnMap.get("r_resultMsg");;
				}
				
				
				//iticket 일일예약에 insert ( 다이아몬드베이 홈티켓 거래처 예매시 )
				//pgResult.
				
			}
			else
			{
				//결제승인 요청 실패, 통신에러 ( return : 9999 )
				
				resultSuccess = 0;
				resultMessage = "결제 승인 통신에 실패하였습니다.\n" + returnMap.get("r_resultMsg");;
			}
		}
		else
		{// 결제 인증이 실패시
			
			// 결제인증실패 기록
			WebPaymentStatusDTO authenticationFinishedStatus = WebPaymentStatusDTO.builder()
					.status("고객-예매-결제인증실패")
					.message("AuthResultCode: " + resultCd + " | AuthResultMsg" + resultMsg)
					.orderNo(ordNo)
					.build();
			
			addWebPaymentStatus(authenticationFinishedStatus);
			
			resultSuccess = 0;
			resultMessage = "결제인증에 실패하였습니다.";
		}
		
		resultOrderNum = CommUtil.rmQuatations(resultOrderNum);
		resultMessage = CommUtil.rmQuatations(resultMessage);
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		/*
		model.addAttribute("success", resultSuccess);
		model.addAttribute("orderNo", resultOrderNum);
		model.addAttribute("message", resultMessage);
		*/
		
		resultMap.put("success", Integer.toString(resultSuccess));
		resultMap.put("orderNo", resultOrderNum);
		resultMap.put("message", resultMessage);
		
		return resultMap;
	}
	
	
	/**
	 * kis pg 사용자 결제 취소 모듈
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public String kisPgPayCancelReult(SaleDTO sale,  HttpServletRequest request, HttpServletResponse response, RedirectAttributes rttr, String redirectPage) throws Exception {
		
		if(reserveTicketCancelCheck(sale, request, rttr, redirectPage))
		{
			return redirectPage;
		}
		
		WebPaymentDTO webPayment 		= getWebPayment(sale.getOrder_num());
		WebPaymentPgResultDTO pgResult 	= getWebPaymentPgResult(sale.getOrder_num());
		
		if(pgResult == null) {
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "결제 되지 않은 티켓은 고객페이지에서의 취소가 불가능합니다. 관리자에게 연락해 주세요.");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return redirectPage;
		}
		
		
		WebPaymentStatusDTO cancelTicketStatus = WebPaymentStatusDTO.builder()
				.status("고객-전체취소-시작")
				.message("OrderNo: " + sale.getOrder_num() + " | Name:" + pgResult.getName() + " | Phone: " + pgResult.getPhone())
				.orderNo(sale.getOrder_num())
				.build();
		addWebPaymentStatus(cancelTicketStatus);
		
		
		String partialCancel	= "0";
		String cancelAmount		= pgResult.getAmt();
		
		// 1. sale_code 기준으로 bc_sale_product 탐색 후 결과 리스트(book_no) 갖고오기
		List<SaleProductDTO> bookNoList = getBookNoBySaleCode(sale);
		
		
		if(sale.getType().equals("1")) 
		{
			/*
			 *  부분환불 
			 *  환불 규정에 의해 전체금액의 50%를 환불, 전체인원이 취소되는 경우 (상황4)
			 *  1. 환불하고자 하는 book_no와 refund_fee를 담아 보내기
			 *  2. pgResult.getAmt() 는 환불하고자 하는 금액 (refund_fee)를 보내기
			 */
			
			partialCancel = "1";
			BigDecimal percent = new BigDecimal("0.5");
			for(int i=0; i<bookNoList.size(); i++) 
			{
				BigDecimal productFee = new BigDecimal(String.valueOf(bookNoList.get(i).getProduct_fee()));
				bookNoList.get(i).setRefund_fee(productFee.multiply(percent));
			}
			
			BigDecimal originalAmount = new BigDecimal(String.valueOf(pgResult.getAmt()));
			int amount 		= originalAmount.multiply(percent).intValue();
			cancelAmount 	= String.valueOf(amount);
		}
		else if(sale.getType().equals("0")) 
		{
			/*
			 *  전체환불
			 *  환불규정에 의해 전체금액 100%를 환불, 전체인원이 취소되는 경우 (상황2)
			 *  1. callCancelApi 호출시 모든 bc_sale_product의 book_no & refund_fee를 list에 담아 보내기
			 *  2. pgResult.getAmt() 는 기존 전체 금액 그대로 보내기 (수정 필요 X)
			 */
			partialCancel = "0";
			
			for(int i=0; i<bookNoList.size(); i++) 
			{
				bookNoList.get(i).setRefund_fee(bookNoList.get(i).getProduct_fee());
			}
		}
		
		
		
		//ISMS 취소 API 호출
		ApiResultVO apiResult = callReserveCancelApi(pgResult, bookNoList, sale.getContent_mst_cd()); 
		
		if(apiResult.getSuccess() != 1) 
		{//ISMS 예매 취소 실패
			
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "예매취소에 실패하였습니다" + apiResult.getErrMsg());
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			return redirectPage;
		}
		else
		{//ISMS 예매 취소 성공
			
			HashMap<String, String> returnCancelMap = new HashMap<String, String>();
			
			if(!pgResult.getPay_method().toString().equals("cust"))
			{
				//KIS PG 결제 취소를 위해 호출
				returnCancelMap =  callKisPgCancelModule(pgResult, cancelAmount, webPayment);
			}
			else
			{
				returnCancelMap.put("payResultFlag", "success");
				returnCancelMap.put("r_resultCode", "2001");
			}
			
			
			if(returnCancelMap.get("payResultFlag").equals("success"))
			{//취소성공
				
				// 2001 : 취소성공, 2211 : 환불성공
				if(returnCancelMap.get("r_resultCode").equals("2001") || returnCancelMap.get("r_resultCode").equals("2211")) 
				{//취소 응답이 성공인 경우
					
					//취소 성공 내용 PG_RESULT 에 기록하기
					updatePgResultTable(returnCancelMap, cancelAmount);
					
					
					SaleVO searchSale = new SaleVO();
					searchSale.setShop_code(webPayment.getShop_code());
					searchSale.setOrder_num(webPayment.getOrder_no());
					
					SaleVO saleVO = getSaleSsByOrderNum(searchSale);
					
					List<SaleProductDTO> saleProducts = getSaleProductDTO(sale);
					saleVO.setSaleProducts(saleProducts);
					
					
					//bc_paymentsale에 마이너스금액으로 기록
					BigDecimal refundFee = insertPaymentMinusSale(sale, saleProducts);
					
					
					//bc_refund_history 기록
					insertRefundHistory(sale, bookNoList, refundFee);
					
					
					ShopDetailVO shopDetail = getShopDetail(webPayment.getShop_code());
					
					//알림톡 메시지 전송
					messageService.sendRefund(request, saleVO, webPayment, pgResult, shopDetail); 

					//취소메일 전송
					mailService.sendRefundOfDiamondbay(request, saleVO, webPayment, pgResult); 
					
					
					sale.setType("1");
					
					List<SaleProductDTO> saleProductDTOList = getSaleProductDTOList(sale);
					if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() > 1)
					{
						ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "prevShowTicket?type=1&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code());
					}
					else if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() == 1)
					{
						
						ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "prevShowTicket?type=0&content_mst_cd="+sale.getContent_mst_cd());
						//ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "checkTicket?content_mst_cd=" + sale.getContent_mst_cd());
					}
					else
					{
						ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "prevShowTicket?type=0&content_mst_cd="+sale.getContent_mst_cd());
						//ScriptUtils.alertAndMovePage(response, "취소에 성공하였습니다", "checkTicket?content_mst_cd=" + sale.getContent_mst_cd());
						
					}
					
					return null;
				}
				else
				{//취소 응답이 정상이 아닌경우
					CompanyVO company = getCompany(webPayment.getShop_code());
					
					sale.setType("1");
					
					List<SaleProductDTO> saleProductDTOList = getSaleProductDTOList(sale);
					if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() > 1)
					{
						ScriptUtils.alertAndMovePage(response, "예약취소에 장애가 발생하였습니다.[" +  returnCancelMap.get("r_resultCode") + 
								"-" + returnCancelMap.get("r_resultMsg") + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
								company.getComp_tel() + ")에게 연락 부탁드립니다.", "prevShowTicket?type=1&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code());
					}
					else if(saleProductDTOList.isEmpty() == false && saleProductDTOList.size() == 1)
					{
						ScriptUtils.alertAndMovePage(response, "예약취소에 장애가 발생하였습니다.[" +  returnCancelMap.get("r_resultCode") + 
								"-" + returnCancelMap.get("r_resultMsg") + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
								company.getComp_tel() + ")에게 연락 부탁드립니다.", "prevShowTicket?type=0&content_mst_cd=" + sale.getContent_mst_cd()+"&member_name="+sale.getMember_name()+"&member_tel="+sale.getMember_tel()+"&shop_code="+sale.getShop_code()+"&sale_code="+saleProductDTOList.get(0).getSale_code()+"&order_num="+saleProductDTOList.get(0).getOrder_num());
					}
					else
					{
						/*
						ScriptUtils.alertAndMovePage(response, "예약취소에 장애가 발생하였습니다.[" +  returnCancelMap.get("r_resultCode") + 
								"-" + returnCancelMap.get("r_resultMsg") + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
								company.getComp_tel() + ")에게 연락 부탁드립니다.", "checkTicket?content_mst_cd=" + sale.getContent_mst_cd());
						*/
						
						ScriptUtils.alertAndMovePage(response, "예약취소에 장애가 발생하였습니다.[" +  returnCancelMap.get("r_resultCode") + 
								"-" + returnCancelMap.get("r_resultMsg") + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
								company.getComp_tel() + ")에게 연락 부탁드립니다.", "prevShowTicket?type=0");
					}
					
					return null;
				}
			}
			else
			{//통신실패
				
				CompanyVO company = getCompany(webPayment.getShop_code());
				
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "예약취소를 위한 통신에 장애가 발생하였습니다.[" +  returnCancelMap.get("r_resultCode") + 
						"-" + returnCancelMap.get("r_resultMsg") + "] 예매가 취소되었으나 환불 금액이 정상적으로 반환되지 않았을 경우, 관리자(" + 
						company.getComp_tel() + ")에게 연락 부탁드립니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				return redirectPage;
			}
		}
		
	}
	
	
	/**
	 * 취소 성공 내용 PG_RESULT 에 기록하기
	 * @param returnCancelMap
	 * @param cancelAmount
	 * @throws Exception
	 */
	public void updatePgResultTable(HashMap<String, String> returnCancelMap, String cancelAmount) throws Exception{
		
		WebPaymentPgResultDTO result = new WebPaymentPgResultDTO();
		result.setResult_code(returnCancelMap.get("r_resultCode"));
		result.setResult_msg(returnCancelMap.get("r_resultMsg"));
		result.setCancel_amt(cancelAmount);
		result.setCancel_time(returnCancelMap.get("r_appDtm"));
		result.setTid(returnCancelMap.get("r_tid"));
		
		updateWebPaymentPgResult(result);
	}
	
	/**
	 * bc_paymentsale에 마이너스금액으로 기록
	 * @param sale
	 * @param saleProducts
	 * @throws Exception
	 */
	public BigDecimal insertPaymentMinusSale(SaleDTO sale, List<SaleProductDTO> saleProducts) throws Exception{
		
		ShopPaymentsaleVO paymentsale = new ShopPaymentsaleVO();
		paymentsale.setShop_code(sale.getShop_code());
		paymentsale.setSale_code(sale.getSale_code());
		paymentsale.setPayment_no(saleProducts.get(0).getSale_sequence());
		List<ShopPaymentsaleVO> list = selectPaymentSale(paymentsale);
		for(int i=0; i<list.size(); i++) 
		{
			if(list.get(i).getPayment_idx().equals("1")) 
			{
				paymentsale = list.get(i);
				int idx = Integer.parseInt(list.get((list.size()-1)).getPayment_idx())+1;
				paymentsale.setPayment_idx(String.valueOf(idx));
			}
		}
		BigDecimal refundFee =  new BigDecimal(paymentsale.getPayment_fee().intValue());
		BigDecimal minus = new BigDecimal(-1);
		paymentsale.setPayment_fee(refundFee.multiply(minus));
		
		insertPaymentSale(paymentsale);
		
		return refundFee;
	}
	
	/**
	 * bc_refund_history 기록
	 * @param sale
	 * @param bookNoList
	 * @param refundFee
	 * @throws Exception
	 */
	public void insertRefundHistory(SaleDTO sale, List<SaleProductDTO> bookNoList, BigDecimal refundFee) throws Exception{
		
		RefundHistoryVO historyVO = new RefundHistoryVO();
		historyVO.setShop_code(sale.getShop_code());
		historyVO.setSale_code(sale.getSale_code());
		historyVO.setCount(bookNoList.size());
		historyVO.setFee(refundFee.toString());
		String bookNo="";
		if(bookNoList != null && !bookNoList.isEmpty()) {
			
			for(int i=0; i<bookNoList.size(); i++) {
				if(i !=( bookNoList.size()-1)) {
					bookNo += bookNoList.get(i).getBook_no()+",";
				}else {
					bookNo += bookNoList.get(i).getBook_no();
				}
			}
		}
		historyVO.setBook_no(bookNo);
		historyVO.setWork_id("WEBRESERVE");
		
		insertRefundHistory(historyVO);
	}
	
	
	/**
	 * 고객 예매 취소 KIS PG 호출
	 * @param pgResult
	 * @param cancelAmount
	 * @param webPayment
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> callKisPgCancelModule(WebPaymentPgResultDTO pgResult, String cancelAmount, WebPaymentDTO webPayment) throws Exception{
		
		String charset     		= "utf-8";   
		String apprCancelReqUrl = propertyService.getString("kisPgPayCanelModule");
		
		/*
		****************************************************************************************
		* <취소요청 파라미터>
		* 취소시 전달하는 파라미터입니다.
		* 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며, 
		* 추가 가능한 옵션 파라미터는 연동메뉴얼을 참고하세요.
		****************************************************************************************
		*/
		String tid 			= pgResult.getTid();		// 거래 ID
		String cancelAmt 	= cancelAmount;				// 취소금액
		String mid 			= pgResult.getMid();		// 상점 ID
		String moid			= pgResult.getMoid();		// 주문번호
		String cancelMsg 	= "clientCancel";			// 취소사유
		String payMethod	= pgResult.getPay_method();	// 결제타입 ( CARD, BANK.. )
		
		VerificationKeyVO keys = getKeys(webPayment.getShop_code());
		
		/*
		 ****************************************************************************************
		 * <해쉬암호화> (수정하지 마세요) SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
		 ****************************************************************************************
		 */
		String merchantKey 	= keys.getPay_merchant_key(); // 상점키
		String ediDate 		= getyyyyMMddHHmmss();
		String encData     	= encrypt(mid + ediDate + cancelAmt + merchantKey);	// 가맹점 검증 해쉬값
		
		
		//결제 승인 취소 요청! 및 상태 저장
		return callKisPgModulePayCancel(charset, tid, cancelAmt, ediDate, mid, encData, moid, apprCancelReqUrl, payMethod, cancelMsg);
		
	}
	
	
	
	/**
	 * 취소 가능한 티켓인지 validation 체크
	 * @param sale
	 * @param request
	 * @param rttr
	 * @param redirectPage
	 * @return
	 * @throws Exception
	 */
	public boolean reserveTicketCancelCheck(SaleDTO sale, HttpServletRequest request, RedirectAttributes rttr, String redirectPage) throws Exception {
		
		Date now = new Date();
		Date today = DateHelper.getDateStart(now);
		
		boolean status = false;
		
		List<RefundVO> refunds = getRefund(sale);
		
		if(refunds == null || refunds.size() == 0) {
			rttr.addFlashAttribute("buyerInfo", sale);
			rttr.addFlashAttribute("message", "환불 정보가 존재하지 않습니다.");
			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			
			status = true;
			return status;
		}
		
		for(RefundVO refund : refunds) {
			if(refund.getRefund_yn().equals("1")) {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "이미 환불처리된 티켓입니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				
				status = true;
				return status;
			}
			
			if(refund.getUsed_count() > 0) {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "이미 사용된 티켓입니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				
				status = true;
				return status;
			}
			
			if(now.after(refund.getPlay_datetime())) {
				rttr.addFlashAttribute("buyerInfo", sale);
				rttr.addFlashAttribute("message", "사용기간이 지난 티켓은 환불이 불가능합니다.");
				request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
				
				status = true;
				return status;
			}
		}
		
		return status;
	}
	
	
	/**
	 * 발권솔루션 API 호출 실패시, 통합발권시스템에 등록된 예매내역 삭제를 위한 자체 API 호출
	 * @param pgResult
	 * @param bookNoList
	 * @param contentMstCd
	 * @return
	 * @throws Exception
	 */
	private ApiResultVO callReserveCancelApi(WebPaymentPgResultDTO pgResult, List<SaleProductDTO> bookNoList, String contentMstCd) throws Exception {
		List<ApiProductRefundDTO> productRefund = new ArrayList<>();
		
		for(int i=0; i<bookNoList.size(); i++) {
			productRefund.add(ApiProductRefundDTO.builder()
					.BOOK_NO(bookNoList.get(i).getBook_no())
					.REFUND_FEE(bookNoList.get(i).getRefund_fee().intValue())
					.build());
		}
		
		//거래처에서 예매한 건이면 terminalCode = "WEBTICKETCUST" 셋팅
		String terminalCode = "WEBCANCEL";
		String userId		= "WEBTICKET";
		if(pgResult.getPay_method().toString().equals("CUST") || pgResult.getPay_method().toString().equals("cust"))
		{
			terminalCode 	= "WEBCANCELCUST";
			userId			= "WEBTICKETCUST";
		}
		
		// api 취소 요청
		ApiSocialCancelDTO apiSocialCancel = ApiSocialCancelDTO.builder()
				.CONTENT_MST_CD(contentMstCd)
				.ONLINE_CHANNEL("1001")
				.ORDER_NUM(pgResult.getMoid())
				.USER_ID(userId)
				.TERMINAL_CODE(terminalCode)
				.TERMINAL_DATETIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
				.CANCEL_TYPE("A") 
				.SALE_PRODUCT_LIST(productRefund)
				.build();
		ApiResultVO apiCancelResult =  callCancelApi(apiSocialCancel);
		
		// 결제승인에러 기록				
		WebPaymentStatusDTO apiCancelStatus = WebPaymentStatusDTO.builder()
			.status(apiCancelResult.getSuccess() == 1 ? "고객-취소Api-성공" : "고객-취소Api-실패")
			.message("Success: " + apiCancelResult.getSuccess() + " | Message: " + apiCancelResult.getErrMsg())
			.orderNo(pgResult.getMoid())
			.build();
		addWebPaymentStatus(apiCancelStatus);
		
		return apiCancelResult;
	}
	
	public HashMap<String, String> callKisPgModulePay(String charset, String tid, String goodsAmt, String ediDate, String mid, String encData, String ordNo, String apprReqUrl) throws Exception {
		
		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		// 결제 승인 요청 기록
		WebPaymentStatusDTO approvalRequestStatus = WebPaymentStatusDTO.builder()
			.status("고객-예매-결제승인요청")
			.message("")
			.orderNo(ordNo)
			.build();
		addWebPaymentStatus(approvalRequestStatus);
		
		
		//결제 승인 데이터 json 가공
		JSONObject jsonObj = new JSONObject();

	    jsonObj.put("mid"     , mid);
	    jsonObj.put("tid"     , tid);
	    jsonObj.put("goodsAmt", goodsAmt);
	    jsonObj.put("ediDate" , ediDate);
	    jsonObj.put("encData" , encData);
	    jsonObj.put("charset" , charset);
	  
	    String apprReqMsg = jsonObj.toString();
	    
	    log.debug("=================KIS PG PAY CALL=======================");
	    log.debug("[apprReqMsg : " + apprReqMsg + "]");
	    String apprRecvMsg = callKisPgApi(apprReqMsg, apprReqUrl, charset);
	    log.debug("[apprRecvMsg : " + apprRecvMsg + "]");
	    log.debug("=======================================================");
	  
	    
	    if("9999".equals(apprRecvMsg))
	    {//결제 승인 실패
	    	// 결제승인에러 기록
			WebPaymentStatusDTO approvalErrorStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-결제승인통신에러")
				.message(apprRecvMsg)
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(approvalErrorStatus);
			
			
			//KIS PG 결제 모듈은 망취소가 따로 없는 것 같음
			//..
			
			returnMap.put("payResultFlag"	, "fail");
	    }
	    else
	    {
	    	/*승인 응답파라미터 정의*/
	    	HashMap<String,String> apprRecvHash = jsonDataParser(apprRecvMsg);
	    	
	    	returnMap.put("payResultFlag"	, "success");
		    returnMap.put("r_resultCode"	, apprRecvHash.get("resultCd"));	//승인 결과코드	
		    returnMap.put("r_resultMsg"		, apprRecvHash.get("resultMsg"));	//승인 결과메시지
		    returnMap.put("r_payMethod"		, apprRecvHash.get("payMethod"));	//승인 결제수단
		    returnMap.put("r_tid"			, apprRecvHash.get("tid"));			//거래번호
		    returnMap.put("r_appDtm"		, apprRecvHash.get("appDtm"));		//거래일시
		    returnMap.put("r_appNo"			, apprRecvHash.get("appNo"));		//승인번호
		    returnMap.put("r_ordNo"			, apprRecvHash.get("ordNo"));		//주문번호
		    returnMap.put("r_goodsName"		, apprRecvHash.get("goodsName"));	//결제 상품명
		    returnMap.put("r_amt"			, apprRecvHash.get("amt"));			//결제 금액
		    returnMap.put("r_ordNm"			, apprRecvHash.get("ordNm"));		//주문자명 (결제자이름)
		    returnMap.put("r_fnNm"			, apprRecvHash.get("fnNm"));		//카드사명
		    returnMap.put("r_appCardCd"		, apprRecvHash.get("appCardCd"));	//발급사 코드
		    returnMap.put("r_acqCardCd"		, apprRecvHash.get("acqCardCd"));	//매입사 코드
		    returnMap.put("r_quota"			, apprRecvHash.get("quota"));		//승인 할부개월 (카드할부기간)
		    returnMap.put("r_usePointAmt"	, apprRecvHash.get("usePointAmt"));	//사용 포인트 양
		    returnMap.put("r_cardType"		, apprRecvHash.get("cardType"));	//카드타입 (0 : 신용, 1 : 체크 )
		    returnMap.put("r_authType"		, apprRecvHash.get("authType"));	//인증타입 (01 : Keyin, 02 : ISP, 03 : VISA)
		    returnMap.put("r_cashCrctFlg"	, apprRecvHash.get("cashCrctFlg"));	//현금영수증 (0 : 사용안함, 1 : 사용 )
		    returnMap.put("r_vacntNo"		, apprRecvHash.get("vacntNo"));		//가상계좌번호
		    returnMap.put("r_lmtDay"		, apprRecvHash.get("lmtDay"));		//입금기한
		    returnMap.put("r_socHpNo"		, apprRecvHash.get("socHpNo"));		//휴대폰번호
		    returnMap.put("r_cardNo"		, apprRecvHash.get("cardNo"));		//카드번호 (마스킹 카드번호)
		    returnMap.put("r_mbsReserved"	, apprRecvHash.get("mbsReserved"));	//가맹점 예약 필드
		    returnMap.put("r_crctType"		, apprRecvHash.get("crctType"));	//현금영수증타입 (0:미발행 1:소득공제 2:지출증빙)
		    returnMap.put("r_crctNo"		, apprRecvHash.get("crctNo"));		//현금영수증번호 (휴대폰번호 | 사업자번호)
		    
		    
		    // 결제 승인 결과 기록
			WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-승인-성공")
				.message(returnMap.get("r_payMethod") + ": " + returnMap.get("r_resultCode"))
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(approvalResultStatus);
	    }
	    
		return returnMap;
	}
	
	
	public HashMap<String, String> callKisPgModulePayCancel(String charset, String tid, String goodsAmt, String ediDate, String mid, String encData, String ordNo, String apprReqUrl, String payMethod, String cancelMsg) throws Exception {
		
		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		// 결제 승인 요청 기록
		WebPaymentStatusDTO approvalRequestStatus = WebPaymentStatusDTO.builder()
			.status("고객-예매-결제승인취소요청")
			.message("")
			.orderNo(ordNo)
			.build();
		addWebPaymentStatus(approvalRequestStatus);
		
		
		//결제 승인 취소 데이터 json 가공
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("payMethod"		, payMethod);
		jsonObj.put("tid"     		, tid);
	    jsonObj.put("mid"     		, mid);
	    jsonObj.put("ordNo"			, ordNo);
	    jsonObj.put("canAmt"		, goodsAmt);
	    jsonObj.put("canMsg"		, cancelMsg);
	    jsonObj.put("partCanFlg"	, "0");			//전체취소 : 0;
	    jsonObj.put("encData" 		, encData);
	    jsonObj.put("ediDate" 		, ediDate);
	    jsonObj.put("charset" 		, charset);
	    
	    
	    String apprReqMsg = jsonObj.toString();
	    
	    
	    log.debug("=====================KIS PG PAY CANCEL CALL===============");
	    log.debug("[apprReqMsg : " + apprReqMsg + "]");
	    String apprRecvMsg = callKisPgApi(apprReqMsg, apprReqUrl, charset);
	    log.debug("[apprRecvMsg : " + apprRecvMsg + "]");
	    log.debug("==========================================================");
	    
	    if("9999".equals(apprRecvMsg))
	    {//결제 승인 실패
	    	// 결제승인에러 기록
			WebPaymentStatusDTO approvalErrorStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-결제승인취소 통신에러 999")
				.message(apprRecvMsg)
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(approvalErrorStatus);
			
			
			//KIS PG 결제 모듈은 망취소가 따로 없는 것 같음
			//..
			
			returnMap.put("payResultFlag"	, "fail");
	    }
	    else
	    {
	    	/*승인 응답파라미터 정의*/
	    	HashMap<String,String> apprRecvHash = jsonDataParser(apprRecvMsg);
	    	
	    	returnMap.put("payResultFlag"	, "success");
	    	
		    returnMap.put("r_resultCode"	, apprRecvHash.get("resultCd"));	//승인취소 결과코드	
		    returnMap.put("r_resultMsg"		, apprRecvHash.get("resultMsg"));	//승인취소 결과메시지
		    returnMap.put("r_payMethod"		, apprRecvHash.get("payMethod"));	//승인취소 결제수단
		    returnMap.put("r_tid"			, apprRecvHash.get("tid"));			//거래번호
		    returnMap.put("r_appDtm"		, apprRecvHash.get("appDtm"));		//거래일시
		    returnMap.put("r_appNo"			, apprRecvHash.get("appNo"));		//승인번호
		    returnMap.put("r_ordNo"			, apprRecvHash.get("ordNo"));		//주문번호
		    returnMap.put("r_amt"			, apprRecvHash.get("amt"));			//결제 금액
		    returnMap.put("r_cancelYN"		, apprRecvHash.get("cancelYN"));	//취소여부
		    
		    
		    // 결제 승인취소 결과 기록
			WebPaymentStatusDTO approvalResultStatus = WebPaymentStatusDTO.builder()
				.status("고객-예매-승인취소-성공")
				.message(returnMap.get("r_payMethod") + ": " + returnMap.get("r_resultCode"))
				.orderNo(ordNo)
				.build();
			addWebPaymentStatus(approvalResultStatus);
	    }
	    
		return returnMap;
	}
	
	public WebPaymentPgResultDTO insertPgResultTable(String resultCd, String resultMsg, String apprReqUrl, String mid, HashMap<String, String> returnMap ) throws Exception{
		
		// PG사 결제 결과 정보 저장
		WebPaymentPgResultDTO pgResult = new WebPaymentPgResultDTO();
		
		pgResult.setAuth_result_code(resultCd);		//인증 응답코드
		pgResult.setAuth_result_msg(resultMsg);
		pgResult.setNext_ap_url(apprReqUrl);
		pgResult.setTransaction_id(returnMap.get("r_tid"));	//kispg는 승인번호 apprNo로 대체 -> tid로 대체
		pgResult.setAuth_token("");
		pgResult.setPay_method(returnMap.get("r_payMethod"));
		pgResult.setMid(mid);
		pgResult.setMoid(returnMap.get("r_ordNo"));
		pgResult.setAmt(returnMap.get("r_amt"));
		pgResult.setNet_cancel_url("");
		pgResult.setAuth_signature_comparison("0");
		pgResult.setApproval_result_code(returnMap.get("r_resultCode"));
		pgResult.setApproval_result_msg(returnMap.get("r_resultMsg"));
		pgResult.setTid(returnMap.get("r_tid"));
		
		if(returnMap.get("r_payMethod").equals("CARD"))
		{
			pgResult.setCardNo(returnMap.get("r_cardNo"));
			pgResult.setCardQuota(returnMap.get("r_quota"));
			pgResult.setCardCode(returnMap.get("r_appCardCd"));
			pgResult.setAcquCardCode(returnMap.get("r_acqCardCd"));
			pgResult.setAcquCardName(returnMap.get("r_fnNm"));
			pgResult.setRcptType("");	//뭔지모름;; 블루컴아놔
		}
		else if(returnMap.get("r_payMethod").equals("BANK"))
		{
			pgResult.setCardCode(returnMap.get("vacntNo"));	//가상계좌
			pgResult.setCardName(returnMap.get("fnNm"));	//은행명
			pgResult.setRcptType("");
		}
		else if(returnMap.get("r_payMethod").equals("cust"))
		{
			pgResult.setCardNo(returnMap.get(""));
			pgResult.setCardQuota(returnMap.get(""));
			pgResult.setCardCode(returnMap.get(""));
			pgResult.setAcquCardCode(returnMap.get(""));
			pgResult.setAcquCardName(returnMap.get(""));
			pgResult.setRcptType("");	//뭔지모름;; 블루컴아놔
		}
		
		pgResult.setAuth_code(returnMap.get("r_appNo"));
		pgResult.setAuth_date(returnMap.get("r_appDtm"));
		
		addWebPaymentPgResult(pgResult);
		
		return pgResult;
	}
	
	
	public HashMap<String,String> jsonDataParser(String jsonStr){
		HashMap<String,String> jsonHash = new HashMap<String,String>();
	    
	    try{
	    	JSONParser parser = new JSONParser();    
	        Object obj = parser.parse(jsonStr);  
	        JSONObject jsonObj = (JSONObject) obj;
	        
	        Iterator iter = jsonObj.keySet().iterator();
	        
	        while(iter.hasNext()){
	           String key   = (String) iter.next();
	           String value = (String) jsonObj.get(key);
	    
	           jsonHash.put(key, value);
	        }
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }

	    return jsonHash;
	}
	
	public final synchronized String getyyyyMMddHHmmss(){
	  	SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	  	return yyyyMMddHHmmss.format(new Date());
	}
	
	public String encrypt(String strData){
		String passACL = null;
	  	MessageDigest md = null;
	  	try{
	  		md = MessageDigest.getInstance("SHA-256");
	  		md.reset();
	  		md.update(strData.getBytes());
	  		byte[] raw = md.digest();
	  		passACL = encodeHex(raw);
	  	}catch(Exception e){
	  		System.out.print("암호화 에러" + e.toString());
	  	}
	  	return passACL;
	  }
	  
	public String encodeHex(byte [] b){
		char [] c = Hex.encodeHex(b);
		return new String(c);
	}
	  
	  
	  
	public static String callKisPgApi(String reqMsg, String reqUrl, String charSet){
		HttpsURLConnection conn		= null;
		BufferedReader resultReader	= null;
		PrintWriter pw				= null;
		URL url						= null;
	      
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
	  
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
	  
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

	      
		String apprRecvMsg = null;

		int statusCode = 0;
		int msgLen     = 0;
		StringBuffer recvBuffer = new StringBuffer();
	      
		try{
			msgLen = reqMsg.getBytes(charSet).length;
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();

	        //return apprRecvMsg;
			return "9999";
		}

		try{
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	  
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			url = new URL(reqUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(25000);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json; charset=" + charSet);
			conn.setRequestProperty("Content-Length", String.valueOf(msgLen));

			
			pw = new PrintWriter(conn.getOutputStream());
			pw.write(reqMsg);
			pw.flush();
	          
			statusCode = conn.getResponseCode();

			resultReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

			for(String temp; (temp = resultReader.readLine()) != null;){
				recvBuffer.append(temp).append("\n");
			}
	          
			if(!(statusCode == HttpURLConnection.HTTP_OK)){
				throw new Exception();
			}

			apprRecvMsg = recvBuffer.toString().trim();
	          
			return apprRecvMsg;
		}catch (Exception e){
			e.printStackTrace();

			//return apprRecvMsg;
			return "9999";
		}finally{
			recvBuffer.setLength(0);
	          
			try{
				if(resultReader != null){
					resultReader.close();
				}
			}catch(Exception ex){
				resultReader = null;
			}
	          
			try{
				if(pw != null) {
					pw.close();
				}
			}catch(Exception ex){
				pw = null;
			}
	          
			try{
				if(conn != null) {
					conn.disconnect();
				}
			}catch(Exception ex){
				conn = null;
			}
		}
	}
	
	
	public boolean getPaymentResultStatus(HashMap<String, String> returnMap) {
		
		boolean resultBool = false;
		
		if(returnMap.get("r_payMethod").equals("CARD"))
		{
			if(returnMap.get("r_resultCode").equals("3001"))
			{
				resultBool = true;
			}
		}
		else if(returnMap.get("r_payMethod").equals("BANK"))
		{
			if(returnMap.get("r_resultCode").equals("4000"))
			{
				resultBool = true;
			}
		}
		else if(returnMap.get("r_payMethod").equals("CELLPHONE"))
		{
			if(returnMap.get("r_resultCode").equals("A000"))
			{
				resultBool = true;
			}
		}
		else if(returnMap.get("r_payMethod").equals("VBANK"))
		{
			if(returnMap.get("r_resultCode").equals("4100"))
			{
				resultBool = true;
			}
		}
		else if(returnMap.get("r_payMethod").equals("SSG_BANK"))
		{
			if(returnMap.get("r_resultCode").equals("0000"))
			{
				resultBool = true;
			}
		}
		else if(returnMap.get("r_payMethod").equals("CMS_BANK"))
		{
			if(returnMap.get("r_resultCode").equals("0000"))
			{
				resultBool = true;
			}
		}
		else if(returnMap.get("r_payMethod").equals("cust"))
		{
			if(returnMap.get("r_resultCode").equals("0000"))
			{
				resultBool = true;
			}
		}
		
		return resultBool;
	}
	
	
	public HashMap<String, String> selectNoticeInfo(SaleProductDTO vo) throws Exception {
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		resultMap = ticketingMapper.selectNoticeInfo(vo);
		
		return resultMap;
	}
	
	
	@Override
	public TicketValidVO selectTicketValid(TicketValidVO ticketValid) throws Exception {

		return ticketingMapper.selectTicketValid(ticketValid);
	}
	
	
	@Override
	public List<RefundVO> getRefund_noSchedule(SaleDTO_noSchedule sale) throws Exception {
		
		return ticketingMapper.selectRefund_noSchedule(sale);
	}
	
	
}


