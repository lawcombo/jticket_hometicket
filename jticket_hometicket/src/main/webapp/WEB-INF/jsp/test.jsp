<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	   uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="./include/diamondbay/header-single.jsp" %>


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
		
		<!-- 
		<div>
			<a href="/ticketing/programInfos?content_mst_cd=JEJUBEER_0_1&product_group_code=103">금능해변<span style="color:red">(가상결제!)</span></a>
		</div>
		<div>
			<a href="/ticketing/programInfos?content_mst_cd=JEJUBEER_0_1&product_group_code=104">베럴테이스팅<span style="color:red">(가상결제!)</span></a>
		</div>
		 -->
		<!--  
	 	<div>
			<a href="/ticketing/programInfos?content_mst_cd=JEJUBEER_0_1&product_group_code=106">오늘제주맥주<span style="color:red">(가상결제!)</span></a>
		</div>
		 -->
		<div>
			<!-- <a onclick="window.open('/ticketing/diamondbay','window_name','width=1215,height=900,location=no,status=no,scrollbars=yes');" style="cursor: pointer;">다이아몬드베이<span style="color:red">(가상결제!)</span></a> -->
			<a onclick="pop.openDiamondBay();" style="cursor:pointer;">다이아몬드베이<span style="color:red">(가상결제!)</span></a>
		</div>
		
		
	</div>
	
	<div style="clear:both;display:flex;justify-content:space-evenly;width:100%;padding:30px;">
		<div>
			<a onclick="pop.openGanghwa();" style="cursor:pointer;">강화키즈카페 메인<span style="color:red">(가상결제!)</span></a>
		</div>
		<div>
			<a onclick="pop.openGanghwa1();" style="cursor:pointer;">강화키즈카페 예매<span style="color:red">(가상결제!)</span></a>
		</div>
		<div>
			<a onclick="pop.openGanghwa2();" style="cursor:pointer;">강화키즈카페 예매 확인<span style="color:red">(가상결제!)</span></a>
		</div>
	</div>
	
</div>

<script>



var pop = {
		openDiamondBay : function(){

			var popupWidth = 1215;
			var popupHeight = 900;
			
			//팍업 가운데 정렬을 위한 화면 해상도 계산
			var popupX = (window.screen.width / 2) - (popupWidth / 2);
			var popupY= (window.screen.height / 2) - (popupHeight / 2);
			
			window.open('/ticketing/diamondbay', 'window_name', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
		},
		
		openGanghwa : function(){
			var popupWidth = 1250;
			var popupHeight = 900;
			
			//팍업 가운데 정렬을 위한 화면 해상도 계산
			var popupX = (window.screen.width / 2) - (popupWidth / 2);
			var popupY= (window.screen.height / 2) - (popupHeight / 2);
			
			window.open('/ticketing/ghkidscafe', 'window_name', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
		},
		
		openGanghwa1 : function(){
			var popupWidth = 1250;
			var popupHeight = 900;
			
			//팍업 가운데 정렬을 위한 화면 해상도 계산
			var popupX = (window.screen.width / 2) - (popupWidth / 2);
			var popupY= (window.screen.height / 2) - (popupHeight / 2);
			
			//window.open('/ticketing/ghkidscafe', 'window_name', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
			window.open('/ticketing/ghkidscafe/schedule?content_mst_cd=GANGHWAKIDS_0_1&product_group_code=101', 'window_name', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
		},
		
		openGanghwa2 : function(){
			var popupWidth = 1250;
			var popupHeight = 900;
			
			//팍업 가운데 정렬을 위한 화면 해상도 계산
			var popupX = (window.screen.width / 2) - (popupWidth / 2);
			var popupY= (window.screen.height / 2) - (popupHeight / 2);
			
			//window.open('/ticketing/ghkidscafe', 'window_name', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
			window.open('/ticketing/checkTicket?content_mst_cd=GANGHWAKIDS_0_1', 'window_name', 'height=' + popupHeight  + ', width=' + popupWidth  + ', left='+ popupX + ', top='+ popupY);
		}
}

</script>

<%-- <%@ include file="./include/footer-single.jsp" %> --%>