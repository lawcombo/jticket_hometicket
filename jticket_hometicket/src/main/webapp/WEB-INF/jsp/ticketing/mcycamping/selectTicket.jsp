<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<%@ include file="../../include/mcycamping/header-single.jsp" %>


<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">
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
				<li><b>온라인 예매</b></li>
			</ul>
		</div>
	</div>
</div>


<div id="page-wrapper" class="wrp">

	<section class="sel_sec">
	
		<div class="sel_tit aos-init aos-animate" data-aos="fade-up" data-aos-easing="linear" data-aos-duration="700" data-aos-delay="300">
			<h2>온라인 예매</h2>
			<span class="bar"></span>
			<div class="desc">
				<em>'MCY파크(MCYpark)' 빠르게 만나는 방법!!</em>
				온라인 예매로 아름다운 MCY파크(MCYpark)를 지금 만나보세요! 
			</div>
		</div>
		<div class="res_btbx">
			<button id="checkReservationOpenButton">예매확인 및 취소</button>
		</div>
	
		<div class="re_banner">
			<div class="re_ban_w ewp_re_ban_w">
				<p class="ban_l"><img src="/resources/noSchedule/images/con2_1_bann01.png" alt="" /></p>
				<p class="ban_r">
					MCY파크(MCYpark) <b>쉽게 만나는 방법!</b><span class="inline_b"></span>온라인 예매로 구매하세요!
					<span class="t1"> <b>온라인 예매로 아름다운 MCY파크(MCYpark)</b>를 지금 만나볼 수 있습니다.</span>
				</p>
			</div>
		</div>
		
		<div id="step-section">
			<div class="row-h1 bc_status">
				<ul class="aftCB stepul proc01">
					<li class="on">
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
					<li>
						<p>
							<!-- <i>3</i> -->
							<span>03. 확인 및 결제</span>
						</p>
					</li>
				</ul>
			</div>
		</div>
	
		
		<div id="select-tickect-section" class="justify-content-around bok_wrap">
		
			
		
			<div id="select-tickect-section" class="justify-content-around">
			
				<!-- <div id="broshure-section">
					안내영역
				</div> -->
				<div class="bk_wps">
					<div class="booking_con01">
						<div class="con01_01">
							<h5 class="con01_01_tit">모바일 티켓 사용 방법</h5>
							<div class="con01_01_imgbx">
								<p class="con_imgbx01">
									<img src="/resources/noSchedule/images/con2_1_icon1.png">
									<span>홈페이지에서<br>온라인 티켓 구매 완료</span>
								</p>
								<p class="con_imgbx02">
									<img src="/resources/noSchedule/images/con2_1_icon3.png">
									
								</p>
								<p class="con_imgbx03">
									<img src="/resources/noSchedule/images/con2_1_icon2.png">
									<span>현장 방문 후 매표소에서<br>모바일 티켓 제시 후 탑승권 수령</span>
								</p>
							</div>		
						</div>
					</div><!-- booking_con01 end -->
					<div id="product-group-section" class="booking_con02">
						<h5 class="con01_01_tit">상품</h5>
						<div>
							<c:forEach var="productGroup" items="${productGroups }" varStatus="status">
								<div class="prod_div">
									<input type="radio" name="productGroupRadio" id="productGroupRadio${status.index }" 
										value="${productGroup.product_group_code }" <c:out value="${status.index eq 0 ? 'checked' : '' }" />/>
									<label for="productGroupRadio${status.index }">${productGroup.product_group_name }</label>
								</div>
							</c:forEach>
						</div>
						
					</div>
					<div id="product-section" class="booking_con03">
						<h5 class="con01_01_tit">
							권종선택(금액)
						</h5>
						<form:form role="reserve" action="/ticketing/mcycamping/insertReserver" method="GET" class="sel_form">
							<input type="hidden" name="content_mst_cd" value="<c:out value='${shopInfo.content_mst_cd }' />"/>
							<input type="hidden" name="productGroup.product_group_code" />
							<div id="products">
								
							</div>
							
							<input type="hidden" name="loginUserId" value="<c:out value='${loginUserId}' />"/>
							<input type="hidden" name="loginUserNm" value="<c:out value='${loginUserNm}' />"/>
							
						</form:form>
						<div>			
							<button id="reserve-button">구매하기</button>
						</div>
					</div>
				</div>
			</div>
			<!-- 
			<div class="booking_text1">
				<p class="b_t1">[권종 구분]</p>
				<p class="b_t2">1. 스피드<br>
								- 누구나 대기 없이 바로 탑승 가능하며<br>
								- 다른 일행들과 동승 가능합니다.<br>
								2. 프리미엄<br>
								- 누구나 대기 없이 일행끼리만 바로 탑승 가능(10인승 캐빈)<br>
				</p>
			</div>
			 -->
			<!-- 
			<div class="booking_text1">
				<p class="b_t1">[야간 할인]</p>
				<p class="b_t2">
								- 예매 시 주중, 주말 17시 이후 ~ 운영 마감시간 30분 전까지 탑승 가능<br>
								- 대기 많을 시 다른 일행들과 동승 가능합니다.<br>
				</p>
			</div>
			 -->
			 <!-- 
			<div class="booking_text1">
				<p class="b_t1">[유의사항]</p>
				<p class="b_t2">별도의 회원가입 없이 구매 가능합니다.<br>
								외부 온라인 채널과 달리 홈페이지에서는 당일 구매 당일 발권이 가능합니다.<br>
								소인 (36개월 이상 ~ 13세(초등학생) 이하)은 어른과 함께 동반 시 탑승 가능<br>
								사회적 거리두기 규칙 준수 (집합인원, 마스크 착용, 체온체크 협조)<br>
								지역할인, 법정할인 정기권 등 할인 적용은 현장 구매 시 가능합니다.<br>
								케이블카는 우천시에도 정상 운영하며, 운영 중 우천으로 인한 취소 및 환불은 불가합니다.<br>
								(단, 강풍,낙뢰,폭우,폭설 등 천재지변으로 인하여 운행이 불가능 할 경우에는 취소,환불이 가능합니다)<br>
								반려동물 탑승 가능합니다.(단, 시설 및 캐빈 내 케이지 및 유모차 필수 사용 / 케이지, 켄넬, 유모차 크리스탈 캐빈 탑승 불가)<br>
								케이블카 내 음식물 반입 금지입니다.<br>
								티켓 분실 시 재발행 되지 않으며, 이로 인한 취소는 불가능 합니다.<br>
								탑승마감시간 : 운행종료 30분전입니다. *상황에 따라 변동 될 수 있습니다.<br>
				</p>
			</div>
			<div class="booking_text1">
				<p class="t1"><b>[장애 및 거동 불편 고객 이동 안내]</b></p>
				<ul>
					<li>서해랑 전곡 정류장은 가파른 언덕길에 있어 장애 및 거동 불편 고객에게 불편함을 끼칠 수 있습니다.<br>
서해랑 제부 정류장은 완만한 지형에 있어 장애 및 거동 불편 고객이 케이블카를 이용하기에 용이합니다.<br>
장애 및 거동 불편 고객분들은 서해랑 제부 정류장 이용을 권장합니다.</li>
				</ul>
			</div>
			 -->
		</div>

	</section><!-- sel_sec end -->
	
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

<%@ include file="../../include/mcycamping/check-ticket.jsp" %>


<script>

$(function() {
	
	
	// 상품그룹 선택
	$("input[name='productGroupRadio']").change(function() {
		
		
		var productGroupCode = $(this).val();
		var productGroup = {
			content_mst_cd: '<c:out value="${shopInfo.content_mst_cd}" />',
			shop_code: '<c:out value="${shopInfo.shop_code}" />',
			product_group_code: productGroupCode,
			type: 1
		}
		
		$("input[name='productGroup.product_group_code']").val(productGroupCode);
		getProducts(productGroup)
	});
	

	// 권종 +1
	var plusCount = function (){
		var targetData = $(this).data('target');
		var target = $("#" + targetData);
		target.val(Number(target.val()) + 1);
		return false;
	}
	
	// 권종 -1
	var minusCount = function (){
		var targetData = $(this).data('target');
		var target = $("#" + targetData);
		var value = Number(target.val()) - 1;
		if(value < 0)
			value = 0;
		target.val(value);
		return false;
	}
	
	// 상품그룹코드로 상품 조회
	function getProducts(productGroup) {
		
		// 권종 항목 클리어
		$("#products").empty();		
		showShadow();
		
		$.ajax({
			type : 'post',  				       
			url : '<c:url value="/ticketing/sogeumsan/getProducts" />',
			headers : {
				"Content-type" : "application/json;charset=utf-8",
				"X-HTTP-Method-Override" : "POST"
			},
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			dataType : 'json',
			data : JSON.stringify(productGroup),
			success: function(data, status, xhr) {
				
				// error
				if(data.result_code) {
					alert('[' + data.result_code + ']' + data.result_message);
				}
				
				// 정상
				for(var i=0; i<data.length; i++) {
					var product = data[i];
					$("#products").append(
							"<div class='pmbx'>"
								+ "<div class='pmcont'>"
									+ "<label for='product" + product.product_code + "'>" + product.product_name + "(" + numberWithCommas(product.product_fee) + ")" + "</label>" 
									+ "<input type='hidden' id='product" + product.product_code + "' name='products[" + i + "].product_code' value='" + product.product_code + "' />"
								+ "</div>"
								+ "<div class='pmbtn'>"

									+ "<button class='minusCount' data-target='productCount" + i + "'>&nbsp;&#45;&nbsp;</button>"				
									+ "<input class='productCount' type='number' id='productCount" + i + "' value='0' name='products[" + i + "].count'/>"
									+ "<button class='plusCount' data-target='productCount" + i + "'>&nbsp;&#43;&nbsp;</button>"		
								+ "</div>"
							+ "</div>");
					
				}
				// 이벤트 추가
				$(".plusCount").click(plusCount);
				$(".minusCount").click(minusCount);
				hideShadow();
				
			},
			error: function (jqXhr, textStatus, errorMessage) { // error callback 
		        alert("오류가 발생하였습니다. 반복시 관리자를 호출해 주세요." + errorMessage);
		        hideShadow();
		    }
		});
	} 
	
	// 첫 상품 목록 보기
	$("input[name='productGroup.product_group_code']").val(<c:out value='${productGroups[0].product_group_code}' />);
	getProducts({
		content_mst_cd: '<c:out value="${shopInfo.content_mst_cd}" />',
		shop_code: '<c:out value="${shopInfo.shop_code}" />',
		product_group_code: "<c:out value='${productGroups[0].product_group_code}' />",
		type: 1
	});
	
	// 예매하기 버튼 클릭
	$("#reserve-button").on("click", function() {
		
		// TODO: 상품그룹을 골랐는지 체크
		if(!$("input[name='productGroup.product_group_code']").val()) {
			alert('상품을 선택해 주세요.');
			$("input[name='productGroupRadio']:first").focus();
			return;
		}
		// TODO: 권종의 개수가 0인지 체크
		var productsCount = 0;
		$(".productCount").each(function() {
			productsCount += Number($(this).val());
		});
		
		if(productsCount <= 0) {
			alert('권종을 선택해 주세요.');
			$(".productCount:first").focus();
			return;
		}
		
		
		
		var form = $("form[role='reserve']");
		form.submit();
	});
	
// 	$("#nextButton").on('click', function(e) {
// 		var totalCount = $('#totalCountInput').val();
// 		if(totalCount <= 0) {
// 			alert('매수를 선택헤 주세요.');
// 			return;
// 		}
// 		e.preventDefault();
// 		var form = $('form[role="form"]');
// 		form.submit();
// 	});
	
// 	$('.countSelect').change(function() {
// 		var totalFee = 0;
// 		var totalCount = 0;
// 		$('.countSelect').each(function(idx, c){
// 			var count = $(c).val();
// 			var idx = $(c).data('idx');
// 			var fee = $('.feeSpan.fee-' + idx).text();
// 			count = Number(count);
// 			fee = Number(fee);
// 			if(!isNaN(count) && !isNaN(fee)) {
// 				totalFee += (fee * count);
// 				totalCount += count;
// 			}
// 		});
// 		$('#totalFeeSpan').text(totalFee)
// 		$('#totalFeeInput').val(totalFee);

// 		$('#totalCountSpan').text(totalCount)
// 		$('#totalCountInput').val(totalCount);
// 	});
	
// 	document.querySelectorAll('.countSelect').forEach(function(item){
// 		item.selectedIndex = 0;
// 	});
})
</script>
<script>
  AOS.init();
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

// contentMstCd 쿠키 남기
function setCookie(cookieName, value, exdays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString() + ";path=/;domain=" + window.location.hostname);
    document.cookie = cookieName + "=" + cookieValue;
}

$(function() {
	setCookie("ticketingJebuContentMstCd", "<c:out value='${shopInfo.content_mst_cd }' />", 30);
});


</script>



<%@ include file="../../include/mcycamping/footer-single.jsp" %>
