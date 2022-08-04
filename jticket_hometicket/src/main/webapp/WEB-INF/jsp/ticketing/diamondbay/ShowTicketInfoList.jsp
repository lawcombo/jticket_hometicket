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

<c:set var="now" value="<%=new java.util.Date() %>" />
<fmt:formatDate var="today" value="${now }" pattern="yyyy-MM-dd"/>
<c:set var="isUsed" value="false" />
<c:set var="isRefunded" value="false" />
<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">
<style>
a.disabled {
	pointer-events: none;
	cursor: default;
}
</style>
<div class="app">
	<%-- <%@ include file="../include/top_menu.jsp" %> --%>
	
	<div style="background-image: url('${pageContext.request.contextPath }/resources/images/diamondbay/visual1.jpg'); height:300px;"> 
		<div style="position:absolute; top:10%; left:19%;">
			<img src="${pageContext.request.contextPath }/resources/images/diamondbay/visual_title.png" lat="DIAMOND BAY">
		</div>
	</div>
	

	<!-- 뒤로가기 버튼 -->
	<%-- 
	<div style="text-align:left; padding-top:10px; padding-left:10px;">
		<img src="${pageContext.request.contextPath }/resources/images/diamondbay/backImg.png" onclick="history.back()" style="cursor: pointer; width: 50px;" />
	</div>
	 --%>
	
    <section class="head">
         <div class="intro res_tit">
            <h1>
            	다이아몬드베이<br>
            	예매 확인 / 취소
            </h1>
        </div>
    </section>
	<div class="conts_box ewp_cb">
		<section class="reserve reserve-form ewp_list_ex">
		    <div class="verticalAlignMiddle">
				<div class="reserveDl full mt50 el_tit">
					<h3 class="reserveDt"><span class="ewp_check_em">${buyerInfo.member_name}</span> 님의 예매내역</h3>
				</div>
		        <c:forEach var="product" items="${dataList}" varStatus="status">
			        <div class="reserveDl full mt50 el_btn">
			        	<a type="button" class="reserveDt orderNum" id="${product.order_num}_${product.sale_code}">${product.product_group_name} / ${product.play_date} ${product.schedule_text} / ${product.actual_quantity} 명</a>
<%-- 			            <a type="button" class="reserveDt orderNum ${product.play_date eq today ? 'disabled' : '' }" id="${product.order_num}_${product.sale_code}">${product.product_name} / ${product.play_date} ${product.schedule_text} / ${product.quantity} 명</a> --%>
			        </div>
		        </c:forEach>
		    </div>
		</section>
		<div id="hidden-section" style="display:none;">
			<form:form name="form" method="POST" action="/ticketing/prevShowTicket?content_mst_cd=${buyerInfo.content_mst_cd}">
				<input type="hidden" name="order_num" id="order_no" />		
				<input type='hidden' name='shop_code' value="${buyerInfo.shop_code }">
				<input type='hidden' name='sale_code' id='sale_code'>
				<input type='hidden' name='member_tel' value="${buyerInfo.member_tel }">
				<input type='hidden' name='member_name' value="${buyerInfo.member_name }">
				<input type="hidden" name="type" value="0" />
			</form:form>
		</div>
	</div>
</div>
<%-- <%@ include file="../include/footer-single.jsp" %> --%>
<%@ include file="../../include/diamondbay/footer-single.jsp" %>
</body>
</html>
<script>
$(function() {
	$("#confirm").attr('class', 'sub-a active');
	
	$(".orderNum").on('click', function(e){
		e.preventDefault();
		var data = $(this).attr('id');
		var id = data.split("_");
		$("#order_no").val(id[0]);
		$("#sale_code").val(id[1]);
		
		
		var form = $('form[name="form"]');
		form.submit();
	});
	
	<%-- $('#cancelButton').on('click', function(e){
		e.preventDefault();
		
		
		if(confirm('현재 예약을 취소하시겠습니까?')) {
			var form = $('form[role="cancel"]');
			form.action="/ticketing/showTicketInfo?content_mst_cd="+'<%=request.getParameter("content_mst_cd")%>';
			form.submit();
		}
	}); --%>
	
	<c:if test="${not empty message}">
		alert('<c:out value="${message}" />');
	</c:if>
})


</script>

</body>
</html>