<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<footer style="background-color:#f5f5f5;">
    <section class="footer">
        <div class="footer-menu">
            <div class="footer-menu__gnb ">
                <a href="" class="gray" style="color: var(--gray-6); margin-right: 4px; border-radius:21px; background-color:var(--gray-c);" onclick="movePage.guideLinePersonal();">개인정보 처리방침</a> 
                <a href="" class="gray" style="color: var(--gray-6); margin-right: 4px;" onclick="movePage.location();">| 찾아오시는길 | </a>
                <a href="" class="gray" style="color: var(--gray-6);" onclick="movePage.sitemap();">사이트맵</a>
            </div>
            
        </div>

        <div class="footer-info">
            <p class="address">
				[23037] 인천광역시 강화군 강화읍 남문로 19 <br />
				<!-- 통신판매업신고번호 제 2021-부산해운대-1975 호 <br /> -->
				행복센터 032)934-3901~2 / 키즈카페 032)934-3903 Fax. 032)724-4009
				
            </p><br />
            <p class="copyright"><span>Copyright</span> ⓒ 2022 Ganghwa-gun <span>District Incheon Metropolitan City. All rights reserved.</span></p>
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
				window.open("https://www.ganghwa.go.kr/open_content/main/guidance/personal.jsp");
			},
			
			//찾아오시는길
			location : function(){
				window.open("https://ghhc.dofnet.kr/introduce/location.jsp");
			},
			
			//사이트맵
			sitemap : function(){
				window.open("https://ghhc.dofnet.kr/guide/sitemap.jsp");
			}
	}
	
</script>