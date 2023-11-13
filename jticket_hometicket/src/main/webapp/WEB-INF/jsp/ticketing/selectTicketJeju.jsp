<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>


<%@ include file="../include/header-single.jsp" %>


<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">

<div class="app">
	<%@ include file="../include/top_menu.jsp" %>
	
	<%@ include file="../common/channelTalk.jsp" %>
	
	<div class="body-contents">
		<div class="body-contents-wrap">
			<div class="intro res_tit res_padi">
				<h1>예약하기</h1>
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
							<span>예약정보입력</span>
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
				<c:if test="${productGroup.product_group_code eq '101' }">
					<div class="datepick_txt">
						<p>5월 1일 부터</p>
						<p>양조장투어+맥주 : 22,000원  (제주맥주 샘플러 250ml*4잔) <span>*신분증 필참</span></p>
						<p>양조장투어+음료 : 19,000원  (제주 음료 제공)</p>
						<p>*옵션 변경을 희망하실 경우 예약 취소 후 재예약 하셔야 합니다.</p>
					</div>
				</c:if>
				<c:if test="${productGroup.product_group_code eq '103' }">
					<div class="datepick_txt">
						<!-- <p>5월 1일 부터</p> -->
						<p>양조장투어+맥주 : 19,000원  (제주맥주 샘플러 330ml*1잔) <span>*신분증 필참</span></p>
						<p>양조장투어+음료 : 19,000원  (제주 음료 제공)</p>
						<p>*옵션 변경을 희망하실 경우 예약 취소 후 재예약 하셔야 합니다.</p>
					</div>
				</c:if>
				<c:if test="${productGroup.product_group_code eq '106' }">
					<div class="datepick_txt">
						<p>7월12일 ~ 8월16일</p>
						<p>제주라거 무제한 : 15,000원</p>
						<p>* 일행 전원이 사전 예약하셔야 이용하실 수 있는 상품입니다.</p>
						<p>* 현장 방문 시 일행 전원 <span>*신분증 지참</span>은 필수 입니다.</p>
						<p>* 주문 시간 기준 2시간 현장 이용할 수 있으며, TAKE OUT 불가한 상품입니다.</p>
					</div>
				</c:if>
			</section>
			<section class="order-wrap time-wrap">
				<h3>
					<!-- <span class="material-icons">event_available</span> -->
					<img src="../../../resources/images/img_icon_calendar.png">
					<span class="reserve-date"></span>
				</h3>
				<div class="reserve-list th_res_list">
				
				</div>
				
				<div class="ewp_date_info">
<!-- 					<h3>· 예약 인원 관련 안내 </h3> -->
<!-- 					<p> -->
<!-- 						<font style="background-color: rgb(29, 202, 211); font-weight:bold;">최대 8인까지 입장 및 예약 가능</font><br> -->
<!-- 						한 일행 당 9인 이상은 <span style="text-decoration:underline; font-weight:bold;">따로 예약 및 동선 분리를 하셔도 입장이 절대 불가</span>하며,<Br> -->
<!-- 						현장에서 9인 이상으로 확인될 경우, <span style="text-decoration:underline; font-weight:bold;">환불은 불가하며 퇴장 조치</span> 될 수 있습니다.<br> -->
<!-- 						<br> -->
<!-- 						* 자세한 내용은 공지사항을 참고해주세요! -->
<!-- 					</p> -->
				</div>
				
			</section>
		</div>
		<form:form role="form" name="paymentInfo" id="ticket_frm"  action="/ticketing/insertReserver?content_mst_cd=${essential.content_mst_cd}&product_group_code=${essential.product_group_code}"  method="post">
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
		</form:form>
	</div><!-- mx1200 end -->
	

<script type="text/x-mustache" id="list-template">
	<ul>
	<li class="list_tit_li"><div class="ch_time">시간 선택</div><div class="ch_num">잔여석</div></li>
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
					<div class="order_spbx">
						<span data-id="{{id}}">{{name}}</span>
						<button type="button" data-id="{{id}}" id="{{id}}{{code}}" class="btn js-minus">
							<span class="material-icons">remove</span>
						</button>
					
						<div class="data-input-cnt" data-input-id="input{{id}}{{code}}">
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
						<button type="button" data-id="{{id}}" class="btn js-order">예약</button>
					{{/reserv}}
				</div>
			</div>
		</li>
	{{/data}}
	</ul>
</script>

<!-- 제주맥주 상품 추가로 인해 화면 구성 수정_2022_07_07 -->
<script type="text/x-mustache" id="list-template-for-product106">
	<ul>
	<li class="list_tit_li"><div class="ch_time">시간 선택</div><div class="ch_num" style="padding-left:64px;">잔여석</div></li>
	{{#data}}
		<li class="th_list_tem_li">
			<div data-id="item{{id}}"
				data-time="{{time}}"
				data-seat="{{seat}}"
				data-limit="{{seatlimit}}"
				data-user-seat="0"
				class="cell {{^reserv}}disable{{/reserv}}">
				<label class="time jejuRadioButton calch" style="width:153px;">
					<input type="radio" id="c{{id}}" name="cal" value="{{id}}" {{^reserv}}disabled{{/reserv}} /><span class="radio"></span>
					{{time}}
					{{scheduleNm}}
				</label>
				<div class="seat">
					<span>{{seat}}</span>
				</div>
				<div class="order" style="width:209px;">
					{{#reserv}}
					{{#product.product}}
					<div class="order_spbx">
						<span data-id="{{id}}">{{name}}</span>
						<button type="button" data-id="{{id}}" id="{{id}}{{code}}" class="btn js-minus">
							<span class="material-icons">remove</span>
						</button>
					
						<div class="data-input-cnt" data-input-id="input{{id}}{{code}}">
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
						<button type="button" data-id="{{id}}" class="btn js-order">예약</button>
					{{/reserv}}
				</div>
			</div>
		</li>
	{{/data}}
	</ul>
</script>

<script>
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
					console.log(json.schedule);
					console.log(json.products);
					var schedule = json.schedule;
					products = json.products;
					
					let productAry = {product: []};
					for(var j=0; j<products.length; j++){
						productAry.product.push({
							code:products[j].product_code,
							name:products[j].product_name,
						})
					}
					
					let dataAry = {data: []};
					var reserv = false;
					
					
					//오늘제주맥주 라는 상품분류 추가되어 분기작업 ( 회차시간에 이름(ex-17:00 델문도 김녕점)도 포함해서 해야하기때문에 ) 
					if(schedule[0].product_group_code == '106')
					{
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
								
								//추가
								scheduleNm: schedule[i].play_sequence,
								
								seat: Number(schedule[i].sumCout),
								seatlimit: Number(schedule[i].total_count),
								reserv: reserv,
								product:productAry,
							})
						}
						
						var temlate = $('#list-template-for-product106').html();
						var html = Mustache.render(temlate, dataAry);
						
						$('.reserve-list').append(html);
					}
					else
					{
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
					}
					
					
					
					
					//var temlate = $('#list-template').html();
					//var html = Mustache.render(temlate, dataAry);
					
					//$('.reserve-list').append(html);
					
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

		//예약하기
		$('.reserve-list').on('click', '.cell.active .js-order', function (e) {
			const dataID = $(e.currentTarget).data('id')
			const myseat = Number($('[data-input-id="input'+dataID+'"]').text());
			const time  = $('[data-id="item'+dataID+'"]').data('time');
			//location.href = 'reserve.html?id='+ Id +'&date=' + seletcDate + '&time=' + time + '&seat=' + myseat;
			//submit으로 변경
			$("#shop_code").val(shop_code);
			
			const Tg = $('[data-input-id="input'+Id+'"]');
			var totalFee = 0;
			for(var i=0; i<products.length; i++){
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
</script></div>


</div>
<%@ include file="../include/footer-single.jsp" %>
</body>
</html>
