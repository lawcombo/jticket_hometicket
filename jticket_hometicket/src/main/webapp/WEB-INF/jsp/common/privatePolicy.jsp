<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	   uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../include/header-single.jsp" %>


<section class="dmzbt_sec reserved bok_res">
	<div class="dmz_list ddid_list">
	<div class="reservedbgbx">
		<div class="did_list_wrap">
			<h1 class="tith1">예매자 정보 입력</h1>
			<div id="main-section">
			<!-- <div id="select-section" > -->
				<form:form name='inputForm' action='/ticketing/showTicketInfo' method="post">
					<div>					
						<input type='hidden' name='contentMstCd' id='contentMstCd' value='<%=request.getParameter("contentMstCd")%>'>
						<input type='hidden' name='online_channel' id='online_channel' value='<%=request.getParameter("online_channel")%>'>	
						<input type='hidden' name='type' id='type' value='<%=request.getParameter("type")%>'>	
						<input type='hidden' name='shopCode' id='shopCode'>
						<input type='hidden' name='saleCode' id='saleCode'>
						<input type='hidden' name='member_tel' id='member_tel'>
						<%-- <input type='text' name='sale_code' id='sale_code' value='${sale_code}'> --%><br>						
						
						<div class="social_txtbx">
							<div class="username">
								<label for="order_num">예매 번호</label>
								<input type='text' name='order_num' id='order_num' placeholder='예매번호를 입력해주세요' required class="required"><br>
							</div>
							<div class="username">
								<label for="member_name">예매자 성함</label>
								<input type='text' name='member_name' id='member_name' placeholder='이름을 입력해주세요' required class="required"><br>
							</div>
							<div class="userphonnum">
								<label for="order_num">휴대폰 번호</label>
								<input type="text" name="phone1" id="phone1" title="연락처" maxlength="3" value="" required class="required"> -
								<input type="text" name="phone2" id="phone2" title="연락처" maxlength="4" value="" required class="required"> -
								<input type="text" name="phone3" id="phone3" title="연락처" maxlength="4" value="" required class="required">
							</div>
							
						
							<!--  <input type='submit' name='submitBt' value='승인'>  -->						
							
						</div>
						<div class="product_btns reserv">
<%-- 							<a href="/ticketing/process<c:out value='${reserver.type }' />/showTicketInfo">예매자 정보 확인</a> --%>
							<a href="javascript:chkInput();">예매자 정보 확인</a>
						</div>
					</div>
				</form:form>
				
			<!-- </div> -->
			</div>
		</div>
	</div>
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

<%@ include file="../include/footer-single.jsp" %>