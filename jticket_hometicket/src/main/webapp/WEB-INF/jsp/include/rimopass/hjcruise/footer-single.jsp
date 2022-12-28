<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<footer style="background-color:#f5f5f5;">
    <section class="footer">
        <div class="footer-menu">
            <div class="footer-menu__gnb ">
                <a href="" class="gray" style="color: var(--gray-6); margin-right: 4px; border-radius:21px; background-color:var(--gray-c);" onclick="movePage.guideLinePersonal();">개인정보 처리방침</a> 
                <a href="" class="gray" style="color: var(--gray-6); margin-right: 4px;" onclick="movePage.location();">| 찾아오시는길 | </a>
                <a href="" class="gray" style="color: var(--gray-6); margin-right: 4px;" onclick="movePage.usePolicy();"> 이용약관  </a>
                <!-- <a href="" class="gray" style="color: var(--gray-6);" onclick="movePage.sitemap();">사이트맵</a> -->
            </div>
            
        </div>

        <div class="footer-info">
            <p class="address">
            	대표 : 이명관 | 상호명 : 에이치제이레저개발 <br />
            	031-585-7163, defcon@cheonshim.com <br />
            	사업자등록번호 : 276-86-02273 <br />
				경기도 가평군 설악면 미사리로 267-293, 2층
				
            </p><br />
            <p class="copyright"><span>Copyright</span> ⓒ 2022 HJ레저개발 <span>All rights reserved</span></p>
        </div>
    </section>
</footer>



<script type="text/javascript">
	$(document).ready(function(e) {
		$("#close_info").click(function(){
			$(".reservation_info_fixed").toggle(300);
			$("#close_info").toggleClass("open_info");
			return false;
		})
	});


	//Language
		function languageopen()
		{
		var language=document.getElementById("language");
		//language.style.display="block";
		if(language.style.display == 'none'){
			language.style.display="block";
		}else{
			language.style.display="none";
		}
		}
	//Language
	
	
	var movePage = {
			//개인정보 처리 방침.
			guideLinePersonal : function(){
				window.open("https://hjleisure.imweb.me/?mode=privacy");
			},
			
			//이용약관
			usePolicy : function(){
				window.open("https://hjleisure.imweb.me/?mode=policy");
			},
			
			//찾아오시는길
			location : function(){
				window.open("https://hjleisure.imweb.me/36");
			},
			
			//사이트맵
			sitemap : function(){
				window.open("https://ghhc.dofnet.kr/guide/sitemap.jsp");
			}
	}
	
</script>