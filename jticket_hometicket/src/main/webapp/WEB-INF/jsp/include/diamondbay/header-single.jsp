<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="robots" content="NOINDEX, NOFOLLOW">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
<meta name="description" content="" />
<meta name="author" content="" />
<title> NICE 홈페이지 예매 </title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/jeju/html5reset-1.6.1.css" >
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/jeju/fonts.css?v=180227" >
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/jeju/style.css" >
<!-- favicon -->

<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/resources/images/company_logo.png">

<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/locale/ko.min.js" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/mustache@4.2.0/mustache.min.js" crossorigin="anonymous"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/ticketingUtils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/all.min.js"></script><!-- font-awesome -->

<style type="text/css">
/* 	@import url(/resource/css/fonts.css); */
	html * {
		font-family: 'Gotham A', 'Gotham B', 'Spoqa Han Sans';
		/* font-weight: 500; */
		letter-spacing: -0.5px;
	}
	#shadow-section {width: 100%;height: 100%;background: rgba(0, 0, 0, 0.4);position:fixed; top: 0;left: 0;z-index: 9999;display:none;}
	#shadow-section.show{display:block;}
</style>
</head>
<body>
<div id="shadow-section" class="justify-content-center align-items-center">
	<div class="ewp_center">
		<i id="spinnerIcon" class="fas fa-spinner fa-pulse fa-3x shd_icon" style="color: #fff; position:absolute !important; left:50%; top:50%; transform:translate(-50%,-50%) !important;"></i>
	</div>
</div>
<script>
// 그림자 표시
function showShadow(showIcon) {
	if(showIcon === false) {
		$("#spinnerIcon").css('display', 'none')
		$("#shadow-section").removeClass('d-flex');
	} else {
		$("#spinnerIcon").css('display', 'block');
		$("#shadow-section").addClass('d-flex');
	}
	$("#shadow-section").addClass("show");
	
}
//  그림자 삭제
function hideShadow() {
	$("#shadow-section").removeClass("show");
}
</script>