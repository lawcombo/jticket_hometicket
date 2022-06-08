<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>



<header class="header">
    <div class="header-top">
        <div class="header-top__left"></div>
        <h1><a href="https://jejubeer.co.kr/"><img alt="제주맥주" src="https://cdn.imweb.me/thumbnail/20200721/275273a70cc40.png" /></a></h1>
        <div class="header-top__right">
            <div class="header-lnb">
                <a href="https://jejubeer.co.kr/login?back_url=L2JyZXdlcnktcHJvZ3JhbQ%3D%3D&used_login_btn=Y">LOGIN</a>
                <a href="https://jejubeer.co.kr/site_join_type_choice?back_url=L2JyZXdlcnktcHJvZ3JhbQ%3D%3D">JOIN</a>
                <a href="https://jejubeer.co.kr/shop_cart">CART</a>
                <a href="https://sites.google.com/view/jejubeer-recruit/%EC%A0%9C%EC%A3%BC%EB%A7%A5%EC%A3%BC%EB%8B%A4%EC%9B%80" target="_blank">RECRUIT</a>
                <div class="res_btn">
                	<a href="https://jejubeer.co.kr/brewery-program">
                		양조장예약
                	</a>
                </div>
            </div>

            <button type="button"
                    class="btn js-asideMenu">
                <span class="material-icons">menu</span>
            </button>
        </div>
    </div>
    <div class="header-gnb-top">
        <!-- <a href="https://jejubeer.co.kr/diversity">DIVERSITY</a> -->
        <a href="https://jejubeer.co.kr/mycampingcar">EXPERIENCE</a>
        <a href="https://jejubeer.co.kr/2021barrelseries">COLLABORATION</a>
        <a href="https://jejubeer.co.kr/shop">SHOP</a>
        <a href="https://jejubeer.co.kr/brewery" class="active">BREWERY</a>
        
    </div>
    <div class="header-gnb">
       <a href="https://jejubeer.co.kr/brewery-intro">양조장 소개</a>
       <a href="https://jejubeer.co.kr/brewery-program" class="active">예약</a>
       <a href="https://jejubeer.co.kr/brewery-news">공지사항</a>
    </div>
    <div class="header-sub">
        <a href="https://jejubeer.co.kr/brewery-program" class="sub-a" id="reservation">프로그램 예약</a>
        <a href="/ticketing/checkTicket?content_mst_cd=JEJUBEER_0_1" class="sub-a" id="confirm">예약 확인/취소</a>
        <a href="https://jejubeer.co.kr/brewery-visit" class="sub-a">방문 안내</a>
    </div>
</header>

<script>

$(function() {
	var path = window.location.pathname;
	
	$('.header .header-sub .sub-a').removeClass('active');
	
	if(path == '/ticketing/programInfos' 
			|| path == '/ticketing/selectSchedule'
			|| path == '/ticketing/insertReserver') {
		$('#reservation').addClass('active');
	} else if(path == '/ticketing/checkTicket' 
			|| path == '/ticketing/showTicketInfoList'
			|| path == '/ticketing/showTicketInfo') {
		$('#confirm').addClass('active');
	} 
});
</script>