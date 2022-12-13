<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>



<header class="header">
    <div class="header-top">
      	<!-- 
       <div class="header-top__left"></div>
        -->
        
        <div class="wrap">
	        <h2>
	        	키즈카페 홈페이지 예매
	       	</h2>
        </div>
        
		<!-- 
        <div class="header-top__right">
            <div class="header-lnb">
                <a href="">LOGIN</a>
                <a href="">JOIN</a>
                <a href="">CART</a>
                <a href="">RECRUIT</a>
                <div class="res_btn">
                	<a href="">
                		홈페이지 예매
                	</a>
                </div>
            </div>

            <button type="button"
                    class="btn js-asideMenu">
                <span class="material-icons">menu</span>
            </button>
        </div>
		-->
        
    </div>
    <!-- 
    <div class="header-gnb-top">
        <a href="">EXPERIENCE</a>
        <a href="">COLLABORATION</a>
        <a href="">SHOP</a>
        <a href="" class="active">홈페이지 예매</a>
        
    </div>
    <div class="header-gnb">
       <a href="" class="active">예매</a>
       <a href="">예매 확인 및 취소</a>
       <a href="">공지사항</a>
    </div>
     -->
    <!-- 
    <div class="header-sub">
        <a href="" class="sub-a" id="reservation">예매</a>
        <a href="" class="sub-a" id="confirm">예매 확인/취소</a>
        <a href="" class="sub-a">방문 안내</a>
    </div>
     -->
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