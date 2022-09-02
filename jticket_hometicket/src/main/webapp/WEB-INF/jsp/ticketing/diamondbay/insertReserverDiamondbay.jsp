<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator"
	uri="http://www.springmodules.org/tags/commons-validator"%>
<%
	NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

	String sSiteCode 		= (String) request.getAttribute("siteCode");			// NICE로부터 부여받은 사이트 코드
	String sSitePassword 	= (String) request.getAttribute("sitePassword");		// NICE로부터 부여받은 사이트 패스워드
	
	String sRequestNumber = "REQ0000000001";			// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
														// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
	sRequestNumber = niceCheck.getRequestNO(sSiteCode);
	session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
	
	String sAuthType 	= "";			// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
	
	String popgubun 	= "N";			//Y : 취소버튼 있음 / N : 취소버튼 없음
	String customize 	= "";			//없으면 기본 웹페이지 / Mobile : 모바일페이지
	
	String sGender 		= ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자 
	
	//운영에는 https 처리
	
	// CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
	//리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : https://www.~ 리턴 url : https://www.~
	String sReturnUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/success?content_mst_cd=" + ((com.bluecom.ticketing.domain.EssentialDTO)request.getAttribute("essential")).getContent_mst_cd();     // 성공시 이동될 URL
	String sErrorUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/fail?content_mst_cd=" + ((com.bluecom.ticketing.domain.EssentialDTO)request.getAttribute("essential")).getContent_mst_cd();         // 실패시 이동될 URL
	
	// 입력될 plain 데이타를 만든다.
	String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
						"8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
						"9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
						"7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
						"7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
						"11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
						"9:CUSTOMIZE" + customize.getBytes().length + ":" + customize + 
						"6:GENDER" + sGender.getBytes().length + ":" + sGender;
	
	String sMessage = "";
	String sEncData = "";
	
	int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
	if( iReturn == 0 )
	{
		sEncData = niceCheck.getCipherData();
	}
	else if( iReturn == -1)
	{
		sMessage = "암호화 시스템 에러입니다.";
	}
	else if( iReturn == -2)
	{
		sMessage = "암호화 처리오류입니다.";
	}
	else if( iReturn == -3)
	{
		sMessage = "암호화 데이터 오류입니다.";
	}
	else if( iReturn == -9)
	{
		sMessage = "입력 데이터 오류입니다.";
	}
	else
	{
		sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
	}
%>

<%@ include file="../../include/diamondbay/header-single.jsp" %>

<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">

<div class="app">

	<!-- 다이아몬드 Top 배경 -->
	<div style="background-image: url('${pageContext.request.contextPath }/resources/images/diamondbay/visual1.jpg'); height:300px;"> 
		<div style="position:absolute; top:10%; left:19%;">
			<img src="${pageContext.request.contextPath }/resources/images/diamondbay/visual_title.png" lat="DIAMOND BAY">
		</div>
	</div>
	

	<!-- 뒤로가기 버튼 -->
	<div style="text-align:left; padding-top:10px; padding-left:10px;">
		
		<img src="${pageContext.request.contextPath }/resources/images/diamondbay/backImg.png" onclick="history.back()" style="cursor: pointer; width: 50px;" />
	</div>



	<div class="body-contents-wrap reservediv">

		<div class="intro res_padi" style="padding-top:35px;">
			<p style="font-size:40px; font-weight:bold;">예약하기</p>
		</div>

		<section class="tabs ewp_tabs">
			<ul>
				<li>
					<div>
						<strong>01</strong> <span>날짜 및 시간 선택</span>
					</div> <span class="material-icons">arrow_forward_ios</span>
				</li>
				<li class="active">
					<div>
						<strong>02</strong> <span>예약정보입력</span>
					</div> <span class="material-icons">arrow_forward_ios</span>
				</li>
				<li>
					<div>
						<strong>03</strong> <span>확인 및 결제</span>
					</div> <span class="material-icons">arrow_forward_ios</span>
				</li>
			</ul>
		</section>
		<form:form role="form">
			<div class="conts_box ins mt50 ewp_fe res_l">
				<section class="reserve reserve-form s_ftsize">
					<div class="verticalAlignMiddle insertrs">
						<dl class="reserveDl cod101">
							<dt class="reserveDt">프로그램 명</dt>
							<dd class="reserveDd">${paymentInfo.productGroup.product_group_name}
							</dd>
						</dl>
						<dl class="reserveDl cod101 mt50">
							<dt class="reserveDt">예약일</dt>
							<dd class="reserveDd" id="play_date">
								<c:out value="${paymentInfo.play_date }" />
							</dd>
						</dl>
						<dl class="reserveDl cod101 mt50">
							<dt class="reserveDt">예약시간</dt>
							<dd class="reserveDd">
								<c:out value="${paymentInfo.schedule.start_time }" />
							</dd>
						</dl>
						<dl class="reserveDl cod101 mt50">
							<dt class="reserveDt">인원</dt>
							<dd class="reserveDd">
								<c:out value="${paymentInfo.totalCount }" />
								명
							</dd>
						</dl>
						<div class="pageLine pglinemt"></div>
						<dl class="reserveDl full mt50">
							<button type="button" id="reserverAuthenticationButton" class="buttonTypeCyan full textLarge" style="cursor:pointer">본인인증</button>
							<p class="ewp_m_info_tb">전자상거래에 의거하여 만 14세 이상만 이용 가능합니다.</p>
						</dl>
						<dl class="reserveDl full mt20">
							<dt class="reserveDt">
								신청자명 <span class="require"></span>
							</dt>
							<dd class="reserveDd">
								<input type="text" name="reserver.name" class="jejuInputBox" id="userName" readonly>
							</dd>
						</dl>
						<dl class="reserveDl full mt20">
							<dt class="reserveDt pb15"> 핸드폰 번호 <span class="require"></span>
							</dt>
							<dd class="reserveDd">
								<dl class="columnDl">
									<dt class="columnDt ewp_input_100">
										<input type="tel" name="reserver.phone" id="phone"
											class="jejuInputBox full gray num_only">
										<p class="ewp_m_info_tb">나이스평가정보에서 인증 받은 휴대폰 번호를 사용하고
											있습니다.</p>
									</dt>
								</dl>
							</dd>
						</dl>
						<dl class="reserveDl full mt20">
							<dt class="reserveDt pb15">
								이메일 <span class="require"></span> <sub>*입력하신 메일주소로 결제정보가 발송됩니다.</sub>
							</dt>
							<dd class="reserveDd">
								<input id="emailInput" type="text" name="reserver.email"
									class="jejuInputBox full gray">
							</dd>
						</dl>
					</div>
				</section>

				<!-- <div class="pageLine dashed gap m_none"></div> -->
				<div class="paybx">
					<div id="hiddenArea">
						<input type="hidden" name="productGroup.shop_code" value="${paymentInfo.productGroup.shop_code }" /> 
						<input type="hidden" name="productGroup.product_group_code" value="${paymentInfo.productGroup.product_group_code }" /> 
						<input type="hidden" name="productGroup.content_mst_cd" value="${paymentInfo.productGroup.content_mst_cd }" />
						<%-- <input type="hidden" name="productGroup.product_group_kind" value="${paymentInfo.productGroup.product_group_kind }" /> --%>
						
						<c:forEach var="product" items="${paymentInfo.products }" varStatus="status">
							<input type="hidden" name="products[${status.index }].shop_code" value="${product.shop_code }" />
							<input type="hidden" name="products[${status.index }].product_group_code" value="${product.product_group_code }" />
							<input type="hidden" class="productCode" name="products[${status.index }].product_code" value="${product.product_code }" />
							<input type="hidden" class="productCount" name="products[${status.index }].count" value="${product.count }" />
							<input type="hidden" class="productFee" name="products[${status.index }].product_fee" value="${product.product_fee}" />
						</c:forEach>
						<input type="hidden" name="visitorType" value="${paymentInfo.visitorType }" /> 
						<input type="hidden" name="totalFee" value="${paymentInfo.totalFee }" /> 
						<input type="hidden" name="fee" /> 
						<input type="hidden" name="totalCount" value="${paymentInfo.totalCount }" /> 
						<input type="hidden" name="schedule_code" value="${paymentInfo.schedule_code}" /> 
						<input type="hidden" name="play_date" value="${paymentInfo.play_date}" /> 
						<input type="hidden" name="couponFee" value="0" /> 
						<input type="hidden" name="reserver.idx" />
					</div>
					<section class="reserve reserve-agree agreesec line_he">
						<div class="verticalAlignMiddle ewp_insert_agree_ex">
							<ul class="agreementUl">
								<li class="agreementLi question toggleHeader">
									<div class="columnDl fontSize20 agree_top ewp_insert_agree_top">
										<div class="columnDt textAlignLeft ewp_iat_left">
											<div class="agreementToggle ft20 tgicon">
												<p class="ewp_insert_agree_tit">개인정보 수집 동의 (보기)</p>
												<img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_arrowDown.png" alt="key visual" class="imgAgreementArrowDown" /> 
												<span class="require"></span>
											</div>
										</div>
										<div class="columnDd textAlignRight reservationRenewPage ewp_iat_right ewp_iat_right2">
											<label for="agreementPrivacy" class="jejuRadioButton jjrb">
												<input type="radio" name="agree_1" id="agreementPrivacy" value="1"> 
												<span class="check"></span> <span class="ag_txt">동의</span>
											</label>
										</div>
									</div>
								</li>
								<li class="agreementLi answer toggleContent"
									style="display: none;">
									<div class="pt20 pb20 ag_tg">
										<c:out value="${reserveInfo.info_a }" escapeXml="false"/>
									</div>
								</li>
							</ul>
							<ul class="agreementUl mt40 last_agree">
								<li class="agreementLi question noBorder">
									<div class="columnDl fontSize20 agree_top ewp_insert_agree_top">
										<div
											class="columnDt textAlignLeft ft20 termsOfUseToggle ewp_iat_left">
											<div>
												<p class="ewp_insert_agree_tit">이용 규정에 대한 동의(보기)</p>
												<img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_arrowDown.png" alt="key visual" class="imgAgreementArrowDown"> 
												<span class="require"></span>
											</div>
										</div>
										<div
											class="columnDd textAlignRight reservationRenewPage ewp_iat_right">
											<div class="chmd">
												<input type="radio" name="agreementTermsOfUse" id="agreementTermsOfUse" class="delinp" value="1">
												<label class="jejuRadioButton jjrb ag_txt">
												<span class='check'></span>동의</label>
											</div>
											<%-- 
											<div class="rad_modal">
												<div class="rad_modal_content">
													<div class="rad_modal_x">
														<div id="agreementTermsOfUse-modal-section"
															class="md_cont">
															<c:out value="${reserveInfo.info_c }" escapeXml="false"/>
															<div class="modal_tb">
																<div class="mt_top">
																	<h2>
																		<strong>잠깐!</strong>
																	</h2>
																	<p class="mt_top_text">필수 확인 사항들을 한번 더 안내 드릴게요.</p>
																</div>
																<div class="mt_bot">
																	<h3>
																		10분 이상 지각 시 참여 및 환불 불가
																	</h3>
																	<p class="mt_txt">
																		- 비행기 지연 및 결항, 건강상의 이유, 천재지변으로 인한 지각은 전화/채팅 문의
																	</p>
																	<h3>
																		취소·환불·변경 (예약시점 X / 이용시점 O)
																	</h3>
																	<p class="mt_txt">
																		- 이용 하루 전 자정까지 : 100% ­취소·환불·변경 가능<br>
																		- 이용 당일 : 취소·환불·변경 모두 불가
																	</p>
																</div>
															</div>

															<button class="md_bts" style="cursor:pointer;">모두 확인했고, 동의합니다.</button>
														</div>
													</div>
												</div>
												<div class="rad_modal_bk"></div>
											</div>
											 --%>
										</div>
									</div>
								</li>
								<li class="agreementLi answer termsOfUseToggleContent"
									style="display: none; border-bottom: none;">
									<div class="pt20 pb20 ag_tg">
										<c:out value="${reserveInfo.info_b }" escapeXml="false"/>
									</div>
								</li>
							</ul>
						</div>
					</section>
					<div class="pageLine mt10 mb70 isPb"></div>
					<section class="pay">
						<div class="pay-info">
							<dl class="receiptDl">
								<dt class="receiptDt">
									<h2 class="pageSubTitle">결제금액</h2>
								</dt>
								<dd class="receiptDd">
									<h2 class="pageSubTitle">
										<fmt:formatNumber type="number" maxFractionDigits="3"
											value="${paymentInfo.totalFee }" /> 원
									</h2>
								</dd>
							</dl>

							<c:forEach var="product" items="${paymentInfo.products }" varStatus="status">
								<dl class="receiptDl mt70 instR mt62">
									<dt class="receiptDt">
										<div class="pageDescription">
											${product.product_name} <sub class="sb">(<fmt:formatNumber type="number" maxFractionDigits="3" value="${product.product_fee }" /> 원 × ${product.count }명)
											</sub>
										</div>
									</dt>
									<dd class="receiptDd">
										<div class="pageDescription">
											<fmt:formatNumber type="number" maxFractionDigits="3" value="${product.product_fee * product.count}" /> 원
										</div>
									</dd>
								</dl>
							</c:forEach>
							<div id="couponArea"></div>
							<div class="pageLine inst bd2"></div>
							<dl class="receiptDl mt15 instR ">
								<dt class="receiptDt">
									<div>합계</div>
								</dt>
								<dd class="receiptDd">
									<div class="pageDescription">
										<span id="totalFee"><fmt:formatNumber type="number" maxFractionDigits="3" value="${paymentInfo.totalFee }" /> 원</span>
									</div>
								</dd>
							</dl>
						</div>

						<div class="pay-role mt15">
							<div class="payment reservationRenewPage">
								<label for="paymentTypeCard" class="jejuRadioButton"> 
								<input type="radio" name="payMethod" id="paymentTypeCard" value="CARD" checked> 
									<span class="radio"></span> <span>신용카드</span>
								</label>
							</div>
							<div class="payment reservationRenewPage txtright">
								<label for="paymentTypeBank" class="jejuRadioButton"> 
								<input type="radio" name="payMethod" id="paymentTypeBank" value="BANK">
									<span class="radio"></span> <span>계좌이체</span>
								</label>
							</div>
							
							<div class="payment reservationRenewPage txtright">
								<label for="paymentTypeCust" class="jejuRadioButton"> 
								<input type="radio" name="payMethod" id="paymentTypeCust" value="CUST">
									<span class="radio"></span> <span>거래처</span>
								</label>
							</div>
							<!-- <div class="payment reservationRenewPage">
								    <label for="paymentTypePhone" class="jejuRadioButton">
								        <input type="radio" name="payMethod" id="paymentTypePhone" value="CELLPHONE">
								        <span class="radio"></span> <span>휴대폰 소액결제</span>
								    </label>
								</div> -->
						</div>
						<!-- <p class="termsLi ps_p isPc">현대카드는 사용하실 수 없습니다. 다른 카드나 실시간 계좌이체를 이용해주세요.</p>
							<p class="termsLi ps_p isMb2">현대카드는 사용하실 수 없습니다.<br>다른 카드나 실시간 계좌이체를 이용해주세요.</p> -->

						<div class="submitbox">
							<button id="settlementButton" class="buttonTypeCyan full bold py">결제하기</button>
						</div>
					</section>

				</div>
				<!-- paybx end -->
			</div>
		</form:form>
	</div>
</div>
<!-- 이용약관 모달 섹션 -->
<%-- <div id="agreementTermsOfUse-modal-section">
		<c:out value="${reserveInfo.info_c }" escapeXml="false"/>
		<button>모두 확인했고, 동의합니다.</button>
	</div>
 --%>
<div id="reserver-authentication-section" style="display: none;">
	<!-- 본인인증 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
	<form name="form_chk" method="post">
		<input type="hidden" name="m" value="checkplusService">
		<!-- 필수 데이타로, 누락하시면 안됩니다. -->
		<input type="hidden" name="EncodeData" value="<%= sEncData %>">
		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
	</form>

	<form role="addReserverAuthenticationResult">
		<input type="hidden" name="name" value="" /> 
		<input type="hidden" name="phone" value="" /> 
		<input type="hidden" name="code" value="" />
		<!-- <input type="hidden" name="authNo" value="" /> -->
		<!-- <input type="hidden" name="responseSEQ" value="" /> -->
	</form>
</div>
<%@ include file="../../include/diamondbay/footer-single.jsp" %>
</body>
</html>


<script>
// 403 에러 처리(Spring Security에서의 Ajax 호출)
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$.ajaxSetup({
	beforeSend: function (xhr) {
    	xhr.setRequestHeader(header, token);
		},
});

//천단위 콤마 펑션
function addComma(value){
	if(value) {
		value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");	
	}
	return value; 
}

function checkArrayContainsObject(a, obj) {
    var i = a.length;
    while (i--) {
       if (a[i] === obj) {
           return true;
       }
    }
    return false;
}

$(function() {
	$("#reservation").attr('class', 'sub-a active');
	// 개인정보 //
	$(".agreementToggle").on('click', function(){
		$(".toggleContent").slideToggle();
	});
	
	// 이용약관 //
	$(".termsOfUseToggle").on('click', function(){
		$(".termsOfUseToggleContent").slideToggle();
	});
	
	
	// 쿠폰번호 입력시 
	$("#couponText").keyup(function(){
		var text = $(this).val();
		$("#couponText").val(text.toUpperCase());
	});
	
	var couponList = new Array();
	var totalFee = $("input[name=totalFee]").val();
	var couponFee = 0;
	var useCoupons = [
		<c:forEach var="item" items="${paymentInfo.productsOrderedByPrice}">
			{
				product_code: "<c:out value='${item.product_code}' />",
				count: <c:out value='${item.count}' />,
				product_fee: "<c:out value='${item.product_fee}' />",
				used_count: 0,
			},
		</c:forEach>
	];
	var useCouponLength=0;
	
	
	
	/***** 본인확인 *****/
	$("#reserverAuthenticationButton").on('click', function(e) {
		
		fnReserverAuthenticationPopup();
			
	});
	
	/***** 결제버튼클릭 *****/
	$("#settlementButton").on('click', function(e) {
		
		var authenticationIdx = $("input[name='reserver.idx']").val();
		if(!authenticationIdx){
			e.preventDefault();
			alert('본인인증을 먼저 진행해 주세요.')
			$("#reserverAuthenticationButton").focus();
			return false;
		}
		
		if(!$("#emailInput").val()) {
			e.preventDefault();
			alert('이메일을 입력해 주세요.')
			$("#emailInput").focus();
			return false;
		}
		
		
		if($("#agreementPrivacy").is(":checked") == false) {
			e.preventDefault();
			alert('개인정보 수집에 동의해 주세요.')
			$("#agreementPrivacy").focus();
			return false;
		}
		
		if($("#agreementTermsOfUse").is(":checked") == false) {
			e.preventDefault();
			alert('이용 규정에 동의해 주세요.')
			$("#agreementTermsOfUse").focus();
			return false;
		}
		
		if($("input[name=fee]").val() == ''){
			$("input[name=fee]").val($("input[name=totalFee]").val());
		}
		
		var popupTitle = 'PayRequest';
		
		popupCenter({url: '', title: popupTitle, w: 650, h: 500});
		
		var form = $('form[role="form"]');
		
		form.attr('target', popupTitle);
		form.attr('method', 'POST');
		form.attr('action', '<c:url value="/ticketing/payRequest?contentMst_cd=${essential.content_mst_cd }" />');
		
		form.submit();
	});
    
});


/***** 본인인증 팝업 *****/
function fnReserverAuthenticationPopup(){
	
	window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
	document.form_chk.target = "popupChk";
	document.form_chk.submit();
}


/***** 본인확인 결과*****/
var setReturnedReserverAuthenticationValuesFromChildWindow = function (result) {
	if(result.success == 0) { // 정상
		console.log(result);
		
	
		//본인인증 응답으로부터 받은 데이터
		$("input[name='name']").val(result.name);		//이름
		if(result.phone == "null")
		{
			$("input[name='phone']").val("");
		}
		else
		{
			$("input[name='phone']").val(result.phone);	//핸드폰번호	
		}
		$("input[name='code']").val(result.code);		//코드
		
	
		var form = $('form[role="addReserverAuthenticationResult"]');
		$.ajax({
			type : 'post',  				       
			url : '<c:url value="/reserverAuthentication/add" />',
			headers : {
				"Content-type" : "application/json;charset=utf-8",
				"X-HTTP-Method-Override" : "POST"
			},
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			dataType : 'json',
			data : JSON.stringify(getFormData(form)),
			success: function(data, status, xhr) {
				console.log(data)
				
				$("input[name='reserver.idx']").val(data.idx);
				$("input[name='reserver.name']").val(data.name);
				$("input[name='reserver.phone']").val(data.phone);
				
			},
			error: function (jqXhr, textStatus, errorMessage) { // error callback 
		        alert("인증 데이터를 저장 할 수 없습니다. 반복시 관리자를 호출해 주세요.");
		      	
				window.close();
		    }
		});
	} else {
		alert('본인인증에 실패하엿습니다.');
	}
	
}

/***** 결제 결과*****/
var setReturnedPayValuesFromChildWindow = function (result) {
	if(result.success == 1) {
		window.location.href= "/ticketing/diamondbay/finish?content_mst_cd=<c:out value='${essential.content_mst_cd }' />&product_group_code=<c:out value='${essential.product_group_code }' />&orderNo=" + result.orderNo;	
	}
	
}


/***** 모달창 *****/
var isCheckedAgreementTermsOfUse = false;
$(function(){
	$(".rad_modal").hide();
	$(".chmd").click(function(e){
		
		var isChecked = $("#agreementTermsOfUse").prop("checked");
		
		
		if(isChecked) {
			$("#agreementTermsOfUse").prop("checked", false);
		} else {
			//$(this).next(".rad_modal").fadeIn();
			e.preventDefault();
			$("#agreementTermsOfUse").prop("checked", true);
		}
	});
	
	$(".rad_modal_bk").click(function(){
		$(".rad_modal").fadeOut();
	});
	
	$("#agreementTermsOfUse").on('click', function(e){
		e.preventDefault();
	});
	
	
	//모두 확인했고, 동의합니다.
	/* 
	$(".md_bts").click(function(e){
		e.preventDefault();
		$("#agreementTermsOfUse").prop("checked", true);
		$(".rad_modal").fadeOut();
		
	});
	 */
});

/***** 전화번호, 주민번호 입력 숫자만 *****/
$('.num_only').css('imeMode','disabled').keypress(function(event) {
	if(event.which && (event.which < 48 || event.which > 57 ) ) {
		event.preventDefault();
	}

}).keyup(function(){
	if( $(this).val() != null && $(this).val() != '' ) {
		$(this).val( $(this).val().replace(/[^0-9]/g, '') );
	}
});

var isCheckedAgreementPrivacy = false;
$("#agreementPrivacy").on("click", function(e) {
	var isChecked = $(this).prop("checked");
	
	if(isCheckedAgreementPrivacy && isChecked) {
		$(this).prop("checked", false);	
		isCheckedAgreementPrivacy = false;
	}else if(!isCheckedAgreementPrivacy && isChecked){
		isCheckedAgreementPrivacy = true;
	} else {
		isCheckedAgreementPrivacy = false;
	}
});
</script>

</body>
</html>
