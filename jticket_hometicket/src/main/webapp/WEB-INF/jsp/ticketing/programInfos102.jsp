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
		<section class="beerwalk-desc reservationRenewPage ewp_section">
			 <div class="ewp_resBtn prog">
				<button class="reserveButton">예약</button>
<!-- 				<p class="reservbt_ps"><strong>10월 8일 이전 일자 예약</strong>을 원하시면 페이지를 이동해주세요.<a href="https://brewery.jejubeer.co.kr/brewery-program/CreateBeerGlass" target="_blank"> 링크</a></p> -->
			</div>
			
		    <div class="beerwalk-desc-intro">
		        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_hero_createbeerglass_2.jpg"
		             alt=""
		             class="isPc" />
		             
		        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_hero_2.jpg"
		             alt=""
		             class="isMb" />
		    </div>
		
			<div class="body-contents-wrap pdplus">
		        <div class="pdwrap">
			        <div class="beerwalk-desc-title">
			            <div>
			                <h2>많고 많은 맥주잔 중, 내 맥주잔 하나는 있어야죠!</h2>
			                <h1>나만의 전용잔 만들기</h1>
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
			                				<th>금액</th>
			                				<th>체험시간</th>
			                				<th>포함사항</th>
			                				<th>정원</th>
			                			</tr>
			                		</thead>
			                		<tbody>
			                			<tr>
			                				<td>15,000원</td>
			                				<td>45분</td>
			                				<td>제주맥주 전용잔, 시음맥주 <sub>200ml</sub></td>
			                				<td >6명</td>
			                			</tr>
			                		</tbody>
			                	</table>
			                	<p class="beer_table_p">
			                		*맥주 시음 희망시 신분증 필참
			                	</p>
			                </div><!-- beer_table_bx end -->
			            
			            <div class="beerwalk-desc-info-help ps_mgt">
			                <dl style="margin-right:0; width:50%">
			                    <dt>
			                        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_calendar_2.png"
			                             alt="key visual">
			                    </dt>
			                    <dd><strong>사전 예약 필수</strong></dd>
			                </dl>
			                <dl class="tableUl mt40" style="width:50%;">
			                    <dt>
			                        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_age_all.png" alt="key visual">
			                    </dt>
			                    <dd class="line_h">
			                    	<strong> 전 연령 참여가능 </strong>
			                    	<br />
			                    	<span class="ewp_spnb">(단, 만 12세 이하의 경우 보호자 동반 필수)</span>
			                    </dd>
			                </dl>
			            </div>
			        </div>
					
			        <div class="pageLine pginfo mt30"></div>
			        
		        </div>
		        
		    </div>
		
		
		
			
<!-- 		    <div class="beerwalk-desc-help bg-gray mt50 pt50 pb50"> -->
<!-- 		        <div class="innerBox pl100 pr100"> -->
<!-- 		            <h1>알림</h1> -->
<!-- 		            <p><strong class="colorCyan mt7">사회적 거리두기 4단계 관련 안내</strong></p> -->
		
<!-- 		            <div class="beerwalk-desc-help-desc"> -->
<!-- 		                <br> -->
<!-- 		                <strong>오후 5시 이전 : 5인 이상의 일행 예약 및 참여 불가</strong><br> -->
<!-- 		                <strong>오후 5시 - 오후 6시 : 3인 이상의 일행일 경우, 오후 6시 정각에 퇴장 안내</strong><br> -->
<!-- 		                <strong>오후 6시 이후 : 3인 이상의 일행 예약 및 참여 불가</strong><br> <br> -->
<!-- 		                <div class="pre-line">모두의 안전을 위한 저희의 결정에 -->
<!-- 		                    동참해주셔서 감사합니다.</div> -->
<!-- 		            </div> -->
<!-- 		        </div> -->
<!-- 		    </div> -->
		    <div class="beerwalk-desc-help cod102">
		        <div class="innerBox pl100 pr100">
		            <strong class="pc_colorCyan">나만의 전용잔 만들기란?</strong><br />
		            <br>
		            <div class="beerwalk-desc-help-desc">
				                제주맥주 전용잔에 원하는 문구를<br>
				                직접 각인해보는 체험입니다.
		            </div>
		        </div>
		    </div>
		
		    <div class="bg-gray isPc ewp_isp mt120">
		        <div class="body-contents-wrap ewp_epdv">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_01.jpg"
		                 alt="key visual"
		                 class="contentImage">
		        </div>
		    </div>
		    
		     <div class="isMb">
		        <div class="beerwalk-desc-trip cod102">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_info_01.jpg"
		                 alt="key visual"
		                 class="imgKeyVisual">
		        </div>
		     </div>
		
		
		    <div class="beerwalk-desc-wrap isPc ewp_pcb  ewp_isp ewp_epdv">
		        <div class="beerwalk-desc-con txt_left">
		        </div>
		        <div class="ewp_imb">
		        	<div>
		            	<img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_02.jpg" alt="key visual" class="full">
		            </div>
		            <div>
		            	<img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_03.jpg" alt="key visual" class="full">
		             </div>
		        </div>
		    </div>
		
		    <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_info_02.jpg" alt="key visual" class="full mt50 isMb">
		                     
		    <div class="beerwalk-desc-wrap isMb mt75">
		        <div class="beerwalk-desc-con"></div>
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_info_03.jpg" alt="key visual" class="full">
		        <div class="beerwalk-desc-con"></div>
		    </div>
		     <div class="beerwalk-desc-wrap isMb mt20 pdnone">
		        <div class="beerwalk-desc-con"></div>
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_info_04.jpg" alt="key visual" class="full">
		        <div class="beerwalk-desc-con"></div>
		    </div>
		           
			<img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_info_05.jpg" alt="key visual" class="full mt75 isMb">
		    <div class="alignBox mAuto textAlignLeft isMb">
		    	<div class="beerwalk-desc-suggest__msg txt_left pd8">
		           누구나 손쉽게 나만의 전용잔을 만들 수 있어요!
		        </div>
		    </div>
		    
		    <div class="body-contents-wrap mt75">
		        <div class="beerwalk-desc-suggest">
		            <div class="beerwalk-desc-suggest__photo">
		                <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_info_06.jpg" alt="key visual" class="full isMb">
		            </div>
		        </div>
		    </div>
		     <div class="beerwalk-desc-wrap isMb mt75">
		        <div class="beerwalk-desc-con"></div>
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/createbeerglass/img_info_07.jpg" alt="key visual" class="full">
		        <div class="beerwalk-desc-con"></div>
		         
		    </div>
		    <div class="alignBox mAuto textAlignLeft isMb">
		    	<div class="beerwalk-desc-suggest__msg txt_left pd8">
		           	 커플이나 친구들끼리 양조장에서<br>특별한 추억을 만들고 싶으신 분께 추천드려요.<br />
		        </div>
		    </div>
		    
		    <!-- <div class="beerwalk-desc-wrap isMb">
		        <div class="beerwalk-desc-con"></div>
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_04.jpg"
		                 alt="key visual"
		                 class="full">
		        <div class="beerwalk-desc-con"></div>
		    </div>
		
		    <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_04.jpg"
		         alt="key visual"
		         class="full isMb" -->
		
		    <div class="beerwalk-desc-con isPc  ewp_isp ewp_paed"></div>
		
		    <div class="isPc  ewp_isp">
		        <div class="beerwalk-desc-wrap">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_04.jpg"
		                 alt="key visual"
		                 class="full" />
		        </div>
		    </div>
		    
		    
		    
		    <div class="bg-gray isPc ewp_medit  ewp_isp">
		        <div class="body-contents-wrap">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_05.jpg"
		                 alt="key visual"
		                 class="contentImage">
		        </div>
		    </div>
		    <div class="ewp_widt isPc">
		    	<p>누구나 손쉽게 나만의 전용잔을 만들 수 있어요!</p>
		    </div>
		    
		    
		    <div class="isPc ewp_medit  ewp_isp">
		        <div class="body-contents-wrap">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/createbeerglass/img_info_createbeerglass_06.jpg"
		                 alt="key visual"
		                 class="contentImage">
		        </div>
		    </div>
		    <div class="ewp_widt isPc">
		    	<p>
		    		커플이나 친구들끼리 양조장에서<br>
					특별한 추억을 만들고 싶으신 분께 추천드려요.
				</p>
		    </div>
		    
		    
		       
		    
		    


			
			

			
			
			
		   <!--  <div class="body-contents-wrap">
		        <div class="beerwalk-desc-suggest">
		            <div class="beerwalk-desc-suggest__photo">
		                <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/beerwalk/img_info_beerwalk_05.jpg"
		                     alt="key visual"
		                     class="full">
		            </div>
		            <div class="beerwalk-desc-suggest__msg txt_left">
		                비어 도슨트의 친절한 설명과 함께
		                맥주를 알아가는 재미에 푹 빠져보세요!
		            </div>
		        </div>
		    </div> -->
		
		
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
