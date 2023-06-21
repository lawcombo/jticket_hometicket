<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
	NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

	String sSiteCode = (String) request.getAttribute("siteCode");			// NICE로부터 부여받은 사이트 코드
	String sSitePassword = (String) request.getAttribute("sitePassword");		// NICE로부터 부여받은 사이트 패스워드

	String sRequestNumber = "REQ0000000001";			// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
														// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
	sRequestNumber = niceCheck.getRequestNO(sSiteCode);
	session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

	String sAuthType = "";			// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

	String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
	String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

	String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자 

    // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
  	//리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
    
	//개발 http
	String sReturnUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/success?content_mst_cd=" + ((com.bluecom.ticketing.domain.PaymentInfoDTO_noSchedule)request.getAttribute("paymentInfo")).getProductGroup().getContent_mst_cd();     // 성공시 이동될 URL
    String sErrorUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/fail?content_mst_cd=" + ((com.bluecom.ticketing.domain.PaymentInfoDTO_noSchedule)request.getAttribute("paymentInfo")).getProductGroup().getContent_mst_cd();         // 실패시 이동될 URL



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

<%-- <%@ include file="../include/header-single.jsp" %> --%>
<%@ include file="../../include/mcycamping/header-single.jsp" %>

<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">

<c:set var="now" value="<%=new java.util.Date() %>" />
<fmt:formatDate var="today" value="${now }" pattern="yyyy-MM-dd"/>
<c:set var="then" value="<%=new java.util.Date() %>" />
<c:set target="${then}" property="time" value="${now.time + 86400000 * paymentInfo.productGroup.valid_period}" />
<fmt:formatDate var="expire" value="${then }" pattern="yyyy-MM-dd"/>


<div class="sub_visual">
	<!-- 	
	<div class="sub_v1 img2">
		<div class="sub_v1_w">
			<h2>예매·예약</h2>
		</div>
	</div>
 	-->
 	
	<div class="sub_v2">
		<div class="sub_v2_w">
			<ul>
				<li>홈</li>
				<li><img src="/resources/noSchedule/images/sub_icon01.jpg" alt="" /></li>
				<li>예매·예약</li>
				<li><img src="/resources/noSchedule/images/sub_icon01.jpg" alt="" /></li>
				<li><b>온라인 예매</b></li>
			</ul>
		</div>
	</div><!-- //sub_v2 -->
</div>


<div id="page-wrapper" class="wrp">
	<section class="sel_sec">
		<!-- <div style="position:relative; float:right;"> -->
			<div class="sel_tit aos-init aos-animate" data-aos="fade-up" data-aos-easing="linear" data-aos-duration="700" data-aos-delay="300">
				<h2>온라인 예매</h2>
				<span class="bar"></span>
			</div>
			<div class="ewp_online_top_btns">
				<%-- 
				<div class="return_bt">
					<a href="<c:url value='/ticketing/sogeumsan/selectTicket' />?content_mst_cd=<c:out value='${paymentInfo.content_mst_cd }' />"><i class="fas fa-arrow-left"></i> 처음으로</a>
				</div>
				 --%>
				<div class="res_btbx" style="float:right;">
					<button id="checkReservationOpenButton">예매확인 및 취소</button>
				</div>
			</div>
		<!-- </div> -->

		<div id="step-section" style="clear:both;">
			<div class="row-h1 bc_status">
				<ul class="aftCB stepul proc01">
					<li>
						<p>

							<!-- <i>1</i> -->
							<span>01. 상품/권종/수량</span>

						</p>
					</li>
					<li class="on">
						<p>

							<!-- <i>2</i> -->
							<span>02. 구매정보입력</span>

						</p>
					</li>
					<li>
						<p>

							<!-- <i>3</i> -->
							<span>03. 확인 및 결제</span>

						</p>
					</li>
				</ul>
			</div>
		</div>
		<form:form role="form" >
			<div class="justify-content-center">
				<div style="display:none;">
					<input type="hidden" name="type" value="${paymentInfo.type }" />
					<input type="hidden" name="productGroup.shop_code" value="${paymentInfo.productGroup.shop_code }" />	
					<input type="hidden" name="productGroup.product_group_code" value="${paymentInfo.productGroup.product_group_code }" />	
					<input type="hidden" name="productGroup.content_mst_cd" value="${paymentInfo.productGroup.content_mst_cd }" />
					<input type="hidden" name="productGroup.type" value="${paymentInfo.productGroup.type }" />	
					<c:forEach var="product" items="${paymentInfo.products }" varStatus="status">
						<input type="hidden" name="products[${status.index }].shop_code" value="${product.shop_code }" />
						<input type="hidden" name="products[${status.index }].product_group_code" value="${product.product_group_code }" />
						<input type="hidden" name="products[${status.index }].product_code" value="${product.product_code }" />
						<input type="hidden" name="products[${status.index }].count" value="${product.count }" />
					</c:forEach>
					<input type="hidden" name="visitorType" value="${paymentInfo.visitorType }" />	
					<input type="hidden" name="totalFee" value="${paymentInfo.totalFee }" />	
					<input type="hidden" name="totalCount" value="${paymentInfo.totalCount }" />
	<%-- 			<input type="hidden" name="schedule_code" value="${paymentInfo.schedule_code}" />	 --%>
	<%-- 			<input type="hidden" name="play_date" value="${paymentInfo.play_date}" /> --%>
	
					<!-- 소금산그랜드밸리 로그인 정보 -->
					<input type="hidden" name="loginUserId" value="${loginUserId}" />
					<input type="hidden" name="loginUserNm" value="${loginUserNm}" />
	
				</div>
				<div class="booking_con2">
				<div id="info-section" class="bk_con2_box bk1">
					<div id="product-info-section" class="box3">
						<h4>상품정보</h4>
						<%-- <div class="d-flex">
							<div style="width:30%;">상품명</div>
							<div style="width:70%;">${paymentInfo.productGroup.product_group_name }</div>
						</div>
						<div class="d-flex">
							<div style="width:30%;">인원</div>
							<div style="width:70%;">
								<span>
									<c:forEach var="product" items="${paymentInfo.products }" varStatus="status">
										<c:out value="${product.product_name }" /> <c:out value="${product.count }" />명
										<c:if test="${not status.last }">
											&nbsp;&#47;
										</c:if>
									</c:forEach>
								</span>
							</div>
						</div>
						<div class="d-flex">
							<div style="width:30%;">사용기간</div>
							<div style="width:70%;">구매일로부터 <c:out value="${paymentInfo.valid_period }" />일</div>
						</div> --%>
						<table class="bk_tb1">
							<colgroup>
								<col width="35%">
								<col width="65%">
							</colgroup>
							<tbody>
								<tr>
									<th>상품명</th>
									<td>${paymentInfo.productGroup.product_group_name }</td>
								</tr>
								<tr>
									<th>인원</th>
									<td>
										<span>
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
									<th>사용기간</th>
									<td>구매일로부터 <c:out value="${paymentInfo.valid_period }" />일</td>
									<!-- <td>2022년 2월 28일까지</td> --><!-- 임시수정 / 2022-02-14 / 조미근 -->
								</tr>
							</tbody>
						</table>
					</div>
					<div id="reserver-info-section" class="box3_1">
						<h4>구매자 정보</h4>
						<input type="hidden" name="reserver.idx" />
						<div class="identitybt iden_wpcus ebtn_cus ins">
							<a id="reserverAuthenticationButton" href="javascript:fnReserverAuthenticationPopup();">
								본인확인
							</a>
						</div>
						<div class="tbl_frm01 tbl_wrap bookinfo">
							<table class="bk_tb2">
								<colgroup>
									<col width='35%'/>
									<col  width='65%'/>
								</colgroup>
								<tbody>
									<tr>
										<th scope="row">예매자 성함</th>
										<td><input readonly type='text' name='reserver.name' placeholder='나이스평가정보에서 인증 받은 이름을 사용' required class="required" style="background-color:#fafafa;"><br></td>
									</tr>
									<tr>
										<th scope="row"><label for="bcd_memo">휴대폰 번호</label></th>
										<td>
											<input readonly type="text" name="reserver.phoneShow" required class='required' style="background-color:#fafafa;" placeholder="나이스평가정보에서 인증 받은 휴대폰 번호를 사용">
											<input readonly type="hidden" name="reserver.phone">
										</td>
									</tr>
									<!-- 
									<tr>
										<th>
											<label for="bcd_memo">E-Mail주소</label>
										</th>
										<td>
											<input type="text" name="reserver.email" id="email">
										</td>
									</tr>
									 -->
									
								</tbody>
							</table>
							<!-- <p class="pstxt">※ 확인메일 수령을 원하시면 email을 기입해 주세요.</p> -->
						</div>
					</div>
					<!-- 방문자 정보 -->
					<c:if test="${paymentInfo.visitorType eq 'P' }">
						<div id="visitor-section" class="admission_tabwrap ewp_tabwrap">
							<c:set var="idx" value="0" />
							<c:forEach var="product" items="${paymentInfo.products }" varStatus="i">
								<c:forEach var="j" begin="0" end="${product.count - 1 }">										
									<input type="hidden" name="visitors[${idx }].product_code" value="${product.product_code }" maxlength="20" value="" required>
									
									
									
	<%-- 										<c:if test="${idx ne 0 }"> --%>
	<!-- 											<div class="copyadress"> -->
	<%-- 												<a href="#" class="copyFirstAddress" data-target="member_addr${idx }"><i class="fas fa-plus"></i> 처음 주소 복사</a> --%>
	<!-- 											</div>	 -->
	<%-- 										</c:if> --%>
									<c:if test="${idx eq '0' }">											
										<h1 id="container_title" class="ect_mtit">방문자 정보 </h1>
									</c:if>
									<c:if test="${idx ne '0' }">											
										<h1 id="container_title" class="ect_mtit">동승자 정보 </h1>
									</c:if>
									<table class="table table-bordered flexible-table bys_tabst bys_tabst2 etble_cus">
										<colgroup>
											<col width='30%'/>
											<col  width='70%'/>
										</colgroup>
										<tbody>
											<tr>
												<th scope="row">상품명</th>
												<td><h5 class="prod_h5 eh5_cus">${product.product_name }</h5></td>
											</tr>
											<tr>
												<th scope="row">이름</th>
												<td><input type='text' name='visitors[${idx }].name' id='member_name' placeholder='이름을 2자이상 입력해주세요' required minlength="2" maxlength="20" class="visitorName required"></td>
											</tr>
											<c:if test="${idx eq '0' }">
												<tr>
													<th scope="row"><label for="bcd_memo">연락처</label></th>
													<td class="mobileNum">
														 <input type="text" name="visitors[${idx }].phone" title="전화번호" maxlength="20" value="" required 	class="visitorPhone required numberInput">
													</td>
												</tr>
											</c:if>
											<c:if test="${idx ne '0' }">
												<tr style="display:none;">
													<th scope="row"><label for="bcd_memo">전화번호 (숫자만)</label></th>
													<td class="mobileNum">
														 <input type="text" name="visitors[${idx }].phone" title="전화번호" maxlength="20" value="" value="" class="visitorPhone required numberInput">
													</td>
												</tr>
											</c:if>
											<tr>
												<th>
													<label for="bcd_memo">주민번호</label>
												</th>
												<td class="line_h mgnone">
													<input type="text" name="visitors[${idx }].jumin1" required id="resident_num" class="visitorJumin1 required numberInput" maxlength="6" placeholder="6자리 숫자">-
													<div class="selectresident_num">
															<select name="visitors[${idx }].jumin2">
																<option value="1">1</option>
																<option value="2">2</option>
																<option value="3">3</option>
																<option value="4">4</option>
															</select>
													</div>
												</td>
											</tr>
											<c:if test="${idx eq '0' }">
												<tr>
													<th scope="row">주소</th>
													<td>
														<!--  <div class="ead_search_btn">
															<a href="#">
																주소검색
															</a>
														</div> -->
														<input type='text' name='visitors[${idx }].addr' id='member_addr${idx }' placeholder='주소를 입력해 주세요' required readonly class="visitorAddr required ewp_add_inp">
													</td>
												</tr>
											</c:if>
											<c:if test="${idx ne '0' }">
												<tr style="display:none;">
													<th scope="row">주소</th>
													<td>
														<!--  <div class="ead_search_btn">
															<a href="#">
																주소검색
															</a>
														</div> -->
														<input type='hidden' name='visitors[${idx }].addr' value="" id='member_addr${idx }' placeholder='주소를 입력해 주세요.' readonly class="visitorAddr required ewp_add_inp">
													</td>
												</tr>
											</c:if>
										</tbody>
									</table>
									<c:set var="idx" value="${idx+1}"></c:set>
								</c:forEach>
								
							</c:forEach>

						</div>
					</c:if>
				</div>
				<div id="pay-section" class="bk_con2_box bk2">
					<div id="aggrement-section" class="box4">
						<div class="box4_3">
							<p class="arr_btn radioIcon">
								<input type="checkbox" id="privacyAggrementInput" />
								<label for="privacyAggrementInput">동의</label>
							</p>
							<div class="list1">
								<h3>개인정보 취급 방침 <span class="ag_rad"><i class="fas fa-caret-down"></i></span></h3>
							</div>
							
							<div class="list1_1">
								<div class="tx_box">
									<div class="etc">
										
										<c:out value="${reserveInfo.info_a }" escapeXml="false"/>
									
									</div><!-- //etc -->

								</div>
							</div><!-- list1_1 on end -->
						</div><!-- box4_3 end -->
						<div class="box4_4">
							<div class="arr_btn radioIcon">
								<input type="checkbox" id="usageAggrementInput" name="agree_1"/>
								<label for="usageAggrementInput">동의</label>
								<input type="hidden" name="agree_2" value="0" />	
							</div>

							<div class="list1">
								<h3>이용약관에 대한 동의 <span class="ag_rad"><i class="fas fa-caret-down"></i></span></h3>
							</div>
							<div class="list1_1">
								<div class="tx_box">
									<div class="etc">
									
										<c:out value="${reserveInfo.info_b }" escapeXml="false"/>
									
									</div><!-- //etc -->
								</div><!-- tx_box end -->
							</div><!-- list1_1 end -->
						</div><!-- box4_4 end -->
						<div class="box4_5">
							<div class="arr_btn radioIcon">
								<input type="checkbox" id="cancelAggrementInput" />
								<label for="cancelAggrementInput">동의</label>
							</div>
							<div class="list1">
								<h3>취소,환불규정에 대한 동의<span class="ag_rad"><i class="fas fa-caret-down"></i></span></h3>
							</div>
							<div class="list1_1">
								<div class="tx_box">
									<div class="etc">
									
										<c:out value="${reserveInfo.info_d }" escapeXml="false"/>
										
									</div><!-- //etc -->
								</div>
							</div>
						</div>
					</div>
					<div id="pay-info-section" class="pay_infbx">
						<h4>결제정보</h4>
						<div class="tx1">
							<c:forEach var="product" items="${paymentInfo.products }" varStatus="status">
								<div class="justify-content-between py_txt">
									<div style="width:30%;" class="py_tit"><span><c:out value="${product.product_name }" /></span></div>
									<span class="bar"></span>
									<div style="width:70%;" class="py_num">
										<fmt:formatNumber type="number" maxFractionDigits="3" value="${product.product_fee * product.count }"/> 원	
									</div>
								</div>
							</c:forEach>
							<div class="justify-content-between py_txt sum_p">
								<div style="width:30%;" class="py_tit"><span>합계</span></div>
								<span class="bar"></span>
								<div style="width:70%;" class="py_num">
									<fmt:formatNumber type="number" maxFractionDigits="3" value="${paymentInfo.totalFee }"/> 원	
								</div>
							</div>
						</div>
					</div>
					<div id="pay-method-section" class="product_bx bx2 box4_1">
						<h4>결제수단</h4>
						<div class="paybx ept_did">
							<p>
								<input type="radio" id="payMethodCard" name="payMethod" value="CARD" checked/>
								<label for="payMethodCard">
									<span></span>
									신용카드
								</label>
							</p>
<!-- 							<p> -->
<!-- 								<input type="radio" id="payMethodBank" name="payMethod" value="BANK" /> -->
<!-- 								<label for="payMethodBank"> -->
<!-- 									<span></span> -->
<!-- 									계좌이체 -->
<!-- 								</label> -->
<!-- 							</p> -->
<!-- 							<p> -->
<!-- 								<input type="radio" id="payMethodCellphone" name="payMethod" value="CELLPHONE" /> -->
<!-- 								<label for="payMethodCellphone"> -->
<!-- 									<span></span> -->
<!-- 									휴대폰결제 -->
<!-- 								</label> -->
<!-- 							</p> -->
						</div>
						<div class="product_btns box4_2">
							 <p>
							 	<a href="javascript:history.go(-1);" class="btn01">이전</a> <!-- 약관 동의 이전으로 되돌아감 -->
							 </p>
							 <p>
							 	<input id="settlementButton" type="button" value="결제하기" class="btn_submit set">
							 </p>

						</div>
					</div>
				</div>
			</div>
		</form:form>
		<div id="hidden-section">
			<div id="reserver-authentication-section" style="display: none;">
				<!-- 본인인증 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
				<form name="form_chk" method="post">
					<input type="hidden" name="m" value="checkplusService">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
					<input type="hidden" name="EncodeData" value="<%= sEncData %>">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
				</form>
				
				<form role="addReserverAuthenticationResult">
					<input type="hidden" name="name" value="" />
					<input type="hidden" name="phone" value="" />
					<input type="hidden" name="code" value="" />
				</form>
			</div>
		
		</div>
	</section>
</div>

<script>
//403 에러 처리(Spring Security에서의 Ajax 호출)
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$.ajaxSetup({
	beforeSend: function (xhr) {
    	xhr.setRequestHeader(header, token);
		},
});

</script>

<%-- 
<%@ include file="../include/check-ticket.jsp" %>
 --%>
<%@ include file="../../include/mcycamping/check-ticket.jsp" %>


<!-- 전환페이지 설정 -->
<!-- <script type="text/javascript" src="//wcs.naver.net/wcslog.js"></script> 
<script type="text/javascript"> 
var _nasa={};
if(window.wcs) _nasa["cnv"] = wcs.cnv("1",'${siteCode}'); // 전환유형, 전환가치 설정해야함. 설치매뉴얼 참고
</script> 
 -->




<%-- 
<%@ include file="../include/footer-single.jsp" %>
 --%>
<%@ include file="../../include/mcycamping/footer-single.jsp" %>

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>

$(function() {
	
	/***** 결제버튼클릭 *****/
	$("#settlementButton").on('click', function() {

		var authenticationIdx = $("input[name='reserver.idx']").val();
		if(!authenticationIdx){
			alert('본인인증을 먼저 진행해 주세요.')
			$("#reserverAuthenticationButton").focus();
			return;
		}
		
		<c:if test="${paymentInfo.visitorType eq 'P' }">
			// 방문자 이름 체크
			var isWrong = false;
			$(".visitorName").each(function(idx, value) {
				if(!$(this).val().trim()){
					alert('방문자 또는 동승자의 성함을 입력해 주세요.')
					$(this).focus();
					isWrong = true;
					return false;
				}
			});
			if(isWrong){
				return;
			}
			
			// 방문자 이름 2자리 이상 체크
			$(".visitorName").each(function(idx, value) {
				if($(this).val().trim().length < 2){
					alert('방문자 또는 동승자의 성함을 2자리 이상 입력해 주세요.')
					$(this).focus();
					isWrong = true;
					return false;
				}
			});
			if(isWrong){
				return;
			}
			
			
			// 방문자 전화번호 체크		
			var phone = $(".visitorPhone:first-child");
			if(!phone.val().trim()) {
				alert('방문자 전화번호를 입력해 주세요.');
				phone.focus();
				return false;
			}
			
			// 방문자 전화번호 숫자만
			var regexp = /^[0-9]+$/;
			var text = phone.val().trim();
			if(!regexp.test(text)){
				alert('방문자 전화번호는 숫자만 입력 가능합니다.')
				phone.focus();
				return false;
			}
			
			// 방문자 주민번호 체크
			$(".visitorJumin1").each(function(idx, value) {
				if(!$(this).val().trim()){
					alert('방문자 또는 동승자의 주민번호를 입력해 주세요.')
					$(this).focus();
					isWrong = true;
					return false;
				}
			});
			if(isWrong){
				return;
			}
			
			// 방문자 주소 체크 
			var addr = $(".visitorAddr:first-child").val().trim();
			
	    	let addrs = addr.split(' ');
	    	if(addrs.length != 3){
	    		 alert('방문자 주소가 올바르지 않습니다.');
	    		 $(".visitorAddr:first-child").focus();
	             return false;        		
	    	}
		</c:if>
		
		
		var privacyAggrement = $("#privacyAggrementInput");
		if(!privacyAggrement.prop("checked")) {
			alert('개인정보 취급 방침에 동의해 주세요.');
			privacyAggrement.focus();
			return false;
		}
		
		var usageAggrement = $("#usageAggrementInput");
		if(!usageAggrement.prop("checked")) {
			alert('이용(운송)약관에 동의해 주세요.');
			usageAggrement.focus();
			return false;
		}
		
		
		var cancelAggrement = $("#cancelAggrementInput");
		if(!cancelAggrement.prop("checked")) {
			alert('취소,환불 규정에 동의해 주세요.');
			cancelAggrement.focus();
			return false;
		}
		
		var popupTitle = 'PayRequest';
		
		//window.open('',popupTitle ,'scrollbars=no,resizable=no');
		
		popupCenter({url: '', title: popupTitle, w: 500, h:400});
		
		var form = $('form[role="form"]');
		form.attr('target', popupTitle);
		form.attr('method', 'POST');
		form.attr('action', '<c:url value="/ticketing/mcycamping/payRequest" />');
		form.submit();
	});
	
	$('.visitorJumin1').css('imeMode','disabled').keypress(function(event) {
	    if(event.which && (event.which < 48 || event.which > 57 ) ) {
	        event.preventDefault();
	    }

	}).keyup(function(){
	    if( $(this).val() != null && $(this).val() != '' ) {
	        $(this).val( $(this).val().replace(/[^0-9]/g, '') );
	    }
	});

})

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
		$("input[name='name']").val(result.name);
		$("input[name='phone']").val(result.phone);	
		$("input[name='code']").val(result.code);
	
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
				$("input[name='reserver.phoneShow']").val(addHyphenToPhoneNumber(data.phone));
				
				$("input[name='visitors[0].name']").val(data.name);
				$("input[name='visitors[0].phone']").val(data.phone);
				
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
		window.location.href= "/ticketing/mcycamping/finish?content_mst_cd=<c:out value='${paymentInfo.content_mst_cd }' />&orderNo=" + result.orderNo + "&userId=<c:out value='${loginUserId}' />&userName=<c:out value='${loginUserNm}' />";	
	}
	
}

/***** 카카오 주소 API 호출 *****/
$(".visitorAddr").on('click', function() {
	var id = $(this).attr('id');
	callKakaoAddrAPI(id);
});

function callKakaoAddrAPI(target) {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수
            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }
            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                
                addr += extraAddr;
            
            } 
            
            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById(target).value = data.sido.split(" ").join("") + " " + data.sigungu.split(" ").join("") + " " + data.bname.split(" ").join("");
            // 커서를 상세주소 필드로 이동한다.
            //document.getElementById("tCompAddr2Input").focus();
        }
    }).open();
}


/***** 키다운 이벤트 동작 *****/
$('.numberInput').on('input', function() {
	var nonNumReg = /[^0-9]/g
	$(this).val($(this).val().replace(nonNumReg, ''));
});


/***** 버튼변경 *****/

$(document).ready(function() {
  $(".list1_1").hide();
  //content 클래스를 가진 div를 표시/숨김(토글)
  $(".list1").click(function()
  {
    $(this).next(".list1_1").slideToggle(500);
    $(this).next(".list1_1").toggleClass("on");
    $(this).find(".ag_rad svg").toggleClass("fa-caret-down fa-caret-up");
    

  });
  $(".list1_1.on").css("display", "block");
  
});


</script>

</body>
</html>
