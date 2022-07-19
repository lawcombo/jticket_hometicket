<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<%-- <%@ include file="../include/header-single.jsp" %> --%>
<%@ include file="../../include/diamondbay/header-single.jsp" %>

<div class="app">

	<div style="background-image: url('${pageContext.request.contextPath }/resources/images/diamondbay/visual1.jpg'); height:300px;"> 
		<div style="position:absolute; top:10%; left:19%;">
			<img src="${pageContext.request.contextPath }/resources/images/diamondbay/visual_title.png" lat="DIAMOND BAY">
		</div>
	</div>
	

	<!-- 뒤로가기 버튼 -->
	<%-- 
	<div style="text-align:left; padding-top:10px; padding-left:10px;">
		<!-- <button onclick="history.back(); " style="cursor: pointer";>뒤로가기</button> -->
		
		<img src="${pageContext.request.contextPath }/resources/images/diamondbay/backImg.png" onclick="history.back()" style="cursor: pointer; width: 50px;" />
	</div>
 	--%>
 	
	<section class="head">
		<div class="intro res_tit">
			<h1>
				예약이 완료되었습니다!
			</h1>
		</div>
	</section>
	<div class="conts_box">
		<section class="reserve reserve-form ewp_show_ex">
			<div class="verticalAlignMiddle">
				<ul class="reserveDl full mt50 ewp_show_wrapper mb50">
					<li class="reserveDt">
					<p class="sh_tit">
							신청자 명
						</p>
						<p class="sh_text">
							${trade[0].member_name}
						</p>
					</li>
					<li class="reserveDt">
						<p class="sh_tit">
							프로그램 명
						</p>
						<p class="sh_text">
							${trade[0].product_group_name}
						</p>
					</li>
					<li class="reserveDt">
						<p class="sh_tit">
							프로그램 타입
						</p>
						<p class="sh_text">
							<c:forEach var="item" items="${trade}">
								${item.product_name} ${item.quantity } 명<br>
							</c:forEach>
						</p>
					</li>
					<li class="reserveDt">
						<p class="sh_tit">
							방문예약일자
						</p>
						<p class="sh_text">
							${trade[0].play_date } / ${trade[0].start_time }
						</p>
					</li>
					<li class="reserveDt">
						<p class="sh_tit">
							총 인원
						</p>
						<p class="sh_text">
							${trade[0].total_count } 명
						</p>
					</li>
					
					<li class="reserveDt">
						<p class="sh_tit">
							핸드폰 번호
						</p>
						<p class="sh_text">
							<span id="memberTelSpan">${trade[0].member_tel }</span>
						</p>
					</li>
				</ul>
<!-- 			        <div class="bannerbx"> -->
<!-- 			        	<p class="m_jb_banner"> -->
<!-- 			        		<a href="https://www.instagram.com/kash_gvng/" target="_blank"> -->
<!-- 			        			<img src="/resources/images/JBjmkt_banner_mo03_2.png"> -->
<!-- 			        		</a> -->
<!-- 			        	</p> -->
<!-- 			        	<p class="pc_jb_banner"> -->
<!-- 			        		<a href="https://www.instagram.com/kash_gvng/" target="_blank"> -->
<!-- 			        			<img src="/resources/images/JBjmkt_banner_pc01.png"> -->
<!-- 			        		</a> -->
<!-- 			        	</p> -->
<!-- 			        </div> -->
				<div class="pageLine"></div>
				<ul class="reserveDl full mt50 ewp_show_wrapper show_bot_tb">
					<li class="reserveDt">
						<h3>
							[이용안내]
						</h3>
						<p>
							<span>
								프로그램 정원은 조정이 불가능합니다.
							</span>

						</p>
					</li>
					<li class="reserveDt sbt_bot">
						<h3>
							[주의사항]
						</h3>
						<p>
							<span>
								당일 10분 이상 지각 참여 및 환불 불가<br>
								지각으로 인한 환불은 불가능합니다.
							</span>
							<span>
								정원 조정 불가<br>
								프로그램의 정원 추가는 불가능합니다.
							</span>
						</p>
					</li>
				</ul>
			</div>
		</section>
		<div id="hidden-section" style="display:none;">
			<!-- 취소 form -->
			<form:form role="cancel" action="/ticketing/cancelTicket" method="POST">
				<input type="hidden" name="order_num" value="${buyerInfo.order_num }" />
				<input type="hidden" name="content_mst_cd" value="${buyerInfo.content_mst_cd }" />
				<input type='hidden' name='shop_code' id="product_shop_code" value="${buyerInfo.shop_code }">
				<input type='hidden' name='sale_code' value="${buyerInfo.sale_code }">
				<input type='hidden' name='member_tel' value="${buyerInfo.member_tel }">
				<input type='hidden' name='member_name' value="${buyerInfo.member_name }">
			</form:form>
			<!-- 변경 form -->
			<form:form role="modify" action="/ticketing/modifyTicket" method="POST">
				<input type="hidden" name="payMethod" value="${paymentInfo.payMethod }" />
				<input type="hidden" name="order_num" value="${buyerInfo.order_num }" />
				<input type="hidden" name="productGroup.content_mst_cd" value="${buyerInfo.content_mst_cd }" />
				<input type='hidden' name='productGroup.shop_code' value="${buyerInfo.shop_code }">
				<input type='hidden' name='sale_code' value="${buyerInfo.sale_code }">
				<input type='hidden' name='member_tel' value="${buyerInfo.member_tel }">
				<input type='hidden' name='member_name' value="${buyerInfo.member_name }">
				<input type="hidden" name="productGroup.product_group_code" id="productGroup_code" />	
				<input type="hidden" name="products[0].shop_code" id="product_shop_code" value="${products[0].shop_code }" />
				<input type="hidden" name="products[0].product_group_code" value="${products[0].product_group_code }" />
				<input type="hidden" name="products[0].product_code" value="${products[0].product_code }" />
				<input id="productCountInput" type="hidden" name="products[0].count"  />
				<input type="hidden" name="visitorType" value="${paymentInfo.visitorType }" />	
				<input type="hidden" name="totalFee"  id="totalFee"/>	
				<input type="hidden" name="totalCount" id="totalCount"/>	
				<input type="hidden" name="schedule_code" id="schedule_code" />	
				<input type="hidden" name="play_date" id="play_date" />
			</form:form>

		</div>
		
	</div>
</div>
<script>
	$(function() {
	 	// 전화번호에 하이픈 넣기
		$("#memberTelSpan").text(addHyphenToPhoneNumber($("#memberTelSpan").text()));
	});
	
</script>
<%-- <%@ include file="../include/footer-single.jsp" %> --%>
<%@ include file="../../include/diamondbay/footer-single.jsp" %>
</body>
</html>