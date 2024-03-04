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
	<c:otherwise>
		<!-- 기타 -->
	</c:otherwise>
</c:choose>


<div class="app prog">
    <%@ include file="../include/top_menu.jsp" %>
    
    
    <%@ include file="../common/channelTalk.jsp" %>
    
    
    <div class="body-contents ewp_body">
    	<div class="body-contents-wrap">
		    
		    
	
		</div><!-- mx1200 end -->
		<section class="beerwalk-desc reservationRenewPage ewp_section color_ch">
			<div class="ewp_resBtn prog" style="z-index: 9">
				<button class="reserveButton">예약</button>
<!-- 				<p class="reservbt_ps"><strong>10월 8일 이전 일자 예약</strong>을 원하시면 페이지를 이동해주세요.<a href="https://brewery.jejubeer.co.kr/brewery-program/BeerWalk" target="_blank">링크</a></p> -->
			</div>
		    <div class="beerwalk-desc-intro">
		        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/beerwalk/img_hero_beerwalk_2.jpg"
		             alt=""
		             class="isPc" />
		        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/beerwalk/img_hero_2.jpg"
		             alt=""
		             class="isMb" />
		    </div>
		     
		
		    <div class="body-contents-wrap pdplus">
		        <div class="pdwrap">
			        <div class="beerwalk-desc-title">
			            <div>
			                <h2>국내 최초 체험형 투어공간</h2>
			                <h1>제주맥주 양조장 스페셜 투어</h1>
			            </div>
			        </div>
			
			        <div class="pageLine pginfo"></div>
			
			        <div class="beerwalk-desc-info mt30">
			            <!-- <div class="beerwalk-desc-info-type">
			                <div class="beerwalk-desc-info-type-cell prginfo">
			                    <dl>
			                        <dt>금액</dt>
			                        <dd>22,000원</dd>
			                    </dl>
			                    <dl>
			                        <dt>체험시간</dt>
			                        <dd>40분</dd>
			                    </dl>
			                    <dl>
			                        <dt>포함사항 <sub>(택1)</sub></dt>
			                        <dd class="pl_line">제주맥주 4종 샘플러 <sub>(250ml*4)</sub><br>또는 스파클링 음료와 제주맥주 굿즈세트</dd>
			                    </dl>
			                </div>
			            </div> -->
			            <div class="beer_table_bx">
			                	<table>
			                		<colgroup>
			                			<col width="20%">
			                			<col width="15%">
			                			<col width="*">
			                			<col width="20%">
			                		</colgroup>
			                		<thead>
			                			<tr>
			                				<th>옵션</th>
			                				<th>금액</th>
			                				<th>포함사항</th>
			                				<th>체험시간</th>
			                			</tr>
			                		</thead>
			                		<tbody>
			                			<tr>
			                				<td>양조장투어 + 맥주</td>
			                				<td>22,000</td>
			                				<td>제주맥주 4종 맥주 샘플러</td>
			                				<td >40분, 정원 12명</td>
			                			</tr>
			                		</tbody>
			                	</table>
			                	<p class="beer_table_p">
			                		*신분증 필참
			                	</p>
			                </div><!-- beer_table_bx end -->
			            
			            <div class="beerwalk-desc-info-help ps_mgt">
			                <dl>
			                    <dt>
			                        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_calendar_2.png"
			                             alt="key visual">
			                    </dt>
			                    <dd><strong>사전 예약 필수</strong></dd>
			                </dl>
			                <dl class="tableUl mt40">
			                    <dt>
			                        <img src="/resources/images/age_19_icon.png"
			                             alt="key visual">
			                    </dt>
			                    <dd><strong> 만 19세 이상
			                        참여가능 </strong></dd>
			                </dl>
			            </div>
			        </div>
					
			        <div class="pageLine pginfo mt30"></div>
			        
		        </div>
		        
		    </div>
		   
		   
			
			<div class="beerwalk-desc-help ewp_helpb">
		        <div class="innerBox pl100 pr100">
		            <strong class="ewp_str">제주맥주 양조장 투어란?</strong><br />
		            <br>
		            <div class="beerwalk-desc-help-desc">
		                제주맥주 양조장에서는<br>
		                제주맥주의 양조가들이 전세계에서 공수한<br>
		                최첨단 장비로 맥주를 양조하는 모습을<br>
		                직접 관람하실 수 있습니다.<br> <br>
		                맥주에 관련된 다양한 교육과 체험 활동을<br>
		                경험하실 수 있습니다.
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
         console.log(scroll);
         
         if (scroll > 50){
         //이벤트를 적용시킬 스크롤 높이
         console.log("Aaa")     
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
