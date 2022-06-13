<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<footer style="background-color:#333;">
    <section class="footer">
        <div class="footer-menu">
            <div class="footer-menu__gnb ">
                <a href="http://www.diamondbay.co.kr/html/06_guide/04.php" class="gray" style="color: white; margin-right: 4px;">개인정보 처리방침 | </a> 
                <a href="http://www.diamondbay.co.kr/html/06_guide/02.php" class="gray" style="color: white;">채용안내</a>
            </div>
            
        </div>

        <div class="footer-info">
            <p class="address">
				상호 (주)삼주 다이아몬드베이 | 사업자등록번호 602-81-40485 | 부산광역시 해운대구 좌동순환로 395 <br />
				통신판매업신고번호 제 2021-부산해운대-1975 호 <br />
				대표 : 백승용 | 대표번호 1577-0003
				
            </p><br />
            <p class="copyright">COPYRIGHT 2016 DIAMOND BAY. ALL RIGHTS RESERVED. DESIGNED BY FIX.</p>
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
</script>