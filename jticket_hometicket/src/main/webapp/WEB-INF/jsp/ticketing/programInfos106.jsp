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
	<div class="body-contents ewp_body">
		<div class="body-contents-wrap">
	
		</div><!-- mx1200 end -->
		<section class="beerwalk-desc reservationRenewPage ewp_section ewp_section_0">
		<div class="ewp_resBtn prog">
			<button class="reserveButton">예약</button>
<!-- 				<p class="reservbt_ps"><strong>10월 8일 이전 일자 예약</strong>을 원하시면 페이지를 이동해주세요.<a href="https://brewery.jejubeer.co.kr/brewery-program/BeerWalk" target="_blank">링크</a></p> -->
		</div>
		<div class="beerwalk-desc-intro">
			<img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/beerwalk/img_hero_beerwalk_2.jpg" alt="" class="isPc" />
			<img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/beerwalk/img_hero_2.jpg" alt="" class="isMb" />
		</div>
		
		<div class="body-contents-wrap pdplus">
			<div class="pdwrap">
				<div class="beerwalk-desc-title">
					<div>
						<h2>단체투어 예약</h2>
						<h1>제주맥주 양조장 투어</h1>
					</div>
				</div>
			<div class="pageLine pginfo"></div>
				<div class="beerwalk-desc-info mt30">
					<div class="beerwalk-desc-info-type">
						<div class="beerwalk-desc-info-type-cell prginfo">
							<dl>
								<dt>금액</dt>
								<dd>무료 (22,000원 선 결제 후 방문 시 환불)</dd>
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
					</div>
					<div class="beerwalk-desc-info-help ps_mgt">
						<dl>
							<dt><img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_calendar_2.png" alt="key visual"></dt>
							<dd><strong>사전 예약 필수</strong></dd>
						</dl>
						<dl class="tableUl mt40">
							<dt><img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/img_icon_age.png" alt="key visual"></dt>
							<dd><strong> 만 7세 이상 참여가능 </strong></dd>
						</dl>
					</div>
				</div>
				<div class="pageLine pginfo mt30"></div>
			</div>
		</div>
		<div class="beerwalk-desc-help mt50 pt50 pb50 ewp_helpa">
			<div class="innerBox pl100 pr100">
				<div class="beerwalk-desc-help-desc">
					<strong class="ewp_str">필독사항</strong><br />
					<br>
					<div class="beerwalk-desc-help-desc">
						본 투어는 싱가폴 공영방송 “채널 아시아“ 뉴스 프로그램 촬영의 일환으로 진행되어,<br>
						취재팀이 동행합니다.<br>
						<br>
						본 투어 예약/참가자는 사진 및 촬영, 초상권 활용에 대해 동의한 것으로 간주합니다.<br>
						또한, 투어 종료 이후 간단한 인터뷰를 요청드릴 수 있습니다.<br>
						예약 시, 참고 부탁드립니다.
					</div>
				</div>
			</div>
		</div>
		<div class="beerwalk-desc-help mt50 pt50 pb50 ewp_helpa">
			<div class="innerBox pl100 pr100">
				<div class="beerwalk-desc-help-desc">
					<div class="beerwalk-desc-help-desc">
					<strong class="ewp_str">참여 방법</strong><br />
					<br>
					<div class="beerwalk-desc-help-desc">
						1. 2월 10일 (목요일) 12:30 투어를 원하는 인원수에 맞춰 예약한다.<br>
						2. 1인당 22,000원(기존 투어 가격)을 결제한다.<br>
						3. 당일 현장 방문 시, 사진 및 영상 촬영에 대한 동의서 작성 후 전액 환불받는다.<br>
						4. 투어에 참여하고 포함사항을 제공 받는다.
					</div>
				</div>
				</div>
			</div>
		</div>
<!-- 		    <div class="beerwalk-desc-help bg-gray mt50 pt50 pb50 ewp_helpa"> -->
<!-- 		        <div class="innerBox pl100 pr100"> -->
<!-- 		            <h1>알림</h1> -->
<!-- 		            <p><strong class="colorCyan">사회적 거리두기 4단계 관련 안내</strong></p> -->
		
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
		
		    <div class="bg-gray isPc">
		        <div class="body-contents-wrap">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/beerwalk/img_info_beerwalk_01.jpg"
		                 alt="key visual"
		                 class="full">
		        </div>
		    </div>
		
		    <div class="bg-gray isMb">
		        <div class="beerwalk-desc-trip">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/beerwalk/img_info_01.jpg"
		                 alt="key visual"
		                 class="full">
		            <div class="beerwalk-desc-con txt_left ewp_tlft ewp_rmg">
		                제주맥주 양조장 투어는 맥주가 만들어지는 과정을<br />
		                올레길을 걷듯, 오감으로 체험해보는<br />
		                맥주 양조 여행입니다.<br />
		            </div>
		        </div>
		    </div>
		
		    <div class="beerwalk-desc-wrap isPc">
		        <div class="beerwalk-desc-con txt_left ewp_mgbe">
		            제주맥주 양조장 투어는 맥주가 만들어지는 과정을<br />
		            올레길을 걷듯, 오감으로 체험해보는<br />
		            맥주 양조 여행입니다.<br />
		        </div>
		        <div>
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/beerwalk/img_info_beerwalk_02.jpg"
		                 alt="key visual"
		                 class="full"><br /><br /><br />
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/beerwalk/img_info_beerwalk_03.jpg"
		                 alt="key visual"
		                 class="full"><br />
		        </div>
		    </div>
		
		    <div class="beerwalk-desc-wrap isMb">
		        <div class="beerwalk-desc-con"></div>
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/beerwalk/img_info_07.jpg"
		                 alt="key visual"
		                 class="full">
		        <div class="beerwalk-desc-con"></div>
		    </div>
		
		    <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/beerwalk/img_info_02.jpg"
		         alt="key visual"
		         class="full isMb">
		
		    <div class="beerwalk-desc-con isPc ewp_pdbg"></div>
		
		    <div class="bg-gray isPc">
		        <div class="beerwalk-desc-wrap">
		            <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/beerwalk/img_info_beerwalk_04.jpg"
		                 alt="key visual"
		                 class="full" />
		        </div>
		    </div>
		    <div class="beerwalk-desc-wrap">
		        <div class="beerwalk-desc-con txt_left">
		            <div class="isMb">
		                <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/beerwalk/img_info_03.jpg"
		                     alt="key visual"
		                     class="full"><br />
		                <img src="https://dsfyc8ahox9m2.cloudfront.net/static/images/reservationRenew/m/beerwalk/img_info_04.jpg"
		                     alt="key visual"
		                     class="full"><br /><br />
		            </div>
		
		            맥주의 다양한 원료들을<br />
		            직접 맛보고 향을 맡아 보세요.<br />
		        </div>
		    </div>
		
		    <div class="body-contents-wrap mt75 pt50 ewp_lstg">
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
