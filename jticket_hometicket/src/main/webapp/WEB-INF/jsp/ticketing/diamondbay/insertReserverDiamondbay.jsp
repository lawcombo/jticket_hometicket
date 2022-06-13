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
	
	//운영에는 https 처리
	
	// CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
	//리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : https://www.~ 리턴 url : https://www.~
	String sReturnUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/success?content_mst_cd=" + ((com.bluecom.ticketing.domain.EssentialDTO)request.getAttribute("essential")).getContent_mst_cd();     // 성공시 이동될 URL
	String sErrorUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/fail?content_mst_cd=" + ((com.bluecom.ticketing.domain.EssentialDTO)request.getAttribute("essential")).getContent_mst_cd();         // 실패시 이동될 URL
	
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


	<div style="background-image: url('${pageContext.request.contextPath }/resources/images/diamondbay/visual1.jpg'); height:300px;"> 
		<div style="position:absolute; top:10%; left:19%;">
			<img src="${pageContext.request.contextPath }/resources/images/diamondbay/visual_title.png" lat="DIAMOND BAY">
		</div>
	</div>
	

	<!-- 뒤로가기 버튼 -->
	<div style="text-align:left; padding-top:10px; padding-left:10px;">
		<!-- <button onclick="history.back(); " style="cursor: pointer";>뒤로가기</button> -->
		
		<img src="${pageContext.request.contextPath }/resources/images/diamondbay/backImg.png" onclick="history.back()" style="cursor: pointer; width: 50px;" />
	</div>

	<%-- <%@ include file="../../include/top_menu.jsp"%> --%>

	<!-- 필요할 때 사용 -->
	<%-- 
	<c:choose>
		<c:when test="${essential.product_group_code eq '101' }">
			<!-- 투어 -->
			<%@ include file="../../include/tour_subm.jsp"%>
		</c:when>
		<c:when test="${essential.product_group_code eq '102' }">
			<!-- 체험 -->
			<%@ include file="../../include/experience_subm.jsp"%>
		</c:when>
		<c:otherwise>
			<!-- 기타 -->
		</c:otherwise>
	</c:choose>
 	--%>


	<div class="body-contents-wrap reservediv">

		<%-- <section class="head">
	        <div class="intro">
	            <c:if test="${productGroup.product_group_code == '101' }" > <!-- 투어는 1종류이므로 기본적으로 고정 -->
	            	<h3>투어</h3>
	            </c:if>
	            <c:if test="${productGroup.product_group_code != '101' }" > <!-- 투어코드 외는 모두 체험취급 -->
	            	<h3>체험</h3>
	            </c:if>
	            <h1>${paymentInfo.productGroup.product_group_name}</h1>
	            <!-- <p>많고 많은 맥주잔중, 내 맥주잔 하나는 있어야죠!</p> -->
	        </div>
	         <ul class="icons">
	            <li>
	                <div><img alt="" src="${pageContext.request.contextPath }/resources/images/jeju/icon-cal.png" /></div>
	                <div>사전 예약필수</div>
	            </li>
	            <li>
	                <div><img alt="" src="${pageContext.request.contextPath }/resources/images/jeju/icon-7plus.png" /></div>
	                <div>
						만 7세 이상<br>
						참여가능
					</div>
	            </li>
	            <li>
	                <div><img alt="" src="${pageContext.request.contextPath }/resources/images/jeju/icon-beer.png" /></div>
	                <div>
						제주맥주 4종<br>
						샘플러(250ml*4)
						<!--또는 스파클링 음료와 제주맥주 굿즈세트-->
	                </div>
	            </li>
	        </ul>
    	</section> --%>
		
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
							<button type="button" id="reserverAuthenticationButton"
								class="buttonTypeCyan full textLarge">본인인증</button>
							<p class="ewp_m_info_tb">전자상거래에 의거하여 만 14세 이상만 이용 가능합니다.</p>
						</dl>
						<dl class="reserveDl full mt20">
							<dt class="reserveDt">
								신청자명 <span class="require"></span>
							</dt>
							<dd class="reserveDd">
								<input type="text" name="reserver.name" class="jejuInputBox"
									id="userName">
							</dd>
						</dl>
						<dl class="reserveDl full mt20">
							<dt class="reserveDt pb15">
								핸드폰 번호 <span class="require"></span>
							</dt>
							<dd class="reserveDd">
								<dl class="columnDl">
									<dt class="columnDt ewp_input_100">
										<input type="tel" name="reserver.phone" id="phone"
											class="jejuInputBox full gray num_only">
										<p class="ewp_m_info_tb">나이스평가정보에서 인증 받은 휴대폰 번호를 사용하고
											있습니다.</p>
									</dt>
									<!-- <dd class="columnDd w35">
											<button class="buttonTypeCyan full textLarge" id="callCert" type="button">인증번호 발송</button>
										</dd> -->
								</dl>
							</dd>
						</dl>
						<!-- <dl class="reserveDl full mt20">
								<dt class="reserveDt pb15">인증 번호 <span class="require"></span></dt>
								<dd class="reserveDd">
									<dl class="columnDl noPadding mt10">
										<dt class="columnDt w65"><input type="number" name="certCode" id="authNo" class="jejuInputBox full gray"></dt>
										<dd class="columnDd w35">
											<button class="buttonTypeCyan full textLarge disabled" id="confirmCert" type="button">확인</button>
										</dd>
									</dl>
								</dd>
							</dl> -->
						<dl class="reserveDl full mt20">
							<dt class="reserveDt pb15">
								이메일 <span class="require"></span> <sub>*입력하신 메일주소로 결제정보가
									발송됩니다.</sub>
							</dt>
							<dd class="reserveDd">
								<input id="emailInput" type="text" name="reserver.email"
									class="jejuInputBox full gray">
							</dd>
						</dl>
						<!-- 
						<dl class="reserveDl full mt20">
							<dt class="reserveDt pb15">
								쿠폰입력 <sub>*쿠폰 코드가 있을 경우 입력해 주세요.</sub>
							</dt>
							<dd class="reserveDd">
								<dl class="columnDl noPadding">
									<dt class="columnDt">
										<input type="text" id="couponText"
											class="jejuInputBox full gray">
									</dt>
									<dd class="columnDd">
										<button class="buttonTypeCyan full textLarge" type="button"
											id="couponBtn">쿠폰 추가</button>
									</dd>
								</dl>
							</dd>
						</dl>
						 -->
					</div>
				</section>

				<!-- <div class="pageLine dashed gap m_none"></div> -->
				<div class="paybx">
					<div id="hiddenArea">
						<input type="hidden" name="productGroup.shop_code"
							value="${paymentInfo.productGroup.shop_code }" /> <input
							type="hidden" name="productGroup.product_group_code"
							value="${paymentInfo.productGroup.product_group_code }" /> <input
							type="hidden" name="productGroup.content_mst_cd"
							value="${paymentInfo.productGroup.content_mst_cd }" />
						<%-- <input type="hidden" name="productGroup.product_group_kind" value="${paymentInfo.productGroup.product_group_kind }" /> --%>
						<c:forEach var="product" items="${paymentInfo.products }"
							varStatus="status">
							<input type="hidden" name="products[${status.index }].shop_code"
								value="${product.shop_code }" />
							<input type="hidden"
								name="products[${status.index }].product_group_code"
								value="${product.product_group_code }" />
							<input type="hidden" class="productCode"
								name="products[${status.index }].product_code"
								value="${product.product_code }" />
							<input type="hidden" class="productCount"
								name="products[${status.index }].count"
								value="${product.count }" />
							<input type="hidden" class="productFee"
								name="products[${status.index }].product_fee"
								value="${product.product_fee}" />
						</c:forEach>
						<input type="hidden" name="visitorType"
							value="${paymentInfo.visitorType }" /> <input type="hidden"
							name="totalFee" value="${paymentInfo.totalFee }" /> <input
							type="hidden" name="fee" /> <input type="hidden"
							name="totalCount" value="${paymentInfo.totalCount }" /> <input
							type="hidden" name="schedule_code"
							value="${paymentInfo.schedule_code}" /> <input type="hidden"
							name="play_date" value="${paymentInfo.play_date}" /> <input
							type="hidden" name="couponFee" value="0" /> <input type="hidden"
							name="reserver.idx" />
					</div>
					<section class="reserve reserve-agree agreesec line_he">
						<div class="verticalAlignMiddle ewp_insert_agree_ex">
							<ul class="agreementUl">
								<li class="agreementLi question toggleHeader">
									<div class="columnDl fontSize20 agree_top ewp_insert_agree_top">
										<div class="columnDt textAlignLeft ewp_iat_left">
											<div class="agreementToggle ft20 tgicon">
												<p class="ewp_insert_agree_tit">개인정보 수집 동의 (보기)</p>
												<img
													src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_arrowDown.png"
													alt="key visual" class="imgAgreementArrowDown" /> <span
													class="require"></span>
											</div>
										</div>
										<div
											class="columnDd textAlignRight reservationRenewPage ewp_iat_right ewp_iat_right2">
											<label for="agreementPrivacy" class="jejuRadioButton jjrb">
												<input type="radio" name="agree_1" id="agreementPrivacy"
												value="1"> <span class="check"></span> <span
												class="ag_txt">동의</span>
											</label>
										</div>
									</div>
								</li>
								<li class="agreementLi answer toggleContent"
									style="display: none;">
									<div class="pt20 pb20 ag_tg">
										<%--  <c:out value="${reserveInfo.info_a }" escapeXml="false"/> --%>
										<div>
											당사는 개인정보보호법(법률 제 10465호) 등 관련법령에 의거하여, 정보주체로부터 개인정보를 수집함에 있어,
											아래 내용을 안내하고 있습니다.<br> 정보주체가 되는 이용자께서는 아래 내용을 자세히 읽어보신 후에
											동의여부를 결정하여 주시기 바랍니다.<br> <br> <br> 1. 개인정보의 수집
											및 이용 목적<br> 제주맥주는 “개인정보 보호법”에 따라 수집한 개인정보를 다음의 목적을 위해
											활용합니다. 제주맥주 양조장 내 진행 프로그램 예약, 제주맥주 주최 프로그램/행사 예약, 예약 조회, 고객
											문의 게시글 등록, 신청자와의 연락, 개인 맞춤형 광고 및 마케팅 활용, 제공을 위해 개인정보를 수집,
											이용합니다.<br> <br> <br> 2. 수집하는 개인정보의 항목<br>
											제주맥주는 제 1조에 명기된 이용 목적을 위해 아래와 같은 개인정보를 수집하고 있습니다.<br> <br>
											수집항목<br> 필수 : 신청자명, 연락처, 이메일 주소, 방문 희망인원, 방문 희망일시<br>
											수집방법 : 홈페이지 (고객 집적 입력)<br> <br> <br> 3. 개인정보의
											보유 및 이용기간<br> 개인정보의 수집 및 이용목적이 달성되면 지체 없이 파기합니다.<br>
											단, 다음의 정보에 대해서는 아래의 이유로 명시한 기간 동안 보존합니다.<br> 보유 사유 : 제
											1조에 이용목적 활용 후 고객 사후 관리<br> 보유 및 이용기간 : 고객이 서비스를 이용하는 기간에
											한하여 보유 및 이용을 원칙으로 하되, 개인정보 유효기간제에 따라 1년동안 이용하지 않는 이용자의 개인정보는
											파기<br> <br> <br> 4. 수집한 개인정보의 위탁<br> 서비스
											향상 및 효율적인 개인정보 관리를 위하여 외부에 위탁하여 처리하고 있으며, 관계법령에 따라 위탁 계약 시
											개인정보가 안전하게 관리될 수 있도록 규정하고 있습니다. 개인정보취급 수탁자와 그 업무의 내용은 다음과
											같습니다.<br> <br> [수탁자 : 수탁범위]<br> 제주맥주(주),
											한국전자금융(주) : 전산시스템의 구축 및 유지보수<br> <br> <br> 이용자
											개인정보보호를 위하여 수집된 개인정보는 암호화되어 처리됩니다.<br> 고객께서는 본 안내에 따른
											개인정보 수집에 대하여 거부를 하실 수 있는 권리가 있습니다.<br> 단, 개인정보 수집에 대하여
											동의를 하지 않으실 경우에는, 제주맥주에서 주최하는 프로그램에 신청이 이루어지지 않음에 따라 당사의 개인정보
											처리 요구 서비스를 이용하실 수 없습니다.
										</div>
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
												<img
													src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_arrowDown.png"
													alt="key visual" class="imgAgreementArrowDown"> <span
													class="require"></span>
											</div>
										</div>
										<div
											class="columnDd textAlignRight reservationRenewPage ewp_iat_right">
											<div class="chmd">
												<input type="radio" name="agreementTermsOfUse"
													id="agreementTermsOfUse" class="delinp" value="1">
												<label class="jejuRadioButton jjrb ag_txt"><span
													class='check'></span> 확인 후 동의</label>
											</div>
											<div class="rad_modal">
												<div class="rad_modal_content">
													<div class="rad_modal_x">
														<div id="agreementTermsOfUse-modal-section"
															class="md_cont">
															<%-- <c:out value="${reserveInfo.info_c }" escapeXml="false"/> --%>
															<div class="modal_tb">
																<div class="mt_top">
																	<h2>
																		<strong>잠깐!</strong>
																	</h2>
																	<p class="mt_top_text">필수 확인 사항들을 한번 더 안내 드릴게요.</p>
																</div>
																<div class="mt_bot">
																	<h3>양조장투어 + 맥주 선택 시 <span class="txt_red_st">신분증 필참 !</span></h3>
																	<!-- <p class="mt_txt">
																		- 양조장투어 : 만 0세~6세 보호자 동반하여도 참여 불가<br> - 나만의전용잔만들기
																		: 보호자 동반 시 만 12세 이하 참여 가능
																	</p> -->
																	<h3>양조장투어 <span class="txt_red_st">옵션 당일 변경 절대 불가 !</span></h3>
																	<!-- <p class="mt_txt">
																		1. 양조장투어 포함사항은 지각 시에도 제공<br> 2. 비행기 지연 및 결항, 건강상의
																		이유, 천재지변으로 인한 환불은 전화/채팅 문의
																	</p> -->
																	<h3>프로그램 참여 가능 연령 확인!</h3>
																	<p class="mt_txt">
																		- 양조장투어 : 만 7세 이상 (0세~6세는 보호자 동반 시에도 참여 불가)<br>
																		- 나만의잔만들기 : 만 12세 이상 (보호자 동반 시 12세 이하도 참여 가능)
																	</p>
																	<h3>
																		10분 이상 지각 시 참여 및 환불 불가
																	</h3>
																	<p class="mt_txt">
																		- 양조장투어의 맥주 또는 음료는 지각 시에도 제공<br>
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

															<button class="md_bts">모두 확인했고, 동의합니다.</button>
														</div>
													</div>
												</div>
												<div class="rad_modal_bk"></div>
											</div>
										</div>
									</div>
								</li>
								<li class="agreementLi answer termsOfUseToggleContent"
									style="display: none; border-bottom: none;">
									<div class="pt20 pb20 ag_tg">
										<%--  <c:out value="${reserveInfo.info_a }" escapeXml="false"/> --%>
										<div>
											제 1조 (목적)<br> 1. 본 이용약관은 제주맥주 주식회사(이하 ‘회사’)가 제주맥주 양조장 관련
											온/오프라인 서비스(이하 ‘서비스’)를 운영함에 있어 발생할 수 있는 문제상황에 대하여 일관성 있게 대응하기
											위해 서비스 운영 기준과 고객의 책임 사항을 규정함을 목적으로 합니다.<br> 2. 본 이용약관에
											명시되지 않은 사항은 관계법령 및 일반적 사회 통념에 의해 판단, 처리됩니다.<br> <br>
											<br> 제 2조 (운영 일시 및 입장 제한)<br> 1. 매일 12:30 ~ 19:30
											(PUB : 매일 13:00 ~ 19:00)<br> 2. 설, 추석 당일과 전날은 휴무입니다.<br>
											3. 회사는 내부 사정으로 인해 임시 휴무할 수 있으며, 이를 별도 게시하여 사전에 고지합니다.<br>
											4. 제주맥주 양조장은 입장에 연령 제한이 없습니다. 단, 프로그램 참여에는 연령 제한이 있으며 고객은 이를
											사전에 확인하여 준수해야 합니다.<br> 5. 회사는 맥주를 제조하는 제조업장으로, 반려 동물의
											출입이 제한됩니다.<br> 6. 회사는 정부의 코로나19 방역 지침에 따라 운영하고 있으며, 이를
											위반하는 고객의 입장 제한 및 거부할 수 있습니다.<br> <br>
											<br> 제 3조 (공간 이용수칙)<br> &bull;건물<br> 1. 회사는
											고성방가, 폭력행사 등으로 다른 고객 또는 직원에게 피해를 주거나 해가 될 우려가 있는 행동을 하는 고객의
											입장을 제한 및 거부할 수 있으며, 환불 없이 퇴장 조치할 수 있습니다.<br> 2. 회사는 영업시간
											중이더라도 긴급히 안전을 위해 필요하다고 판단한 경우, 고객에게 고지한 후 운영을 즉각 중지하고 퇴장 및 귀가
											요청할 수 있습니다.<br> 3. 회사는 흡연구역 외에서 흡연행위를 제한할 수 있으며, 고객이 정당한
											사유 없이 응하지 않을 경우 퇴장 조치할 수 있습니다.<br> &bull;2층 투어 공간<br>
											1. 양조장투어 프로그램 예약 고객만 입장이 가능하며, 미예약 고객이 임의로 입장 시 퇴장 조치할 수
											있습니다.<br> &bull;브랜드 샵<br> 1. 모든 상품은 별도 택배 서비스가
											불가합니다.<br> 2. 만 19세 이상은 브랜드 샵에서 맥주를 구매할 수 있으며, 1인 24L까지
											구매 가능합니다. (시즈널, 한정판 맥주는 별도의 수량 제한이 있을 수 있습니다.)<br>
											&bull;PUB<br> 1. 회사는 고객에게 음주와 음주운전을 권장하지 않으며, 책임은 고객
											본인에게 있습니다.<br> 2. 맥주 구매 및 시음은 만 19세 이상의 성인만 가능합니다. 고객은
											반드시 신분증을 지참하고, 회사 및 직원이 요구할 시 제시해야 합니다. 이에 응하지 않거나 강제하기 위해
											직원을 강요 및 위계 위력을 사용하는 경우 회사는 관할 경찰서에 신고할 수 있습니다. <br> 3.
											생맥주는 포장 또는 반출이 불가하며, 발견 시 회수 및 퇴장 조치될 수 있습니다.<br> 4. 외부
											음식 반입 가능합니다. 다만 고객은 다른 고객 및 회사에 피해를 주지 않아야 하는 책임이 있으며, 책임을
											다하지 않을 경우 직원이 별도의 요구 및 조치를 취할 수 있습니다.<br> <br>
											<br> 제 4조 (예약 및 할인·쿠폰)<br> &bull; 예약<br> 1.
											프로그램 참여 희망 시, 프로그램 시작 10분 전까지 홈페이지를 통해 예약해야 하며, 현장에서는 잔여석이 남아
											있을 경우에 한해 예약 가능합니다.<br> 2. 브랜드 샵과 PUB은 별도 예약 없이도 자유롭게
											이용할 수 있으며, 입장료는 없습니다.<br> 3. 예약 방법 : 홈페이지 > BREWERY > 예약
											에서 직접 가능하며, 전화 예약 또는 채팅 예약은 불가합니다.<br> &bull; 할인<br>
											1. 제주도민 또는 아시아나 탑승객은 양조장 투어 1인당 2천원 할인이 가능하며, 홈페이지에서 정상 금액으로
											결제 후 방문 시 직원이 증빙 서류 확인하여 부분 환불 처리됩니다.<br> 2. 할인 요금은 회사의
											정책에 따라 변경될 수 있으며, 이를 별도 게시하여 사전에 고지합니다.<br> &bull; 쿠폰<br>
											1. 회사 또는 회사와 제휴를 맺은 제 3자는 고객에게 회사의 시설 이용 또는 상품 구매 시 일정 금액·일정
											비율을 할인 받을 수 있는 쿠폰을 무상으로 지급할 수 있습니다.<br> 2. 쿠폰은 회사가 정한
											품목이나 금액에 따라 사용이 제한될 수 있습니다.<br> 3. 쿠폰에 기재된 유효기간이 경과한 경우
											사용할 수 없으며, 현금으로 환불되지 않습니다.<br> 4. 고객은 회사의 동의 없이 쿠폰을 다른
											고객에게 재판매 할 수 없으며, 재판매 또는 불법적인 방법으로 양도한 것이 확인되는 경우 회사는 해당 쿠폰의
											사용 정지 등의 조치를 취해 사용을 제한할 수 있습니다.<br> 5. 쿠폰을 사용한 예약 건의 예약
											취소 시, 쿠폰은 즉시 복구됩니다.<br> <br>
											<br> 제 5조 (프로그램 이용 수칙)<br> &bull;양조장 투어<br> 1.
											안전사고 예방 및 원활한 운영을 위해 만 7세 이상부터 참여 가능하며, 0세~6세는 보호자 동반하여도 참여가
											불가합니다. <br> 2. 연령 확인을 위하여 직원은 증빙 서류(등본 등) 제시 및 확인을 요청할 수
											있습니다.<br> 3. 투어 시작 후 10분 이상 지각 시 투어 참여는 불가하며, 이로 인한 환불은
											불가합니다. <br> 4. 회사는 내부 사정으로 인해 프로그램의 변경 및 취소할 수 있으며, 고객에게
											직접 연락을 취하여 알리거나 이를 사전에 홈페이지를 통해 고지합니다.<br> &bull;나만의 전용잔
											만들기<br> 1. 안전사고 예방 및 원활한 운영을 위해 만 12세 이상부터 참여 가능하며, 12세
											미만의 경우 보호자 동반 시 참여 가능합니다.<br> 2. 회사는 내부 사정으로 인해 프로그램의 변경
											및 취소할 수 있으며, 고객에게 직접 연락을 취하여 알리거나 이를 사전에 홈페이지를 통해 고지합니다.<br>
											<br>
											<br> 제 6조 (취소·환불·변경)<br> &bull;일반<br> 1.
											취소·환불·변경의 기준은 예약 시점이 아닌 이용 시점입니다.<br> 2. 이용 하루 전 자정까지
											100% 환불이 가능하며, 이용 당일 취소 및 환불·변경은 불가합니다.<br> 3. 취소·환불·변경
											방법 : 홈페이지 > BREWERY > 예약 > 예약확인/취소 에서 직접 가능합니다.<br> <br>
											&bull;유사 시<br> 1. 비행기 지연 및 결항, 건강 상의 문제 또는 천재지변으로 인해 및
											환불·변경이 필요할 경우 회사는 고객에게 증빙 서류를 요청할 수 있으며, 확인 후 취소 및 환불·변경
											처리됩니다.<br> <br>
											<br> 제 7조 (상품 불량 및 분실물 처리)<br> 1. 구매 후 상품의 불량 및 이상이
											있을 경우, 고객은 이를 회사에 알려 환불 및 정상 상품으로 교환 받을 수 있습니다. 단, 고객의 과실로 인해
											상품이 훼손된 경우는 회사가 책임지지 않습니다.<br> 2. 회사는 분실물 입수 시 공유 및
											기록하며, 습득된 물품을 유실물법령에 따라 7일 동안 보관한 후 소유자가 찾아가지 않을 경우 폐기 처리할 수
											있습니다.<br> <br>
											<br> 제 8조 (회사의 배상 책임)<br> 1. 회사는 시설 운영 중 회사의 지휘 감독을
											받는 직원의 고의 ·과실 또는 회사가 유지보수 관리책임을 지는 시설의 하자로 인하여 고객에게 사고가 발생한
											경우, 이에 대한 책임을 부담합니다.<br> 2. 1항으로 인하여 고객에게 신체상 또는 재산상 피해가
											발생한 경우, 회사는 그 손해를 배상합니다. 다만, 그 손해가 불가항력으로 인하여 발생한 경우 또는 그 손해의
											발생이 이용객의 고의 또는 과실로 인한 경우에는 그 배상의 책임이 경감 또는 면제됩니다.<br> <br>
											<br> 제 9조 (고객의 책임)<br> 1. 고객이 고의 또는 과실로 회사의 시설물과
											비품을 훼손한 때에는 그 손해를 배상하여야 합니다.<br> 2. 고객의 고의 또는 과실로 다른 고객,
											회사 직원 등 제 3자에게 손해를 입힌 경우 고객은 이에 대한 책임을 부담해야 합니다.<br> <br>
											<br> 제 10조 (문의)<br> 1. 기타 문의는 064-798-9872 또는
											jejubeer@jejubeer.co.kr로 문의 가능합니다. <br> 2. 카카오톡 채널에
											‘제주맥주 양조장’을 검색하시면 채팅 상담 가능합니다. (매일 11:00~18:00)<br> 3.
											홈페이지 오른쪽 하단 말풍선 아이콘 클릭하시면 채팅 상담 가능합니다. (매일 11:00~18:00)<br>
											<br>
											<br> &lt;부칙&gt;<br> 제1조(시행일) 이 약관은 2020년 07월 23일부터
											시행합니다.
										</div>
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
											value="${paymentInfo.totalFee }" />
										원
									</h2>
								</dd>
							</dl>

							<c:forEach var="product" items="${paymentInfo.products }"
								varStatus="status">
								<dl class="receiptDl mt70 instR mt62">
									<dt class="receiptDt">
										<div class="pageDescription">
											${product.product_name} <sub class="sb">(<fmt:formatNumber
													type="number" maxFractionDigits="3"
													value="${product.product_fee }" /> 원 × ${product.count }명)
											</sub>
										</div>
									</dt>
									<dd class="receiptDd">
										<div class="pageDescription">
											<fmt:formatNumber type="number" maxFractionDigits="3"
												value="${product.product_fee * product.count}" />
											원
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
										<span id="totalFee"><fmt:formatNumber type="number"
												maxFractionDigits="3" value="${paymentInfo.totalFee }" /> 원</span>
									</div>
								</dd>
							</dl>
						</div>

						<div class="pay-role mt15">
							<div class="payment reservationRenewPage">
								<label for="paymentTypeCard" class="jejuRadioButton"> <input
									type="radio" name="payMethod" id="paymentTypeCard" value="CARD"
									checked> <span class="radio"></span> <span>신용카드</span>
								</label>
							</div>
							<div class="payment reservationRenewPage txtright">
								<label for="paymentTypeBank" class="jejuRadioButton"> <input
									type="radio" name="payMethod" id="paymentTypeBank" value="BANK">
									<span class="radio"></span> <span>실시간 계좌이체</span>
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
		<input type="hidden" name="name" value="" /> <input type="hidden"
			name="phone" value="" /> <input type="hidden" name="code" value="" />
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
// 	var products = [
// 		<c:forEach var="item" items="${paymentInfo.products}">
// 			{
// 				product_code: "<c:out value='${item.product_code}' />",
// 				product_count: "<c:out value='${item.count}' />",
// 				product_fee: "<c:out value='${item.product_fee}' />",
// 			},
// 		</c:forEach>
// 	];	
	
	

	
	
	// 쿠폰추가 클릭시
	$("#couponBtn").on('click', function(){
		var couponNum = $("#couponText").val();
		if(couponNum == ''){
			alert("쿠폰 번호를 입력해주세요.");
			return false;
		}
		if(couponList.length != 0){
			for(var i=0; i<couponList.length; i++){
				if(couponList[i] == couponNum){
					alert("이미 적용된 쿠폰입니다.");
					$("#couponText").val("");
					return false;
				}
			}
		}
		var totalCount = $("input[name=totalCount]").val();
		if(couponList.length == totalCount){
			alert("쿠폰은 최대 "+totalCount+"장까지 적용가능합니다.");
			$("#couponText").val("");
			return false;
		}
		
		var productGroupCode = '<%=request.getParameter("product_group_code")%>';
		var contentMstCd = '<%=request.getParameter("content_mst_cd")%>';
		
		
		
		console.log(productGroupCode);
		console.log(contentMstCd);
		var data = {'cpm_num': couponNum, 'product_group_code':productGroupCode, 'content_mst_cd':contentMstCd};
		$.ajax({
			url:"/ticketing/checkCoupon?${_csrf.parameterName}=${_csrf.token}",
			type:"POST",
			dataType : 'json',
			data : data,
			success: function(data){
				console.log(data);
				if(data.msg != ""){
					alert(data.msg);
					$("#couponText").val("");
				}else{
					// 사용가능 쿠폰
					couponList.push(couponNum); // 기 적용쿠폰 확인 위해 배열에 입력한 쿠폰값 집어 넣기
					// useCoupons 는 상품목록이다.
					// %쿠폰
					var couponCost = data.cpn_sale_cost;
					if(data.cpn_sale_type === '1') {
						var rate = data.cpn_sale_cost;
						
						for(var i=0; i<useCoupons.length; i++) {
							var useCoupon = useCoupons[i];
							if(useCoupon.count - useCoupon.used_count > 0) {
								var productFee = useCoupon.product_fee;
						
								couponCost = Math.floor(productFee * (rate / 100));
								couponCost = couponCost.toString();

								useCoupon.used_count += 1;
								break;
							}
						}
					} else {
						// 금액 쿠폰
						for(var i=0; i<useCoupons.length; i++) {
							var useCoupon = useCoupons[i];
							if(useCoupon.count - useCoupon.used_count > 0) {
								if(Number(couponCost) > Number(useCoupon.product_fee)) {
									couponCost = useCoupon.product_fee;
								}
								useCoupon.used_count += 1;
								break;
							}
						}
					}
					
					//쿠폰 금액 적용하기
					$("#couponArea").append('<dl class="receiptDl mt20">'
												+'<dt class="receiptDt">쿠폰 <sub class="sb">('+couponNum+')</sub></dt>'
												+'<dd class="receiptDd">- '+addComma(couponCost)+' 원</dd>'
											+'</dl>');
					$("#couponText").val("");
					totalFee = Number(totalFee) - Number(couponCost);
					$("#totalFee").text(addComma(totalFee.toString())+" 원");
					$("input[name=fee]").val(totalFee);
					couponFee += Number(couponCost);
					$("input[name=couponFee]").val(couponFee);
					
					$("#hiddenArea").append('<input type="hidden" name="coupon['+useCouponLength+'].cpn_sale_cost" value="'+couponCost+'">');
					$("#hiddenArea").append('<input type="hidden" name="coupon['+useCouponLength+'].cpm_num" value="'+data.cpm_num+'">');
					$("#hiddenArea").append('<input type="hidden" name="coupon['+useCouponLength+'].cpn_name" value="'+data.cpn_name+'">');
					$("#hiddenArea").append('<input type="hidden" name="coupon['+useCouponLength+'].cpm_cpn_code" value="'+data.cpm_cpn_code+'">');
					$("#hiddenArea").append('<input type="hidden" name="coupon['+useCouponLength+'].company_code" value="'+data.company_code+'">');
					useCouponLength++;
					
					alert('쿠폰이 등록되었습니다.');
				}
			},
			error : function(xhr,status,error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	});
	
	// 인증번호 발송 클릭시 / 2021-09-15 / 조미근
/* 	$("#callCert").on('click', function(){
		var userName = $("#userName").val();

		if(userName == ''){
			alert("신청자명을 입력해주세요.");
			return false;
		}else{
			var phone = $("#phone").val();
			$("input[name='phone']").val(phone);	
			
			var form = $('form[role="addReserverAuthenticationResult"]');
			$.ajax({
				type : 'post',  				       
				url : '<c:url value="/reserverAuthentication/certification" />',
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
					console.log(data);
					if(data.returnCode == "0000"){
						console.log("성공");
						alert("인증번호가 발송되었습니다.");
						
						$("input[name='name']").val(userName);
						$("input[name='phone']").val(data.phone);	
						$("input[name='responseSEQ']").val(data.responseSEQ);
						$("#callCert").attr("class", "buttonTypeCyan full textLarge disabled");
						$("#confirmCert").attr("class", "buttonTypeCyan full textLarge");
					}else{
						alert("인증번호 전송이 실패되었습니다.");
					}
					
				},
				error: function (jqXhr, textStatus, errorMessage) { // error callback 
					alert("인증 번호를 전송  할 수 없습니다. 반복시 관리자를 호출해 주세요.");
					window.close();
				}
			});
		}
	}); */
	
	// 인증번호 발송 후 확인 클릭시 / 2021-09-15 / 조미근
/* 	$("#confirmCert").on('click', function(){
		$("input[name='authNo']").val($("#authNo").val());
		
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
				console.log(data);
				if(data.returnCode == "0000"){
					console.log("성공");
					alert("인증이 완료되었습니다.");
					$("#confirmCert").attr("class", "buttonTypeCyan full textLarge disabled");
					
					$("input[name='reserver.idx']").val(data.idx);
					$("input[name='reserver.name']").val(data.name);
					$("input[name='reserver.phone']").val(data.phone);
				}else{
					alert("인증번호를 확인해주세요.");
				}
				
			},
			error: function (jqXhr, textStatus, errorMessage) { // error callback 
		        alert("인증 데이터를 저장 할 수 없습니다. 반복시 관리자를 호출해 주세요.");
		      	
				window.close();
		    }
		});
	}); */
	
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
		
		popupCenter({url: '', title: popupTitle, w: 500, h: 500});
		
		var form = $('form[role="form"]');
		
		form.attr('target', popupTitle);
		form.attr('method', 'POST');
		form.attr('action', '<c:url value="/ticketing/payRequest" />');
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
		window.location.href= "/ticketing/finish?content_mst_cd=<c:out value='${essential.content_mst_cd }' />&product_group_code=<c:out value='${essential.product_group_code }' />&orderNo=" + result.orderNo;	
	}
	
}


/***** 모달창 *****/
var isCheckedAgreementTermsOfUse = false;
$(function(){
	$(".rad_modal").hide();
	$(".chmd").click(function(){
		
		var isChecked = $("#agreementTermsOfUse").prop("checked");
		
		
		if(isChecked) {
			$("#agreementTermsOfUse").prop("checked", false);
		} else {
			$(this).next(".rad_modal").fadeIn();
		}
	});
	
	$(".rad_modal_bk").click(function(){
		$(".rad_modal").fadeOut();
	});
	
	$("#agreementTermsOfUse").on('click', function(e){
		e.preventDefault();
	});
	
	$(".md_bts").click(function(e){
		e.preventDefault();
		$("#agreementTermsOfUse").prop("checked", true);
		$(".rad_modal").fadeOut();
		
	});
	
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
