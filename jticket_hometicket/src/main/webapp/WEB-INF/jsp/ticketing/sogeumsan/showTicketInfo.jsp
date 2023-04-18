<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<%@ include file="../../include/sogeumsan/header-single.jsp" %>
<c:set var="isUsed" value="false" />
<c:set var="isRefunded" value="false" />
<c:set var="now" value="<%=new java.util.Date() %>" />
<fmt:formatDate var="today" value="${now }" pattern="yyyy-MM-dd"/>

<!-- Google Tag Manager (noscript) -->
<noscript>
	<iframe src="https://www.googletagmanager.com/ns.html?id=GTM-WDMRHB3" height="0" width="0" style="display: none; visibility: hidden"></iframe>
</noscript>
<!-- End Google Tag Manager (noscript) -->

<style>
a.disabled {
  pointer-events: none;
  cursor: default;
}
</style>

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



<div id="page-wrapper" class="wrp" style="margin-top:103px;">
	<%-- 
	<div class="ewp_online_top_btns">
		<div class="return_bt" style="float:right;">
			<a href="<c:url value='/ticketing/selectTicket' />?content_mst_cd=<c:out value='${sale.content_mst_cd }' />"><i class="fas fa-arrow-left"></i> 온라인 예약</a>
		</div>
	</div>
	 --%>
	<div class="ewp_break_wrapper">
		<div class="ewp_break_wrapper_in">
			<h1 class="ewp_break_tit">
				예매 내역
			</h1>
			<span class="bar"></span>			
			<div class="ewp_break_contents_ex">
			
				<div id="reserver-info-section" class="ewp_break_contents">
					<div class="ewp_break_in_tit">
						<h3>
							예매자정보
						</h3>
					</div>
					<ul>
						<li>
							<span class="ewp_break_th">예매번호</span>
							<span class="ewp_break_td"><c:out value="${sale.order_num }" /></span>
						</li>
						<li>
							<span class="ewp_break_th">예매자명</span>
							<span class="ewp_break_td"><c:out value="${sale.member_name }" /></span>
						</li>
						<li>
							<span class="ewp_break_th">예매자 전화번호</span>
							<span class="ewp_break_td" id="memberTel"><c:out value="${sale.member_tel }" /></span>
						</li>
						<li>
							<span class="ewp_break_th">예매자 이메일</span>
							<span class="ewp_break_td"><c:out value="${sale.member_email }" /></span>
						</li>
						<li>
							<span class="ewp_break_th">총 구매 금액</span>
							<span class="ewp_break_td">
								<fmt:formatNumber type="number" maxFractionDigits="3" value="${totalFee}"/>
							</span>
						</li>
						<li>
							<span class="ewp_break_th">구매일</span>
							<span class="ewp_break_td"><c:out value="${sale.sale_date }" /></span>
						</li>			
						<li>
							<span class="ewp_break_th">사용기한시작일</span>
							<span class="ewp_break_td"><c:out value="${sale.valid_from }" /></span>
						</li>
						<li>
							<span class="ewp_break_th">사용기한만료일</span>
							<span class="ewp_break_td"><c:out value="${sale.valid_to}" /></span>
						</li>
					</ul>
				</div>
				
				<div id="ticket-info-section" class="ewp_break_contents">
					<div class="ewp_break_in_tit">
						<h3>
							티켓정보
						</h3>
					</div>
					
					<c:forEach var="saleProductMap" items="${saleProductMap}">
						<c:set var="saleProduct" value="${saleProductMap.value[0] }" />
						<ul>
							<li>
								<span class="ewp_break_th">상품명</span>
								<span class="ewp_break_td"><c:out value="${saleProduct.product_name }" /></span>
							</li>
							<li>
								<span class="ewp_break_th">사용일</span>
								<span class="ewp_break_td" style="color:blue;"><c:out value="${empty saleProduct.play_date ? '-' : saleProduct.play_date }" /></span>
							</li>
							<li>
								<span class="ewp_break_th">권종별 총금액</span>
								<span class="ewp_break_td"><fmt:formatNumber type="number" maxFractionDigits="3" value="${saleProduct.product_fee}"/></span>
							</li>
							<li>
								<span class="ewp_break_th">매수</span>
								<span class="ewp_break_td"><c:out value="${saleProduct.quantity }" /></span>
							</li>
							<%-- 
							<li>
								<span class="ewp_break_th">상태</span>
								<span class="ewp_break_td"><c:out value="${saleProduct.refund_yn eq '1' ? '환불' : not empty saleProduct.used_date ? '사용' : '미사용' }" /></span>
							</li>
							 --%>
						</ul>
					</c:forEach>
				</div>
			</div>
			
			<div class="ewp_break_btns">
				<!-- 
				<div class="ewp_back_btn">
					<a href="#" id="backButton">
						닫기
					</a>
				</div>
				 -->
				<div style="margin-left:82px;">
					<div id="refund-section" class="ewp_break_cant">
						<a href="#" id="cancelButton">
							예매취소
						</a>
					
					</div>
				</div>
				
			</div>
		</div>
	</div>
</div>
<div id="hidden-section" style="display:none;">
	<!-- 취소 form -->
	<form:form role="cancel" action="/ticketing/sogeumsan/cancelTicket" method="POST">
		<input type="hidden" name="order_num" value="${sale.order_num }" />
		<input type="hidden" name="content_mst_cd" value="${sale.content_mst_cd }" />	
		<input type='hidden' name='shop_code' value="${sale.shop_code }">
		<input type='hidden' name='sale_code' value="${sale.sale_code }">
		<input type='hidden' name='member_tel' value="${sale.member_tel }">
		<input type='hidden' name='member_name' value="${sale.member_name }">
	</form:form>
</div>
<script>

// 취소 버튼 비활성화
function disableCancelButton() {
	$("#cancelButton").addClass('disabled');
	$("#refund-section").css('opacity', '50%');
}

$(function() {
	<c:choose>
		<c:when test="${today gt sale.valid_to}">
			disableCancelButton(); // 만료 
		</c:when>
		<c:when test="${sale.refund_quantity gt 0}">
			disableCancelButton(); // 환불
		</c:when>
		<c:when test="${sale.issued_quantity gt 0}">
			disableCancelButton(); // 사용
		</c:when>
	</c:choose>
	
	
	var playDate = $("#playDate").val();
	$('#cancelButton').on('click', function(e){
		e.preventDefault();
	
		if(confirm('현재 예약을 취소하시겠습니까?')) {
			var form = $('form[role="cancel"]');
			form.submit();
		}
	});
	
	
	$("#backButton").on("click", function(e) {
		e.preventDefault();
		
		//window.location.href = "<c:url value='/ticketing/prevShowTicket' />?content_mst_cd=${sale.content_mst_cd}&today=${today}&member_name=${sale.member_name}&member_tel=${sale.member_tel}";
		window.close();
		
		//window.open('','_self').close();
		
	});
	
	
});
<c:if test="${not empty message}">
	alert('<c:out value="${message}" />');
</c:if>
	
</script>



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
});
</script>



<!-- 전환페이지 설정 -->
<script type="text/javascript" src="//wcs.naver.net/wcslog.js"></script> 
<script type="text/javascript"> 
var _nasa={};
if(window.wcs) _nasa["cnv"] = wcs.cnv("1","10"); // 전환유형, 전환가치 설정해야함. 설치매뉴얼 참고
</script> 


<%@ include file="../../include/sogeumsan/footer-single.jsp" %>
</body>
</html>