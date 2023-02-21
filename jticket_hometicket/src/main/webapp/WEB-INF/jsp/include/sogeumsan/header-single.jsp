<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator"%>
<!DOCTYPE html>
<html lang="ko">
<head>


<!-- Google Tag Manager -->
<script>
	(function(w, d, s, l, i) {
		w[l] = w[l] || [];
		w[l].push({
			'gtm.start' : new Date().getTime(),
			event : 'gtm.js'
		});
		var f = d.getElementsByTagName(s)[0], j = d.createElement(s), dl = l != 'dataLayer' ? '&l='
				+ l
				: '';
		j.async = true;
		j.src = 'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
		f.parentNode.insertBefore(j, f);
	})(window, document, 'script', 'dataLayer', 'GTM-WDMRHB3');
	
</script>
<!-- End Google Tag Manager -->

<!-- Meta Pixel Code -->
<meta name="facebook-domain-verification" content="3toq05w41ncmv46nuweff2jcwbrkvw" />
<script>
!function(f,b,e,v,n,t,s)
{if(f.fbq)return;n=f.fbq=function(){n.callMethod?
n.callMethod.apply(n,arguments):n.queue.push(arguments)};
if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';
n.queue=[];t=b.createElement(e);t.async=!0;
t.src=v;s=b.getElementsByTagName(e)[0];
s.parentNode.insertBefore(t,s)}(window, document,'script',
'https://connect.facebook.net/en_US/fbevents.js');
fbq('init', '435941708651194');
fbq('track', 'PageView');
</script>

<noscript>
	<img height="1" width="1" style="display: none" src="https://www.facebook.com/tr?id=435941708651194&ev=PageView&noscript=1" />
</noscript>
<!-- End Meta Pixel Code -->





<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="robots" content="NOINDEX, NOFOLLOW">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>소금산 그랜드밸리</title>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-214554947-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-214554947-1');
</script>

<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/styles.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/common.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/single.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/byn.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/dataTables.bootstrap4.min.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/reservation.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/noSchedule/css/custom.css" />
<link href="/resources/noSchedule/css/sub.css" rel="stylesheet">
<link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">

<!-- <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" /> -->
<script src="${pageContext.request.contextPath }/resources/noSchedule/js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/popper.min.js"></script>
<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> -->

<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/Chart.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/all.min.js"></script><!-- font-awesome -->
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/moment.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/resources/noSchedule/js/Nmenu.js"></script>
<script src="https://kit.fontawesome.com/2400ef6752.js" crossorigin="anonymous"></script>
<script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
<style>
#shadow-section {width: 100%;height: 100%;background: rgba(0, 0, 0, 0.4);position:fixed; top: 0;left: 0;z-index: 998;display:none;}
#shadow-section.show{display:block;}
</style>
</head>
<body>
<div id="shadow-section" class="justify-content-center align-items-center">
	<div class="ewp_center">
		<i id="spinnerIcon" class="fas fa-spinner fa-pulse fa-3x shd_icon" style="color: #fff"></i>
	</div>
</div>

<script>
// 그림자 표시
function showShadow(showIcon) {
	if(showIcon === false) {
		$("#spinnerIcon").css('display', 'none')
	} else {
		$("#spinnerIcon").css('display', 'block')
	}
	$("#shadow-section").addClass("show");
}

//  그림자 삭제
function hideShadow() {
	$("#shadow-section").removeClass("show");
}
</script>


<!-- header area_Start -->

	<div id="layerPop"></div>
	<div class="hd_w">
	
		<!--모바일 카테고리-->
		<div class="mobileCategory ewp_mobileCategory">
			<article class="mobileNav">
				<div>
					<div class="top">
						<a href="#;" class="close">닫기</a>
					</div>
					<!--gnb-->
					
				</div>
			</article>
			<div class="m_bg"></div>
		</div>
		<!--//모바일 카테고리-->
	</div><!-- //hd_w -->

<!-- header area_End -->

