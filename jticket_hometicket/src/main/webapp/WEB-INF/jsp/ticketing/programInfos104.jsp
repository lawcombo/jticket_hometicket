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
			 <div class="ewp_resBtn prog">
				<button class="reserveButton">예약</button>
<!-- 				<p class="reservbt_ps"><strong>10월 8일 이전 일자 예약</strong>을 원하시면 페이지를 이동해주세요.<a href="https://brewery.jejubeer.co.kr/brewery-program/CreateBeerGlass" target="_blank"> 링크</a></p> -->
			</div>
			
		    <div class="beerwalk-desc-intro">
		    	<div class="bt_main">
		    	</div>
		    	<img src="/resources/images/bt_main_bg_mb01_mob.png" alt="" class="bt_main_mob">
		    </div>
		
		    <div class="body-contents-wrap pdplus">
		    	<div class="pdwrap">
			        <div class="beerwalk-desc-title">
			            <div>
			                <h2>커피와 맥주, 그리고 미식에 진심인<br> 찐-팬들을 위한 투어</h2>
			                <h1>배럴 테이스팅 투어</h1>
			            </div>
			        </div>
			
			        <div class="pageLine pginfo"></div>
			
			        <div class="beerwalk-desc-info mt30">
			            <div class="beerwalk-desc-info-type">
			                <div class="beerwalk-desc-info-type-cell prginfo">
			                    <dl>
			                        <dt>금액</dt>
			                        <dd>28,000원</dd>
			                    </dl>
			                    <dl>
			                        <dt>체험시간</dt>
			                        <dd>양조장 투어 30분</dd>
			                        <dd>배럴시리즈 테이스팅 20분</dd>
			                    </dl>
			                    <dl>
			                        <dt>포함사항 <sub style="word-break:keep-all;"></sub></dt>
			                        <dd>제주맥주 샘플러<sub> (시음필수)</sub></dd>
			                        <dd>굿즈세트로 교환 불가</dd>
			                    </dl>
			                </div>
			            </div>
			            <div class="beerwalk-desc-info-help ps_mgt">
			                <dl>
			                    <dt>
			                        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_calendar_2.png"
			                             alt="key visual">
			                    </dt>
			                    <dd class="line_h"><strong>사전 예약 필수</strong></dd>
			                </dl>
			                <dl class="tableUl mt40">
			                    <dt class="child_7">
			                        <img src="/resources/images/age_18_icon.png"
			                             alt="key visual">
			                    </dt>
			                    <dd class="line_h">
			                    	<strong>만 18세 이상 참여가능 </strong>
			                    </dd>
			                </dl>
			            </div>
			        </div>
					
			        
			       <div class="pageLine pginfo" style="margin-top:32px;"></div>
				</div>
		    </div>
			
			
			
		    <div class="cod102 kn_edit mgbt_50 bt_intro">
		        <div class="beerwalk-desc-title bt_txt_area">
		            <div>
		            	<h1>배럴 테이스팅 투어란?</h1>
		                <h2>
		                	2021년,<br>
		                	배럴 시리즈 : 블루보틀 커피 에디션을 출시하며<br>
		                	이를 기념하기 위해 아주 특별한 시간을 준비했어요.
		                </h2>
		            </div>
		        </div>
		    </div>
		    
		    
		    <div class="cod102 kn_edit mgbt_50 bt_inner bt_after">
				<div class="bt_2img_area">
					<div>
						<img src="/resources/images/bt_intro_img1.png" alt="">
						<div class="bt_prog_intro">
							<h3>PROGRAM 1</h3>
							<p>제주맥주 양조장 투어 : 30분</p>
						</div>
					</div>
					<div>
						<img src="/resources/images/bt_intro_img2.png" alt="">
						<div class="bt_prog_intro">
							<h3>PROGRAM 2</h3>
							<p>배럴 시리즈 테이스팅 세션 : 20분</p>
						</div>
					</div>
				</div>
		    </div>
		    
		    
		    <div class="cod102 kn_edit mgbt_50 bt_intro bt_inner">
		    	<div class="bt_detail">
			        <div>
			        	<div class="bt_det_tb">
			        		<h3>
			        			PROGRAM 1
			        			<span>제주맥주 양조장 투어</span>
			        		</h3>
			        		<p>
			        			제주맥주 양조장에서는 제주맥주의 양조가들이<br>
			        			전세계에서 공수한 최첨단 장비로 맥주를 양조하는 모습을 직접 관람하실 수 있습니다.
			        		</p>
			        	</div>
			        	<div class="bt_det_ib bt_after">
			        		<div>
			        			<img src="/resources/images/bt_det_img1.png" alt="">
			        		</div>
			        		<div>
			        			<img src="/resources/images/bt_det_img2.png" alt="">
			        		</div>
			        	</div>
			        </div>
			        
			        <div>
			        	<div class="bt_det_tb">
			        		<h3>
			        			PROGRAM 2
			        			<span>배럴 시리즈 테이스팅</span>
			        		</h3>
			        		<p>
			        			배럴 시리즈 블루보틀 커피 에디션의 깊은 풍미를 제대로 음미하는 법을 알아보는 시간입니다.<br>
			        			도슨트의 안내에 따라 눈으로 감상하고, 향을 들이마시고, 입에 머금고, 맛을 느껴보세요.
			        		</p>
			        	</div>
			        	
			        	<div class="bt_det_ib">
			        		<div>
			        			<img src="/resources/images/bt_det_img3.png" alt="">
			        		</div>
			        		<div>
			        			<img src="/resources/images/bt_det_img4.png" alt="">
			        		</div>
			        	</div>
			        </div>
		        </div>
		        
		    </div>
		    
		    
		    <div class="cod102 kn_edit mgbt_50 bt_intro bt_series">
		    	<div class="bt_detail bt_inner">
			        <div>
			        	<div class="bt_det_tb">
			        		<h3>
			        			제주맥주 배럴 시리즈: 블루보틀 커피 에디션
			        		</h3>
			        		<p>
			        			2021년, 블루보틀과 제주맥주가 만나<br>
			        			시간, 노력 그리고 커피의 조화로 탄생한<br>
			        			제주맥주 배럴 시리즈 : 블루보틀 커피 에디션을 소개합니다.
			        		</p>
			        	</div>
			        	<div class="bt_series_det">
			        		<div class="bt_sd_ib">
			        			<img src="/resources/images/bt_series_img.png" alt="">
			        		</div>
			        		<div class="bt_sd_tb">
			        			<p>
			        				제주맥주 배럴 시리즈: 블루보틀 커피 에디션은<br>
			        				버번 배럴 속에서 6개월 이상 숙성한<br>
			        				진한 임페리얼 스카우트에<br>
			        				블루보틀 대표 블렌드인 '벨라 도노반'을<br>
			        				드라이 호핑 기법으로-더해 완성했습니다.<br>
			        				<br>
			        				블루보틀 '벨라 도노반'의 초콜릿, 라즈베리 향미와<br>
			        				버번 배럴, 로스팅 몰트에서 오는 묵직한 아로마가<br>
			        				조화롭게 어우러지며 섬세하고<br>
			        				복합적인 풍미를 만들어 냅니다.
			        			</p>
			        			
			        			<div class="sd_tb_bot">
			        				<div class="stb_top">
			        					<h3>BEER GUIDE</h3>
			        					<p>
			        						STYLE : BA Imperial Stout<br>
			        						VOL : 750 mL<br>
			        						AVB : 13.5 %<br>
			        						IBU : 30<br>
			        						Serving Temperature : 7-13℃
			        					</p>
			        				</div>
			        				<div class="stb_bot">
			        					<h3>TASTING NOTES</h3>
			        					<p>
			        						Dark Chocolate, Butterscotch, Vanilla,<br>
			        						Dried Fruits, Red &amp; Black Berries
			        					</p>
			        				</div>
			        			</div>
			        			
			        		</div>
			        	</div>
			        </div>
		        </div>
		    </div>
		    
		    <div class="bt_inner">
		    	<p class="bt_warning">*경고 : 지나친 음주는 뇌졸중, 기억력 손상이나 치매를 유발합니다. 임신 중 음주는 기형아 출생 위험을 높입니다.</p>
		    </div>
		    
		    
		    
			
		
		
		    
		
		
		    
		    
		
		</section>
		
	</div>


</div>

<script>
$(function() {
	$(".reserveButton").on('click', function() {
		window.location.href="/ticketing/selectSchedule?content_mst_cd=${essential.content_mst_cd}&product_group_code=${essential.product_group_code}"
	});
}); 

</script>
<%@ include file="../include/footer-single.jsp" %>
</body>
</html>
