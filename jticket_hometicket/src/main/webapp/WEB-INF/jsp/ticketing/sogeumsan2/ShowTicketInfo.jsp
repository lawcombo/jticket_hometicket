<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<%@ include file="../../include/sogeumsan/header-single2.jsp" %>

<c:set var="isUsed" value="false" />
<c:set var="isRefunded" value="false" />
<c:set var="now" value="<%=new java.util.Date() %>" />
<fmt:formatDate var="today" value="${now }" pattern="yyyy-MM-dd"/>

<div class="app">
	<%-- 
	<%@ include file="../include/top_menu.jsp" %>
	 --%>
	<section class="head">
		<div class="intro res_tit">
			<h1>
				소금산 밸리 예매<br>
				확인/취소
			</h1>
		</div>
	</section>
	<div class="conts_box">
		<section class="reserve reserve-form ewp_show_ex">
			<div class="verticalAlignMiddle">
				<c:forEach var="product" items="${dataList}" varStatus="status">
					<ul class="reserveDl full mt50 ewp_show_wrapper">
						<li class="reserveDt mb50">
							<p class="sh_tit">
								신청자 명
							</p>
							<p class="sh_text">
								${product.person_name}
							</p>
						</li>
						<li class="reserveDt mb50">
							<p class="sh_tit">
								상품그룹명
							</p>
							<p class="sh_text">
								${product.product_group_name}
							</p>
						</li>
						<li class="reserveDt mb50">
							<p class="sh_tit">
								상품명
							</p>
							<p class="sh_text">
								<c:forEach var="purchase" items="${purchase}">
									${purchase.product_name} ${purchase.actual_quantity} 명<br> 
								</c:forEach>
							</p>
						</li>
						<li class="reserveDt ewp_date_box mb50">
							<div class="ewp_db_lef">
								<p class="sh_tit">
									예약일시 <input type="hidden" id="playDate" value="${product.play_date}" />
								</p>
								<p class="sh_text">
									${product.play_date} ${product.schedule_text}
								</p>
							</div>
							<div class="ewp_ecb">
								<div class="cb_top">
									<div class="calender_box">
										<section class="calendar">
											<div id="datepicker"></div>
										</section>
										<section class="order-wrap time-wrap">
											<h3>
												<img src="../../../../resources/images/img_icon_calendar.png">
												<span class="reserve-date"></span>
											</h3>
											<div class="reserve-list chang"></div>
										</section>
									</div>
								</div>
								<div class="cb_bot"></div>
							</div>
						</li>
						<li class="reserveDt mb50">
							<p class="sh_tit">
								총 인원
							</p>
							<p class="sh_text">
								${product.actual_quantity} 명
							</p>
						</li>
						
						<li class="reserveDt mb50">
							<p class="sh_tit">
								핸드폰 번호
							</p>
							<p class="sh_text">
								<span id="memberTelSpan">${product.person_mobile_no}</span>
							</p>
						</li>
						<li class="reserveDt mb50">
							<p class="sh_tit">
								결제 상품금액
							</p>
							<p class="sh_text">
								<fmt:formatNumber type="number" maxFractionDigits="3" value="${product.product_fee}"/> 원
							</p>
						</li>
						<%-- 
						<li class="reserveDt mb50">
							<p class="sh_tit">
								쿠폰 적용금액
							</p>
							<p class="sh_text">
								- <fmt:formatNumber type="number" maxFractionDigits="3" value="${coupon[0].couponFee}"/> 원
							</p>
						</li>
						 --%>
						<li class="reserveDt mb50">
							<p class="sh_tit">
								총 결제금액
							</p>
							<p class="sh_text">
<%-- 								<fmt:formatNumber type="number" maxFractionDigits="3" value="${product.product_fee - coupon[0].couponFee}"/> 원 --%>
								<fmt:formatNumber type="number" maxFractionDigits="3" value="${product.real_fee}"/> 원
							</p>
						</li>
						<li class="reserveDt">
							<p class="sh_tit">
								결제일자
							</p>
							<p class="sh_text">
								${product.sale_date}
							</p>
						</li>
					</ul>
					<dl class="reserveDl full mt50 ewp_show_bot_ex">
						<div class="ewp_show_bot" style="max-width:initial; width:fit-content;">
							<c:choose>
								<%-- <c:when test="${(product.quantity eq product.actual_quantity) && (product.real_fee eq (product.product_fee - coupon[0].couponFee)) }"> --%>
								<c:when test="${product.quantity eq product.actual_quantity}">
									<div>
										<button class="buttonTypeCyan full textLarge ewp_bot_ok" style='<c:out value="${isUsed eq true || isRefunded eq true ? 'pointer-events: none;background-color:#ddd' : '' }" />; cursor:pointer;' onmouseover="this.style.background='#2be6ed'" onmouseout="this.style.background='#05cdd5'" onclick="history.back()" >예매내역 가기</button>
									</div>
									<div>
										<button id="cancelButton" class="buttonTypeCyan full textLarge ewp_bot_ok" style='<c:out value="${isUsed eq true || isRefunded eq true ? 'pointer-events: none;background-color:#ddd' : '' }" />; cursor:pointer; width:285px;' onmouseover="this.style.background='#2be6ed'" onmouseout="this.style.background='#05cdd5'">예매 취소</button>
									</div>
								</c:when>
								<c:otherwise>
									<h4>부분 인원 취소 또는 부분 환불이 진행된 상품의 경우 관리자를 통해서만 취소 가능합니다.</h4>
								</c:otherwise>
							</c:choose>	
						</div>
					</dl>
					<div class="pageLine"></div>
					<!-- 
					<ul class="reserveDl full mt50 ewp_show_wrapper show_bot_tb">
						<li class="reserveDt">
							<h3>
								[이용안내]
							</h3>
							<p>
								<span>
									프로그램 참여 전, 양조장 안내데스크 3층에서 예약 확인이 필요합니다.
								</span>
								<span>
									양조장 당일예약/ 전화예약은 불가능하며,<br>
								    당일 잔여 인원에 한해 현장 결제 순으로 예약 가능합니다.
								</span>
								<span>
									프로그램 정원은 조정이 불가능합니다.
								</span>

							</p>
						</li>
						<li class="reserveDt sbt_bot">
							<h3>
								[주의사항]
							</h3>
							<p>
								<span>
									미취학 아동 참여 불가<br>
									미취학 아동의 경우 프로그램 참여가 제한되므로 양해 부탁드립니다.<br>
									3층 펍, md 샵은 부모동반 하에 자유롭게 이용 가능합니다.
								</span>
								<span>
									당일 10분 이상 지각 참여 및 환불 불가<br>
									양조장 투어, 테이스팅 클래스, 푸드페어링 클래스, 캔들만들기 클래스는<br>
									시작 10분 후에는 프로그램 참여가 불가능하며, 지각으로 인한 환불은 불가능합니다.
								</span>
								<span>
									정원 조정 불가<br>
									프로그램의 정원 추가는 불가능합니다.
								</span>
							</p>
						</li>
					</ul>
					 -->
					<input type="hidden" id="product_group_code" value="${product.product_group_code}" />
					<input type="hidden" id="product_code" value="${product.product_code}" />
					<input type="hidden" id="quantity" value="${product.quantity}" />
					<input type="hidden" id="fee" value="${product.product_fee}" />
					<input type="hidden" id="org_schedule_code" value="${product.schedule_code}" />
					<input type="hidden" id="org_play_date" value="${product.play_date}" />
				</c:forEach>
			</div>
		</section>
		<div id="hidden-section" style="display:none;">
			<!-- 취소 form -->
			<form:form role="cancel" action="/ticketing/cancelTicket" method="POST">
				<input type="hidden" name="order_num" value="${buyerInfo.order_num }" />
				<input type="hidden" name="content_mst_cd" value="${buyerInfo.content_mst_cd }" />
				<input type='hidden' name='shop_code' id="product_shop_code" value="${buyerInfo.shop_code }">
				<input type='hidden' name='sale_code' value="${buyerInfo.sale_code }">
				<input type='hidden' name='member_tel' value="${buyerInfo.member_tel }">
				<input type='hidden' name='member_name' value="${buyerInfo.member_name }">
				<input type="hidden" name="type" id="partialCancelCode" /> <!-- 부분취소 여부 -->
				<%-- 
				<c:forEach var="list" items="${coupon}" varStatus="status">
					<input type="hidden" name="coupon[${status.index }].cpm_num" value="${list.cpm_num}">
					<input type="hidden" name="coupon[${status.index }].company_code" value="${list.company_code}">
					<input type="hidden" name="coupon[${status.index }].cpm_cpn_code" value="${list.cpm_cpn_code}">
					<input type="hidden" name="coupon[${status.index }].cpm_use_info" value="고객요청취소">
				</c:forEach>
				 --%>
			</form:form>
			<!-- 변경 form -->
			<form:form role="modify" action="/ticketing/modifyTicket" method="POST">
				<input type="hidden" name="payMethod" value="${paymentInfo.payMethod }" />
				<input type="hidden" name="order_num" value="${buyerInfo.order_num }" />
				<input type="hidden" name="productGroup.content_mst_cd" value="${buyerInfo.content_mst_cd }" />
				<input type='hidden' name='productGroup.shop_code' value="${buyerInfo.shop_code }">
				<input type='hidden' name='sale_code' value="${buyerInfo.sale_code }">
				<input type='hidden' name='member_tel' value="${buyerInfo.member_tel }">
				<input type='hidden' name='member_name' value="${buyerInfo.member_name }">
				<input type="hidden" name="productGroup.product_group_code" id="productGroup_code" />	
				<input type="hidden" name="products[0].shop_code" id="product_shop_code" value="${products[0].shop_code }" />
				<input type="hidden" name="products[0].product_group_code" value="${products[0].product_group_code }" />
				<input type="hidden" name="products[0].product_code" value="${products[0].product_code }" />
				<input id="productCountInput" type="hidden" name="products[0].count"  />
				<input type="hidden" name="visitorType" value="${paymentInfo.visitorType }" />	
				<input type="hidden" name="totalFee"  id="totalFee"/>	
				<input type="hidden" name="totalCount" id="totalCount"/>	
				<input type="hidden" name="schedule_code" id="schedule_code" />	
				<input type="hidden" name="play_date" id="play_date" />
			</form:form>

		</div>
		<script type="text/x-mustache" id="list-template">
	<ul>
	 	<li class="list_tit_li shtic"><div class="ch_time">시간 선택</div><div class="ch_num">잔여석</div></li>
			{{#data}}
		<li>
			<div data-id="item{{id}}"
				data-time="{{time}}"
				data-seat="{{seat}}"
				data-limit="{{seatlimit}}"
				data-user-seat="0"
				class="cell {{^reserv}}disable{{/reserv}}">
				<label class="time">
					<input type="radio" id="c{{id}}" name="cal" value="{{id}}" {{^reserv}}disabled{{/reserv}} />
					{{time}}
				</label>
				<div class="seat">
					<span>{{seat}}</span>
				</div>
			</div>
		</li>
		{{/data}}
	</ul>
</script>
	</div>
</div>

<script>
	let selectDate = '';
	var min = '';
    var max = '';
    var arrDate = new Array();
	
    //  달력 option
    $.datepicker.setDefaults({
        dateFormat: 'yy-mm-dd',
        minDate: new Date(moment().format('YYYY-MM-DD')),
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
    
$(function() {
	$("#confirm").attr('class', 'sub-a active');
	
	var playDate = $("#playDate").val();
	$('#cancelButton').on('click', function(e){
		e.preventDefault();
		var text;
		if(todayDate > playDate)
		{
			alert("행사 날짜가 지났습니다. 예매취소가 불가능합니다.");
			return false;
		}
		else if(todayDate == playDate)
		{
			alert("행사 당일은 취소가 불가능합니다.");
			return false;
		}
		else
		{
			var yesterday = new Date(playDate);
			yesterday.setDate(yesterday.getDate()-1);
			yesterday = yesterday.toJSON().split("T");
			yesterday = yesterday[0];
			 
			
			
			//=======================================================================
			//취소하고자 하는 날이 예매한날(행사일) 하루 전일 경우
			if(todayDate == yesterday)
			{
				var todayTest 	= new Date();
				var todayHours 	= todayTest.getHours();
				
				//행사일 전날 오후 18시 이전까지만 환불 가능
				if(todayHours >= 18)
				{
					alert("취소 마감시간(행사일 기준 전일 18시) 이후에는 티켓의 환불이 불가합니다.");
					return false;
				}
				else
				{
					text = "현재 예매를 취소하시겠습니까?";
					$("#partialCancelCode").val(0);
				}
			}
			//=======================================================================			
			else
			{
				//if(todayDate <= yesterday)
				if(todayDate < yesterday)
				{ //100% 환불
					text = "현재 예매를 취소하시겠습니까?";
					$("#partialCancelCode").val(0);
				}
				else
				{
					alert("취소 진행이 불가능합니다.");
					return false;
				}
				 /* else if(todayDate > yesterday2[0]){ //50% 환불
					text = "환불 규정이 적용되어 50% 환불 가능합니다. 취소하시겠습니까?";
					$("#partialCancelCode").val(1);
				} */
			}
			
		}
		
		if(confirm(text)) {
			var form = $('form[role="cancel"]');
			form.submit();
		}
	});
	
	<c:if test="${not empty message}">
		alert('<c:out value="${message}" />');
	</c:if>
	
	$(".ewp_db_rig").click(function(){
		<c:if test="${today eq dataList[0].play_date}">
			alert('예약 당일에는 변경 또는 취소가 불가능합니다.');	
		</c:if>
		<c:if test="${today ne dataList[0].play_date}">
			$(".ewp_ecb").css("display","block");
			$(this).find(".db_rig_chan").css("display","none");
			$(this).find(".db_rig_cant").css("display","block");
		</c:if>
		
		return false;
	});

	$(".db_rig_cant").click(function(){
		$(".ewp_ecb").css("display","none");
		$(".db_rig_cant").css("display","none");
		$(this).siblings(".db_rig_chan").css("display","block");
		return false;
	});
	
	var product_group_code=$("#product_group_code").val();
	$("#productGroup_code").val(product_group_code);
    var shop_code = $("#product_shop_code").val();
    var quantity = $("#quantity").val();
    $("#productCountInput").val(quantity);
    $("#totalFee").val($("#fee").val());
    $("#totalCount").val(quantity);
    //var fee = '${productGroup.product_min}';
    
    var org_play_date=null;
    var org_schedule_code=null;
	// 예약 불러오기 (회차정보 return json)
    const getDateReserve = function (targetDate) {
    	selectDate = targetDate;
    	var purc = new Array();
    	<c:forEach items="${purchase}" var="item">
    	purc.push({product_code: "${item.product_code}"});
    	</c:forEach>
    	
    	var newDate = targetDate.split("-");
    	var reserveDate = newDate[0]+"년 "+newDate[1]+"월 "+newDate[2]+"일";
        $('.reserve-date').text(reserveDate);
        $("#play_date").val(targetDate);
		org_schedule_code = $("#org_schedule_code").val();
		org_play_date = $("#org_play_date").val();
        var data = {'shop_code':shop_code, 'contentMstCd':'<%=request.getParameter("content_mst_cd")%>', 'product_group_code':product_group_code, 'play_date' : selectDate};
		$.ajax({
            url:"/ticketing/selectScheduleAjax?${_csrf.parameterName}=${_csrf.token}",
            type:"POST",            
            dataType : 'json',
            data : data,
            success: function(json){
            	let dataAry = {data: []};
            	var reserv = false;
            	var scdAry = json.schedule;
            	var pdAry = json.products;
            	var noScd = 0;
            	for(var i=0; i<pdAry.length; i++){
            		for(var y=0; y<purc.length; y++) {
            			if(pdAry[i].product_code == purc[y].product_code) {
            				noScd++;
            			}
            		}
            	}
            	if(noScd == 0) {
       				alert("해당상품은 예매 변경할 수 없는 날짜입니다.");
       				return;
            	}
                for(var i=0; i<scdAry.length; i++){
                	if(scdAry[i].sumCout <= 0){
                		reserv = false;
                		scdAry[i].sumCout = 0;
                	}else{
                		reserv = true;
                	}
                	if((scdAry[i].schedule_code == org_schedule_code) && (targetDate == org_play_date)){
                		reserv = false;
                	}
                	dataAry.data.push({
                        id: scdAry[i].schedule_code,
                        time: scdAry[i].subject_text,
                        seat: Number(scdAry[i].sumCout),
                        seatlimit: Number(scdAry[i].total_count),
                        reserv: reserv,
                    })
                }
                //console.log(dataAry);
                
                var temlate = $('#list-template').html();
            	var html = Mustache.render(temlate, dataAry);
                $('.reserve-list').append(html);
                
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
        $('.cell [data-input-id]').text(1);
        $('[data-id="item'+ e.currentTarget.value +'"]').addClass('active');
        $("#schedule_code").val(e.currentTarget.value);
        $(".ewp_ecb").css("display","none");
		$(".db_rig_cant").css("display","none");
		$(".db_rig_chan").css("display","block");
    });

    // seat 제거
    $('.reserve-list').on('click','.js-minus', function (e){
        e.preventDefault();
        const Id = $(e.currentTarget).data('id')
        const Tg = $('[data-input-id="input'+Id+'"]')

        let NowNum = (Number(Tg.text()) > 1)? Number(Tg.text()) -1 : 1;
        Tg.text(NowNum);
        $('#productCountInput').val(NowNum);
        $("#totalCount").val(NowNum);
        $("#totalFee").val(NowNum*fee);
    });

    // seat 추가
    $('.reserve-list').on('click','.js-plus', function (e){
        e.preventDefault();

        const Id = $(e.currentTarget).data('id');
        const Tg = $('[data-input-id="input'+Id+'"]');
        const useSeat =  Number($('[data-id="item'+Id+'"]').data('seat')); //500
        const limit = Number($('[data-id="item'+Id+'"]').data('limit')); //500

        let NowNum = (Number(Tg.text()) < limit - useSeat) || (limit - useSeat == 0)? Number(Tg.text()) +1 : limit - useSeat;
        if(NowNum > 4) NowNum=4;
        if(NowNum > useSeat) NowNum = useSeat; 
        Tg.text(NowNum);
        $('#productCountInput').val(NowNum);
        $("#totalCount").val(NowNum);
        $("#totalFee").val(NowNum*fee);
    });

  	//예약 하기 클릭
    $('#confirmButton').on('click', function (e) {
    	
    	<c:if test="${today eq dataList[0].play_date}">
			alert('예약 당일에는 변경 또는 취소가 불가능합니다.');
			return false;
		</c:if>
		<c:if test="${today ne dataList[0].play_date}">
			var play_date = $("#play_date").val();
	    	var schedule_code = $("#schedule_code").val();
	    	if((org_play_date == play_date) && schedule_code == ''){
	    		alert("변경할 일자 및 회차를 선택해주세요.");
	    		return false;
	    	}else if((org_play_date == play_date) && (org_schedule_code == schedule_code)){
	    		alert("변경할 일자 및 회차를 선택해주세요.");
	    		return false;
	    	}
	    	
	    	if (confirm("예약을 변경하시겠습니까?") == true){    //확인
	    		$(this).attr('disabled', true);
	            
	            var form = $('form[role="modify"]');
	    		form.submit();
	    	 }else{   //취소
	
	    	     return false;
	
	    	 }
		</c:if>
    });
    
    //예매 오픈기간 가져오기
    var todayDate = moment().format('YYYY-MM-DD');
    var min = todayDate;
    var data = {'shop_code':'<%=request.getParameter("shop_code")%>', 'content_mst_cd':'<%=request.getParameter("content_mst_cd")%>', 'date':todayDate, 'product_group_code': '<c:out value="${productGroup.product_group_code}" />'  };
	$.ajax({
        url:"/ticketing/ajaxMonthData?${_csrf.parameterName}=${_csrf.token}",
        type:"POST",            
        dataType : 'json',
        data : data,
        success: function(data){
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
                beforeShowDay: available,
            });
        },
        error : function(xhr,status,error) {
        	console.log(xhr);
        	console.log(status);
        	console.log(error);
        }
    });
 	// 오늘 일정불러오기
//     getDateReserve(min);
 	
 	// 전화번호에 하이픈 넣기
    $("#memberTelSpan").text(addHyphenToPhoneNumber($("#memberTelSpan").text()));
});

	//특정일 선택막기
	function available(date) {
		var thismonth = date.getMonth()+1;
		var thisday = date.getDate();
	
		if(thismonth<10){
			thismonth = "0"+thismonth;
		}
	
		if(thisday<10){
			thisday = "0"+thisday;
		}
	    ymd = date.getFullYear() + "-" + thismonth + "-" + thisday;
	
	    if ($.inArray(ymd, arrDate) >= 0) {
	        return [true,"",""];
	    } else {
	        return [false,"",""];
	    }
	}

</script>
<%@ include file="../../include/sogeumsan/footer-single2.jsp" %>
</body>
</html>