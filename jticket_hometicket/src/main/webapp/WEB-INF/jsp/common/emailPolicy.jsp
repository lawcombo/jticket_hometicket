<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	   uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../include/header-single.jsp" %>


<section class="dmzbt_sec bookingsec">
	<div class="dmz_list ddid_list">
		<div class="did_list_wrap">
			<div class="booking_tit">
				<h1>이메일 무단수집 거부</h1>
			</div>
			<div class="email_wrap">
				<div class="email_bx">
					<img src="${pageContext.request.contextPath}/resources/images/email_icon.png">
					<p class="email_tit">나이스티켓은 이메일 무단수집을 거부합니다.</p>
					<p class="email_txt">본 웹사이트에 게시된 이메일 주소가 전자우편 수집 프로그램이나 그밖의 기술적인 장치를 이용하여 무단으로 수집되는 것을 거부하며,<br>이를 위반시 정보통신망법에 의해 형사처벌됨을 유념하시기 바랍니다.</p>
					<p class="email_txt">게시일 : 2021년 7월 27일</p>
				</div>
			</div>
		</div>
	<%@ include file="../include/footer-single.jsp" %>
	</div>
</section>

<script>
var chkInput = function (){
	if($('#order_num').val() == false) {
		$('#order_num').focus();
		alert("예매번호를 입력해주세요");
		return false;
	}
	
	if($('#member_name').val() == false) {
		$('#member_name').focus();
		alert("이름을 입력해주세요");
		return false;
	}
	
	if($('#phone1').val() == false || $('#phone2').val() == false || $('#phone3').val() == false) {
		$('#phone1').focus();
		alert("전화번호를 입력해주세요");
		return false;
	} else {
		$('#member_tel').val("" + $('#phone1').val() + $('#phone2').val() + $('#phone3').val())
	}
	sendAjax();
}

function sendAjax(){
	var data = {'member_tel':$("#member_tel").val(), 'member_name':$("#member_name").val(), 'order_num':$("#order_num").val(), 'contentMstCd':'<%=request.getParameter("contentMstCd")%>', 'online_channel':$("#online_channel").val() };
	$.ajax({
        url:"/ticketing/checkTicketAjax?${_csrf.parameterName}=${_csrf.token}",
        type:"POST",            
        dataType : 'json',
        data : data,
        success: function(data){
        	console.log(data);                
            if (data[0].result_code == "0") alert(data[0].result_message);
            //else location.href = '/ticketing/showTicketInfo?order_num=' + $("#order_num").val() + '&member_name=' + $("#member_name").val() + '&member_tel=' + $("#member_tel").val() + '&contentMstCd=' + $("#contentMstCd").val();  
            else {
            	$("#shopCode").val(data[0].shop_code)
            	$("#saleCode").val(data[0].sale_code);;
            	var form = document.inputForm;
        		form.submit();
            	//document.getElementsByName("inputForm").submit();
            }
        },
        error : function(xhr,status,error) {
        	console.log(xhr);
        	console.log(status);
        	console.log(error);
        }
    });
}
</script>

<%-- <div id="main-section">
	<a href="<c:url value='/login' />">로그인</a>

</div> --%>

