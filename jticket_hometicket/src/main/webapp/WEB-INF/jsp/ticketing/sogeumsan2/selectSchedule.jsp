<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>


<%@ include file="../../include/sogeumsan/header-single2.jsp" %>


<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">



<div class="app">
	<%-- 
	<div style="background-image: url('${pageContext.request.contextPath }/resources/images/ganghwakidscafe/gangwha_kids_top.jpg'); height:300px; width:1000px"> 
	</div>
 	--%>
 
 	
 
	<!-- 뒤로가기 버튼 -->
	<%-- 
	<div style="text-align:left; padding-top:10px; padding-left:10px;">
		<img src="${pageContext.request.contextPath }/resources/images/diamondbay/backImg.png" onclick="history.back()" style="cursor: pointer; width: 50px;" />
	</div>
	 --%>
	<%-- 
	<%@ include file="../../include/ganghwakidscafe/top_menu.jsp"%>
	 --%>
	 
	<div class="body-contents">
		<div class="body-contents-wrap">
			
		
			<div class="intro res_tit res_padi">
				<h1>나오라쇼 예매하기</h1>
			</div>
			
			<div style="float:right; padding-right:18px">
				<button type="button"  class="md_bts" style="background:#1fcad3; border:none; color:#fff; height:35px; cursor:pointer;" onclick="checkBtn.goCheckPop();">예매 확인 / 취소</button>
			</div>
			
			<section class="tabs ewp_tabs">
				<ul>
					<li class="active">
						<div>
							<strong>01</strong>
							<span>날짜 및 시간 선택</span>
						</div>
						<span class="material-icons">arrow_forward_ios</span>
					</li>
					<li>
						<div>
							<strong>02</strong>
							<span>예매정보입력</span>
						</div>
						<span class="material-icons">arrow_forward_ios</span>
					</li>
					<li>
						<div>
							<strong>03</strong>
							<span>확인 및 결제</span>
						</div>
						<span class="material-icons">arrow_forward_ios</span>
					</li>
				</ul>
			</section>
		<div class="calender_box">
			<section class="calendar">
				<div id="datepicker"></div>
				<%-- 
				<c:if test="${productGroup.product_group_code eq '101' }">
					<div class="datepick_txt">
						<p>5월 1일 부터</p>
						<p>양조장투어+맥주 : 22,000원  (제주맥주 샘플러 250ml*4잔) <span>*신분증 필참</span></p>
						<p>양조장투어+음료 : 19,000원  (제주 음료 제공)</p>
						<p>*옵션 변경을 희망하실 경우 예약 취소 후 재예약 하셔야 합니다.</p>
					</div>
				</c:if>
				
				<div class="datepick_txt">
					<p>* 이용요금 할인 대상자는 티켓 수령 및 현장 발권 시 반드시 <span>"신분증 지참"</span></p>
					<p>* 제한인원 초과시 이용이 불가할 수 있습니다.</p>
				</div>
				 --%>
			</section>
			<section class="order-wrap time-wrap">
				<h3>
					<!-- <span class="material-icons">event_available</span> -->
					<img src="../../../../resources/images/img_icon_calendar.png">
					<span class="reserve-date"></span>
				</h3>
				<div class="reserve-list th_res_list">
				</div>
				
				<!-- ==공지사항== -->
				<!-- ========== -->
				
				
				<div class="ewp_date_info">
					<h3>· 이용 안내 </h3>
					<p>
						* 야간경관조명 이용시간: 18:30 ~ 22:00 <br>
						* 출렁다리, 하늘바람길 이용시간: 18:30 ~ 20:30 <br>
						* 나오라쇼 공연시간: 20:30 ~ 21:20
					</p>
				</div>
				
				
				<div class="ewp_date_info">
					<h3>· 티켓 예매 안내 </h3>
					<p>
						* 1인당 예매 수량 제한 : 총 10매 <br>
						* 1일 수량 매진시 관람 불가(1일 수량: 온라인 600매/현장 200매) <br>
						* 대인(13세 이상) / 소인(7세 이상~12세 이하) / 6세 이하 무료 / 단체(20명 이상) <font style="background-color: rgb(255, 153, 0); font-weight:bold; cursor:pointer;" onclick="textInfoClick.modalFade(0)">요금표 확인하기</font> <br>
						* 예매 시 할인(우대) 권종을 숙지 후 예매 <font style="background-color: rgb(255, 153, 0); font-weight:bold; cursor:pointer;" onclick="textInfoClick.modalFade(1)">우대 대상(1) 확인하기</font> <font style="background-color: rgb(255, 153, 0); font-weight:bold; cursor:pointer;" onclick="textInfoClick.modalFade(2)">우대 대상(2) 확인하기</font><br>
						* 현장 티켓 수령 시 할인(우대)을 변경 불가 및 할인(우대)을 관련하여 당일 취소, 변경, 환불 불가 <br>
						* 이용요금 우대 대상자는 티켓 수령 및 현장 발권 시 "신분증 및 증빙서류" 반드시 지참 <br>
						* 현지 기상악화로 휴장할 경우, 동의 없이 자동 취소(100% 환불처리) <br>
						* 제한 수량 초과시 이용이 불가할 수 있습니다.
						<!-- 
						한 일행 당 9인 이상은 <span style="text-decoration:underline; font-weight:bold;">따로 예약 및 동선 분리를 하셔도 입장이 절대 불가</span>하며,<Br>
						현장에서 9인 이상으로 확인될 경우, <span style="text-decoration:underline; font-weight:bold;">환불은 불가하며 퇴장 조치</span> 될 수 있습니다.<br>
						 
						<br>
						* 자세한 내용은 공지사항을 참고해주세요!
						-->
					</p>
				</div>
				
				<div class="ewp_date_info">
					<h3>· 티켓 수령 안내 </h3>
					<p>
						* 증빙서류 및 휴대폰 뒷번호 확인 후 티켓 수량 가능 (신분증 반드시 지참) <br>
						* 할인(우대) 증빙서류 미 지참시 차액 지불 <br>
						* 티켓 배송 불가 / 예매티켓은 공연당일 현장 수령만 가능 <br>
						* 공연 당일 티켓 취소, 변경, 환불 불가 <br>
					</p>
				</div>
				
				<div class="ewp_date_info">
					<h3>· 문의 간현관광지 관리사무소 033-749-4860 </h3>
					<p>
					</p>
				</div>
				
			</section>
			
		</div>
		
		
		<form:form role="form" name="paymentInfo" id="ticket_frm"  action="/ticketing/sogeumsan2/insertReserver?content_mst_cd=${essential.content_mst_cd}&product_group_code=${essential.product_group_code}"  method="post">
			<input type="hidden" name="productGroup.shop_code" id="shop_code" value="${products[0].shop_code }"/>	
			<input type="hidden" name="productGroup.product_group_code" id="product_group_code" value='<c:out value="${productGroup.product_group_code }" />'/>	
			<input type="hidden" name="productGroup.content_mst_cd" id="content_mst_cd"  value='<%=request.getParameter("content_mst_cd")%>'/>	
			<input type="hidden" name="visitorType" id="visitorType" value="A" /> <!-- 일단 A로 고정 -->	
			<input type="hidden" name="totalFee" id="totalFee" />	
			<input type="hidden" name="totalCount" id="totalCount" />	
			<input type="hidden" name="schedule_code" id="schedule_code" />	
			<input type="hidden" name="play_date" id="play_date" />
			<%-- 현재는 대인 하나뿐이기 때문에 권종을 0번 하드코딩 --%>
			<%-- <input type="hidden" name="products[0].shop_code" id="product_shop_code" value="${products[0].shop_code }" />
			<input type="hidden" name="products[0].product_group_code" value="${products[0].product_group_code }" />
			<input type="hidden" name="products[0].product_code" value="${products[0].product_code }" />
			<input id="productCountInput" type="hidden" name="products[0].count"  /> --%>
			<c:forEach var="product" items="${products}" varStatus="status">
				<input type="hidden" name="products[${status.index }].shop_code" value="${product.shop_code }" />
				<input type="hidden" name="products[${status.index }].product_group_code" value="${product.product_group_code }" />
				<input type="hidden" name="products[${status.index }].product_code" value="${product.product_code }" />
				<input type="hidden" name="products[${status.index }].count" id="productCountInput${status.index}" data-productcode="${product.product_code }" class="productCount"/>
			</c:forEach>
			
			<input type="hidden" name="loginUserId" value="${loginUserId}" />
			<input type="hidden" name="loginUserNm" value="${loginUserNm}" />
		</form:form>
	</div><!-- mx1200 end -->
	
	
	
<div class="rad_modal" style="display: none;" id="image1">
	<div class="rad_modal_content" style="max-width:max-content;">
		<div class="rad_modal_x">
			<div id="agreementTermsOfUse-modal-section" class="md_cont">
				<div class="modal_tb">
					<!-- 
					<div class="mt_top">
						<h2>
							<strong>잠깐!</strong>
						</h2>
						<p class="mt_top_text"></p>
					</div>
					 -->
					<div class="mt_bot">
						<img src="../../../../../resources/images/sogeumsan/main_0704.png" style="width:100%;">
					</div>
				</div>
				
				<div style="padding-right:20px; margin-left:20px; padding-bottom:5px">
					<button class="md_bts" style="cursor:pointer;" onclick="nitiConfirm.confirmBtn();">닫기</button>
				</div>
			</div>
		</div>
	</div>
	<div class="rad_modal_bk"></div>
</div>
<!-- 
<div class="rad_modal" style="display: none;" id="image2">
	<div class="rad_modal_content" style="max-width:max-content;">
		<div class="rad_modal_x">
			<div id="agreementTermsOfUse-modal-section" class="md_cont">
				<div class="modal_tb">
					<div class="mt_bot">
						<img src="../../../../../resources/images/sogeumsan/naora8.jpg" style="width:100%;">
					</div>
				</div>
				
				<div style="padding-right:20px; margin-left:20px; padding-bottom:5px">
					<button class="md_bts" style="cursor:pointer;" onclick="nitiConfirm.confirmBtn2();">닫기</button>
				</div>
			</div>
		</div>
	</div>
	<div class="rad_modal_bk2"></div>
</div>
 -->
 
 <!-- 요금표 확인하기 모달 -->
 <div class="rad_modal" style="display: none;" id="image3">
	<div class="rad_modal_content" style="max-width:max-content;">
		<div class="rad_modal_x">
			<div id="agreementTermsOfUse-modal-section" class="md_cont">
				<div class="modal_tb">
					<div class="mt_bot">
						<img src="../../../../../resources/images/sogeumsan/price_new.png" style="width:100%;">
					</div>
				</div>
				
				<div style="padding-right:20px; margin-left:20px; padding-bottom:5px">
					<button class="md_bts" style="cursor:pointer;" onclick="nitiConfirm.confirmBtn3();">닫기</button>
				</div>
			</div>
		</div>
	</div>
	<div class="rad_modal_bk3"></div>
</div>

 <!-- 우대대상 1 모달 -->
 <div class="rad_modal" style="display: none;" id="image4">
	<div class="rad_modal_content" style="max-width:max-content;">
		<div class="rad_modal_x">
			<div id="agreementTermsOfUse-modal-section" class="md_cont">
				<div class="modal_tb">
					<div class="mt_bot">
						<img src="../../../../../resources/images/sogeumsan/fre1new.png" style="width:100%;">
					</div>
				</div>
				
				<div style="padding-right:20px; margin-left:20px; padding-bottom:5px">
					<button class="md_bts" style="cursor:pointer;" onclick="nitiConfirm.confirmBtn4();">닫기</button>
				</div>
			</div>
		</div>
	</div>
	<div class="rad_modal_bk4"></div>
</div>
 
 <!-- 우대대상 2 모달 -->
 <div class="rad_modal" style="display: none;" id="image5">
	<div class="rad_modal_content" style="max-width:max-content;">
		<div class="rad_modal_x">
			<div id="agreementTermsOfUse-modal-section" class="md_cont">
				<div class="modal_tb">
					<div class="mt_bot">
						<img src="../../../../../resources/images/sogeumsan/fre2new.png" style="width:100%;">
					</div>
				</div>
				
				<div style="padding-right:20px; margin-left:20px; padding-bottom:5px">
					<button class="md_bts" style="cursor:pointer;" onclick="nitiConfirm.confirmBtn5();">닫기</button>
				</div>
			</div>
		</div>
	</div>
	<div class="rad_modal_bk5"></div>
</div>


<script type="text/x-mustache" id="list-template">
	<ul>
	<li class="list_tit_li"><div class="ch_time">선택</div><div class="ch_num">수량</div></li>
	{{#data}}
		<li class="th_list_tem_li">
			<div data-id="item{{id}}"
				data-time="{{time}}"
				data-seat="{{seat}}"
				data-limit="{{seatlimit}}"
				data-user-seat="0"
				class="cell {{^reserv}}disable{{/reserv}}">
				<label class="time jejuRadioButton calch">
					<input type="radio" id="c{{id}}" name="cal" value="{{id}}" {{^reserv}}disabled{{/reserv}} /><span class="radio"></span>
					{{time}}
				</label>
				<div class="seat">
					<span>{{seat}}</span>
				</div>
				<div class="order">
					{{#reserv}}
					{{#product.product}}
					<div class="order_spbx" style="text-align:right; padding-right:15px; ">
						<span data-id="{{id}}" style="float:left;">{{name}}</span>
						<button type="button" data-id="{{id}}" id="{{id}}{{code}}" class="btn js-minus">
							<span class="material-icons">remove</span>
						</button>
					
						<div class="data-input-cnt" data-input-id="input{{id}}{{code}}" id="countIdTmp">
							0
						</div>
					
						<button type="button" data-id="{{id}}"  id="{{id}}{{code}}" class="btn js-plus">
							<span class="material-icons">add</span>
						</button>
					</div>
					{{/product.product}}
					{{/reserv}}
				</div>
				<div class="submit">
					{{^reserv}}
						<button type="button" data-id="{{id}}"  disabled class="btn js-order">불가</button>
					{{/reserv}}
					{{#reserv}}
						<button type="button" data-id="{{id}}" class="btn js-order">예매</button>
					{{/reserv}}
				</div>
			</div>
			
		</li>
	{{/data}}
	</ul>
</script>
<script>


$(document).ready(function() {
	
	//===============================소금산 그랜드밸리 이미지 팝업 ===============================
	//popOpen.openImage1();
	//popOpen.openImage2();
	
	//$(".rad_modal").fadeIn();
	
	$("#image1").fadeIn();
	
});
//=======================================

	
var textInfoClick = {
		
		modalFade : function(flag){
			
			if(flag == 0)
			{//요금표 확인하기
				$("#image3").fadeIn();
			}
			else if(flag == 1)
			{//우대 대상(1) 확인하기
				$("#image4").fadeIn();
			}
			else if(flag == 2)
			{//우대 대상(2) 확인하기
				$("#image5").fadeIn();
			}
			
		}
		
}
	
var checkBtn = {
		goCheckPop : function(){
			var popupWidth = 1250;
			var popupHeight = 900;
			
			//팍업 가운데 정렬을 위한 화면 해상도 계산
			var popupX = (window.screen.width / 2) - (popupWidth / 2);
			var popupY= (window.screen.height / 2) - (popupHeight / 2);
			
			//window.open('/ticketing/ghkidscafe', 'window_name', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
			window.open('/ticketing/checkTicket?content_mst_cd=SOGEUMSAN_0_1', 'window_name2', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
		},
}


	let selectDate = ''
	var min = '';
	var max = '';
	var arrDate = new Array();
	//  달력 option
	$.datepicker.setDefaults({
		dateFormat: 'yy-mm-dd',
		prevText: '이전 달',
		nextText: '다음 달',
		currentText: '이번달로 이동',
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		showMonthAfterYear: true,
		yearSuffix: '년',
		showButtonPanel: true,
	});
	
	$( function() {
		$("#reservation").attr('class', 'sub-a active');
		
		var product_group_code=$("#product_group_code").val();
		var shop_code = $("#shop_code").val();
		var fee = '${productGroup.product_min}';
		var products = '';
		// 예약 불러오기 (회차정보 return json)
		const getDateReserve = function (targetDate) {
			
			
			// ==========================================================================================================================
			console.log('===============================');
			var todayTest = new Date();
			/*
			var todayYear 	= todayTest.getFullYear();
			var todayMonth 	= todayTest.getMonth()+1;
			var todayDay 	= todayTest.getDate();
			var todayHours 	= todayTest.getHours();
			var todayFormat = todayYear + '-' + todayMonth + '-' + todayDay;
			*/
			var todayHours 	= todayTest.getHours();
			console.log("오늘 시간 : " + todayTest.getHours());
			
			
			console.log('===============================');
			//오늘 날짜 + 1 (즉, 내일 날짜 가공)
			var tomorrowTest = new Date(todayTest.setDate(todayTest.getDate() +1));
			
			var tomorrowYear 	= tomorrowTest.getFullYear();
			var tomorrowMonth 	= tomorrowTest.getMonth()+1;
			var tomorrowDay 	= tomorrowTest.getDate();
			
			if(tomorrowMonth < 10)
			{
				tomorrowMonth = '0' + tomorrowMonth;
			}
			
			if(tomorrowDay < 10)
			{
				tomorrowDay = '0' + tomorrowDay;
			}
			// 내일 날짜 포맷
			var tomorrowFormat 	= tomorrowYear + '-' + tomorrowMonth + '-' + tomorrowDay;
			
			console.log("내일 날짜 : " + tomorrowFormat);
			console.log("선택 날짜 : " + targetDate);
			
			
			
			//선택한 예매일이 다음날일 경우, 
			//행사 시작 요일 전날 오후 18시까지만 예매 가능하도록
			//오늘 예매를 내일 날짜로 할경우, 오늘 18시 이전까지만 예매 가능하도록.
			if(tomorrowFormat == targetDate)
			{
				if(todayHours >= 18)
				{
					var reserveDateTmp = tomorrowYear + "년 " + tomorrowMonth + "월 "+ tomorrowDay + "일";
					
					$('.reserve-date').text(reserveDateTmp);
					$('.reserve-list').html('<span style="color:red; font-weight:bolder">' + reserveDateTmp + ' 예매는 마감되었습니다.</span>');
					return false;
				}
			}
			
			
			
			//소금산 그랜드밸리 당인예매 불가능
			//전날 오후 6시까지만 예매 가능 ( 선택한 날짜 전날 )
			var todayDateTmp = moment().format('YYYY-MM-DD');
			if(todayDateTmp == targetDate)
			{
				var newDateTmp = targetDate.split("-");
				var reserveDateTmp = newDateTmp[0]+"년 "+newDateTmp[1]+"월 "+newDateTmp[2]+"일";
				
				$('.reserve-date').text(reserveDateTmp);
				$('.reserve-list').html('<span style="color:red; font-weight:bolder">' + reserveDateTmp + ' 예매는 마감되었습니다.</span>');
				return false;
			}
			// ==========================================================================================================================
			
			
			selectDate = targetDate;
			var newDate = targetDate.split("-");
			var reserveDate = newDate[0]+"년 "+newDate[1]+"월 "+newDate[2]+"일";
			$('.reserve-date').text(reserveDate);
			$("#play_date").val(targetDate);
			var data = {'shop_code':shop_code, 'contentMstCd':'<%=request.getParameter("content_mst_cd")%>', 'product_group_code':'<%=request.getParameter("product_group_code")%>', 'play_date' : selectDate};
			$.ajax({
				url:"/ticketing/selectScheduleAjax?${_csrf.parameterName}=${_csrf.token}",
				type:"POST",
				dataType : 'json',
				data : data,
				success: function(json){
					//console.log(json.schedule);
					//console.log(json.products);
					var schedule = json.schedule;
					products = json.products;
					
					console.log(products);
					
					let productAry = {product: []};
					for(var j=0; j<products.length; j++){
						productAry.product.push({
							code:products[j].product_code,
							name:products[j].product_name,
						})
					}
					
					console.log(productAry);
					
					let dataAry = {data: []};
					var reserv = false;
					for(var i=0; i<schedule.length; i++){
						if(schedule[i].sumCout <= 0){
							reserv = false;
							schedule[i].sumCout = 0;
						}else{
							reserv = true;
						}
						dataAry.data.push({
							id: schedule[i].schedule_code,
							time: schedule[i].subject_text,
							seat: Number(schedule[i].sumCout),
							seatlimit: Number(schedule[i].total_count),
							reserv: reserv,
							product:productAry,
						})
					}
					
					var temlate = $('#list-template').html();
					var html = Mustache.render(temlate, dataAry);
					
					$('.reserve-list').append(html);
					
				},
				complete: function(){
					
				},
				error : function(xhr,status,error) {
					console.log(xhr);
					console.log(status);
					console.log(error);
				}
			});
			$('.reserve-list').html('');
		}

		//예약시간 선택
		$('.reserve-list').on('change','input[type=radio]', function (e){
			$('.cell').removeClass('active');
			$('.cell [data-input-id]').text(0);
			$('[data-id="item'+ e.currentTarget.value +'"]').addClass('active');
			//$('#productCountInput').val(1);
			$("#schedule_code").val(e.currentTarget.value);
			$("#totalCount").val(0);
			$("#totalFee").val(fee);
			
		});

		// seat 제거
		$('.reserve-list').on('click','.js-minus', function (e){
			e.preventDefault();
			const Id2 = $(e.currentTarget).data('id');
			const Id = $(e.currentTarget).attr('id');
			const Tg = $('[data-input-id="input'+Id+'"]')
			var totalCnt = Number($("#totalCount").val());

			let NowNum = (Number(Tg.text()) > 0)? Number(Tg.text()) -1 : 0;
			Tg.text(NowNum);
			$('#productCountInput').val(NowNum);
			
			
			var cntSum = 0;
			$(".data-input-cnt").each(function() {
				var cnt = Number($(this).text());
				cntSum = cntSum + cnt; 
			})
			
			if(totalCnt - cntSum > 0) {
				totalCnt--;
			}
			$("#totalCount").val(totalCnt);
			console.log(totalCnt + ", " + cntSum);
		});

		// seat 추가
		var targetID = '';
		$('.reserve-list').on('click','.js-plus', function (e){
			e.preventDefault();
			var totalCnt = Number($("#totalCount").val());
			var totalFee = Number($("#totalFee").val());
			
			const dataID = $(e.currentTarget).data('id');
			const useSeat =  Number($('[data-id="item'+dataID+'"]').data('seat')); //잔여석 9			
			const limit = Number($('[data-id="item'+dataID+'"]').data('limit')); //정원 상품마다 다름
			
			const Id = $(e.currentTarget).attr('id');
			const Tg = $('[data-input-id="input'+Id+'"]');
			
			const codeSplit = Id.split(dataID); //product_code 값 추출
			
			let NowNum = 0;
			if(totalCnt >= limit){
				$("#totalCount").val(limit);
				totalCnt = limit;
			}else{

				var cntSum = 0;
				$(".data-input-cnt").each(function() {
					var cnt = Number($(this).text());
					cntSum = cntSum + cnt; 
				})
				
				if(cntSum < useSeat) {
					NowNum = Number(Tg.text()) +1;
					totalCnt++;
					Tg.text(NowNum);
					
					$("#totalCount").val(totalCnt);
				}
			}
			console.log(totalCnt + ", " + NowNum);
		});

		//예매하기
		$('.reserve-list').on('click', '.cell.active .js-order', function (e) {
			
			
			const dataID = $(e.currentTarget).data('id')
			const myseat = Number($('[data-input-id="input'+dataID+'"]').text());
			const time  = $('[data-id="item'+dataID+'"]').data('time');
			//location.href = 'reserve.html?id='+ Id +'&date=' + seletcDate + '&time=' + time + '&seat=' + myseat;
			//submit으로 변경
			$("#shop_code").val(shop_code);
			
			const Tg = $('[data-input-id="input'+Id+'"]');
			var totalFee = 0;
			for(var i=0; i<products.length; i++)
			{
				
				var Id = dataID+products[i].product_code;
				const Tg = $('[data-input-id="input'+Id+'"]');
				
				totalFee += Number(Tg.text())*Number(products[i].product_fee);
				
				var productCount = $(".productCount[data-productcode=" + products[i].product_code + "]");
				productCount.val(Number(Tg.text()));
// 				$("#productCountInput"+i).val(Number(Tg.text()));
			}
			$("#totalFee").val(totalFee);
			
			e.preventDefault();
			
			if(totalFee <=0) {
				alert("티켓 매수를 선택해 주세요.");
				return;
			}
			
			//소금산그랜드밸드 10매 제한
			if($("#totalCount").val() > 10)
			{
				alert("예매 가능한 총 매수는 10 매 제한입니다.\n선택하신 총 매수는 " +$("#totalCount").val() + " 매 입니다.");
				return;
			}
			
			
			
			
			$('#ticket_frm').submit();
		});
		
		
		//예매 오픈기간 가져오기
		var todayDate = moment().format('YYYY-MM-DD');
		//var min=moment().add(1, 'days').format('YYYY-MM-DD');
		var min = todayDate;
		var max = '';
		var arrDate = new Array();

		// CNA 무료 투어 추가
		var tempGroup = '${param.product_group_code}';
		if(tempGroup == '105'){
			min = '2022-02-10';
			todayDate = '2022-02-10';
		}

		var data = {'shop_code':'<%=request.getParameter("shop_code")%>', 'content_mst_cd':'<%=request.getParameter("content_mst_cd")%>', 'date':todayDate, 'product_group_code': '<c:out value="${productGroup.product_group_code}" />'  };
		$.ajax({
			url:"/ticketing/ajaxMonthData?${_csrf.parameterName}=${_csrf.token}",
			type:"POST",
			dataType : 'json',
			data : data,
			success: function(data){
				//console.log(data);
				var length = data.length-1;
				if(data[0].play_date > todayDate) {
					min = data[0].play_date;	
				}
				
				max = data[length].play_date;
				for(var i=0; data.length > i; i++){
					arrDate[i] = data[i].play_date;
				}
				$( "#datepicker" ).datepicker({
					minDate:min,
					maxDate:max,
					onSelect: function(dateText, inst) {
						getDateReserve(dateText); 
						
						//공지 hide
						$("#notiArea").hide();
					},
					beforeShowDay: function(date) {
						var thismonth = date.getMonth()+1;
						var thisday = date.getDate();
	
						if(thismonth<10){
							thismonth = "0"+thismonth;
						}
	
						if(thisday<10){
							thisday = "0"+thisday;
						}
	
						ymd = date.getFullYear() + "-" + thismonth + "-" + thisday;
	
						//console.log(ymd+' : '+($.inArray(ymd, arrDate)));
	
						if ($.inArray(ymd, arrDate) != -1) {
						    return [true, "","Available"]; 
						} else{
						     return [false,"","unAvailable"]; 
						}
					},
				});
			},
			error : function(xhr,status,error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			}
		});
	// 오늘 일정불러오기
	getDateReserve(min);
});
	


$(function(){
	$(".rad_modal_bk").click(function(){
		$("#image1").fadeOut();
		
		//$("#image2").fadeIn();
	});
});


$(function(){
	$(".rad_modal_bk3").click(function(){
		$("#image3").fadeOut();
	});
	
	$(".rad_modal_bk4").click(function(){
		$("#image4").fadeOut();
	});
	
	$(".rad_modal_bk5").click(function(){
		$("#image5").fadeOut();
	});
});


/* 
$(function(){
	$(".rad_modal_bk2").click(function(){
		$("image2").fadeOut();
	});
});
 */
var nitiConfirm = {
		confirmBtn : function(){
			$("#image1").fadeOut();
			
			//$("#image2").fadeIn();
		},
		
		confirmBtn2 : function(){
			//$("#image2").fadeOut();
		},
		
		confirmBtn3 : function(){
			$("#image3").fadeOut();
		},
		
		confirmBtn4 : function(){
			$("#image4").fadeOut();
		},
		
		confirmBtn5 : function(){
			$("#image5").fadeOut();
		},
		
		
}
</script></div>

</div>
<%@ include file="../../include/sogeumsan/footer-single2.jsp" %>
</body>
</html>
