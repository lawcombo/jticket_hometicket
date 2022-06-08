<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title>NICEPAY PAY RESULT(UTF-8)</title>
<meta charset="utf-8">
</head>
<body>
<% pageContext.setAttribute("newLineChar", "\n"); %>	
<script>

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
	
	if(result.success != 1){
		alert(result.message);
	}
	
	returnValuesToParentWindow(result);

</script>
</body>
</html>