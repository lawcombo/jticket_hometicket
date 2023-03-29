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
String payMethod        = webPayment.getPay_method(); 							// 결제수단
String merchantKey 		= webPayment.getMerchantKey(); 							// 상점키
String merchantID 		= webPayment.getMerchantID(); 							// 상점아이디
String goodsName 		= webPayment.getProduct_group_name().toString(); 		// 결제상품명
String price 			= webPayment.getTotal_fee().toString(); 				// 결제상품금액	
String buyerName 		= webPayment.getReserverName(); 						// 구매자명
String buyerTel 		= webPayment.getReserverPhone(); 						// 구매자연락처
String buyerEmail 		= webPayment.getReserverEmail(); 						// 구매자메일주소
String moid 			= webPayment.getOrder_no(); 							// 상품주문번호	
String returnURL 		= "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/mcycamping/payResult"; // 결과페이지(절대경로) - 모바일 결제창 전용

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
<%-- <%@ include file="../include/header-single.jsp" %> --%>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="robots" content="NOINDEX, NOFOLLOW">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-214554947-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-214554947-1');
</script>
<%-- <title><spring:message code="site.title" /></title> --%>

<title>소금산 그랜드밸리</title>

<!-- 아래 js는 PC 결제창 전용 js입니다.(모바일 결제창 사용시 필요 없음) -->
<script src="https://web.nicepay.co.kr/v3/webstd/js/nicepay-3.0.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/styles.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/common.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/single.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/byn.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/dataTables.bootstrap4.min.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/reservation.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/custom.css" />
<link href="/resources/noSchedule/css/sub.css" rel="stylesheet">
<link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
<script src="${pageContext.request.contextPath }/resources/noSchedule/js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/common.js"></script>
<style>
	#nice_layer {overflow:hidden !important;}
</style>
</head>
<script type="text/javascript">

//결제창 최초 요청시 실행됩니다.
function nicepayStart(pay){

	if(pay>0) {
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
<section class="dmzbt_sec bookingsec pyrt">
	<div class="dmz_list ddid_list jw_list edt_ex">
		<div class="did_list_wrap ot_dlw ewp_ot">
			<div class="bookingbt_bx">
				<div class="bookingbt_bx_in pro2 payR">
					<div class="main-sec_wrap">
						<div id="pay-info-section" class="paytop bk_box">
							<h2>결제정보 확인</h2>
							<form name="payForm" method="post" action="<c:url value="${webPayment.total_fee gt 0 ? '/ticketing/mcycamping/payResult' : '/ticketing/mcycamping/pay0Result' }" />">
							
								<input type="hidden" name="userId" value="${loginUserId}" />
								<input type="hidden" name="userName" value="${loginUserNm}" />
							
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
								<div class="tbl_frm01 tbl_wrap bookinfo">
									<table class="table table-bordered flexible-table bys_tabst bys_tabst2 etble_cus et_jb">
										<colgroup>
									        <col width='45%'/>
									        <col  width='55%'/>
								    	</colgroup>
								    	<tbody>
											<tr>
												<th><span>결제 상품명</span></th>
												<td><input type="text" name="GoodsName" value="<%=goodsName%>" readonly></td>
											</tr>
											<tr>
												<th><span>결제 인원</span></th>
												<td style="text-align:left;">
													<span style="color:#333;">
														<c:forEach var="product" items="${paymentInfo.products }" varStatus="status">
															<c:out value="${product.product_name }" /> <c:out value="${product.count }" />명
															<c:if test="${not status.last }">
																&nbsp;&#47;
															</c:if>
														</c:forEach>
													</span>
												</td>
											</tr>
											<tr>
												<th><span>결제 상품금액</span></th>
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
												<td style="text-align:left;">
													<input type="hidden" name="BuyerEmail" value="<%=buyerEmail%>" readonly>
													<span style="color:#333;"><%=buyerEmail%></span>
												</td>
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
									
									
									<!-- 소금산밸리 로그인 ID -->
									<%-- 
									<input type="hidden" name="loginUserId" value="${loginUserId}" />
									<input type="hidden" name="loginUserNm" value="${loginUserNm}" />
									 --%>
								</div>

								
								<p class="mo_t1">결제 하시겠습니까?</p>
								
								<div class="req payR eidt_cus mo_btn ewp_mo_btn" style="margin-top:10px;">
									<a href="#" class="btn_blue btn_ok" onClick="nicepayStart(<c:out value='${webPayment.total_fee }'/>);">결제</a>
								</div> 
								
								<%--<div class="mo_btn ewp_mo_btn">
									<button class="btn_no" type="button">취 &nbsp;&nbsp;&nbsp; 소</button>
									<button class="btn_ok" type="button" onClick="nicepayStart(<c:out value='${webPayment.total_fee }'/>);">확 &nbsp;&nbsp;&nbsp; 인</button>
								</div>--%>

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