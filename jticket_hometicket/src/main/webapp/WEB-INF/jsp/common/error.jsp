<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	   uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../include/header-single.jsp" %>

<section class="dmzbt_sec bookingsec fstpg">
	<div id="error-section" class="">
		<div class="dmz_list ddid_list">
			<div class="did_list_wrap">
				<div class="error_wrap">
					<div class="er_ex" style="width:100%; max-width:768px; margin:0 auto;">
						<img src="/resources/images/error_img_211013.jpg" alt="" style="width:100%">
					</div>
					
					<div class="identitybt ewp_idtbt" style="background:#444331; width:100%; max-width:200px; line-height:45px; border-radius:5px; margin:0 auto;">
						<a id="backButton" href="#" style="color:#fff; width:100%; height:100%; display:block; text-align:center;">돌아가기</a>
					</div>
				</div>
			</div>
				<%-- <%@ include file="../include/footer.jsp" %> --%>
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
