<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	   uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../include/header-single.jsp" %>
<c:set var="isUsed" value="false" />
<c:set var="isRefunded" value="false" />

<div class="app ewp_app">
	<%@ include file="../include/top_menu.jsp" %>
    <section class="head">
        <div class="intro ewp_intro">
            <h1>이용약관</h1>
        </div>
    </section>
	
</div>

<script type="text/javascript">

// 	$(document).ready(function () {
		
// 		var formObj = $("form[name='fWork']");
		
// 		$("#mainButton").on('click', function(e) {
	
// 			e.preventDefault();
			
// 			formObj.attr('action', '/member/main');
// 			formObj.attr('method', 'get');
// 			formObj.submit();
// 		});				
		
// 		$("#editButton").on('click', function(e) {
			
// 			e.preventDefault();
			
// 			formObj.attr('action', '/member/myPage/edit');
// 			formObj.attr('method', 'get');
// 			formObj.submit();
// 		});
		
// 		$('.saleItem').on('click', function() {
			
// 			$('#fSale input[name="order_num"]').val($(this).data('ordernum'));	
// 			document.sale.submit();
// 		});
// 	});
	
</script>


<div class="ewp_book">
<section class="dmzbt_sec bookingsec">
	<div class="dmz_list ddid_list jw_list pt_list io_list pd_bot">

		
		<div class="pv_box pb_wrapper">
			<div class="af_box si_box asc_box">
				<div class="asc_in">
					<h3>이용약관</h3>
				</div>
			</div>
			<form:form id="fWork" name="fWork" class="" method="post" >
			<input type="hidden" name="contentMstCd" value="${contentMstCd}">
			<input type="hidden" name="type" value="${type}">
			<div class="ws_box">
				<div class="modal_bx wp_modal" style="display:block !important;">
					<div class="modal_content person">
						<p class="modal_close"><a href="#"></a></p>
						<div class="modal_txt">
						 	<div class="bys_steps">
								<div class="d-flex row-h1 bc_status">
									<ul class="aftCB stepul proc01">
										<li>
											<span>${reserveInfo.info_c_title }</span>
											<span>${reserveInfo.info_c }</span>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			
			
<!-- 			<div class="pv_btn_wrapper"> -->
<!-- 				<a href="#"  id="mainButton"> -->
<!-- 					메인으로 -->
<!-- 				</a> -->
<!-- 			</div> -->
			</form:form>
		</div>
		<%-- <%@ include file="../include/footer-single.jsp" %> --%>
	</div>
</section>
<%@ include file="../include/footer-single.jsp" %>
</div>



<script>
</script>

<%-- <%@ include file="../include/footer-single.jsp" %> --%>