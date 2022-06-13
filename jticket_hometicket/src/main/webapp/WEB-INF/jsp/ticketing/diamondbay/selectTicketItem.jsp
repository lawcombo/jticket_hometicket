<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>


<%@ include file="../../include/diamondbay/header-single.jsp" %>

<style>
  .container content, .container aside {
    position: relative;
  }

  figure {
    width: 100%;
    position: relative;
  }
  
  figure img {
    display: block;
    width: 100%;
    height: auto;
  } 
  figure h4 {
    position: absolute;
    top: calc(100% - 50px);
    left: 0;
    width: 100%;
    height: 50px;
    color: #fff;
    background: rgba(0, 0, 0, 0.6);
    margin: 0;
  } 
  figure .overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    overflow: hidden;
    width: 100%;
    height: 0;
    color: #fff;
    background: rgba(0, 0, 0, 0.6);
        -webkit-transition: .6s ease;
        transition: .6s ease;
  }
  figure .overlay .description {
    font-size: 20px;
    position: absolute;
    top: 50%;
    left: 50%;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    text-align: center;
  }
  
  figure:hover h4 {
    display: none;
  } 
  figure:hover .overlay {
    display: block;
    height: 100%;
  }
</style>


<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf" content="${_csrf.token}">

<div class="app" >
	<%-- <%@ include file="../../include/top_menu.jsp" %> --%>
	<div class="body-contents">
		<div class="body-contents-wrap">
			
			<div style="background-image: url('${pageContext.request.contextPath }/resources/images/diamondbay/visual1.jpg'); height:300px;"> 
				<div style="position:absolute; top:10%; left:19%;">
					<img src="${pageContext.request.contextPath }/resources/images/diamondbay/visual_title.png" lat="DIAMOND BAY">
				</div>
			</div>
		
		
			<div class="intro res_padi" style="padding-top:35px;">
				<p style="font-size:40px; font-weight:bold;">코스 선택</p>
			</div>
			
			 <div class="container">
				<div class="row">
					<content class="col-md-8 col-lg-8 col-sm-8" style="text-align: -webkit-center;">
						<figure style="width:500px; cursor:pointer;">
							<a href="/ticketing/diamondbay/schedule?content_mst_cd=DIAMONDBAY_0_1&product_group_code=101">
								<img src="${pageContext.request.contextPath }/resources/images/diamondbay/boat_1.png" class="img-responsive img-rounded" valign="absmiddle" />
								<h4>오륙도</h4>
								<div class="overlay">
									<div class="description" onclick="">
										오륙도 코스
									</div>
								</div>
							</a>
						</figure>
						
						<br />
						
						<figure style="width:500px; cursor:pointer;" href="/ticketing/diamondbay/schedule?content_mst_cd=DIAMONDBAY_0_1&product_group_code=102">
							<a href="/ticketing/diamondbay/schedule?content_mst_cd=DIAMONDBAY_0_1&product_group_code=102">
								<img src="${pageContext.request.contextPath }/resources/images/diamondbay/boat_2.png" class="img-responsive img-rounded" valign="absmiddle" />
								<h4>해운대</h4>
								<div class="overlay">
									<div class="description">
										해운대 코스
									</div>
								</div>
							</a>
						</figure>
					</content>
				</div>
			</div>
			
		</div><!-- mx1200 end -->
	
	
	</div>
</div>

<%@ include file="../../include/diamondbay/footer-single.jsp" %>


<script>
	
</script>