<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	   uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="./include/header-single.jsp" %>



<div id="select-section" style="width:100%;padding:30px;">
	
	<div style="">
		<select name="" id="">
			<option value="DMZGONDOLA_0_1">DMZ곤돌라</option>
			<option value="MEMBER0010_30_006">개인테스트</option>
			<option value="MEMBER0010_30_001">테스트</option>
			<option value="ULJINFMO_0_2">안전체험관</option>
		</select>
	</div>
	<div style="clear:both;display:flex;justify-content:space-evenly;width:100%;padding:30px;">
		
		<div>
			<a href="/adm">관리자</a>
<!-- 			<a href="/ticketing/selectSchedule?content_mst_cd=JEJUBEER_0_1&product_group_code=101">투어</a> -->
		</div>
		<div>
			<a href="/ticketing/programInfos?content_mst_cd=JEJUBEER_0_1&product_group_code=101">투어<span style="color:red">(가상결제!)</span></a>
		</div>
		<div>
			<a href="/ticketing/programInfos?content_mst_cd=JEJUBEER_0_1&product_group_code=102">체험<span style="color:red">(가상결제!)</span></a>
		</div>
		<div>
			<a href="/ticketing/programInfos?content_mst_cd=JEJUBEER_0_1&product_group_code=103">금능해변<span style="color:red">(가상결제!)</span></a>
		</div>
		<div>
			<a href="/ticketing/programInfos?content_mst_cd=JEJUBEER_0_1&product_group_code=104">베럴테이스팅<span style="color:red">(가상결제!)</span></a>
		</div>
	</div>
	
</div>

<script>

</script>

<%-- <%@ include file="./include/footer-single.jsp" %> --%>