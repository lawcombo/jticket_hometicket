<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%-- 
<%@ include file="../../include/sogeumsan/header-single.jsp" %>
 --%>
<%@ include file="../../include/mcycamping/header-single.jsp" %>

<c:set var="now" value="<%=new java.util.Date() %>" />
<fmt:formatDate var="today" value="${now }" pattern="yyyy-MM-dd"/>
<c:set var="isUsed" value="false" />
<c:set var="isRefunded" value="false" />
<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">


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
				<li><b>온라인 예매</b></li>
			</ul>
		</div>
	</div><!-- //sub_v2 -->
</div>



<div id="page-wrapper" class="wrp" style="margin-top:103px; margin-bottom:298px">
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
				예매 내역 목록
			</h1>
			<span class="bar"></span>
			<div class="ewp_blist_box">
				<div id="list-section">
					<h3 class="ewp_blist_user">
						<span>${sales[0].member_name}</span>님의 예약내역
					</h3>
					<!-- <p>상품명/에매일/인원</p> -->
					<c:forEach var="sale" items="${sales}" varStatus="status">
				        <div class="ewp_blist_list">
				        	<a class="orderNum" type="button" id="${sale.order_num}_${sale.sale_code}">${sale.product_group_name} / ${sale.valid_from} &#126; ${sale.valid_to } / ${sale.total_quantity} 명</a>
				        </div>
			        </c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="hidden-section" style="display:none;">
	<form:form name="form" method="GET" action="/ticketing/mcycamping/showTicketInfo">
		<input type="hidden" name="content_mst_cd" value="${sale.content_mst_cd}" />		
		<input type="hidden" name="order_num" id="order_no" />		
		<input type='hidden' name='shop_code' value="${sale.shop_code }">
		<input type='hidden' name='sale_code' id='sale_code'>
		<input type='hidden' name='member_tel' value="${sale.member_tel }">
		<input type='hidden' name='member_name' value="${sale.member_name }">
	</form:form>
</div>

<script>
$(function() {
	$("#confirm").attr('class', 'sub-a active');
	
	$(".orderNum").on('click', function(e){
		e.preventDefault();
		var data = $(this).attr('id');
		var id = data.split("_");
		$("#order_no").val(id[0]);
		$("#sale_code").val(id[1]);
		
		
		var form = $('form[name="form"]');
		form.submit();
	});
	
	<%-- $('#cancelButton').on('click', function(e){
		e.preventDefault();
		
		
		if(confirm('현재 예약을 취소하시겠습니까?')) {
			var form = $('form[role="cancel"]');
			form.action="/ticketing/showTicketInfo?content_mst_cd="+'<%=request.getParameter("content_mst_cd")%>';
			form.submit();
		}
	}); --%>
	
	<c:if test="${not empty message}">
		alert('<c:out value="${message}" />');
	</c:if>
})


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


</script>

<!-- 전환페이지 설정 -->
<script type="text/javascript" src="//wcs.naver.net/wcslog.js"></script> 
<script type="text/javascript"> 
var _nasa={};
if(window.wcs) _nasa["cnv"] = wcs.cnv("1","10"); // 전환유형, 전환가치 설정해야함. 설치매뉴얼 참고
</script> 


<%@ include file="../../include/sogeumsan/footer-single.jsp" %>