<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<footer style="background-color:#f5f5f5;">
    <section class="footer" style="background-color:lightgray;">
        <div class="footer-menu">
            <div class="footer-menu__gnb " style="padding-left:30px">
                <a href="" class="gray" style="color: var(--gray-6); margin-right: 4px; border-radius:21px; background-color:var(--gray-c);" onclick="movePage.guideLinePersonal();">개인정보 처리방침</a> 
                <a href="" class="gray" style="color: var(--gray-6); margin-right: 4px;" onclick="movePage.location();">| 찾아오시는길 </a>
                <!-- 
                <a href="" class="gray" style="color: var(--gray-6);" onclick="movePage.sitemap();">사이트맵</a>
                 -->
            </div>
            
        </div>


        <div class="footer-info">
            <p class="address" style="padding-left:30px">
				[26359] 강원도 원주시 지정면 소금산길 12 <br />
				<!-- 통신판매업신고번호 제 2021-부산해운대-1975 호 <br /> -->
				간현관광지 소금산밸리 안내: 033-749-4860
				
            </p><br />
            <p class="copyright" style="padding-left:30px"><span>Copyright ⓒ 2020 Wonju-si. All Rights Reserved</span></p>
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
				window.open("https://cms.wfmc.kr/web/lay1/bbs/S1T207C216/A/15/view.do?article_seq=43&cpage=1&rows=10&condition=&keyword=");
			},
			
			//찾아오시는길
			location : function(){
				window.open("https://cms.wfmc.kr/web/lay1/S1T128C154/contents.do");
			},
			
			
			//사이트맵
			sitemap : function(){
				window.open("https://ghhc.dofnet.kr/guide/sitemap.jsp");
			}
	}
	
</script>