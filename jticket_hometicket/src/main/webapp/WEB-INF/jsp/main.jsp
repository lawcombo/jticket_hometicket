<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	   uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="./include/header-single.jsp" %>

<section class="dmzbt_sec bookingsec fstpg">
	<div id="error-section" class="">
		<div class="dmz_list ddid_list">
			<div class="did_list_wrap">
				<div class="error_wrap">
					<div class="booking_tit">
						<h1>에러</h1>
					</div>
					<div class="error_pg">
						<p class="">
							<img src="<c:url value='/resources/images/error_green.png' />" alt="" />
						</p>
						<br />
						<h4 class="">이용에 불편을 드려 죄송합니다.</h4>
						<br />
						<p class="error_txt">
							찾으시는 주소가 잘못 입력되었거나,<br />변경 혹은 삭제로 인해 현재 사용하실 수 없습니다.<br />입력하신
							페이지의 주소가 정확한지 다시 한번 확인해 주시길 부탁드립니다.
						</p>
					</div>
					<div class="identitybt">
						<a id="backButton" href="#">돌아가기</a>
					</div>
				</div>
			</div>
				<%@ include file="./include/footer.jsp" %>
		</div>
	</div>
</section>
<script>
$(function() {
	$("#backButton").on('click', function(e) {
		e.preventDefault();
		window.history.go(-1);
	})
});
</script>
