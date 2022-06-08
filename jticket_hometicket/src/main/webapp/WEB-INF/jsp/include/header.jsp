<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="robots" content="NOINDEX, NOFOLLOW">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0">
<meta name="description" content="" />
<meta name="author" content="" />
<title>나이스티켓</title>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath }' />/resources/js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="<c:out value='${pageContext.request.contextPath }' />/resources/js/bluebird.min.js"></script> <!-- 3.5.0 -->
<link href="${pageContext.request.contextPath }/resources/css/reset.css" rel="stylesheet">
<link href="${pageContext.request.contextPath }/resources/css/main.css" rel="stylesheet">
<link href="${pageContext.request.contextPath }/resources/css/bootstrap4.css" rel="stylesheet">
<link href="${pageContext.request.contextPath }/resources/css/sub.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/styles.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/common.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/custom.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/byn.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/dataTables.bootstrap4.min.css" />

<!-- <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/by.css"/> -->

<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/plugin/bootstrap-datepicker.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/d3-tip.css">
	
<!-- 탭 컨트롤 관련 -->
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/swiper-bundle.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/swiper-bundle.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/swiper-bundle.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/swiper-bundle.min.js"></script>
<!-- 탭 컨트롤 관련 -->


<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bluebird.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/fetch.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/d3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/d3-tip.js"></script>

<%-- <script type="text/javascript" src="<c:out value='${pageContext.request.contextPath }' />/resources/js/jquery-1.12.4.js"></script> --%>
<%-- <script type="text/javascript" src="<c:out value='${pageContext.request.contextPath }' />/resources/js/bluebird.min.js"></script> --%> <!-- 3.5.0 -->
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/popper.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/plugin/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/plugin/bootstrap-datepicker.ko.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/Chart.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/all.min.js"></script><!-- font-awesome -->
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/excel-download.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/barLineChart.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/lineChart.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/doughnutChart.js"></script>


</head>
<body class="dmzbody didlist sb-nav-fixed">
<nav class="sb-topnav navbar navbar-expand navbar-dark bg-nice bg-nice2">
		<div class="by_btnsar">
		   	<a class="bynmenu" id="sidebarToggle2" href="#">
			   <span></span>
			   <span></span>
			   <span></span>
			</a>
		</div>
		<div class="ml-auto d-flex align-items-center">
			<!-- 접속자 정보 -->
			<c:if test="${user ne null }">
				<div class="byn_info">
					<p>
						<c:out value='${user.dutyName }' /> <span class="byn_grcol"><c:out value='${user.userName }' /></span> 님
					</p>
				</div>
			</c:if>
			
			<!-- 로그인 / 로그아웃 -->
			<c:if test="${user eq null }">
				<a class="btn btn-light loginbt" href="${pageContext.request.contextPath }/login">
					<img src="${pageContext.request.contextPath }/resources/images/nice_login_bt_icon.png">
					<p class="login_txt">로그인</p>
				</a>
			</c:if>
			<c:if test="${user ne null }">
				<div class="byn_logout  grbg">
					<a name="logout" href="#" class="loginbt">
						<img src="${pageContext.request.contextPath }/resources/images/nice_logout_bt_icon.png">
						<span class="login_txt">LOGOUT</span>
					</a>
				</div>
			</c:if>
		</div>
		
	</nav>
	<div id="layoutSidenav">

		<div id="layoutSidenav_nav">
			<!-- <nav class="ipck2">
			   		        <a class="btn btn-link order-1 order-lg-0 sidebartg" id="sidebarToggle2" href="#">
			        <i class="fas fa-chevron-left" id="sidebartg_icon"></i>
			    </a>
			</nav> -->
			<nav class="sb-sidenav accordion sb-sidenav-light grbg" id="sidenavAccordion">
				<div class="sb-sidenav-menu">

					<div class="byn_name_tit">
						<h3><a href="<c:out value='${pageContext.request.contextPath }' />${pathPart}/sel">DMZ 평화곤돌라 시설 이용현황</a></h3>	
					</div>

					<div class="box my-4">
						<div class="box-body">
							<div class="td_ap"><p id="todayAPM">AM</p></div>
							<div class="byn_timewrp">
								<div class="byn_time_ring t12s">
									<div class="byn_time_ringin">
										<div id="todayTime" class="display-4 mb-1 text-center">10:11</div>	
									</div>
								</div>
							</div>
							<div id="todayDate" class="text-center">1월1일</div>
						</div>
					</div>
					<!-- 권한에 따라 사이드 메뉴 표시 -->
					<%@ include file="./side-menu.jsp"%>
				</div>
				<div class="sb-sidenav-footer">
					<div class="small" align="center"></div>
				</div>
			</nav>
		</div>
		<div id="layoutSidenav_content">
		

<form:form id="logoutForm" action="${pageContext.request.contextPath}/logout" method="POST">

</form:form>
<script>
	
	$("a[name='logout']").on("click", function(e){
		e.preventDefault();
		
		var form = $("#logoutForm");
		form.submit();
	});
</script>
</script>