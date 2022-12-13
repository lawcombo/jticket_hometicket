<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>


<%@ include file="../../include/ganghwakidscafe/header-single.jsp" %>

<style>
  *{
  margin: 0px;
  padding: 0px;
}
.container{
  width: 1000px;
  margin: 0 auto;
}
.container div{
  text-align: center;
  display: table;
}
.container div span{
  display: table-cell;
  vertical-align: middle;
}
.top{
  margin-top: 20px;
  outline: 1px solid #9F9F9F;
  width: 1000px;
  height: 80px;
  display: table;
  background-color: #FF5E00;
}
.middle{
  margin-top: 20px;
  width: 1000px;
  height: 500px;
  position: relative;
}
.middle-left{
  outline: 1px solid #9F9F9F;
  position: absolute;
  top: 0px;
  width: 600px;
  height: 500px;
  background-color: #00D8FF;
}
.middle-right{
  position: absolute;
  top: 0px;
  left: 620px;
  width: 380px;
  height: 500px;
}
.middle-right-1{
  outline: 1px solid #9F9F9F;
  width: 380px;
  height: 150px;
  background-color: #FF00DD;
}
.middle-right-2{
  outline: 1px solid #9F9F9F;
  margin-top: 10px;
  width: 380px;
  height: 150px;
  background-color: #FFE400;
}
.middle-right-3{
  outline: 1px solid #9F9F9F;
  margin-top: 10px;
  width: 380px;
  height: 180px;
  background-color: #99E000;
}
.bottom{
  margin-top: 20px;
  margin-bottom: 20px;
  outline: 1px solid #9F9F9F;
  width: 1000px;
  height: 100px;
  background-color: #5D5D5D;
  color: #fff;
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

<div class="container">
  <!-- TOP -->
  <div class="top">
    <span>TOP (1000 x 80) <br/> #FF5E00;</span>
  </div>

  <!-- MIDDLE -->
  <div class="middle">

    <div class="middle-left"> <span>LEFT (600 x 500) <br/> #00D8FF;</span> </div>

    <div class="middle-right">

      <div class="middle-right-1"> <span>RIGHT1 (380 x 150) <br/> #FF00DD;</span> </div>
      <div class="middle-right-2"> <span>RIGHT2 (380 x 150) <br/> #FFE400;</span> </div>
      <div class="middle-right-3"> <span>RIGHT3 (380 x 170) <br/> #99E000;</span> </div>

    </div>

  </div>

  <div class="bottom"> <span>BOTTOM (1000 x 100) <br/> #5D5D5D;</span> </div>

</div>

<%@ include file="../../include/ganghwakidscafe/footer-single.jsp" %>


<script>
	
	var check = {
			reserveCheck : function(){
				var newURL = window.location.protocol + "//" + window.location.host + "/ticketing/checkTicket?content_mst_cd=DIAMONDBAY_0_1";
				 window.location.href=newURL;
			}
	}
	
</script>