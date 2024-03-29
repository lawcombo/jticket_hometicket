<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="org.apache.commons.codec.binary.Hex" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
/*
*******************************************************
* <결제요청 파라미터>
* 결제시 Form 에 보내는 결제요청 파라미터입니다.
*******************************************************
*/
com.bluecom.ticketing.domain.WebPaymentDTO webPayment = (com.bluecom.ticketing.domain.WebPaymentDTO)request.getAttribute("webPayment");
/* 
String payMethod        = webPayment.getPay_method(); 					// 결제수단
String merchantKey 		= webPayment.getMerchantKey(); 					// 상점키
String merchantID 		= webPayment.getMerchantID(); 					// 상점아이디
String goodsName 		= webPayment.getProduct_group_name().toString(); // 결제상품명
String price 			= webPayment.getFee().toString(); 				// 실제 결제 금액
String buyerName 		= webPayment.getReserverName(); 				// 구매자명
String buyerTel 		= webPayment.getReserverPhone(); 				// 구매자연락처
String buyerEmail 		= webPayment.getReserverEmail(); 				// 구매자메일주소
String moid 			= webPayment.getOrder_no(); 					// 상품주문번호	
String coupon			= webPayment.getCouponFee(); 					//쿠폰 총 금액
String fee				= webPayment.getTotal_fee().toString(); 		//총 상품 금액
 */

 
String payMethod        = webPayment.getPay_method(); 						// 결제수단 /// CARD(신용카드), BANK(계좌이체)
payMethod = payMethod.toLowerCase();
String merchantKey 		= webPayment.getMerchantKey();						// 가맹점 고유 검증키
String merchantID 		= webPayment.getMerchantID();						// 가맹점아이디
String goodsNm	 		= webPayment.getProduct_group_name().toString();	// 결제상품명
String goodsAmt			= webPayment.getTotal_fee().toString();				// 결제상품금액	
String ordNm 			= webPayment.getReserverName();						// 구매자명
String ordTel 			= webPayment.getReserverPhone();	 				// 구매자연락처
String ordEmail 		= webPayment.getReserverEmail();					// 구매자메일주소
String ordNo 			= webPayment.getOrder_no(); 						// 상품주문번호	

//결과페이지(절대경로)
//String returnURL 		= "./payResultSample.jsp";      

//운영에는 https 처리
String returnURL 		= "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ticketing/diamondbay/payResultFromKisPG"; // 결과페이지(절대경로) - 모바일 결제창 전용

/*
*******************************************************
* <해쉬암호화> (수정하지 마세요)
* SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다. 
*******************************************************
*/
/* 
DataEncrypt sha256Enc 	= new DataEncrypt();
String ediDate 			= getyyyyMMddHHmmss();	
String hashString 		= sha256Enc.encrypt(ediDate + merchantID + price + merchantKey);
*/
DataEncrypt sha256Enc 	= new DataEncrypt();
String ediDate 			= getyyyyMMddHHmmss();												// 전문 생성일시
String encData	 		= sha256Enc.encrypt(merchantID + ediDate + goodsAmt + merchantKey);	// Hash 값
System.out.println("해쉬 값 : "+encData);
%>
<%@ include file="../../include/diamondbay/header-single.jsp" %>

<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">

<style>
a.disabled {
  pointer-events: none;
  cursor: default;
}

/* kis PG 결제창 스타일 */
#mask {position:absolute;z-index:9000;background-color:#000;display:none;left:0;top:0;width:100%;height:100%;}
.window {display: none;position:fixed;top:0%;width:100%;height:100%;z-index:10000;}
.cont{width:100%;height:100%;}
</style>


<section class="dmzbt_sec bookingsec">
	<div class="dmz_list ddid_list jw_list edt_ex">
		<div class="did_list_wrap ot_dlw ewp_ot">
			<div class="bookingbt_bx">
				<div class="bookingbt_bx_in pro2 payR">
					<div class="main-sec_wrap">

						<div class="paytop">
							<!-- <p class="square_tit mt0" style="text-align:left;"><strong>결제정보</strong></p> -->
							<h1 id="container_title" class="pay_ch_tit">결제정보 확인!</h1>
							<form name="payInit" method="post" target="pay_frame">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
								<div class="tbl_frm01 tbl_wrap bookinfo">
									<table class="table table-bordered flexible-table bys_tabst bys_tabst2 etble_cus pay_rtb">
										<colgroup>
									        <col width='40%'/>
									        <col width='60%'/>
								    	</colgroup>
								    	<tbody>
											<tr style="display:none;">
												<td><span style="font-weight:bolder;">결제수단</span></td>
												<td><input type="text" id="payMethod" name="payMethod" value="<%=payMethod%>"></td>
											</tr>
											<tr style="display:none;">
												<td><span style="font-weight:bolder;">결제타입</span></td>
												<td><input type="text" id="trxCd" name="trxCd" value="0"></td><!-- 일반(0)/에스크로(1) --> 
											</tr>
											<%-- 
											<tr>
												<td><span style="font-weight:bolder;">가맹점ID</span></td>
												<td><input type="text" name="mid" value="<%=merchantID%>"></td>
											</tr>
											 --%>
											<tr>
												<td><span style="font-weight:bolder;">상품명</span></td>
												<td><input type="text" id="goodsNm" name="goodsNm" value="<%=goodsNm%>"></td>
											</tr>
											<tr>
												<td><span style="font-weight:bolder;">주문번호</span></td>
												<td><input type="text" id="ordNo" name="ordNo" value="<%=ordNo%>"></td>
											</tr>
											<tr>
												<td><span style="font-weight:bolder;">결제금액</span></td>
												<td><input type="text" id="goodsAmt" name="goodsAmt" value="<%=goodsAmt%>"></td>
											</tr>
											<tr>
												<td><span style="font-weight:bolder;">구매자명</span></td>
												<td><input type="text" id="ordNm" name="ordNm" value="<%=ordNm%>"></td>
											</tr>
											<tr>
												<td><span style="font-weight:bolder;">구매자연락처</span></td>
												<td><input type="text" id="ordTel" name="ordTel" value="<%=ordTel%>"></td>
											</tr>
											<tr>
												<td><span style="font-weight:bolder;">구매자이메일</span></td>
												<td><input type="text" id="ordEmail" name="ordEmail" value="<%=ordEmail%>"></td>
											</tr>
											<%-- 
											<tr style="display:none;">
												<td><span>returnUrl</span></td>
												<td><input type="text" name="returnUrl" value="<%=returnURL%>"></td>
											</tr>		
											 --%>
										</tbody>
									</table>
									<!-- 옵션 --> 
									<!-- 
									<input type="hidden" name="userIp"	value="0:0:0:0:0:0:0:1">
									<input type="hidden" name="mbsUsrId" value="user1234">
									<input type="hidden" name="ordGuardEmail" value="">
									<input type="hidden" name="rcvrAddr" value="서울특별시">
									<input type="hidden" name="rcvrPost" value="00100">
									<input type="hidden" name="mbsIp" value="127.0.0.1">
									<input type="hidden" name="mbsReserved" value="MallReserved">
									<input type="hidden" name="rcvrMsg" value="rcvrMsg">
									<input type="hidden" name="goodsSplAmt" value="0">
									<input type="hidden" name="goodsVat" value="0">
									<input type="hidden" name="goodsSvsAmt" value="0">
									 -->
									
									
									<input type="hidden" name="model" value="WEB">
									<input type="hidden" name="payReqType" value="1">
									<input type="hidden" name="charSet" value="UTF-8">
									
									<!-- <input type="hidden" name="period" value="별도 제공기간없음"> -->
									<!-- <input type="hidden" name="billHpFlg" value="0"> -->
									<!-- <input type="hidden" name="model" value="MOB"> -->
									<!-- <input type="hidden" name="channel" value="0001"> -->
									
									<input type="hidden" name="mid" 			value = "<%=merchantID%>" readonly>	<!-- 가맹점 ID -->
									<input type="hidden" name="returnUrl" 		value = "<%=returnURL%>"> 			<!-- 인증완료 결과처리 URL -->
									<input type="hidden" name="currencyType" 	value = "KRW">						<!-- 통화구분 -->
									
									<!-- 변경 불가능 -->
									<input type="hidden" name="ediDate" 		value = "<%=ediDate%>">				<!-- 전문 생성일시 -->
									<input type="hidden" name="encData" 		value = "<%=encData%>">				<!-- 해쉬값 -->
									
								</div>
								<div class="identitybt req payR eidt_cus qu_box">
									<p class="qu_txt">해당 내용으로 결제를 진행하시겠습니까?</p>
									<a href="#" id="payBtn" class="btn_blue">결제</a>
									<!-- <a href="#;" id="payBtn" class="btn_sty01 bg01" style="margin:15px;">결제하기</a> -->
								</div>
							</form>	
						</div>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</section>	


<div class="rad_modal" style="display: none;">
	<div class="rad_modal_content">
		<div class="rad_modal_x">
			<div id="agreementTermsOfUse-modal-section" class="md_cont">
				<c:out value="${reserveInfo.info_c }" escapeXml="false"/>
				<div class="modal_tb">
					<div class="mt_top">
						<h2>
							<strong>거래처 인증!</strong>
						</h2>
						<p class="mt_top_text">현장에서 전달 받은 <br/>거래처 코드와 사업자번호 입력하세요.</p>
					</div>
					<div class="mt_bot">
						<h3>
							거래처코드
						</h3>
							<dd class="reserve reserveDd" style="padding-top:0">
								<input type="text" name="reserver.name" class="jejuInputBox" id="custCode" minlength="3" maxlength="3" placeholder="거래처코드 3자리">
							</dd>
						<h3>
							사업자번호
						</h3>
							<dd class="reserve reserveDd" style="padding-top:0">
								<input type="text" name="reserver.name" class="jejuInputBox" id="custRegNo" minlength="5" maxlength="5" placeholder="사업자번호 맨뒤 5자리">
							</dd>
					</div>
				</div>

				<button class="md_bts" style="cursor:pointer;" onclick="checkCustId.selectCustIdCheck();">확인</button>
			</div>
		</div>
	</div>
	<div class="rad_modal_bk"></div>
</div>


<div id="mask"></div>
<div class="window">
	<div class="cont">
		<iframe id="pay_frame" name="pay_frame" style="width:100%; height:100%;" src="" marginwidth="0" marginheight="0" frameborder="no" scrolling="no"></iframe>
	</div>
</div>


<%-- 
<script>
//숫자를 3자리마다 콤마 찍기
function numberWithCommas(x) {
    x = x.toString();
    var pattern = /(-?\d+)(\d{3})/;
    while (pattern.test(x))
        x = x.replace(pattern, "$1,$2");
    return x;
}

	$(function() {
		$("input[name='ShowAmt']").val(numberWithCommas('<%=price %>'));
		$("#productFee").val(numberWithCommas('<%=fee %>'));
		$("#coupon").val("- "+numberWithCommas('<%=coupon %>'));
		
		$('#payButton').on('click', function(e) {
			$(this).addClass('disabled');
		});
		
		$("#memberTel").val(addHyphenToPhoneNumber($("#memberTel").val()));
	});
</script>
 --%>
 
</body>
</html>


<% pageContext.setAttribute("newLineChar", "\n"); %>	

<!-- 아래 script 는 PC 결제창 전용 js입니다. kis 결제 모듈 script -->
<script type="text/javascript">

//운영
//var url = "https://api.kispg.co.kr";
//테스트
var url = "https://testapi.kispg.co.kr";

$(document).ready(function() {

	//결제창 요청시 실행됩니다.
	$("#payBtn").click(function (){
		
		if($('input[name=payMethod]').val() == 'cust')
		{//거래처 예매 결제
			$(".rad_modal").fadeIn();
			
			return false;
		}
		else
		{
			//Calculate mask size
			var maskHeight 	= $(document).height();
			var maskWidth 	= $(document).width();
				
			$("#mask").fadeIn(0);
			$("#mask").fadeTo("slow", 0.6);        
			
			document.payInit.action = url + "/v2/auth";
			document.payInit.submit();
			$(".window").show();
		}
		
	});
	
});

$(function(){
	$(".rad_modal_bk").click(function(){
		$("#custRegNo").val('');
		$("#custCode").val('');
		
		$(".rad_modal").fadeOut();
	});
});
// 결제수단 - 거래처일경우 ID / PWD 검증


var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');
var checkCustId = {
		selectCustIdCheck : function(){
			
			
			const searchParams = new URLSearchParams(location.search);
			//alert(searchParams.get('contentMst_cd'));
			
			if($("#custCode").val() == "")
			{
				alert("거래처코드를 입력하세요");
				$("#custCode").focus();
				return false;
			}
			
			if($("#custRegNo").val() == "")
			{
				alert("사업자 번호 5자리를 입력하세요.");
				$("#custRegNo").focus();
				return false;
			}
			
			if($("#custRegNo").val().length < 5)
			{
				alert("사업자 번호 맨뒤 5자리를 입력하세요.");
				$("#custRegNo").focus();
				return false;
			}
			
			$.ajax({
				url : '${pageContext.request.contextPath}${pathPart}/reserverAuthentication/checkCustReserve',
				type : 'post', 
				dataType : 'json',
				data : {
					contentMstCd 	: searchParams.get('contentMst_cd'),
					custCode		: $("#custCode").val(),
					custRegNo		: $("#custRegNo").val()
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				
				success: function(data) {
					console.log(data);
					console.log(data.result);
					
					if(data.result > 0)
					{// ID, PASSWORD 검증 성공
						checkCustId.insertReserveForCust(searchParams);
					}
					else
					{// ID, PASSWORD 검증 실패
						alert("거래처코드/사업자번호가 유효하지 않습니다.");
						return false;
					}
				},
				error: function (jqXhr, textStatus, errorMessage) { // error callback 
			        alert("인증 데이터를 저장 할 수 없습니다. 반복시 관리자를 호출해 주세요.");
			      	
					//window.close();
			    }
			});
			
		},
		
		
		insertReserveForCust : function(searchParams){
			
			console.log("payMethod : " + $("#payMethod").val());
			console.log("trxCd : " + $("#trxCd").val());
			console.log("goodsNm : " + $("#goodsNm").val());
			console.log("ordNo : " + $("#ordNo").val());
			console.log("goodsAmt : " + $("#goodsAmt").val());
			console.log("ordNm : " + $("#ordNm").val());
			console.log("ordTel : " + $("#ordTel").val());
			console.log("ordEmail : " + $("#ordEmail").val());
			
			$.ajax({
				url : '${pageContext.request.contextPath}${pathPart}/ticketing/diamondbay/payResultFromCust',
				type : 'post', 
				dataType : 'json',
				data : {
					contentMstCd 	: searchParams.get('contentMst_cd'),
					custCode		: $("#custCode").val(),
					custRegNo		: $("#custRegNo").val(),
					
					payMethodStr	: $("#payMethod").val(),
					trxCd			: $("#trxCd").val(),
					goodsNm			: $("#goodsNm").val(),
					ordNo			: $("#ordNo").val(),
					goodsAmt		: $("#goodsAmt").val(),
					ordNm			: $("#ordNm").val(),
					ordTel			: $("#ordTel").val(),
					ordEmail		: $("#ordEmail").val()
						
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
					showShadow(true);
				},
				
				success: function(data) {
					
					console.log(data.result.success);
					
					if(data.result.success == '0')
					{
						alert(data.result.message);
					}
					else
					{
						alert("예매 성공!");
					}
					
					returnValuesToParentWindow(data.result);
					
				},
				complete: function(){
					showShadow(false);
					hideShadow();
				},
				error: function (jqXhr, textStatus, errorMessage) { // error callback 
			        alert("인증 데이터를 저장 할 수 없습니다. 반복시 관리자를 호출해 주세요.");
			      	
					//window.close();
			    }
			});
		}
}

//팝업창에서 부모창으로 값을 돌려줌
function returnValuesToParentWindow(result) {
	
	if(!window.opener != null && !window.opener.closed) {
		window.opener.setReturnedPayValuesFromChildWindow(result);
	}
	window.close();
}

var result = {
		success: '<c:out value="${success}" />',
		message: '<c:out value="${fn:replace(message, newLineChar, ' ')}" escapeXml="false" />',
		orderNo: '<c:out value="${orderNo}" />',
	}


const ajax = getXMLHTTPRequest();

function getXMLHTTPRequest() {
	let request = false;
	try { request = new XMLHttpRequest(); }
	catch(err01) {
		try { request = new ActiveXObject("Msxml2.XMLHTTP"); }
		catch(err02) { 
			try { request = new ActiveXObject("Microsoft.XMLHTTP"); }
			catch(err03) { request = false; }
		}
	}
	return request;
}

//결제창 종료 함수 <<수정 불가능>>
window.addEventListener("message", returnData, false);


//결제창 종료 함수 <<'returnData' 수정 불가능>>
function returnData (e){
	var frm = document.payInit;

	console.log("e.data : " + JSON.stringify(e.data));

    if (e.data.resultCode == '0000'){
    	
		receive_result(e.data.data, frm.returnUrl.value, frm.charSet.value);
    }
    else if(e.data.resultCode == 'XXXX'){ //인증실패시
        console.log("[e.data.resultCode : " + e.data.resultCode + "]");

    	alert("인증실패!!!");
    	
		var resData = e.data.data;

        alert("[RESULTCD : " + resData.resultCd + "] / [RESULT MSG : " + resData.resultMsg + "]");			
	}

	$("#mask, .window").hide();
	$("#pay_frame").attr("src", "");
}

function receive_result(data, url, charSet){
	
	//var form = $('form[role="payInit"]');
	var form = document.createElement("form");
	
	document.getElementsByTagName('body')[0].appendChild(form);
	for(var key in data){
		
		var input = document.createElement("input");
		input.name = key;
		input.type = "hidden";
		input.value = data[key];
		form.appendChild(input);
	}
	/* 
	alert('${_csrf.parameterName}');
	alert('${_csrf.token}');
	 */
	//---- 내부적 csrf값 추가 ----------------------
	var input = document.createElement("input");
	input.name = '${_csrf.parameterName}';
	input.type = "hidden";
	input.value = '${_csrf.token}';
	form.appendChild(input);
	//-------------------------------------------
	
	form.acceptCharset = charSet;
	form.action = url;
	form.method = 'post';
	
	form.submit();
}
</script>


<%!
public final synchronized String getyyyyMMddHHmmss(){
	SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	return yyyyMMddHHmmss.format(new Date());
}
// SHA-256 형식으로 암호화
public class DataEncrypt{
	MessageDigest md;
	String strSRCData = "";
	String strENCData = "";
	String strOUTData = "";
	
	public DataEncrypt(){ }
	public String encrypt(String strData){
		String passACL = null;
		MessageDigest md = null;
		try{
			md = MessageDigest.getInstance("SHA-256");
			md.reset();
			md.update(strData.getBytes());
			byte[] raw = md.digest();
			passACL = encodeHex(raw);
		}catch(Exception e){
			System.out.print("암호화 에러" + e.toString());
		}
		return passACL;
	}
	
	public String encodeHex(byte [] b){
		char [] c = Hex.encodeHex(b);
		return new String(c);
	}
}
%>