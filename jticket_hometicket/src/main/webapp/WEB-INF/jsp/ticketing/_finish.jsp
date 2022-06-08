<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<%@ include file="../include/header-single.jsp" %>
	<section class="dmzbt_sec bookingsec">
	<div class="dmz_list ddid_list jw_list edt_ex">
		<div class="did_list_wrap ot_dlw eot_wrapper">
			
			<div class="res_conf erc_wrapper wrap bookingbt_bx_in pro2">
				<div class="res_conf_imgbx">
				</div>
				<div class="res_conf_txtbx">
					<div>
						<h1 id="container_title" class="pay_ch_tit">결제완료</h1>
						<%-- <p class="erc_tit"><fmt:formatNumber type="number" maxFractionDigits="3" value="${trade.total_fee }"/>원</p> --%>
					</div>
					<div class="erc_tb dv">
						<ul>
							<li>
								<p class="p_tit">신청자명</p>
								
								<p class="p_txt">
									${trade.member_name }
								</p>
							</li>
							<li>
								<p class="p_tit">프로그램명</p>
								
								<p class="p_txt">
									${trade.product_group_name }
								</p>
							</li>
							<li>
								<p class="p_tit">방문예약일자</p>
								
								<p class="p_txt">
									${trade.play_date }<br>${trade.play_sequence }회차(${trade.start_time }-${trade.end_time })
								</p>
							</li>
							<li>
								<p class="p_tit">인원</p>								
								<p class="p_txt">
									${trade.total_count } 명
								</p>
							</li>
							<li>
								<p class="p_tit">핸드폰 번호</p>								
								<p class="p_txt">
									${trade.member_tel }
								</p>
							</li>
							<li>
								<p class="p_tit">예매번호</p>
								<p class="p_txt">
									${trade.order_num }
								</p>
							</li>
							<li>
								<p class="p_tit">결제금액</p>
								
								<p class="p_txt">
									<fmt:formatNumber type='number' maxFractionDigits='3' value='${trade.total_fee }'/> 원
								</p>
							</li>
							<li>
								<p class="p_tit">결제수단</p>
								<p class="p_txt">
									${trade.pay_method }
								</p>
							</li>
							<li>
								<p class="p_tit">예약일자</p>
								<p class="p_txt">
									${trade.sale_date }
								</p>
							</li>
						</ul>
					</div>
					<div class="erc_ty">
						<p class="qu_txt">Thank you</p>
					</div>
					<div class="product_btns reserv ercy_btn fin_okbt">
						<input id="okButton" type='submit' value='확인'>
					</div>
					
					<!-- 
					<div class="res_table">
						<ul class="res_table_txt">
							<li>예약공간</li>
							<li><c:out value="${trade.shop_name }" /></li>
							<li>예약자명</li>
							<li><c:out value="${trade.member_name }" /></li>
						</ul>
						<ul class="res_table_txt">
							<li>예약번호</li>
							<li><c:out value="${trade.order_num }" /></li>
							<li>예약자 연락처</li>
							<li><c:out value="${trade.member_tel }" /></li>
						</ul>
						<ul class="res_table_txt">
							<li>예약일</li>
							<li><c:out value="${trade.sale_date }" /></li>
							<li>예약자 이메일</li>
							<li><c:out value="${trade.member_email}" /></li>
						</ul>
						<ul class="res_table_txt">
							<li><c:out value="${trade.product_group_kind eq 'A' ? '사용기한' : '사용일시' }" /></li>
							<li>
								<c:if test="${trade.product_group_kind eq 'A' }">
									<c:out value="${trade.expire_date }" /> 
								</c:if>
								<c:if test="${trade.product_group_kind eq 'S' }">
									<c:out value="${trade.play_date}" />
									(<c:out value="${trade.start_time }" /> 
									&#126;
									<c:out value="${trade.end_time }" /> )
								</c:if>
								
							</li>
							<li>예약인원</li>
							<li><c:out value="${trade.quantity}" /></li>
						</ul>
					</div> --><!-- res_table end -->
					
				</div><!-- res_conf_txtbx end -->
			</div>
		
		</div>

	</div>
	</section>
<script>
$(function() {
	$("#okButton").on('click', function() {
		// 테스트 서버
		window.location.href = "<c:out value='${pageContext.request.contextPath}' />/";
		// TODO: 리얼서버
// 		window.location.href = "https://jejubeer.co.kr/brewery-program";
		
		
	});
})
</script>


</body>
</html>