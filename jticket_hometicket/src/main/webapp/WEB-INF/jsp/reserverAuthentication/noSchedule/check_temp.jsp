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
	<a href="#">닫기</a>
	<form role="form">
		<input type="hidden" name="name" value="test" />
		<input type="hidden" name="phone" value="01012341234" />
		<input type="hidden" name="code" value="code1234" />
	</form>
	
	<script>
	
	// 403 에러 처리(Spring Security에서의 Ajax 호출)
	var header = $("meta[name='_csrf_header']").attr('content');
	var token = $("meta[name='_csrf']").attr('content');
	
	$.ajaxSetup({
		beforeSend: function (xhr) {
        	xhr.setRequestHeader(header, token);
 		},
	});

	
	$(function() {
		
		$('a').on('click', function(e){
			e.preventDefault();
			var form = $('form[role="form"]');
			$.ajax({
  				type : 'post',  				       
  				url : '<c:url value="/reserverAuthentication/add" />',
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
  					alert('인증이 완료되었습니다.');
  					returnValuesToParentWindow(data);
  					window.close();
  				},
  				error: function (jqXhr, textStatus, errorMessage) { // error callback 
  			        alert("인증 데이터를 불러올 수 없습니다. 반복시 관리자를 호출해 주세요.");
  			      	returnValuesToParentWindow(null);
  					window.close();
  			    }
  			});
		})
		$('a').click();
	})
	

	</script>
<%@ include file="../include/footer-single.jsp" %>