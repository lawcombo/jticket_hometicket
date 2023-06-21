<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="checkReservationModal" class="ewp_certify_modal">
	<p class="close_w">
		<button class="popCloseBtnCmmn" type="button" data-num="1">
			<img src="/resources/noSchedule/images/close_bth.jpg" alt="" />
		</button>
	</p>
	<h3>예약확인 및 취소</h3>
	<button id="callAuthenticationButton" class="ewp_certify_btn">본인인증하기</button>
	<!-- <button id="checkReservationCloseButton">닫기</button> -->
</div>

<div id="check-reservation-authentication-section" style="display: none;">
	<!-- 본인인증 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
	<form name="checkReservationAuthentication" method="post">
		<input type="hidden" name="m" value="checkplusService">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
		<input type="hidden" name="EncodeData" value="<c:out value='${selfAuthentication.encData }' />">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
	</form>

	<form id="checkTicketAjax" role="checkTicketAjax" >
		<input type="hidden" id="checkTicketAjaxMemberName" name="member_name" value="" />
		<input type="hidden" id="checkTicketAjaxMemberTel" name="member_tel" value="" />
		<input type="hidden" id="checkTicketContentMstCd" name="content_mst_cd" value="<c:out value='${shopInfo.content_mst_cd }' />" />
		<input type="hidden" name="today" value="<c:out value='${today }' />" />
	</form>
</div>
<script>

/************* 예약 확인 *************/

var checkTicketContentMstCd = '<c:out value='${shopInfo.content_mst_cd}' />';	
if(!checkTicketContentMstCd) {
	checkTicketContentMstCd = '<c:out value='${paymentInfo.content_mst_cd}' />';
}


var isModalOn = false;

// 그림자 없애기
function hideModal() {
	if(isModalOn === true) {
		$("#checkReservationModal").css('display', 'none');
		hideShadow();
		isModalOn = false;
	}
}

//모달 쉐도우 보이기
function showModel() {
	if(isModalOn === false) {
		$("#checkReservationModal").css('display', 'block');
		showShadow(false);
		isModalOn = true;
	}
}

$(function() {
	
	
	
	// 예매확인취소 버튼 클릭
	$("#checkReservationOpenButton").on("click", function() {
		showModel();
	});
	
	
	// 모달 닫기 버튼 클릭
	$("#checkReservationCloseButton").on('click', function() {
		hideModal();
	});
	
	// 모달팝업시 그림자 영역 클릭
	$(".close_w").on('click', function() {
		hideModal();	
	});
	
	
	// 본인인증 버튼 클릭
	$("#callAuthenticationButton").on("click", function() {
		fnCheckTicketReserverAuthenticationPopup();
	})
});

/***** 본인인증 팝업 *****/
function fnCheckTicketReserverAuthenticationPopup(){
	window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.checkReservationAuthentication.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
	document.checkReservationAuthentication.target = "popupChk";
	document.checkReservationAuthentication.submit();
}

// 본인확인 결과
var setReturnedCheckReservationAuthenticationValuesFromChildWindow = function (result) {
	if(result.success == 0) { // 정상
		console.log(result);
		$("#checkTicketAjaxMemberName").val(result.name);
		$("#checkTicketAjaxMemberTel").val(result.phone);
		$("#checkTicketContentMstCd").val(checkTicketContentMstCd);

		var form = $('form[role="checkTicketAjax"]');
		$.ajax({
			type : 'post',  				       
			url : '<c:url value="/ticketing/mcycamping/checkTicketAjax" />',
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
				
				if(!data || data.length <= 0) {
					alert('예매이력이 없습니다.');
					hideModal();
					return;
				}
				
				
				
				window.location = "<c:url value='/ticketing/mcycamping/prevShowTicket' />?content_mst_cd=" + checkTicketContentMstCd + "&member_name=" 
						+ data[0].member_name + "&member_tel=" +  data[0].member_tel + "&today=<c:out value='${today}' />&sale_code=" + data[0].sale_code;
				
			},
			error: function (jqXhr, textStatus, errorMessage) { // error callback 
		        alert("예매확인 페이지를 불러올 수 없습니다. 반복시 관리자를 호출해 주세요.");
		      	
				window.close();
		    }
		});
	} else {
		alert('본인인증에 실패하엿습니다.');
	}
}


</script>