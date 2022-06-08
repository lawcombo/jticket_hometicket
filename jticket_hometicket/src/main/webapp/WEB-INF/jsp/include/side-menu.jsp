<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<div class="byn_met">
	<p>MENU</p>
</div>
<div class="nav navnew">
	<a class="nav-link" href="#">
		<!-- <div class="sb-nav-link-icon">
			<i class="fas fa-chart-area"></i>
		</div> --> 
		<span class="dotspan"></span><span>대시보드</span>
	</a>
	<div class="ds_side_tm">
		<a class="nav-link collapsed" data-toggle="collapse" data-target="#packageSettlementLayouts" aria-expanded="false" aria-controls="userLayouts">
			<!-- <div class="sb-nav-link-icon">
				<i class="fas fa-map"></i>
			</div> --> 
			<span class="dotspan"></span><span>패키지 정산</span>
			<div class="sb-sidenav-collapse-arrow">
				<span class="side_pls"></span>
			</div>
		</a>
		<div class="collapse" id="packageSettlementLayouts" aria-labelledby="headingOne" data-parent="#sidenavAccordion">
			<nav class="sb-sidenav-menu-nested nav">
				<a class="nav-link" href="#">통합</a>			
				<a class="nav-link" href="#">시설별</a>
			</nav>
		</div>
	</div>
	
</div>