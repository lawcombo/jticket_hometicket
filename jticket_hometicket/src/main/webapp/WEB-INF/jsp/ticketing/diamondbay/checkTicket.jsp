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

	String sSiteCode 		= (String) request.getAttribute("siteCode");			// NICE로부터 부여받은 사이트 코드
	String sSitePassword 	= (String) request.getAttribute("sitePassword");		// NICE로부터 부여받은 사이트 패스워드
	
	String sRequestNumber 	= "REQ0000000001";			// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
														// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
	sRequestNumber = niceCheck.getRequestNO(sSiteCode);
	session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
	
	String sAuthType 	= "";			// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
	
	String popgubun 	= "N";			//Y : 취소버튼 있음 / N : 취소버튼 없음
	String customize 	= "";			//없으면 기본 웹페이지 / Mobile : 모바일페이지
	
	String sGender 		= ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자 
	
	// CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
	//리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : https://www.~ 리턴 url : https://www.~
	String sReturnUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/success?content_mst_cd=" + ((com.bluecom.ticketing.domain.EssentialDTO)request.getAttribute("essential")).getContent_mst_cd();     // 성공시 이동될 URL
	String sErrorUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reserverAuthentication/fail?content_mst_cd=" + ((com.bluecom.ticketing.domain.EssentialDTO)request.getAttribute("essential")).getContent_mst_cd();         // 실패시 이동될 URL
	
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
<%@ include file="../../include/diamondbay/header-single.jsp" %>

<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">

<div class="app">
	<%-- <%@ include file="../include/top_menu.jsp" %> --%>
	
	
	
	<div style="background-image: url('${pageContext.request.contextPath }/resources/images/diamondbay/visual1.jpg'); height:300px;"> 
		<div style="position:absolute; top:10%; left:19%;">
			<img src="${pageContext.request.contextPath }/resources/images/diamondbay/visual_title.png" lat="DIAMOND BAY">
		</div>
	</div>
	

	<!-- 뒤로가기 버튼 -->
	<%-- 
	<div style="text-align:left; padding-top:10px; padding-left:10px;">
		<img src="${pageContext.request.contextPath }/resources/images/diamondbay/backImg.png" onclick="history.back()" style="cursor: pointer; width: 50px;" />
	</div>
	 --%>
	
	
	<section class="head">
		<div class="intro res_tit">
			<h1>
				다이아몬드베이<br>
				예매 확인 / 취소
			</h1>
		</div>
	</section>
	<div class="conts_box ewp_checkBox res_bot_box">
		<section class="reserve reserve-form">
			<div class="verticalAlignMiddle">
				<dl class="reserveDl full mt50">
					<dt class="reserveDt">
						<p class="res_tb">
							예매시 인증을 진행하셨던<span class="ewp_block_m"></span> 휴대폰 번호를 입력해주세요.<br>
							예매 내역을 확인하실 수 있습니다.
							<br><br>
							예매 정보 확인 후<span class="ewp_block_m"></span> 예매 취소를 하실 수 있습니다.
							<br>
							<span style="color:red; font-size:smaller">(거래처로 예매하셨을 경우에는 예매취소가 불가능하니다.)</span>
						</p>
					</dt>
				</dl>
				
				<button type="button" id="reserverAuthenticationButton" class="buttonTypeCyan full textLarge">예매취소 본인인증</button>
				
				<div>
					<button type="button" class="buttonTypeCyan full textLarg" style="cursor:pointer;" onclick="goReserve.itemSelect();">예매하러 가기</button>
				</div>
				
				<!-- 본인인증이 없을때, 안될때 아래 주석 풀고 사용 -->
				<!-- 
				<div class="ewp_certi">
					<dl class="reserveDl full mt20">
						<dt class="reserveDt">신청자명 <span class="require"></span></dt>
						<dd class="reserveDd"><input type="text" name="userName" class="jejuInputBox" id="userName"></dd>
					</dl>
					
					<dl class="reserveDl full mt20">
						<dt class="reserveDt">핸드폰번호 <span class="require"></span></dt>
						<dd class="reserveDd"><input type="text" name="userPhone" class="jejuInputBox" id="userPhone"></dd>
					</dl>
				</div>
				
				<button type="button" id="reserveCheck" class="buttonTypeCyan full textLarge" onclick="check.reserveCheck();">예매확인</button>
				 -->
				
			</div>
		</section>
		<form name="form_chk" method="post">
			<input type="hidden" name="m" value="checkplusService">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
			<input type="hidden" name="EncodeData" value="<%= sEncData %>">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
		</form>
		
		<form role="addReserverAuthenticationResult">
			<input type="hidden" name="name" value="" />
			<input type="hidden" name="phone" value="" />
			<input type="hidden" name="code" value="" />
			<input type="hidden" name="authNo" value="" />
			<input type="hidden" name="responseSEQ" value="" />
		</form>
		<form:form name='inputForm' method="get">
			<input type='hidden' name='shop_code' id='shopCode'>
			<input type='hidden' name='sale_code' id='saleCode'>

			<input type='hidden' name='member_tel' id='member_tel'>
			<input type='hidden' name='member_name' id='member_name'>

			<input type="hidden" name="order_num" id="order_num">
			<input type="hidden" name="type" id="dataType" >
			<input type="hidden" name="content_mst_cd" value='<%=request.getParameter("content_mst_cd")%>' />
		</form:form>
	</div>
</div>

<%-- <%@ include file="../include/footer-single.jsp" %> --%>
<%@ include file="../../include/diamondbay/footer-single.jsp" %>
</body>
</html>
<script>
//403 에러 처리(Spring Security에서의 Ajax 호출)
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$.ajaxSetup({
	beforeSend: function (xhr) {
		xhr.setRequestHeader(header, token);
		},
});

var length = 0;

$(function(){
	$("#confirm").attr('class', 'sub-a active');
	
	/***** 본인확인 *****/
	$("#reserverAuthenticationButton").on('click', function(e) {
		showShadow(true);
		fnReserverAuthenticationPopup();
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
		
		var data = {'member_tel': result.phone, 'member_name':result.name, 'content_mst_cd':'<%=request.getParameter("content_mst_cd")%>'};
		$.ajax({
			url:"/ticketing/checkTicketAjax?${_csrf.parameterName}=${_csrf.token}",
			type:"POST",            
			dataType : 'json',
			data : data,
			success: function(data){
				console.log(data);
				length = data.length;
				if (data[0].result_code == "0"){
					alert('입력하신 신청자명 및 핸드폰번호에 해당하는 예약 정보가 없습니다.');
				}
				else{
					
					if(length == 1){ //예약이 1건만 존재하는 경우
						$("#shopCode").val(data[0].shop_code);
						$("#saleCode").val(data[0].sale_code);
						$("#order_num").val(data[0].order_num);
						$("#dataType").val(0);
					}
					else if(length > 1){ //예약이 n건 존재하는 경우
						$("#dataType").val(1);
						$("#shopCode").val(data[0].shop_code);
					}
					
					$("#member_tel").val(result.phone);
					$("#member_name").val(result.name);
					
					var form = document.inputForm;
					form.action="/ticketing/prevShowTicket";
					form.submit();
				}
			},
			error : function(xhr,status,error) {
				console.log(xhr);
				console.log(status);
				console.log(error);
			},
			complete : function() {
				showShadow(false);
				hideShadow();
            }
		});
	} else {
		alert('본인인증에 실패하였습니다.');
	}
	
}

// 본인인증 return 핸드폰번호가 안넘어와서 일단 입력받은 핸드폰번호, 이름으로 예매조회하기.
// 다이아몬드베이는 본인인증 성공시 리턴 핸드폰번호 필수로 신청안되어있음.
var check = {
		reserveCheck : function(){
			
			
			if($("#userName").val() == "")
			{
				alert("이름을 입력하세요.");
				$("#userName").focus();
				return false;
			}
			
			if($("#userPhone").val() == "")
			{
				alert("핸드폰번호를 입력하세요.");
				$("#userPhone").focus();
				return false;
			}
			
			var data = {'member_tel': $("#userPhone").val(), 'member_name':$("#userName").val(), 'content_mst_cd':'<%=request.getParameter("content_mst_cd")%>'};
			
			console.log(data);
			
			$.ajax({
				url:"/ticketing/checkTicketAjax?${_csrf.parameterName}=${_csrf.token}",
				type:"POST",            
				dataType : 'json',
				data : data,
				success: function(data){
					console.log(data);
					length = data.length;
					if (data[0].result_code == "0"){
						alert('입력하신 신청자명 및 핸드폰번호에 해당하는 예약 정보가 없습니다.');
					}
					else{
						
						console.log(data[0]);
						
						if(length == 1){ //예약이 1건만 존재하는 경우
							$("#shopCode").val(data[0].shop_code);
							$("#saleCode").val(data[0].sale_code);
							$("#order_num").val(data[0].order_num);
							$("#dataType").val(0);
						}
						else if(length > 1){ //예약이 n건 존재하는 경우
							$("#dataType").val(1);
							$("#shopCode").val(data[0].shop_code);
						}
						
						$("#member_tel").val(data[0].member_tel);
						$("#member_name").val(data[0].member_name);
						$("#order_num").val(data[0].order_num);
						
						var form = document.inputForm;
						form.action="/ticketing/prevShowTicket";
						form.submit();
					}
				},
				error : function(xhr,status,error) {
					console.log(xhr);
					console.log(status);
					console.log(error);
				},
				complete : function() {
					showShadow(false);
					hideShadow();
	            }
			});
		}
}

var goReserve = {
		itemSelect : function(){
			var newURL = window.location.protocol + "//" + window.location.host + "/ticketing/diamondbay?content_mst_cd=DIAMONDBAY_0_1";
			 window.location.href=newURL;
		}
}

</script>