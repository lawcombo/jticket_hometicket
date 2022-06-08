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
		    	<div class="kn_main">
		    		<!-- <div class="kn_main_tb">
		    		
		    			<div class="kmt_stit">
			    			<p>제주맥주</p>
			    			<span>X</span>
			    			<p>금능해변</p>
		    			</div>
		    			
		    			<div class="kmt_mtit">
		    				<div class="kmt_year">2021</div>
		    				<h3 class="kmt_mt">늦가을 피크닉</h3>
		    				<p class="kmt_st">제주맥주의 반려해변인 금능해변에서 즐기는 원데이 클래스</p>
		    			</div>
		    			
		    			<p class="kmt_info">
		    				11월 7일 일요일, 오후 3시 30분 - 오후 6시<br>
		    				제주맥주 양조장 &amp; 금능해수욕장
		    			</p>
		    			
		    		</div> -->
		    		<!-- 
		    		<div class="kn_main_sh">
		    			<ul>
		    				<li>
		    					<p class="kms_t">오후<br>3시 30분</p>
		    					<p>제주맥주 양조장 투어</p>
		    				</li>
		    				<li>
		    					<p class="kms_t">오후<br>4시 30분</p>
		    					<p>금능해변 정화활동</p>
		    				</li>
		    				<li>
		    					<p class="kms_t">오후<br>5시</p>
		    					<p>선셋캠핑</p>
		    				</li>
		    			</ul>
		    		</div>
		    		 -->
		    	</div>
		    </div>
		
		    <div class="body-contents-wrap pdplus">
		    	<div class="pdwrap">
			        <div class="beerwalk-desc-title">
			            <div>
			                <h2>제주맥주 X 금능해변 반려해변 프로그램</h2>
			                <h1>제주맥주 원데이 클래스</h1>
			            </div>
			        </div>
			
			        <div class="pageLine pginfo"></div>
			
			        <div class="beerwalk-desc-info mt30">
			            <div class="beerwalk-desc-info-type">
			                <div class="beerwalk-desc-info-type-cell prginfo">
			                    <dl>
			                        <dt>금액</dt>
			                        <dd>22,000원</dd>
			                    </dl>
			                    <dl>
			                        <dt>체험시간</dt>
			                        <dd>2시간</dd>
			                    </dl>
			                    <dl>
			                        <dt>포함사항 <sub style="word-break:keep-all;">(택1)</sub></dt>
			                        <dd>제주맥주 3종 샘플러 <sub>(330ml*3)</dd>
			                        <dd>제주맥주 굿즈세트</dd>
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
			                        <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_age.png"
			                             alt="key visual">
			                    </dt>
			                    <dd class="line_h">
			                    	<strong>만 7세 이상 참여가능 </strong>
			                    </dd>
			                </dl>
			            </div>
			        </div>
					
			        
			       
				</div>
		    </div>
			
			
			
		    <div class="cod102 kn_edit mgbt_50">
		        <div class="innerBox pl100 pr100">
		            <strong class="pc_colorCyan">JEJU BEER ONE-DAY CLASS</strong>
		        </div>
		        <div class="kn_sub"></div>
		        <div class="kn_sub_tb">
		        	<p>
		        		제주맥주 양조장 투어와 금능 해변 정화 활동.<br>
		        		시원한 맥주와 함께하는 금능 해변 선셋 캠핑까지!<br>
		        	</p>
		        	<p>
		        		제주맥주의 반려해변인 금능해변에서 즐기는<br>
		        		늦가을 피크닉에 여러분을 초대합니다.<br>
		        	</p>
		        </div>
		    </div>
		    
		    
		    
		    <div class="cod102 kn_edit kn_mx">
		        <div class="kn_con2_tit txtbd">
		            <p><span>제주맥주 X 금능해변</span><br>금능 해변은 제주맥주가 가꾸고 돌보는 반려 해변입니다.</p>
		        </div>
		        <div class="kn_con2_box">
		        	<div class="kn_box_con">
		        		<div class="kn_img_area kn_ia1"><img src="/resources/images/kn_jb_logo.png" alt=""></div>
		        		<div class="kn_txt_area">
		        			<h3>WHY 제주맥주 X 금능 해변?</h3>
		        			<p>
		        				<strong>제주맥주의 메인 컬러, <span class="kn_mint">민트</span><br>
		        				금능 해변에서 영감을 얻다.</strong>
		        			</p>
		        			<p>
		        				제주맥주의 메인 컬러인 민트색은<br>
		        				제주맥주 양조장 가까이에 있는 금능 해변의 색을 가져왔습니다.
		        			</p>
		        		</div>
		        	</div>
		        	
		        	<div class="kn_box_con">
		        		<div class="kn_img_area kn_ia2"></div>
		        		<div class="kn_txt_area">
		        			<h3>반려해변 프로그램이란?</h3>
		        			<p>
		        			<strong>
		        				바다를지키는 기분좋은 시도.<br></strong>
		        			</p>
		        			<p>
		        				특정해변을 기업 또는 단체가<br>
		        				자신의 반려동물처럼 아끼고 사랑하자는<br>
		        				취지에서 시작된 프로그램입니다.
		        			</p>
		        		</div>
		        	</div>
		        	
		        	<div class="kn_con2_tit">
			            <p>제주맥주의 상징, <span class="kn_mint">민트</span><br>바로 그 민트의 모티브가 된 금능 해변을 가꾸는 첫 번째 프로젝트에 동참해보세요!</p>
			        </div>
		        	
		        </div>
		    </div> 
		    
		    
		    
		    
		    <div class="cod102 kn_edit">
		        <div class="innerBox pl100 pr100">
		            <strong class="pc_colorCyan">ONE-DAY CLASS TIME TABLE</strong>
		        </div>
				<div class="kn_con3_box">
					<ul>
						<li>
							<div class="kn_con3_ib"><img src="/resources/images/kn_con_img3.png" alt=""></div>
							<div class="kn_con3_icon b_line"><img src="/resources/images/kn_con_icon1.png" alt="" class="kni_mar kni_sc"></div>
							<div class="kn_con3_tit"><h3><span class="kni_mo_num">1. </span>제주맥주 양조장 투어</h3></div>
							<div class="kn_con3_add"><p><img src="/resources/images/kn_location_icon_ewp.png" alt="" class="ewp_location_icon">제주 한림읍 금능농공길 62-11,<br>제주맥주 양조장</p></div>
							
							<div class="kn_con3_time"><p>오후 3시 30분 - 4시<br>(30분)</p></div>
							<div class="kn_con3_info"><span>제주맥주 도슨트 투어</span><br><p>재밌고 생생한 맥주 이야기!</p></div>
						</li>
						
						<li>
							<div class="kn_con3_ib"><img src="/resources/images/kn_con_img4.png" alt=""></div>
							<div class="kn_con3_icon b_line2 kn_ac"><img src="/resources/images/kn_con_icon2.png" alt="" class="kni_sc2"></div>
							<div class="kn_con3_tit"><h3><span class="kni_mo_num">2. </span>금능해변 정화 활동</h3></div>
							<div class="kn_con3_add"><p><img src="/resources/images/kn_location_icon_ewp.png" alt="" class="ewp_location_icon">제주한림읍 금능길 119-10,<br>금능해수욕장</p></div>
							
							<div class="kn_con3_time"><p>오후 4시 30분 - 5시<br>(30분)</p></div>
							<div class="kn_con3_info"><span>해변 정화 용품 제공</span><br><p>쓰레기는 치우고, 마음은 채우고!</p></div>
						</li>
						
						<li>
							<div class="kn_con3_ib"><img src="/resources/images/kn_con_img5.png" alt=""></div>
							<div class="kn_con3_icon"><img src="/resources/images/kn_con_icon3.png" alt="" class="kni_mar kni_sc"></div>
							<div class="kn_con3_tit"><h3><span class="kni_mo_num">3. </span>선셋 캠핑</h3></div>
							<div class="kn_con3_add"><p><img src="/resources/images/kn_location_icon_ewp.png" alt="" class="ewp_location_icon">제주 한림읍 금능길 119-10,<br>금능해수욕장</p></div>
							
							<div class="kn_con3_time"><p>오후 5시 - 6시<br>(30분-1시간:자유)</p></div>
							<div class="kn_con3_info"><span>미니 캠핑 용품 제공</span><br><p>타오르는 노을보며 CHEERS!</p></div>
						</li>
						
					</ul>
				</div>
		    </div>
		    
		    
		    <div class="cod102 kn_edit kn_mx">
		    	<div class="innerBox pl100 pr100">
		            <strong class="pc_colorCyan">SPECIAL GIFT</strong>
		        </div>
		        <div class="kn_con2_tit kn_tm">
		            <p>
		            	제주맥주 원데이 클래스를 수료하신 모든 분들께<br>
		            	<span class="kn_empa">수료 축하 선물</span>
		            	<span class="kn_under">(택1)</span>
		            	을 드립니다.
		            </p>
		        </div>
		        <div class="kn_goods_ib">
		        	<ul>
		        		<li class="kng_gf">
		        			<div class="kng_ib kng_gift"></div>
		        			<div class="kng_tb"><p>제주맥주 굿즈</p></div>
		        		</li>
		        		<li class="kng_sp">
		        			<span>or</span>
		        		</li>
		        		<li class="kng_gf">
		        			<div class="kng_ib kng_beer"></div>
		        			<div class="kng_tb"><p>제주맥주 샘플러(330ml*3)</p></div>
		        		</li>
		        	</ul>
		        </div>
		    </div>
		    
		    
		    <div class="cod102 kn_edit kn_mx kn_800 after_n">
		    	<div class="innerBox pl100 pr100">
		            <strong class="pc_colorCyan">Q &amp; A</strong>
		        </div>
		        <div class="kn_qa_box">
		        	<ul>
		        		<!-- <li>
		        			<p class="knq_q">
		        				Q1. 참여 기준이 어떻게 되나요?
		        			</p>
		        			<p class="knq_a">
		        				A1. 필독! 기본 신청자격<br>
		        				맥주를 마실 수 있는 20세 이상 성인 누구나 신청 가능합니다.<br>
		        				단! 1팀 당 1개의 텐트가 제공될 예정으로 원활한 텐트 사용을 위해<br>
		        				2인 1팀으로 예약하시는 것을 권장합니다
		        			</p>
		        		</li> -->
		        		
		        		<li>
		        			<p class="knq_q">
		        				Q1. 제주맥주 양조장에서 금능 해변까지 이동은 어떻게 하나요?
		        			</p>
		        			<p class="knq_a">
		        				A1. 자가용 혹은 대중교통(택시 혹은 버스 등)을 이용하여 개별 이동하는 것을 원칙으로 합니다.<br>
		        			</p>
		        		</li>
		        		
		        		<li>
		        			<p class="knq_q">
		        				Q2. 날씨가 좋지 않은 경우에는 어떻게 진행되나요?
		        			</p>
		        			<p class="knq_a">
		        				A2. 실외 활동이 포함된 프로그램으로 기상 상황에 따라 연기 혹은 취소될 수 있다는 점 양해부탁드립니다.<br>
		        				기상 악화 시, 행사 당일 오전까지의 상황을 보고 개별 연락드릴 예정입니다.
		        			</p>
		        		</li>
		        		
		        		<!-- <li>
		        			<p class="knq_q">
		        				Q4. 개인적인 사정으로 맥주를 마시지 못하는 경우에는 어떻게 하나요?
		        			</p>
		        			<p class="knq_a">
		        				A4. 개인적인 사정으로 맥주는 마시지 못하는 고객님들을 위해 제주맥주 굿즈 세트를 준비할 예정입니다.<br>
		        				마실 수 있는 음료와 다양한 제주맥주 굿즈로 세트를 구성할 예정입니다.
		        			</p>
		        		</li> -->
		        		
		        		<li>
		        			<p class="knq_q">
		        				Q3. 어린이와 함께 참여해도 되나요?
		        			</p>
		        			<p class="knq_a">
		        				A3. 투어가 포함된 프로그램이다보니 만 7세 이상의 어린이 고객님의 경우, 참여 가능합니다.
		        			</p>
		        		</li>
		        		
		        		<li>
		        			<p class="knq_q">
		        				Q4. 텐트에서 숙박도 가능할까요?
		        			</p>
		        			<p class="knq_a">
		        				A4. 숙박은 어렵습니다. 준비해드리는 텐트가 숙박용이 아닌 피크닉용이다보니 프로그램 진행 시간 내 (오후 5시 - 오후 6시)에만 사용 가능하십니다.
		        			</p>
		        		</li>
		        		
		        		<li>
		        			<p class="knq_q">
		        				Q5. 추가적인 문의 사항이 있을 때는 어디로 연락하면 되나요?
		        			</p>
		        			<p class="knq_a">
		        				A5. 제주맥주 양조장 유선 번호 (064-798-9800) 혹은 제주맥주 채널톡/카카오톡으로 문의주시면 빠르게 답변드리도록 하겠습니다.
		        			</p>
		        		</li>
		        		
		        	</ul>
		        </div>
		        
		        <div class="kn_con2_tit kn_tm kn_fot">
		            <p>
		            	제주맥주 X 금능해변, 반려해변 프로그램<br>
		            	<span class="kn_empa">제주맥주 원데이 클래스에서 곧 만나요!</span>
		            </p>
		        </div>
		        
		    </div>
		    
		    
		    
			<div class="cod102 kn_edit kn_mx kn_800">
			
		        <div class="kn_con2_tit kn_tm kn_fot kn_tog">
		            <p>
		            	함께하는 사람들
		            </p>
		        </div>
		        
		        <div class="kn_logos">
		        	<ul>
		        		<li>
		        			<img src="/resources/images/kn_fs_logo1.png" alt="">
		        		</li>
		        		<li>
		        			<img src="/resources/images/kn_fs_logo2.png" alt="">
		        		</li>
		        		<li>
		        			<img src="/resources/images/kn_fs_logo3.png" alt="">
		        		</li>
		        		<li>
		        			<img src="/resources/images/kn_fs_logo4.png" alt="">
		        		</li>
		        	</ul>
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
}); 

</script>
<%@ include file="../include/footer-single.jsp" %>
</body>
</html>
