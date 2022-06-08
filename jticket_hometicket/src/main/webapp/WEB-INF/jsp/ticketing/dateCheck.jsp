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

<div class="app">
    <%@ include file="../include/top_menu.jsp" %>
    <section class="head">
        <div class="intro res_tit">
            <h1>
            	양조장 예약<br>
            	확인/변경/취소
            </h1>
        </div>
    </section>
	<div class="conts_box ewp_checkBox res_bot_box">
		<section class="reserve reserve-form">
		    <div class="verticalAlignMiddle">
		    
		    	<dl class="reserveDl full mt50">
					<dt class="reserveDt">
						<p class="res_tb res_wse">
							홈페이지 리뉴얼로 인해 10월8일 이전 / 10월9일 이후<br>
							프로그램 예약 확인 페이지가 각각 운영되는 점 양해 부탁드립니다.
						</p>
					</dt>
				</dl>
		    
		    
		        <div class="ewp_certi">
			        <dl class="reserveDl full mt20">
			            <dd class="reserveDd">
			            	<dl class="columnDl fuldl">
				           		 <dd class="columnDd">
			                          <a class="buttonTypeCyan full textLarge" id="callCert" href="https://brewery.jejubeer.co.kr/reserve/detail/cert" target="_blank";>10/8 이전 방문예정고객님</a>
			                          <a class="buttonTypeCyan full textLarge" id="moveToCheckTicket" target="_blank";>10/9 이후 방문예정고객님</a>
			                      </dd>
		                     </dl>
			            </dd>
			        </dl>
		        </div>
		    </div>
		</section>
		
	</div>
</div>

<%@ include file="../include/footer-single.jsp" %>
</body>
</html>
<script>
//403 에러 처리(Spring Security에서의 Ajax 호출)
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$.ajaxSetup({
	beforeSend: function (xhr) {
    	xhr.setRequestHeader(header, token);
		},
});

var length = 0;

$(function(){
	$("#confirm").attr('class', 'sub-a active');
	
	$("#moveToCheckTicket").on("click", function(){
		 var newURL = window.location.protocol + "//" + window.location.host + "/ticketing/checkTicket?content_mst_cd=JEJUBEER_0_1";
		 window.location.href=newURL;
	});
	
});

</script>