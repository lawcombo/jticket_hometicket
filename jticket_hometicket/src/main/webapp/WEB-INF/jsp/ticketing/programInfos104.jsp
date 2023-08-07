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

<!-- 필요할 때 사용 -->
<c:choose>
	<c:when test="${essential.product_group_code eq '101' }">
		<!-- 투어 -->
	</c:when>
	<c:when test="${essential.product_group_code eq '102' }">
		<!-- 체험 -->
	</c:when>
	<c:when test="${essential.product_group_code eq '103' }">
		<!-- 금능해변 -->
	</c:when>
	<c:otherwise>
		<!-- 기타 -->
	</c:otherwise>
</c:choose>


<div class="app prog ftcolor">
    <%@ include file="../include/top_menu.jsp" %>
    <div class="body-contents ewp_body">
    	<div class="body-contents-wrap">
	
		</div><!-- mx1200 end -->
		<section class="beerwalk-desc reservationRenewPage ewp_section color_ch">
			 <div class="ewp_resBtn prog" style="z-index:1;">
				<button class="reserveButton">예약</button>
<!-- 				<p class="reservbt_ps"><strong>10월 8일 이전 일자 예약</strong>을 원하시면 페이지를 이동해주세요.<a href="https://brewery.jejubeer.co.kr/brewery-program/CreateBeerGlass" target="_blank"> 링크</a></p> -->
			</div>
			
		    <div class="beerwalk-desc-intro">
		    	<div class="bt_main">
		    	</div>
		    	<img src="/resources/images/jeju/000000.png" alt="" class="bt_main_mob">
		    </div>
		
		    <div class="body-contents-wrap pdplus">
		    	<div class="pdwrap">
			        <div class="beerwalk-desc-title">
			            <div>
			                <h2>맛있는 맥주 문화 만들기 part.1</h2>
			                <h1>비어 테이스팅 클래스</h1>
			            </div>
			        </div>
			
			        <div class="pageLine pginfo"></div>
			
			        <div class="beerwalk-desc-info mt30 info102">
			            <div class="beerwalk-desc-info-type">
			                <div class="beerwalk-desc-info-type-cell prginfo">
			                    <dl>
			                        <dt>금액</dt>
			                        <dd>28,000원</dd>
			                    </dl>
			                    <dl>
			                        <dt>체험시간</dt>
			                        <dd>1시간</dd>
			                    </dl>
			                    <dl>
			                        <dt>포함사항 <sub style="word-break:keep-all;"></sub></dt>
			                        <dd>비어테이스팅 노트</dd>
			                        <dd>제주맥주 볼펜</dd>
			                        <dd>시음맥주 500ml <sub>(제주위트 or 제주누보)</sub></dd>
			                    </dl>
			                </div>
			            </div>
			            <div class="beerwalk-desc-info-help ps_mgt">
			                <div class="row">
				                <dl>
				                    <dt>
				                        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_calendar_2.png"
				                             alt="key visual">
				                    </dt>
				                    <dd class="line_h"><strong>사전 예약 필수</strong></dd>
				                </dl>
			                </div>
			                <div class="row">
				                <dl class="tableUl mt40">
				                    <dt class="child_7">
				                        <img src="/resources/images/age_19_icon.png"
				                             alt="key visual">
				                    </dt>
				                    <dd class="line_h">
				                    	<strong>만 19세 이상 참여가능 </strong>
				                    </dd>
				                </dl>
			                </div>
			            </div>
			        </div>
			       <div class="pageLine pginfo" style="margin-top:32px;"></div>
				</div>
		    </div>
			
			
			
		    <div class="cod102 kn_edit mgbt_50 bt_intro" style="margin-top:0">
		        <div class="beerwalk-desc-title bt_txt_area">
		            <div>
		            	<h1 style="color:#1ecad3">비어 테이스팅 클래스?</h1>
		                <h2>
		                	제주맥주 비어도슨트의 안내로 양조장 시설을<br>
		                	둘러보고 맥주 테이스팅 방법을 배울 수 있습니다.
		                </h2>
		            </div>
		        </div>
		    </div>
		    
		    
		    <div class="cod102 kn_edit mgbt_50 bt_intro bt_inner">
		    	<div class="bt_detail">
			        <div>
			        	<div class="bt_after">
			        		<div>
			        			<img src="/resources/images/jeju/111111.png" alt="">
			        		</div>
			        	</div>
			        </div>
		        </div>
		    </div>
		    
		    <div class="cod102 kn_edit mgbt_50 bt_intro bt_inner">
		    	<div class="bt_detail bt_after">
		    		<div>
			        	 <div class="ewp_imb2">
				        	<div style="padding-left:10px;padding-right:10px">
				            	<img src="/resources/images/jeju/5DSC07674.JPG" alt="key visual" class="full">
				            </div>
				            <div style="padding-left:10px;padding-right:10px">
				            	<img src="/resources/images/jeju/1DSC07468.JPG" alt="key visual"s class="full">
				             </div>
				             <div style="padding-left:10px;padding-right:10px">
				            	<img src="/resources/images/jeju/4DSC07532.JPG" alt="key visual" class="full">
				             </div>
				        </div>
			        </div>
		    	</div>
		    </div>
		    
		    <div class="cod102 kn_edit mgbt_50 bt_intro bt_inner">
		    	<div class="bt_detail">
					<div>
			        	 
			        	<div>
			        		<div>
			        			<img src="/resources/images/jeju/222222.png" alt="">
			        		</div>
			        	</div>
			        </div>
		    	</div>
		    </div>
		</section>
		
	</div>


</div>

<script>
$(function() {
	$(".reserveButton").on('click', function() {
		window.location.href="/ticketing/selectSchedule?content_mst_cd=${essential.content_mst_cd}&product_group_code=${essential.product_group_code}"
	});
	
	var lastScroll = 0;
    $(window).scroll(function(){
         var scroll = $(this).scrollTop();
         
         if (scroll > 50){
         //이벤트를 적용시킬 스크롤 높이
         $(".ewp_resBtn.prog").addClass("fixed_a");
         if($(".ewp_resBtn.prog").hasClass("fixed_a"))
        	 {
        		 $(".footer").addClass("mt")
        	 }
         }
         else {
              $(".ewp_resBtn.prog").removeClass("fixed_a");
         }
         lastScroll = scroll;
    });
}); 

</script>
<%@ include file="../include/footer-single.jsp" %>
</body>
</html>
