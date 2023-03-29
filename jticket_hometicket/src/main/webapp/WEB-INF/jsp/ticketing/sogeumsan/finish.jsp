<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<%-- <%@ include file="../include/header-single.jsp" %> --%>
<%@ include file="../../include/sogeumsan/header-single.jsp" %>

<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">

<!-- Google Tag Manager (noscript) -->
<noscript>
	<iframe src="https://www.googletagmanager.com/ns.html?id=GTM-WDMRHB3" height="0" width="0" style="display: none; visibility: hidden"></iframe>
</noscript>
<!-- End Google Tag Manager (noscript) -->


<script>

var orderNum = '${trade.order_num}';
var shopName = '${trade.shop_name}';
var totalFee = Number('${trade.total_fee}');
var products = new Array();

<c:forEach var="products" items="${products}">
	products.push({id:"${products.product_code}", 
					name:"${products.product_name}", 
					category:"${products.category}", 
					quantity:"${products.quantity}", 
					price:"${products.product_fee}"});
</c:forEach>

// GA 전자상거래 
gtag('event', 'purchase', {
	  "transaction_id": orderNum,
	  "affiliation": shopName,
	  "value": totalFee,
	  "currency": "KRW",
	  "tax": 0,
	  "shipping": 0,
	  "items": products
	});
	
</script>
<c:set var="now" value="<%=new java.util.Date() %>" />
<fmt:formatDate var="today" value="${now }" pattern="yyyy-MM-dd"/>

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
				<li><b>온라인 예약</b></li>
			</ul>
		</div>
	</div><!-- //sub_v2 -->
</div>

<div id="hiddenArea" style="display:none;">
	<input type="hidden" id="gaOrderNum" value="${trade.order_num}" />
	<input type="hidden" id="gaShopName" value="${trade.shop_name}" />
	<input type="hidden" id="gaTotalFee" value="${trade.total_fee }" />
</div>



<div id="page-wrapper" class="wrp">
	<section class="sel_sec">
		<%-- <div style="position:relative; float:right;">
			<div class="">
				<a href="<c:url value='/ticketing/selectTicket' />?content_mst_cd=<c:out value='${shopInfo.content_mst_cd }' />"><i class="fas fa-arrow-left"></i> 처음으로</a>
			</div>
			<div>
				<button>예약확인 및 취소</button>
			</div>
		</div> --%>
		
		<div class="sel_tit aos-init aos-animate" data-aos="fade-up" data-aos-easing="linear" data-aos-duration="700" data-aos-delay="300">
			<h2>온라인 예약</h2>
			<span class="bar"></span>
		</div>
		
		<div class="ewp_online_top_btns">
			<%-- 
			<div class="return_bt">
				<a href="<c:url value='/ticketing/selectTicket' />?content_mst_cd=<c:out value='${shopInfo.content_mst_cd }' />"><i class="fas fa-arrow-left"></i> 처음으로</a>
			</div>
			 --%>
			<div class="res_btbx" style="float:right;">
				<button id="checkReservationOpenButton">예매확인 및 취소</button>
			</div>
		</div>

		<div id="step-section" style="clear:both;">
			<div class="row-h1 bc_status">
				<ul class="aftCB stepul proc01">
					<li>
						<p>
							<!-- <i>1</i> -->
							<span>01. 상품/권종/수량</span>
						</p>
					</li>
					<li>
						<p>
							<!-- <i>2</i> -->
							<span>02. 구매정보입력</span>
						</p>
					</li>
					<li class="on">
						<p>
							<!-- <i>3</i> -->
							<span>03. 확인 및 결제</span>
						</p>
					</li>
				</ul>
			</div>
		</div>
	<!--  
	<div id="info-section" class="d-flex justify-content-center">
		<div id="trade-info-section">
			<h6>구매가 완료되었습니다.</h6>
			<div class="erc_tb">
				<ul>
					<li>
						<p>
							<span>상품명</span>
							${trade.product_group_name }
						</p>
					</li>
					<li>
						<p>
							<span>인원</span>
							${trade.product_names_counts }							
						</p>
					</li>
					<li>
						<p>
							<span>예매번호</span>
							${trade.order_num }
						</p>
					</li>
					<li>
						<p>
							<span>구매자명</span>
							${trade.member_name }
						</p>
					</li>
					<li>
						<p>
							<span>구매자 연락처</span>
							<span id="phoneNumber">${trade.member_tel }</span>
						</p>
					</li>
					<c:if test="${not empty trade.member_email }">
						<li>
							<p>
								<span>구매자명</span>
								${trade.member_email }
							</p>
						</li>
					</c:if>
					<li>
						<p>
							<span>총결제금액</span>
							<fmt:formatNumber type="number" maxFractionDigits="3" value="${trade.total_fee }"/> 원	
						</p>
					</li>							
					<li>
						<p>
							<span>결제수단</span>
							${trade.pay_method }
						</p>
					</li>
					<li>
						<p>
							<span>구매일</span>
							${trade.sale_date }
						</p>
					</li>
					<li>
						<p>
							<span>티켓사용 만료일</span>
							${trade.expire_date }
						</p>
					</li>
				</ul>
			</div>
			<div>
				<a href="<c:url value='/ticketing/selectTicket?content_mst_cd=${shopInfo.content_mst_cd }' />">확인</a>
			</div>
		</div>
		<div class="guide-section">
			모바일 티켓 사용법
		</div>
	</div>-->
	
		<div class="con_wid">
		<!-- 확인 및 결제 시작 -->
		
		<div>
			<div class="booking_con2">
				<div class="bk_con2_box bk1">
					<div class="box2">
						<h4>구매가 완료 되었습니다.</h4>
						<table class="bk_tb1">
							<colgroup>
								<col width="55%;">
								<col width="45%;">
							</colgroup>
							<tbody>
								<tr>
									<th>상품명</th>
									<td>${trade.product_group_name }</td>
								</tr>
								<tr>
									<th>인원</th>
									<td>${trade.product_names_counts }</td>
								</tr>
								<tr>
									<th>주문번호</th>
									<td>${trade.order_num }</td>
								</tr>
								<tr>
									<th>총결제금액</th>
									<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${trade.total_fee }"/></td>
								</tr>
								<tr>
									<th>결제수단</th>
									<td>${trade.pay_method }</td>
								</tr>
								<tr>
									<th>구매자명</th>
									<td>${trade.member_name }</td>
								</tr>
								<tr>
									<th>구매자 연락처</th>
									<td id="memberTel">${trade.member_tel }</td>
								</tr>
								<c:if test="${not empty trade.member_email }">
									<tr>
										<th>구매자 이메일</th>
										<td>${trade.member_email }</td>
									</tr>
								</c:if>
								<tr>
									<th>구매일</th>
									<td>${trade.sale_date }</td>
								</tr>
								<tr>
									<th>티켓사용 기간</th>
									<td>${trade.valid_from } ~ ${trade.valid_to }</td>
								</tr>
							</tbody>
						</table>
					</div><!-- //box2 -->
					<div class="btn_box">
<!-- 						<button type="button" class="btn1">구매취소</button> -->
						<button type="button" id="goHomeButton" class="btn2">확 &nbsp;&nbsp; 인</button>
					</div>
				</div>
	
				<div class="bk_con2_box bk2 ewp_bk2">
					<div class="box1">
						<p class="t1"><b>[모바일 티켓 사용 방법]</b></p>
						<div class="ex_box">
							<p class="ex_box1">
								<img src="/resources/noSchedule/images/con2_1_icon1.png" alt="">
								<span>홈페이지에서 <br>온라인 티켓 구매 완료</span>
							</p>
							<p class="ex_box2"><img src="/resources/noSchedule/images/con2_1_icon3.png" alt=""></p>
							<p class="ex_box1">
								<img src="/resources/noSchedule/images/con2_1_icon2.png" alt="">
								<span>현장 방문 후 매표소에서 <br>모바일 티켓 제시 후 탑승권 수령</span>
							</p>
						</div>
					</div>
					
					<div class="box1">
						<p class="t1"><b>[유의사항]</b></p>
						<ul>
							<li>예매번호는 구매일 기준 30일까지 유효합니다. </li>
						</ul>
					</div>
					
	
				</div>
			</div>
		</div>
		
		<!-- 확인 및 결제 끝 -->
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
<script type="text/javascript" src="//wcs.naver.net/wcslog.js"></script> 
<script type="text/javascript"> 
var _nasa={};
//if(window.wcs) _nasa["cnv"] = wcs.cnv("1","10"); // 전환유형, 전환가치 설정해야함. 설치매뉴얼 참고
if(window.wcs) _nasa["cnv"] = wcs.cnv("1", '${trade.total_fee}' ); // 전환유형, 전환가치 설정해야함. 설치매뉴얼 참고
</script> 
<%-- 
<%@ include file="../include/footer-single.jsp" %>
 --%>
 
 <%@ include file="../../include/sogeumsan/footer-single.jsp" %>
 
<script>

$(window).resize(function(){ 
	if (window.innerWidth > 1150) { // 다바이스 크기가 480이상일때 
	
		$('#gnb, .gnb_bg').mouseover(function(){
			$('header').css({"background-color":"#fff"})
			$('#gnb > li > a').css("color", "#314134")
			$('.logo_off').css({"display":"none"})
			$('.logo_on').css({"display":"block"})
		})
		$('#gnb, .gnb_bg').mouseout(function(){
			$('header').css({"background-color":"transparent"})
			$('#gnb > li > a').css({"color":"#fff"})
			$('.logo_off').css({"display":"block"})
			$('.logo_on').css({"display":"none"})
		})

		$('#gnb').hover(function(){
			$("#layerPop").stop(true).fadeIn();
			}, function() {
			$("#layerPop").stop(true).fadeOut();
		})

		$('.g1').mouseover(function(){
			$('#gnb > li.g1 > a').css({"font-weight":"bold"})
		})	
		$('.g1').mouseout(function(){
			$('#gnb > li.g1 > a').css({"font-weight":"300"})
		})	
		$('.g2').mouseover(function(){
			$('#gnb > li.g2 > a').css({"font-weight":"bold"})
		})	
		$('.g2').mouseout(function(){
			$('#gnb > li.g2 > a').css({"font-weight":"300"})
		})	
		$('.g3').mouseover(function(){
			$('#gnb > li.g3 > a').css({"font-weight":"bold"})
		})	
		$('.g3').mouseout(function(){
			$('#gnb > li.g3 > a').css({"font-weight":"300"})
		})	
		$('.g4').mouseover(function(){
			$('#gnb > li.g4 > a').css({"font-weight":"bold"})
		})	
		$('.g4').mouseout(function(){
			$('#gnb > li.g4 > a').css({"font-weight":"300"})
		})	
		$('.g5').mouseover(function(){
			$('#gnb > li.g5 > a').css({"font-weight":"bold"})
		})	
		$('.g5').mouseout(function(){
			$('#gnb > li.g5 > a').css({"font-weight":"300"})
		})
		$('.g6').mouseover(function(){
			$('#gnb > li.g6 > a').css({"font-weight":"bold"})
		})	
		$('.g6').mouseout(function(){
			$('#gnb > li.g6 > a').css({"font-weight":"300"})
		})
	} else { 
	
		$('.hd_w').mouseover(function(){
			$('header').css({"background-color":"transparent"})
			$('#gnb > li > a').css("color", "#fff")
			$('.logo_off').css({"display":"block"})
			$('.logo_on').css({"display":"none"})
		})
		$('.hd_w').mouseout(function(){
			$('header').css({"background-color":"transparent"})
			$('#gnb > li > a').css({"color":"#fff"})
			$('.logo_off').css({"display":"block"})
			$('.logo_on').css({"display":"none"})
		})
		$('.hd_w').hover(function(){
			$("#layerPop").stop(true).fadeIn();
			$('#layerPop').css({"display":"none"})
			}, function() {
			$("#layerPop").stop(true).fadeOut();
			$('#layerPop').css({"display":"none"})
		}) 
	} 
}).resize();



$(function() {

	$("#memberTel").text(addHyphenToPhoneNumber($("#memberTel").text()));	
	
	$("#goHomeButton").on("click", function() {
		/* 
		window.location.href = '<c:url value='/ticketing/selectTicket' />?content_mst_cd=<c:out value='${shopInfo.content_mst_cd }' />';
		 */
		 
		window.close();
	});
});
</script>



</body>
</html>
