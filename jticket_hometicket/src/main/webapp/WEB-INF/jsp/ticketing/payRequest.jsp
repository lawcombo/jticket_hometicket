<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="org.apache.commons.codec.binary.Hex" %>

<%
/*
*******************************************************
* <결제요청 파라미터>
* 결제시 Form 에 보내는 결제요청 파라미터입니다.
* 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며, 
* 추가 가능한 옵션 파라미터는 연동메뉴얼을 참고하세요.
*******************************************************
*/
com.bluecom.ticketing.domain.WebPaymentDTO webPayment = (com.bluecom.ticketing.domain.WebPaymentDTO)request.getAttribute("webPayment");
String payMethod        = webPayment.getPay_method(); // 결제수단
String merchantKey 		= webPayment.getMerchantKey(); // 상점키
String merchantID 		= webPayment.getMerchantID(); // 상점아이디
String goodsName 		= webPayment.getProduct_group_name().toString(); // 결제상품명
String price 			= webPayment.getFee().toString(); // 실제 결제 금액
String buyerName 		= webPayment.getReserverName(); // 구매자명
String buyerTel 		= webPayment.getReserverPhone(); // 구매자연락처
String buyerEmail 		= webPayment.getReserverEmail(); // 구매자메일주소
String moid 			= webPayment.getOrder_no(); // 상품주문번호	
String coupon			= webPayment.getCouponFee(); //쿠폰 총 금액
String fee				= webPayment.getTotal_fee().toString(); //총 상품 금액
String returnURL 		= "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/payResult"; // 결과페이지(절대경로) - 모바일 결제창 전용

/*
*******************************************************
* <해쉬암호화> (수정하지 마세요)
* SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다. 
*******************************************************
*/
DataEncrypt sha256Enc 	= new DataEncrypt();
String ediDate 			= getyyyyMMddHHmmss();	
String hashString 		= sha256Enc.encrypt(ediDate + merchantID + price + merchantKey);
%>
<%@ include file="../include/header-single.jsp" %>
<style>
a.disabled {
  pointer-events: none;
  cursor: default;
}
</style>
<!-- 아래 js는 PC 결제창 전용 js입니다.(모바일 결제창 사용시 필요 없음) -->
<script src="https://web.nicepay.co.kr/v3/webstd/js/nicepay-3.0.js" type="text/javascript"></script>
<script type="text/javascript">

//결제창 최초 요청시 실행됩니다.
function nicepayStart(pay){
	if(pay > 0) {
		if(checkPlatform(window.navigator.userAgent) == "mobile"){//모바일 결제창 진입
			document.payForm.action = "https://web.nicepay.co.kr/v3/v3Payment.jsp";
			document.payForm.acceptCharset="euc-kr";
			document.payForm.submit();
		}else{//PC 결제창 진입
			goPay(document.payForm);
		}
	} else {
		document.payForm.submit();
	}
}

//[PC 결제창 전용]결제 최종 요청시 실행됩니다. <<'nicepaySubmit()' 이름 수정 불가능>>
function nicepaySubmit(){
	document.payForm.submit();
}

//[PC 결제창 전용]결제창 종료 함수 <<'nicepayClose()' 이름 수정 불가능>>
function nicepayClose(){
	alert("결제가 취소 되었습니다");
}

//pc, mobile 구분(가이드를 위한 샘플 함수입니다.)
function checkPlatform(ua) {
	if(ua === undefined) {
		ua = window.navigator.userAgent;
	}
	
	ua = ua.toLowerCase();
	var platform = {};
	var matched = {};
	var userPlatform = "pc";
	var platform_match = /(ipad)/.exec(ua) || /(ipod)/.exec(ua) 
		|| /(windows phone)/.exec(ua) || /(iphone)/.exec(ua) 
		|| /(kindle)/.exec(ua) || /(silk)/.exec(ua) || /(android)/.exec(ua) 
		|| /(win)/.exec(ua) || /(mac)/.exec(ua) || /(linux)/.exec(ua)
		|| /(cros)/.exec(ua) || /(playbook)/.exec(ua)
		|| /(bb)/.exec(ua) || /(blackberry)/.exec(ua)
		|| [];
	
	matched.platform = platform_match[0] || "";
	
	if(matched.platform) {
		platform[matched.platform] = true;
	}
	
	if(platform.android || platform.bb || platform.blackberry
			|| platform.ipad || platform.iphone 
			|| platform.ipod || platform.kindle 
			|| platform.playbook || platform.silk
			|| platform["windows phone"]) {
		userPlatform = "mobile";
	}
	
	if(platform.cros || platform.mac || platform.linux || platform.win) {
		userPlatform = "pc";
	}
	
	return userPlatform;
}
</script>
<section class="dmzbt_sec bookingsec">
	<div class="dmz_list ddid_list jw_list edt_ex">
		<div class="did_list_wrap ot_dlw ewp_ot">
			<div class="bookingbt_bx">
				<div class="bookingbt_bx_in pro2 payR">
					<div class="main-sec_wrap">

						<div id="pay-info-section" class="paytop">
							<h1 id="container_title" class="pay_ch_tit">결제정보 확인</h1>
							<form name="payForm" method="post" action="<c:url value="${webPayment.getFee() > 0 ? '/ticketing/payResult' : '/ticketing/pay0Result'}" />">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
								<div class="tbl_frm01 tbl_wrap bookinfo">
									<table class="table table-bordered flexible-table bys_tabst bys_tabst2 etble_cus pay_rtb">
										<colgroup>
									        <col width='40%'/>
									        <col  width='60%'/>
								    	</colgroup>
								    	<tbody>
										<tr>
											<th><span>결제 상품명</span></th>
											<td><input type="text" name="GoodsName" value="<%=goodsName%>" readonly></td>
										</tr>
										<tr>
											<th><span>결제 상품금액</span></th>
											<td>
												<input type="text" id="productFee" readonly>
												<input type="hidden" value="<%=fee%>" readonly>
											</td>
										</tr>
										<tr>
											<th><span>쿠폰 적용금액</span></th>
											<td>
												<input type="text" id="coupon" readonly>
												<input type="hidden" name="coupon" value="<%=coupon%>" readonly>
											</td>
										</tr>	
										<tr>
											<th><span>총 결제금액</span></th>
											<td>
												<input type="text" name="ShowAmt" readonly>
												<input type="hidden" name="Amt" value="<%=price%>" readonly>
											</td>
										</tr>			
										<tr>
											<th><span>구매자명</span></th>
											<td><input type="text" name="BuyerName" value="<%=buyerName%>" readonly></td>
										</tr>	 
										<tr>
											<th><span>구매자 연락처</span></th>
											<td><input id="memberTel" type="text" name="BuyerTel" value="<%=buyerTel%>" readonly></td>
										</tr>	
										<tr>
											<th><span>구매자 이메일</span></th>
											<td><input type="text" name="BuyerEmail" value="<%=buyerEmail%>" readonly></td>
										</tr>			
							
										
										</tbody>
									</table>
									<!-- 히든 2 -->
									<input type="hidden" name="PayMethod" value="<%=payMethod%>" readonly>
									<input type="hidden" name="MID" value="<%=merchantID%>" readonly>
									<input type="hidden" name="Moid" value="<%=moid%>" readonly>
									<!-- 히든 -->
									<input type="hidden" name="ReturnURL" value="<%=returnURL%>"> <!-- 인증완료 결과처리 URL -->
									<input type="hidden" name="VbankExpDate" value="">	<!-- 가상계좌입금만료일(YYYYMMDD) -->
									<!-- 옵션 --> 
									<input type="hidden" name="GoodsCl" value="1"/>						<!-- 상품구분(실물(1),컨텐츠(0)) -->
									<input type="hidden" name="TransType" value="0"/>					<!-- 일반(0)/에스크로(1) --> 
									<input type="hidden" name="CharSet" value="utf-8"/>					<!-- 응답 파라미터 인코딩 방식 -->
									<input type="hidden" name="ReqReserved" value=""/>					<!-- 상점 예약필드 -->
												
									<!-- 변경 불가능 -->
									<input type="hidden" name="EdiDate" value="<%=ediDate%>"/>			<!-- 전문 생성일시 -->
									<input type="hidden" name="SignData" value="<%=hashString%>"/>	<!-- 해쉬값 -->
								</div>
								<div class="identitybt req payR eidt_cus qu_box">
									<p class="qu_txt">해당 내용으로 결제를 진행하시겠습니까?</p>
									<a href="#" id="payButton" class="btn_blue" onClick="nicepayStart(<%=price%>);">결제</a>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			
			</div>
		</div>
</section>	

<script>
//숫자를 3자리마다 콤마 찍기
function numberWithCommas(x) {
    x = x.toString();
    var pattern = /(-?\d+)(\d{3})/;
    while (pattern.test(x))
        x = x.replace(pattern, "$1,$2");
    return x;
}

	$(function() {
		$("input[name='ShowAmt']").val(numberWithCommas('<%=price %>'));
		$("#productFee").val(numberWithCommas('<%=fee %>'));
		$("#coupon").val("- "+numberWithCommas('<%=coupon %>'));
		
		$('#payButton').on('click', function(e) {
			$(this).addClass('disabled');
		});
		
		$("#memberTel").val(addHyphenToPhoneNumber($("#memberTel").val()));
	});
</script>
</body>
</html>
<%!
public final synchronized String getyyyyMMddHHmmss(){
	SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	return yyyyMMddHHmmss.format(new Date());
}
// SHA-256 형식으로 암호화
public class DataEncrypt{
	MessageDigest md;
	String strSRCData = "";
	String strENCData = "";
	String strOUTData = "";
	
	public DataEncrypt(){ }
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
}
%>
