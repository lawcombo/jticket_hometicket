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
	String sReturnUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + ":9443/reserverAuthentication/success?content_mst_cd=" + ((com.bluecom.ticketing.domain.PaymentInfoDTO_noSchedule)request.getAttribute("paymentInfo")).getProductGroup().getContent_mst_cd();     // 성공시 이동될 URL
    String sErrorUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + ":9443/reserverAuthentication/fail?content_mst_cd=" + ((com.bluecom.ticketing.domain.PaymentInfoDTO_noSchedule)request.getAttribute("paymentInfo")).getProductGroup().getContent_mst_cd();         // 실패시 이동될 URL



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
<%@ include file="../../include/sogeumsan/header-single.jsp" %>

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
										<td><input readonly type='text' name='reserver.name' placeholder='본인확인 버튼을 눌러주세요' required class="required" style="background-color:#fafafa;"><br></td>
									</tr>
									<tr>
										<th scope="row"><label for="bcd_memo">휴대폰 번호</label></th>
										<td>
											<input readonly type="text" name="reserver.phoneShow" required class='required' style="background-color:#fafafa;">
											<input readonly type="hidden" name="reserver.phone">
										</td>
									</tr>
									<tr>
										<th>
											<label for="bcd_memo">E-Mail주소</label>
										</th>
										<td>
											<input type="text" name="reserver.email" id="email">
										</td>
									</tr>
									
								</tbody>
							</table>
							<p class="pstxt">※ 확인메일 수령을 원하시면 email을 기입해 주세요.</p>
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
										당사는 개인정보보호법(법률 제 10465호) 등 관련법령에 의거하여, 정보주체로부터 개인정보를 수집함에 있어, 아래 내용을 안내하고 있습니다.<br>
										정보주체가 되는 이용자께서는 아래 내용을 자세히 읽어보신 후에 동의여부를 결정하여 주시기 바랍니다.
									
										<p class="t1">1. 개인정보의 수집 및 이용 목적</p>
										제부도해상케이블카 주식회사는 “개인정보 보호법”에 따라 수집한 개인정보를 다음의 목적을 위해 활용합니다. 제부도해상케이블카 탑승권 예매, 예매 조회, 예매자와의 연락, 개인 맞춤형 광고 및 마케팅 활용, 제공을 위해 개인정보를 수집, 이용합니다.
									
										<p class="t1">2. 수집하는 개인정보의 항목</p>
										제부도해상케이블카 주식회사는 제 1조에 명기된 이용 목적을 위해 아래와 같은 개인정보를 수집하고 있습니다.
										<p class="t2"></p>
										수집하는 개인정보는 다음과 같습니다.<br>
										<p class="t2"></p>
										서비스 이용 과정에서 아래와 같은 최소한의 개인정보를 수집하고 있으며, 이용자의 민감 정보를 수집하지 않습니다.<br>
										<p class="t2"></p>
										<b>[케이블카 예매 시]</b> 필수 : 예매자명, 연락처, 이메일 주소<br>
										<b>[결제 및 환불 시]</b> 필수 : 결제카드 정보, (계좌이체의 경우)은행명, 예금주명, 계좌번호<br>
										<b>[고객센터 이용 시]</b> 필수 : 휴대폰번호, 이름<br>
										<b>[현금영수증 발행 시]</b> 필수 : 휴대폰번호, 현금영수증, 카드번호<br>
										수집방법 : 홈페이지의 서비스 이용 과정에서 이용자가 개인정보 수집에 대한 동의를 하고 직접 정보를 입력하는 경우<br>

										<p class="t1">3. 개인정보의 보유 및 이용기간</p>
										개인정보의 수집 및 이용목적이 달성되면 지체 없이 파기합니다.<br>
										단, 다음의 정보에 대해서는 아래의 이유로 명시한 기간 동안 보존합니다.<br>
										<p class="t2"></p>
										보유 사유 : 제 1조에 이용목적 활용 후 고객 사후 관리<br>
										보유 및 이용기간 : 고객이 서비스를 이용하는 기간에 한하여 보유 및 이용을 원칙으로 하되, 개인정보 유효기간제에 따라 이용하지 않는 이용자의 개인정보는 파기<br>
										<p class="t2"></p>
										전자상거래 등에서 소비자 보호에 관한 법률<br>
										<p class="t3">
										-	계약 또는 청약철회 등에 관한 기록: 5년 보관<br>
										-	대금결제 및 재화 등의 공급에 관한 기록:5년 보관<br>
										-	소비자의 불만 또는 분쟁처리에 관한 기록:3년 보관<br>
										-	소비자의 불만 또는 분쟁처리에 관한 기록:3년 보관<br>
										-	표시/광고에 관한 기록: 6개월<br>
										</p>
										국세기본법<br>
										<p class="t3">
										-	세법이 규정하는 모든 거래에 관한 장부 및 증빙서류: 5년<br>
										</p>
										전자금융거래법<br>
										<p class="t3">
										-	전자금융에 관한 기록:5년 보관<br>
										</p>
										통신비밀보호법<br>
										<p class="t3">
										-	로그인 기록:3개월<br>
										</p>
									
										<p class="t1">4. 제3자에게 이용자의 개인정보 제공</p>
										수사기관이나 기타 정부기관으로부터 공식 문서로 정보제공을 요청 받은 경우<br>
										이용자의 위법행위에 대해 확인이 필요한 경우<br>
										판매자 또는 배송업체 등에게 거래 및 배송 등에 필요한 최소한의 개인정보(성명, 주소, 휴대전화번호)를 제공하는 경우<br>
										구매가 성사된 때에 그 이행을 위하여 필요한 경우와 구매가 종료된 이후에도 반품, 교환, 환불, 취소 등을 위하여 필요한 경우<br>
										기타 법령에 의해 요구되는 경우<br>
									
										<p class="t1">5. 수집한 개인정보의 위탁</p>
										서비스 향상 및 효율적인 개인정보 관리를 위하여 외부에 위탁하여 처리하고 있으며, 관계법령에 따라 위탁 계약 시 개인정보가 안전하게 관리될 수 있도록 규정하고 있습니다. 개인정보취급 수탁자와 그 업무의 내용은 다음과 같습니다.<br>
										[수탁자 : 한국전자금융 주식회사 ]<br>
									
										<table class="etc_tb" cellpadding="0" cellspacing="0" border="0">
											<colgroup>
												<col width="30%">
												<col width="40%">
												<col width="30%">
											</colgroup>
											<thead>
												<tr>
													<th>수탁업체</th>
													<th>위탁업무 내용</th>
													<th>개인정보 이용기간</th>
												</tr>
											</thead>
											<thead>
												<tr>
													<td>한국전자금융 주식회사</td>
													<td>개인정보 처리 및 보호</td>
													<td>위탁계약 종료 시까지</td>
												</tr>
											</thead>
										</table>
									
										개인정보보호법 제29조에 따라 다음과 같이 안정성 확보에 필요한 기술적/관리적 및 물리적 조치를 하고 있습니다.<br>
										<p class="t3">
										-	개인정보의 암호화 이용자의 개인정보는 암호화 되어 저장 및 관리되고 있습니다.<br>
										-	접속기록의 보관 및 위변조 방지시스템의 로그를 6개월간 보관하고 있습니다.<br>
										-	개인정보에 대한 접근 제한개인정보를 처리하는 데이터베이스시스템에 대한 접근을 접근통제 시스템으로 통제되고 있습니다.<br>
										</p>
										고객께서는 본 안내에 따른 개인정보 수집에 대하여 거부를 하실 수 있는 권리가 있습니다.<br>
										단, 개인정보 수집에 대하여 동의를 하지 않으실 경우에는, 제부도해상케이블카 탑승권 예매가 이루어지지 않음에 따라 당사의 개인정보 처리 요구 서비스를 이용하실 수 없습니다.
									
									
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
								<h3>이용(운송)약관에 대한 동의 <span class="ag_rad"><i class="fas fa-caret-down"></i></span></h3>
							</div>
							<div class="list1_1">
								<div class="tx_box">
									<div class="etc">
										<p class="t1 mt0">제 1조 (목적)</p>
										이 약관(이하 "약관"이라고 합니다)은 제부도해상케이블카 주식회사(이하 "회사"라고 합니다)가 제공하는 “서비스”의 이용과 관련하여 “회사”와 “이용자” 간의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.
										
										<p class="t1">제 2조 (정의)</p>
										"약관"에서 사용하는 용어의 정의는 아래와 같습니다.
										<p class="t2"></p>
										① "서비스"라 함은 “이용자”와 "회사"간 티켓을 구매하고 제공하는 서비스를 의미합니다.<br>
										② “이용자”라 함은 "회사"의 "서비스"를 이용하기 위해 홈티켓 서비스(home-ticket.co.kr)에서 "약관"에 따라 "회사"와 이용계약을 체결하고 "회사"가 제공하는 "상품 등"을 이용하기 위하여 "서비스"를 통하여 주문이나 예매신청을 하는 자를 의미합니다.<br>
										③ "시설"이라 함은 "회사"의 "서비스"를 이용하여 “이용자”에게 “상품 등”을 제공하는 케이블카 등의 각 시설을 의미합니다.<br>
										④ "홈티켓"이라 함은 “이용자”가 "서비스"의 이용과 관련된 사항 및 예매 및 주문 현황 등을 확인할 수 있도록 "회사"가 제공하는 웹사이트를 의미합니다.<br>
										⑤ "홈티켓 솔루션"(이하 "솔루션"이라고 합니다)이라 함은 "회사"가 "서비스"를 이용하여 제작한 “이용자”의 예매나 주문 등을 받을 수 있는 도구 등의 산출물을 의미합니다.<br>
										⑥ "게시정보"라 함은 "회사"가 "솔루션" 상에 기재, 게시한 부호ㆍ문자ㆍ이미지 등의 정보 및 링크 등을 의미합니다.<br>
										⑦ "약관"에서 사용하는 용어 중 본 조 제1항 내지 제6항에서 정하지 아니한 것은 관련 법령 및 일반적인 상관례에 따릅니다.<br>
										
										<p class="t1">제 3조 (약관의 게시와 개정)</p>
										① "회사"는 "약관"의 내용을 “이용자”가 쉽게 알 수 있도록 "홈티켓"초기 화면 또는 연결 화면을 통하여 게시합니다.<br>
										② "회사"는 필요한 경우 관련 법령을 위배하지 않는 범위 내에서 "약관"을 개정할 수 있습니다.<br>
										③ "회사"는 "약관"을 개정할 경우 개정내용과 적용일자를 명시하여 제9조에 따른 방법으로 적용일자 7일 전부터 적용일자 전일까지 통지합니다. 다만, “이용자”에게 불리하게 개정되는 경우에는 적용일자 30일 전부터 적용일자까지 공지하고, “이용자”가 기재한 전자우편주소 또는"홈티켓" 접속시 노출되는 팝업 창 등의 전자적 수단을 통해 별도로 통지합니다.<br>
										④ "회사"가 전항에 따라 "약관"의 개정을 공지하면서 “이용자”에게 적용일자 전일까지 이의 제기 등 거부의 의사표시를 하지 않으면 동의의 의사표시가 표명된 것으로 본다는 뜻을 명확하게 고지했거나 제9조에 따른 방법으로 통지하였음에도 “이용자”가 명시적으로 거부의 의사표시를 하지 않은 경우 “이용자”가 개정되는 "약관"에 동의한 것으로 봅니다.<br>
										⑤ “이용자”는 "약관"의 개정에 동의하지 않을 경우 적용일자 전일까지 "회사"에 거부의사를 표시하고 서비스 이용계약을 해지할 수 있습니다.<br>
										
										<p class="t1">제 4조 (회사의 개인정보보호의무)</p>
										"회사"는 "정보통신망 이용촉진 및 정보보호 등에 관한 법률", "개인정보보호법" 등 관련 법령이 정하는 바에 따라 “이용자”의 개인정보를 보호하기 위해 노력합니다. 개인정보의 보호 및 이용에 대해서는 관련 법령 및 "회사"의 개인정보처리방침이 적용됩니다. 다만, "회사"의 공식 사이트 이외의 링크된 사이트에서는 "회사"의 개인정보처리방침이 적용되지 않습니다.
										
										<p class="t1">제 5조 (주문정보 등의 변경)</p>
										① “이용자”는 제부도해상케이블카 주식회사 주문정보 관리화면을 통하여 언제든지 본인의 주문정보를 열람할 수 있으며, 이를 수정할 수 있습니다.<br>
										② “이용자”는 주문시 기재한 사항이 변경되었을 경우, 주문정보 관리화면을 통하여 구매취소등의 변경을 할 수 있습니다.<br>
										③ “이용자”가 제2항을 위반하여 변경을 하지않아 발생한 불이익에 대해서 "회사"는 책임지지 않습니다.<br>
										
										<p class="t1">제 6조 (대리 및 보증의 부인)</p>
										① "회사"는 “이용자”와 "시설"간의 편리한 "상품 등"의 거래를 위한 “서비스”를 운영 및 관리, 제공하며, “이용자”와 "시설" 사이에 성립된 “상품 등”의 이용에 관련한 책임과 “이용자” 및 "시설"이 제공한 정보에 대한 책임은 해당 “이용자” 및 "시설"이 직접 부담하여야 합니다.
										
										<p class="t1">제 7조 (서비스의 이용 및 제한)</p>
										①	 “이용자”는 "홈티켓"에서 "서비스"이용시 “약관”에 동의하는 방법으로 “서비스” 이용가입신청을 하고 “회사”가 이를 승낙함으로써 “서비스”를 이용할 수 있습니다. 다만 “회사”는 “회원”의 “서비스”이용신청이 타인의 이름, 휴대폰번호를 도용한 경우, 미성년자인 경우 등 기타 관련 법령상 정당한 사유가 있는 경우 “서비스” 이용가입을 제한할 수 있습니다.<br>
										② “서비스”를 이용하고자 하는 “이용자”는 “서비스” 이용과 관련하여 “회사”가 요구하는 사항(“이용자”의 성명, 휴대전화번호, 이메일 주소 등)을 “회사”에 제공함으로써 서비스를 이용할 수 있습니다.<br>
										③ “회사”는 “이용자”에게 "회사"이 제공하는 “상품 등”의 예매 및 주문을 할 수 있도록 아래와 같은 “서비스”를 제공합니다.<br>
										<p class="t3">
										1. "회사"가 제공하는 “상품 등”의 예매 및 주문을 위한 솔루션<br>
										2. 필요 시 “결제”를 위한 “결제 PG사” 연계<br>
										3. 기타 “이용자”의 “서비스” 이용 관리 등을 위한 제반 서비스<br>
										</p>
										④ “회사”는 “이용자”가 “서비스”의 본래 목적에 어긋나게 이를 사용하거나 또는 "회사"의 사업을 방해할 목적으로 반복적으로 구매의사 없이 예매 또는 주문을 남발하는 경우 등에는 “서비스” 이용을 제한할 수 있습니다.<br>
										⑤ “회사”는 원칙적으로 “이용자”의 이용신청을 승낙한 때로부터 “서비스”를 연중무휴로 1일 24시간 제공합니다. 다만, “회사”의 기술상 또는 업무상의 장애로 인하여 “서비스”를 즉시 개시하지 못하는 경우 “회사”는 제8조에 따른 방법으로 “이용자”에게 통지합니다.<br>
										⑥ “회사”는 안정적인 “서비스” 제공을 위하여 “서비스”의 내용, 운영상 또는 기술상 사항 등을 변경할 수 있습니다. 이 경우 “회사”는 “서비스” 중요사항을 변경할 경우 변경내용과 적용일자를 명시하여 제8조에 따른 방법으로 사전에 통지합니다.<br>
										⑦ “회사”는 다음 각 호에 해당하는 경우 “서비스”의 일부 또는 전부의 제공을 일시적으로 중단할 수 있습니다. 이 경우 회사는 “서비스” 이용의 중단 사실을 제9조의 방법에 따라 즉시 공지합니다.<br>
										<p class="t3">
										1. 설비의 보수 등을 위하여 부득이한 경우<br>
										2. 「전기통신사업법」에 규정된 기간통신사업자가 전기통신서비스를 중지하는 경우<br>
										3. 국가비상사태, 정전, 서비스 설비의 장애 또는 서비스 이용의 폭주 등으로 정상적인 “서비스” 제공에 지장이 있는 경우<br>
										4. 기타 “회사”가 “서비스”를 제공할 수 없는 위 제1호 내지 제3호에 준하는 사유가 발생한 경우<br>
										5. "회사”가 제공하는 통상적인 수준의 트래픽, 용량 등을 과도하게 초과하여 “회사”의 정상적인 “서비스” 제공에 지장을 초래할 우려가 있는 경우<br>
										</p>
										⑧ “이용자”는 언제든지 “회사”에 대한 통지로서 “서비스” 이용계약을 해지할 수 있으며, “회사”는 관련 법령이 정하는 바에 따라 이를 즉시 처리하여야 합니다. 이 경우 “회사”는 관련 법령 및 개인정보처리방침에 따라 보유하는 “이용자”의 정보를 제외한 “이용자”의 예매정보 등을 삭제합니다.
										
										<p class="t1">제 8조 (통지)</p>
										① “회사”가 “이용자”에 대하여 통지를 하는 경우 “약관”에 별도의 규정이 없는 한 “이용자”가 제공한 전자우편주소, (휴대)전화번호, 주소 등으로 할 수 있습니다.<br>
										②“회사”는 “이용자” 전체에 대하여 통지를 하는 경우 “제부도해상케이블카 홈” 초기화면 또는 게시판에 게시함으로써 전항의 통지에 갈음할 수 있습니다. 다만, “이용자”의 “서비스” 이용에 중대한 영향을 주는 사항에 대해서는 전항의 통지 수단 중 2개 이상의 방법으로 통지합니다.<br>
										③ “이용자”는 “회사”에 실제로 연락이 가능한 전자우편, (휴대)전화번호, 주소 등의 정보를 제공하고 해당 정보들을 최신으로 유지하여야 하며, “회사”의 통지를 확인하여야 합니다.<br>
										
										<p class="t1">제 9조 (책임제한)</p>
										① “회사”는 천재지변, 디도스(DDOS) 공격, IDC 장애, 기간통신사업자의 회선 장애 또는 이에 준하는 불가항력으로 인하여 “서비스”를 제공할 수 없는 경우에는 “서비스” 제공에 관한 책임을 지지 않습니다.<br>
										② “회사”는 “이용자”의 귀책사유로 인한 “서비스” 이용의 장애에 대하여는 책임을 지지 않습니다.<br>
										③ “회사”는 제7조 제6항 및 제7항의 “서비스” 이용의 변경 및 중단의 경우에 고의 과실 등 귀책사유가 없는 한 책임을 지지 않습니다.<br>
										④ "회사"가 “서비스”를 이용하여 제작한 “솔루션” 및 “솔루션” 내 “게시정보”의 내용에 관하여는 "회사"가 그에 대한 일체의 책임을 부담합니다. 단, <br>
										⑤ “회사”는 “이용자” 간 또는 “이용자”와 제3자 상호간의 “서비스”를 매개로 한 거래 등에 관하여 책임을 지지 않습니다.<br>
										
										<p class="t1">제 10조 (취소·환불)</p>
										"회사"는 “서비스”를 통해 이루어지는 거래에 관한 취소 및 환불 관련 규정을 정하여 "회사"의 예매 및 주문 화면에 게시할 수 있습니다. 단, 이러한 취소 및 환불 규정은 전자상거래등에서의 소비자보호에 관한 법률 등 관련 법령에 위반되지 아니하여야 합니다.<br>
										<p class="t2"></p>
										① “회사”는 이용자로부터 상품을 반환 받은 경우 3영업일 이내에 이미 지급받은 상품의 대금을 환급합니다. 이 경우 “사이트”가 이용자에게 상품의 환급을 지연한 때에는 그 지연기간에 대하여 「전자상거래 등에서의 소비자보호에 관한 법률 시행령」제21조의2에서 정하는 지연이자율을 곱하여 산정한 지연이자를 지급합니다.<br>
										②“회사”는 위 대금을 환급함에 있어서 이용자가 신용카드 또는 전자화폐 등의 결제수단으로 상품의 대금을 지급한 때에는 지체 없이 당해 결제수단을 제공한 사업자로 하여금 상품의 대금 청구를 정지 또는 취소하도록 요청합니다.<br>
										③청약철회 등의 경우 공급받은 상품의 반환에 필요한 비용은 이용자가 부담합니다. “회사”는 이용자에게 청약철회 등을 이유로 위약금 또는 손해배상을 청구하지 않습니다. 다만 상품의 내용이 표시·광고 내용과 다르거나 계약내용과 다르게 이행되어 청약철회 등을 하는 경우 상품의 반환에 필요한 비용은 “회사”가 부담합니다.<br>
										④ 이용자가 상품을 제공받을 때 발송비를 부담한 경우에 “사이트”는 청약철회 시 그 비용을 누가 부담하는지를 이용자가 알기 쉽도록 명확하게 표시합니다.
										
										<p class="t1">제 11조 (준용규정)</p>
										본 “약관”에서 정하지 아니한 사항은 제부도해상케이블카 주식회사 서비스 약관 및 개인정보처리방침에 따릅니다.
										<p class="t1">[부칙]</p>
										본 약관은 2021년 10월 12일부터 시행됩니다.
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
								<h3>취소,환불 규정에 대한 동의<span class="ag_rad"><i class="fas fa-caret-down"></i></span></h3>
							</div>
							<div class="list1_1">
								<div class="tx_box">
									<div class="etc">
									①사용기간이 지난 예매권은 취소/환불이 불가능합니다.</br>
									②한번에 구매한 예매권 중 1매라도 사용시 취소/환불이 불가하오니 유의하시기 바랍니다.</br>
									③사용된 예매권은 취소/환불이 불가 합니다. </br>
									④왕복권을 발권하여 편도구간만 이용하더라도, 잔여 구간에 대한 금액을 반환하지 않습니다.</br>
									⑤사용기간이 남아 있는 예매권의 경우 취소/환불이 가능합니다. </br>
									
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
<%@ include file="../../include/sogeumsan/check-ticket.jsp" %>


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
<%@ include file="../../include/sogeumsan/footer-single.jsp" %>

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
		form.attr('action', '<c:url value="/ticketing/sogeumsan/payRequest" />');
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
		window.location.href= "/ticketing/sogeumsan/finish?content_mst_cd=<c:out value='${paymentInfo.content_mst_cd }' />&orderNo=" + result.orderNo + "&userId=<c:out value='${loginUserId}' />&userName=<c:out value='${loginUserNm}' />";	
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
